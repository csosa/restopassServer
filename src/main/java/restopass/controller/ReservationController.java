package restopass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import restopass.dto.Reservation;
import restopass.service.ReservationService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    private String USER_ID = "userId";

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void createReservation(@RequestBody Reservation restaurant) {
        this.reservationService.createReservation(restaurant);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void getReservationByUser(HttpServletRequest request) {
        String userId = request.getAttribute(USER_ID).toString();
        this.reservationService.getReservationsForUser(userId);
    }

    @RequestMapping(value = "/cancel/{reservationId}", method = RequestMethod.PATCH)
    public void cancelReservation(@PathVariable String reservationId) {
        this.reservationService.cancelReservation(reservationId);
    }


    @RequestMapping(value = "/done/{reservationId}", method = RequestMethod.PATCH)
    public void doneReservation(@PathVariable String reservationId) {
        this.reservationService.doneReservation(reservationId);
    }


}
