package com.sz.reservation.listingManagement.infrastructure.adapter.inbound.reservation;

import com.sz.reservation.util.annotation.NotNullNotWhitespace;
import jakarta.validation.constraints.NotNull;

public class ApprovalReservationRequestDto {

    @NotNullNotWhitespace(message = "reservationId cannot be null or contain whitespaces")
    private String reservationId;

    @NotNull(message = "approval cannot be null")
    private boolean approved;

    public ApprovalReservationRequestDto(String reservationId, boolean approved) {
        this.reservationId = reservationId;
        this.approved = approved;
    }

    public String getReservationId() {
        return reservationId;
    }

    public boolean isApproved() {
        return approved;
    }
}
