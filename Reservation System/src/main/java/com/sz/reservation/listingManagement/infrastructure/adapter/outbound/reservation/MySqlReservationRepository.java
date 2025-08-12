package com.sz.reservation.listingManagement.infrastructure.adapter.outbound.reservation;

import com.sz.reservation.listingManagement.domain.Reservation;
import com.sz.reservation.listingManagement.domain.ReservationStatus;
import com.sz.reservation.listingManagement.domain.port.outbound.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MySqlReservationRepository implements ReservationRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MySqlReservationRepository(@Qualifier("listing.jdbcTemplate")JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findConflictingReservations(String listingId, LocalDate checkIn, LocalDate checkOut) {
     String sql = "SELECT " +
             "BIN_TO_UUID(r.id_reservation) as idReservation," +
             "BIN_TO_UUID(r.id_host) as idHost," +
             "BIN_TO_UUID(r.id_guest) as idGuest," +
             "BIN_TO_UUID(r.id_listing) as idListing," +
             "r.check_in_date," +
             "r.check_out_date," +
             "r.number_guests,"+
             "r.total_price,"+
             "s.status "+
        "FROM reservation as r " +
             "INNER JOIN reservation_status as s on r.id_status = s.id_reservation_status" +
             " WHERE r.id_listing = UUID_TO_BIN(?)" +
             " AND  ? > r.check_in_date AND ? < r.check_out_date " +
             " AND r.id_status IN (?,?)";

     List<Reservation> reservations = jdbcTemplate.query(sql, new ResultSetExtractor<List<Reservation>>() {
             @Override
             public List<Reservation> extractData(ResultSet rs) throws SQLException, DataAccessException {
                 List<Reservation> reservations = new ArrayList<>();
                 while (rs.next()){
                     reservations.add(Reservation.create(rs.getString("idReservation"),
                             rs.getString("idGuest"),
                             rs.getString("idHost"),
                             rs.getString("idListing"),
                             rs.getDate("r.check_in_date").toLocalDate(),
                             rs.getDate("r.check_out_date").toLocalDate(),
                             rs.getInt("r.number_guests"),
                             rs.getBigDecimal("r.total_price"),
                             ReservationStatus.valueOf(rs.getString("s.status"))));
                 }
                 return reservations;
             }
         },listingId,checkOut,checkIn,
             ReservationStatus.CONFIRMED.getId(),ReservationStatus.PENDING_APPROVAL.getId());
     return reservations;
    }

    @Override
    public void save(Reservation reservation) {
        String sql  = "INSERT INTO reservation" +
                "(id_reservation,id_host,id_guest," +
                "id_listing,check_in_date,check_out_date," +
                "number_guests,total_price,id_status)" +
                "VALUES" +
                "(UUID_TO_BIN(?)," +
                "UUID_TO_BIN(?)," +
                "UUID_TO_BIN(?)," +
                "UUID_TO_BIN(?)," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?);";
        jdbcTemplate.update(sql,reservation.getReservationId(),
                reservation.getHostId(),
                reservation.getGuestId(),
                reservation.getListingId(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getNumberOfGuests(),
                reservation.getTotalPrice(),
                reservation.getStatus().getId());

    }

    @Override
    public void update(Reservation reservation) {
        String sql = "UPDATE reservation SET id_status = ? WHERE id_reservation = UUID_TO_BIN(?);";
        jdbcTemplate.update(sql,reservation.getStatus().getId(),reservation.getReservationId());
    }

    @Override
    public Optional<Reservation> findById(String reservationId) {
        String sql = "SELECT " +
                "BIN_TO_UUID(r.id_reservation) as idReservation," +
                "BIN_TO_UUID(r.id_host) as idHost," +
                "BIN_TO_UUID(r.id_guest) as idGuest," +
                "BIN_TO_UUID(r.id_listing) as idListing," +
                "r.check_in_date," +
                "r.check_out_date," +
                "r.number_guests,"+
                "r.total_price,"+
                "s.status "+
                "FROM reservation as r " +
                "INNER JOIN reservation_status as s on r.id_status = s.id_reservation_status" +
                " WHERE r.id_reservation = UUID_TO_BIN(?)";

        Reservation reservation = jdbcTemplate.query(sql, new ResultSetExtractor <Reservation>() {
            @Override
            public Reservation extractData(ResultSet rs) throws SQLException, DataAccessException {
                Reservation reservation = null;
                while (rs.next()){
                    reservation = Reservation.create(rs.getString("idReservation"),
                            rs.getString("idGuest"),
                            rs.getString("idHost"),
                            rs.getString("idListing"),
                            rs.getDate("r.check_in_date").toLocalDate(),
                            rs.getDate("r.check_out_date").toLocalDate(),
                            rs.getInt("r.number_guests"),
                            rs.getBigDecimal("r.total_price"),
                            ReservationStatus.valueOf(rs.getString("s.status")));
                }
                return reservation;
            }
        },reservationId);
        return Optional.ofNullable(reservation);
    }
}
