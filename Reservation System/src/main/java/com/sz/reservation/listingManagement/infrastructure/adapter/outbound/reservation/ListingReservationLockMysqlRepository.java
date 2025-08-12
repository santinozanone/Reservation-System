package com.sz.reservation.listingManagement.infrastructure.adapter.outbound.reservation;

import com.sz.reservation.listingManagement.domain.port.outbound.ListingReservationLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ListingReservationLockMysqlRepository implements ListingReservationLockRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ListingReservationLockMysqlRepository(@Qualifier("listing.jdbcTemplate")JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void lockByListingId(String listingId) {
        String sql = "SELECT 1 FROM " +
                "listing_reservation_lock where " +
                "id_listing = UUID_TO_BIN(?) and enabled = true " +
                "FOR UPDATE";
        jdbcTemplate.query(sql,rs -> {},listingId);
    }
}
