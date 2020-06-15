package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restopass.client.FirebaseClient;
import restopass.dto.firebase.ReservationPushNotifData;
import restopass.dto.firebase.ScorePushNotifData;
import restopass.dto.firebase.SimpleTopicPush;

import java.util.List;

@Service
public class FirebaseService {
    @Autowired
    private FirebaseClient firebaseClient;

    public void sendCancelReservationNotification(List<String> confirmedUsers, String reservationId, String userOwnerName, String restaurantName, String date) {
        confirmedUsers.forEach( user -> {
            SimpleTopicPush<ReservationPushNotifData> notif = this.buildCancelReservationNotification(user, reservationId, userOwnerName, restaurantName, date);
            this.firebaseClient.sendReservationNotification(notif);
                });

    }

    public void sendNewInvitationNotification(List<String> users, String reservationId, String userOwnerName, String restaurantName, String date) {
        users.forEach(user -> {
            SimpleTopicPush<ReservationPushNotifData> notif = this.buildNewInvitationNotification(user, reservationId, userOwnerName, restaurantName, date);
            this.firebaseClient.sendReservationNotification(notif);
        });
    }

    public void sendConfirmedInvitationNotification(String userOwner, String reservationId, String userInviteName, String restaurantName, String date) {
        SimpleTopicPush<ReservationPushNotifData> notif = this.buildConfirmedInvitationNotification(userOwner, reservationId, userInviteName, restaurantName, date);
        this.firebaseClient.sendReservationNotification(notif);
    }

    public void sendScoreNotification(List<String> users, String restaurantId, String restaurantName) {
        users.forEach(user -> {
            SimpleTopicPush<ScorePushNotifData> notif = this.buildScoreExperienceNotification(user, restaurantId, restaurantName);
            this.firebaseClient.sendScoreNotification(notif);
        });
    }

    public SimpleTopicPush<ReservationPushNotifData> buildCancelReservationNotification(String userId, String reservationId, String userOwnerName, String restaurantName, String date) {
        SimpleTopicPush<ReservationPushNotifData> simpleTopicPush = new SimpleTopicPush<>();
        simpleTopicPush.setTo(userId);
        ReservationPushNotifData reservationPushNotifData = new ReservationPushNotifData();
        reservationPushNotifData.setDescription("El usuario " + userOwnerName + "ha cancelado la reserva del " + date + "en " + restaurantName);
        reservationPushNotifData.setTitle("Reserva cancelada");
        reservationPushNotifData.setReservationId(reservationId);
        reservationPushNotifData.setType("CANCEL_RESERVATION");
        simpleTopicPush.setData(reservationPushNotifData);

        return simpleTopicPush;
    }

    public SimpleTopicPush<ReservationPushNotifData> buildNewInvitationNotification(String userId, String reservationId, String userOwnerName, String restaurantName, String date) {
        SimpleTopicPush<ReservationPushNotifData> simpleTopicPush = new SimpleTopicPush<>();
        simpleTopicPush.setTo(userId);
        ReservationPushNotifData reservationPushNotifData = new ReservationPushNotifData();
        reservationPushNotifData.setDescription("Chequea tu mail para aceptar la invitación");
        reservationPushNotifData.setTitle("El usuario " + userOwnerName + "te ha invitado a " + restaurantName + "el " + date);
        reservationPushNotifData.setReservationId(reservationId);
        reservationPushNotifData.setType("INVITE_RESERVATION");
        simpleTopicPush.setData(reservationPushNotifData);

        return simpleTopicPush;
    }

    public SimpleTopicPush<ReservationPushNotifData> buildConfirmedInvitationNotification(String userOwner, String reservationId, String userInviteName, String restaurantName, String date) {
        SimpleTopicPush<ReservationPushNotifData> simpleTopicPush = new SimpleTopicPush<>();
        simpleTopicPush.setTo(userOwner);
        ReservationPushNotifData reservationPushNotifData = new ReservationPushNotifData();
        reservationPushNotifData.setTitle("Confirmaron tu invitación");
        reservationPushNotifData.setDescription("El usuario " + userInviteName + "ha confirmado su asistencia a " + restaurantName + "el " + date);
        reservationPushNotifData.setReservationId(reservationId);
        reservationPushNotifData.setType("CONFIRMED_RESERVATION");
        simpleTopicPush.setData(reservationPushNotifData);

        return simpleTopicPush;
    }

    public SimpleTopicPush<ScorePushNotifData> buildScoreExperienceNotification(String userId, String restaurantId, String restaurantName) {
        SimpleTopicPush<ScorePushNotifData> simpleTopicPush = new SimpleTopicPush<>();
        simpleTopicPush.setTo(userId);
        ScorePushNotifData reservationPushNotifData = new ScorePushNotifData();
        reservationPushNotifData.setDescription("Dejanos tu opinión para poder mejorar nuestro servicio");
        reservationPushNotifData.setTitle("¿Como estuvo tu experiencia en " + restaurantName + "?");
        reservationPushNotifData.setRestaurantId(restaurantId);
        reservationPushNotifData.setType("SCORE_EXPERIENCE");
        simpleTopicPush.setData(reservationPushNotifData);

        return simpleTopicPush;
    }
}
