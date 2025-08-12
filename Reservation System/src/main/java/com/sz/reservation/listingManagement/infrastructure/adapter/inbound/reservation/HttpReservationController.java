package com.sz.reservation.listingManagement.infrastructure.adapter.inbound.reservation;

import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetails;
import com.sz.reservation.listingManagement.application.useCase.reservation.ReservationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservation")
public class HttpReservationController {
    private ReservationUseCase reservationUseCase;

    @Autowired
    public HttpReservationController(ReservationUseCase reservationUseCase) {
        this.reservationUseCase = reservationUseCase;
    }

    @PostMapping("/")
    public ResponseEntity<String> makeReservation(@ModelAttribute ReservationRequestDto reservationRequestDto){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String guestId = userDetails.getId();
        reservationUseCase.makeReservation(guestId,reservationRequestDto.listingId(),
                reservationRequestDto.checkIn(),reservationRequestDto.checkOut(),reservationRequestDto.numberOfGuests());
        return new ResponseEntity<>("Listing reserved Successfully",HttpStatus.OK);
    }


}
