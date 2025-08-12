package com.sz.reservation.listingManagement.infrastructure.adapter.inbound.reservation;

import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetails;
import com.sz.reservation.listingManagement.application.useCase.reservation.ReservationUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/host/reservation")
public class HttpHostReservationController {
    private ReservationUseCase reservationUseCase;

    @Autowired
    public HttpHostReservationController(ReservationUseCase reservationUseCase) {
        this.reservationUseCase = reservationUseCase;
    }

    @PostMapping("/approval")
    public ResponseEntity<?> approveOrRejectReservation(@Valid @ModelAttribute ApprovalReservationRequestDto approvalReservationRequestDto){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String hostId = userDetails.getId();
        reservationUseCase.approvalReservation(hostId,approvalReservationRequestDto);
        return ResponseEntity.ok("Reservation modified correctly");
    }



}
