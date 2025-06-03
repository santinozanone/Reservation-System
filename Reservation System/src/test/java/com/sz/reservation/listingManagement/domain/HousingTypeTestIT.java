package com.sz.reservation.listingManagement.domain;

import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.listingManagement.configuration.ListingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

//THIS CLASS WILL TEST THAT THE VALUES ON THE Housing Type TABLE MATCHES THE ENUM ONES

@WebAppConfiguration
@ActiveProfiles(profiles = {"test","default"})
@ContextConfiguration(classes = {RootConfig.class, ListingConfig.class})
@ExtendWith(SpringExtension.class)
@DisplayName("Housing Type Enum , Database SYNC test")
class HousingTypeTestIT {

    @Autowired
    @Qualifier("listing.jdbcTemplate")
    JdbcTemplate jdbcTemplate;


    @Test
    public void AllEnumValues_ShouldMatch_DB(){
        //arrange
        String sql = "SELECT * FROM housing_type";
        Map<Integer,String> resultMap = new HashMap<>();
        HousingType[] housingTypes = HousingType.values();


        //act
        // put everything in a map in case its not ordered from db
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                resultMap.put(rs.getInt("id_housing_type"),rs.getString("type"));
            }
        });

        //assert
        for (int i = 0; i<housingTypes.length; i++){
            String type = housingTypes[i].name();
            int id = housingTypes[i].getId();
            assertEquals(type,resultMap.get(id),"Mismatch: DB has extra or missing rows compared to enum");
        }
    }

}