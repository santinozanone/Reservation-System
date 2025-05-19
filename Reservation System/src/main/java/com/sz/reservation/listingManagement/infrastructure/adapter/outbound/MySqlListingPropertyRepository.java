package com.sz.reservation.listingManagement.infrastructure.adapter.outbound;

import com.sz.reservation.listingManagement.domain.ListingProperty;
import com.sz.reservation.listingManagement.domain.*;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MySqlListingPropertyRepository implements ListingPropertyRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MySqlListingPropertyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(String hostId, ListingProperty listingProperty) {
        if (listingProperty == null) throw new IllegalArgumentException("listing property cannot be null");

        // first, create an address
        AddressInfo addressInfo = listingProperty.getAddressInfo();
        String addressSql = "INSERT INTO address" +
                "(`id_address_info`,\n" +
                "`country`,\n" +
                "`street_address`,\n" +
                "`apartment_number`,\n" +
                "`postal_code`,\n" +
                "`region`,\n" +
                "`locality`)\n" +
                "VALUES (UUID_TO_BIN(?),?,?,?,?,?,?)";
        // make the address insert
        jdbcTemplate.update(addressSql, addressInfo.getId(), addressInfo.getCountry(), addressInfo.getStreetAddress(),
                addressInfo.getApartmentNumber(), addressInfo.getPostalCode(), addressInfo.getRegion(), addressInfo.getLocality());



        Integer propertyTypeId = listingProperty.getPropertyType().getId();
        Integer housingTypeId = listingProperty.getHousingType().getId();
        Integer reservationTypeId = listingProperty.getReservationType().getId();

        //make the listing insert
        String listingSql = "INSERT INTO listing (id_listing,host_id,title,description,address_id,number_guest_allowed,number_beds,number_bedrooms,number_bathrooms,price_per_night,property_type_id,housing_type_id,reservation_type_id,created_at,enabled) VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?, UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?,?, ?, ?)";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime localDateTime = LocalDateTime.now();
        String currentDate = dateFormatter.format(localDateTime);
        jdbcTemplate.update(listingSql, listingProperty.getId(), hostId, listingProperty.getListingTitle(),
                listingProperty.getListingDescription(), addressInfo.getId(), listingProperty.getNumberOfGuestAllowed(),
                listingProperty.getNumberOfBeds(), listingProperty.getNumberOfBedrooms(), listingProperty.getNumberOfBathroom(),
                listingProperty.getPricePerNight(), propertyTypeId, housingTypeId, reservationTypeId, currentDate, listingProperty.isEnabled());


        //make the amenities insert IN BATCH
        String amenitiesSql = "INSERT INTO listing_amenities (listing_id,amenities_id) VALUES (UUID_TO_BIN(?),?)";

        List<Object[]> batch = new ArrayList<>();
        for (AmenitiesType a: listingProperty.getAmenities()){
            Object values[] = new Object[]{listingProperty.getId(),a.getId()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(amenitiesSql,batch);
    }

    @Override
    public Optional<ListingProperty> findById(String listingId) {
        String amenitiesSql = "SELECT a.amenity FROM listing_amenities as la " +
                "INNER JOIN amenities as a ON a.id_amenities = la.amenities_id " +
                "WHERE la.listing_id = UUID_TO_BIN(?)";

        String listingSql = "SELECT BIN_TO_UUID(l.id_listing) as listingID, BIN_TO_UUID(l.host_id) as host_id,l.title, l.description" +
                ",BIN_TO_UUID(ad.id_address_info) as addressID, ad.country,ad.street_address,ad.apartment_number" +
                ",ad.postal_code,ad.region,ad.locality" +
                ",l.number_guest_allowed, l.number_beds ,l.number_bedrooms" +
                ",l.number_bathrooms,l.price_per_night,l.enabled" +
                ",pt.type as p_type , rt.type as r_type ,ht.type as h_type " +
                "from listing as l " +
                "inner join address as ad on l.address_id = ad.id_address_info " +
                "inner join property_type as pt on pt.id_property_type = l.property_type_id " +
                "inner join housing_type as ht on ht.id_housing_type = l.housing_type_id " +
                "inner join reservation_type as rt on rt.id_reservation_type = reservation_type_id " +
                "where l.id_listing = UUID_TO_BIN(?)";

        // TODO : ADD VERIFICATION FOR EMPTY OR NULL LIST OF AMENITIES
       List<AmenitiesType> amenitiesList = jdbcTemplate.query(amenitiesSql, rs ->{
           List<AmenitiesType> amenitiesTypeList = new ArrayList<>();
           while (rs.next()){
                amenitiesTypeList.add(AmenitiesType.valueOf(rs.getString("amenity")));
            }
           return amenitiesTypeList;
        },listingId);


        ListingProperty listing = jdbcTemplate.query(listingSql, rs -> {
            if (!rs.next()) return null;

            AddressInfo addressInfo = new AddressInfo(
                    rs.getString("addressID"),
                    rs.getString("ad.apartment_number"),
                    rs.getString("ad.country"),
                    rs.getString("ad.locality"),
                    rs.getString("ad.postal_code"),
                    rs.getString("ad.region"),
                    rs.getString("ad.street_address")
            );

            return new ListingProperty(
                    rs.getString("listingID"),
                    rs.getString("host_id"),
                    rs.getString("l.title"),
                    rs.getString("l.description"),
                    addressInfo,
                    rs.getInt("l.number_guest_allowed"),
                    rs.getInt("l.number_beds"),
                    rs.getInt("l.number_bedrooms"),
                    rs.getInt("l.number_bathrooms"),
                    rs.getDouble("l.price_per_night"),
                    PropertyType.valueOf(rs.getString("p_type")),
                    HousingType.valueOf(rs.getString("h_type")),
                    ReservationType.valueOf(rs.getString("r_type")),
                    amenitiesList,
                    rs.getBoolean("l.enabled")
            );
        }, listingId);
        return Optional.ofNullable(listing);
    }

    @Override
    public void delete(String listingId) {
        String sql = "Update listing set enabled = false where id_listing = UUID_TO_BIN(?)";
        jdbcTemplate.update(sql,listingId);
    }


}
