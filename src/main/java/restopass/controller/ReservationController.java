package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import restopass.dto.Reservation;
import restopass.service.ReservationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    private String USER_ID = "userId";

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void createReservation(@RequestBody Reservation restaurant, HttpServletRequest request) {
        String userId = request.getAttribute(USER_ID).toString();
        this.reservationService.createReservation(restaurant, userId);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Reservation> getReservationByUser(HttpServletRequest request) {
        String userId = request.getAttribute(USER_ID).toString();
        return this.reservationService.getReservationsForUser(userId);
    }

    @RequestMapping(value = "/cancel/{reservationId}", method = RequestMethod.PATCH)
    public void cancelReservation(@PathVariable String reservationId) {
        this.reservationService.cancelReservation(reservationId);
    }


    @RequestMapping(value = "/done/{reservationId}", method = RequestMethod.PATCH)
    public void doneReservation(@PathVariable String reservationId,
                                @RequestParam(value = "restaurant_id") String restaurantId,
                                @RequestParam(value = "user_id") String userId) {
        this.reservationService.doneReservation(reservationId, restaurantId, userId);
    }




}
