package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import restopass.dto.*;
import restopass.mongo.ReservationRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class ReservationService {

    private MongoTemplate mongoTemplate;
    private ReservationRepository reservationRepository;
    private UserService userService;
    private String RESTAURANT_ID = "restaurantId";
    private String OWNER_USER_ID = "ownerUser";
    private String RESERVATION_ID = "reservationId";
    private String RESERVATION_STATE = "state";
    private String SLOTS_FIELD = "slots";
    private String RESERVATION_COLLECTION = "reservations";
    private String RESTAURANT_CONFIG_COLLECTION = "restaurant_configs";
    private Integer SIZE_CALENDAR = 45;

    @Autowired
    public ReservationService(MongoTemplate mongoTemplate, ReservationRepository reservationRepository, UserService userService) {
        this.mongoTemplate = mongoTemplate;
        this.reservationRepository = reservationRepository;
        this.userService = userService;
    }

    public void createReservation(Reservation reservation) {
        String reservationId = UUID.randomUUID().toString();
        reservation.setReservationId(reservationId);
        this.reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsForUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(OWNER_USER_ID).is(userId));

        return this.mongoTemplate.find(query, Reservation.class);
    }

    public void cancelReservation(String reservationId) {
        this.updateReservationState(reservationId, ReservationState.CANCELED);
    }

    public void doneReservation(String reservationId, String userId) {
        this.updateReservationState(reservationId, ReservationState.DONE);
    }

    private void updateReservationState(String reservationId, ReservationState state) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESERVATION_ID).is(reservationId));

        Update update = new Update();
        update.set(RESERVATION_STATE, state);

        this.mongoTemplate.updateMulti(query, update, RESERVATION_COLLECTION);
    }

    public RestaurantConfig generateSlotsByRestaurantConfig(String restaurantId) {
        RestaurantConfig restaurantConfig = this.findConfigurationByRestaurantId(restaurantId);
        List<RestaurantHours> restaurantHours = restaurantConfig.getDateTimeAvailable();
        LocalDateTime today = LocalDateTime.now();

        List<RestaurantSlot> slots = new ArrayList<>();

        //Genero el calendario de 45 dias
        IntStream.rangeClosed(0, SIZE_CALENDAR).forEach(i -> {
                    LocalDateTime date = today.plusDays(i);

                    //Busco la configuracion de horas que incluye el dia que estoy queriendo generar.
                    //Si esta cerrado el local, no va a existir esa configuracion por eso Optional
                    Optional<RestaurantHours> optionalDay = restaurantHours.stream().filter(rh -> rh.getOpeningDays().stream().anyMatch(op -> op.equals(date.getDayOfWeek()))).findFirst();

                    if (optionalDay.isPresent()) {
                        RestaurantHours day = optionalDay.get();
                        RestaurantSlot slot = new RestaurantSlot();
                        List<List<DateTimeWithTables>> allDateTimeWithTables = new ArrayList<>();

                        List<PairHour> pairHours = day.getPairHours();

                        //Por cada par hora inicio hora fin genero los distintos horarios con sus mesas
                        pairHours.forEach(pair -> {
                            LocalDateTime startHour = date.withHour(pair.getOpeningHour()).withMinute(pair.getOpeningMinute());
                            LocalDateTime endHour = date.withHour(pair.getClosingHour()).withMinute(pair.getClosingMinute());
                            List<DateTimeWithTables> dateTimeWithTables = new ArrayList<>();

                            Integer minutes = restaurantConfig.getMinutesGap();
                            int minutesCount = 0;
                            long minutesUntilEndHour = startHour.until(endHour, ChronoUnit.MINUTES);

                            //Empiezo a generar las distintas horas con sus mesas
                            while (minutesCount < minutesUntilEndHour) {
                                DateTimeWithTables dt = new DateTimeWithTables();
                                dt.setDateTime(startHour.plusMinutes(minutesCount));
                                dt.setTablesAvailable(restaurantConfig.getTablesPerShift());
                                dateTimeWithTables.add(dt);
                                minutesCount = minutesCount + minutes;
                            }
                            //Cuando termino de generar todos los horarios para un par inicio fin lo agrego al conjunto de todos los pares inicio fin
                            allDateTimeWithTables.add(dateTimeWithTables);
                        });
                        //Cuando tengo todos los pares inicio fin los agrego al dia
                        slot.setDateTime(allDateTimeWithTables);
                        slots.add(slot);
                    }
                }
        );

        this.updateSlotsInDB(restaurantId, slots);
        restaurantConfig.setSlots(slots);
        return restaurantConfig;
    }

    public RestaurantConfig findConfigurationByRestaurantId(String restaurantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        return this.mongoTemplate.findOne(query, RestaurantConfig.class);
    }

    private void updateSlotsInDB(String restaurantId, List<RestaurantSlot> slots) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RESTAURANT_ID).is(restaurantId));

        Update update = new Update();
        update.set(SLOTS_FIELD, slots);

        this.mongoTemplate.updateMulti(query, update, RESTAURANT_CONFIG_COLLECTION);
    }

}
