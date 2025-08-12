package com.sz.reservation.listingManagement.infrastructure.adapter.inbound.reservation;

import com.github.f4b6a3.uuid.util.UuidValidator;
import com.sz.reservation.globalConfiguration.exception.InvalidRequestException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.aspectj.lang.annotation.Before;

import java.time.LocalDate;

public record ReservationRequestDto(
        @NotBlank(message = "listingId cannot be null") String listingId,
        @NotNull(message = "checkIn cannot be null") LocalDate checkIn,
        @NotNull(message = "checkOut cannot be null") LocalDate checkOut,
        @Min(value = 1,message = "Minimum numberOfGuests is 1") int numberOfGuests) {
    public ReservationRequestDto{}
}
