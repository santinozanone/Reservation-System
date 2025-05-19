package com.sz.reservation.listingManagement.infrastructure.adapter.outbound;

import com.sz.reservation.listingManagement.application.useCase.listing.ListingImageMetadata;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingImageMetadataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class MySqlListingImageMetadataRepository implements ListingImageMetadataRepository {

    private static final Logger log = LogManager.getLogger(MySqlListingImageMetadataRepository.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MySqlListingImageMetadataRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(ListingImageMetadata listingImageMetadata) {
        log.debug("ImageId:{}",listingImageMetadata.getImageId());
        String sql = "INSERT into listing_images (id_listing_images,filepath,listing_id) values (UUID_TO_BIN(?),?,UUID_TO_BIN(?))";
        jdbcTemplate.update(sql,listingImageMetadata.getImageId(),listingImageMetadata.getPathName(),listingImageMetadata.getListingId());
    }

    @Override
    public Optional<ListingImageMetadata> findById(String imageId) {
        String sql = "SELECT BIN_TO_UUID(id_listing_images),filepath, BIN_TO_UUID(listing_id) from listing_images where id_listing_images = UUID_TO_BIN(?)";
        ListingImageMetadata listingImageMetadata = jdbcTemplate.query(sql, new ResultSetExtractor<ListingImageMetadata>() {
            @Override
            public ListingImageMetadata extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()){
                    return new ListingImageMetadata(
                            rs.getString("id_listing_images"),
                            rs.getString("filepath"),
                            rs.getString("listing_id"));
                }
                return null;
            }
        });
        return Optional.ofNullable(listingImageMetadata);
    }


}
