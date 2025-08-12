package com.sz.reservation.listingManagement.domain;

import com.sz.reservation.listingManagement.domain.exception.*;
import com.sz.reservation.util.UuidUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private String reservationId;
    private String guestId;
    private String hostId;
    private String listingId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfGuests;
    private BigDecimal totalPrice;
    private ReservationStatus status;

    private final static int MIN_NUMBER_GUESTS = 1;
    private final static BigDecimal MIN_TOTAL_PRICE = BigDecimal.ONE;

    private Reservation(String reservationId, String guestId, String hostId, String listingId, LocalDate checkIn,
                        LocalDate checkOut, int numberOfGuests, BigDecimal totalPrice,ReservationStatus status) {
        this.reservationId = reservationId;
        this.guestId = guestId;
        this.hostId = hostId;
        this.listingId = listingId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
        this.status = status;
    }


    public static Reservation create(String reservationId,String guestId, String hostId, String listingId,
                                     LocalDate checkIn, LocalDate checkOut,int numberOfGuests,
                                     BigDecimal totalPrice,ReservationStatus status){
        Objects.requireNonNull(status);

        if (!UuidUtil.isUuidV7Valid(reservationId)){
            throw new InvalidUUIDException("The reservationId (" +reservationId +") is not a valid uuid v7");
        }
        if (!UuidUtil.isUuidV7Valid(guestId)){
            throw new InvalidUUIDException("The guestId (" +guestId +") is not a valid uuid v7");
        }

        if (!UuidUtil.isUuidV7Valid(hostId)){
            throw new InvalidUUIDException("The hostId (" +hostId + ") is not a valid uuid v7");
        }

        if (!UuidUtil.isUuidV7Valid(listingId)){
            throw new InvalidUUIDException("The listingId (" +listingId + ") is not a valid uuid v7");
        }

        if (checkIn.isAfter(checkOut)){
            throw new InvalidDateException("The start date cannot be after the end date");
        }

        if (checkOut.isEqual(checkIn)){
            throw new InvalidDateException("The end date cannot be equals to the start date");
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidDateException("The check-in date must be today or in the future");
        }

        if (checkOut.isBefore(LocalDate.now())) {
            throw new InvalidDateException("The check-Out date must be in the future");
        }

        if (numberOfGuests < MIN_NUMBER_GUESTS){
            throw new InvalidNumberGuestsException("The number of guest must be minimum: "+ MIN_NUMBER_GUESTS);
        }

        if (totalPrice.compareTo(MIN_TOTAL_PRICE) < 0){
            throw new InvalidPriceException("The minimum price is: " + MIN_TOTAL_PRICE);
        }

        return new Reservation(reservationId,guestId,hostId,listingId,checkIn,checkOut,
                numberOfGuests,totalPrice,status);
    }

    public void confirmReservation(){
        if (status != ReservationStatus.PENDING_APPROVAL){
            throw new InvalidReservationStateException("Can only confirm reservations that are Pending Approval");
        }
        status = ReservationStatus.CONFIRMED;
    }
    public void cancelReservation(){
        if (status != ReservationStatus.CONFIRMED){
            throw new InvalidReservationStateException("Can only cancel reservations that are confirmed");
        }
        status = ReservationStatus.CANCELED;
    }
    public void disapproveReservation(){
        if (status != ReservationStatus.PENDING_APPROVAL){
            throw new InvalidReservationStateException("Can only disapprove reservations that are Pending Approval");
        }
        status = ReservationStatus.DISAPPROVED;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestId() {
        return guestId;
    }

    public String getHostId() {
        return hostId;
    }

    public String getListingId() {
        return listingId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;
        return getNumberOfGuests() == that.getNumberOfGuests() && Objects.equals(getReservationId(), that.getReservationId()) &&
                Objects.equals(getGuestId(), that.getGuestId()) && Objects.equals(getHostId(), that.getHostId()) &&
                Objects.equals(getListingId(), that.getListingId()) && Objects.equals(getCheckIn(), that.getCheckIn()) &&
                Objects.equals(getCheckOut(), that.getCheckOut()) && (getTotalPrice().compareTo(that.getTotalPrice()) == 0) &&
                getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getReservationId());
        result = 31 * result + Objects.hashCode(getGuestId());
        result = 31 * result + Objects.hashCode(getHostId());
        result = 31 * result + Objects.hashCode(getListingId());
        result = 31 * result + Objects.hashCode(getCheckIn());
        result = 31 * result + Objects.hashCode(getCheckOut());
        result = 31 * result + getNumberOfGuests();
        result = 31 * result + Objects.hashCode(getTotalPrice());
        result = 31 * result + Objects.hashCode(getStatus());
        return result;
    }
}
