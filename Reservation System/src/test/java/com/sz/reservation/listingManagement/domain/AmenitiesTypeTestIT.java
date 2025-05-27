package com.sz.reservation.listingManagement.domain;

import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.listingManagement.configuration.ListingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


//THIS CLASS WILL TEST THAT THE VALUES ON THE Amenities Type TABLE MATCHES THE ENUM ONES

@WebAppConfiguration
@ActiveProfiles(profiles = {"test","default"})
@ContextConfiguration(classes = {RootConfig.class, ListingConfig.class})
@ExtendWith(SpringExtension.class)
@DisplayName("Amenities Type Enum , Database SYNC test")

class AmenitiesTypeTestIT {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Test
    public void AllEnumValues_ShouldMatch_DB(){
        //arrange
        String sql = "SELECT * FROM amenities";
        Map<Integer,String> resultMap = new HashMap<>();
        AmenitiesType[] amenitiesTypes = AmenitiesType.values();

        //act
        // put everything in a map in case its not ordered from db
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                resultMap.put(rs.getInt("id_amenities"),rs.getString("amenity"));
            }
        });

        //assert
        for (int i = 0; i<amenitiesTypes.length; i++){
            String type = amenitiesTypes[i].name();
            int id = amenitiesTypes[i].getId();
            assertEquals(type,resultMap.get(id),"Mismatch: DB has extra or missing rows compared to enum");
        }
    }

}