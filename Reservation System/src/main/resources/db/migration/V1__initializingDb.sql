
    DELIMITER //

    CREATE PROCEDURE init_db()
    BEGIN
    #create phone number table
    CREATE TABLE IF NOT EXISTS `phone_number` (
  `id_phone_number` binary(16) NOT NULL,
  `country_code` varchar(4) NOT NULL,
  `number` varchar(12) NOT NULL,
  PRIMARY KEY (`id_phone_number`),
  UNIQUE KEY `id_phone_number_UNIQUE` (`id_phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


    #create account table
		CREATE TABLE IF NOT EXISTS `account` (
	  `id_user` binary(16) NOT NULL,
	  `username` varchar(55) NOT NULL,
	  `name` varchar(55) NOT NULL,
	  `surname` varchar(55) NOT NULL,
	  `email` varchar(255) NOT NULL,
	  `birth_date` date NOT NULL,
	  `password` varchar(255) NOT NULL,
	  `profile_picture_path` varchar(128) NOT NULL,
	  `phone_number_id` binary(16) NOT NULL,
	  `created_at` datetime NOT NULL,
	  `verified` tinyint NOT NULL,
	  `enabled` tinyint(1) NOT NULL,
	  PRIMARY KEY (`id_user`),
	  UNIQUE KEY `username` (`username`),
	  UNIQUE KEY `email` (`email`),
	  UNIQUE KEY `id_user_UNIQUE` (`id_user`),
	  KEY `fk_account_phoneNumber_idx` (`phone_number_id`),
	  CONSTRAINT `fk_account_phoneNumber` FOREIGN KEY (`phone_number_id`) REFERENCES `phone_number` (`id_phone_number`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

	#create verification token table
    CREATE TABLE IF NOT EXISTS `verification_token` (
  `id_verification_token` int NOT NULL AUTO_INCREMENT,
  `token` binary(16) NOT NULL,
  `created_at` date NOT NULL,
  `expires_at` date NOT NULL,
  `account_id` binary(16) NOT NULL,
  PRIMARY KEY (`id_verification_token`),
  UNIQUE KEY `token` (`token`),
  UNIQUE KEY `id_verification_token_UNIQUE` (`id_verification_token`),
  KEY `fk_verificationToken_account_idx` (`account_id`),
  CONSTRAINT `fk_verificationToken_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=2247 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

#create property type
CREATE TABLE IF NOT EXISTS `property_type` (
  `id_property_type` INT NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id_property_type`),
  UNIQUE KEY `idproperty_type_UNIQUE` (`id_property_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO `property_type` (`type`) VALUES
('HOUSE'),('APARTMENT'),('BARN'),('BED_AND_BREAKFAST'),('BOAT'),('CABIN'),
('CAMPER'),('CASTLE'),('CAVE'),('CONTAINER'),('FARM'),('HOTEL');


#create reservation type table
CREATE TABLE IF NOT EXISTS `reservation_type` (
  `id_reservation_type`INT NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id_reservation_type`),
  UNIQUE KEY `idreservation_type_UNIQUE` (`id_reservation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO `reservation_type` (`type`) VALUES
('OWNER_APPROVAL'),('AUTOMATIC_APPROVAL');


#create housing type table
CREATE TABLE IF NOT EXISTS `housing_type` (
  `id_housing_type` INT NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id_housing_type`),
  UNIQUE KEY `idhousing_type_UNIQUE` (`id_housing_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `housing_type` (`type`) VALUES
('ENTIRE'),
('ROOM'),
('SHARED_ROOM');

# create amenities table
CREATE TABLE IF NOT EXISTS `amenities` (
  `id_amenities` INT NOT NULL AUTO_INCREMENT,
  `amenity` varchar(45) NOT NULL,
  PRIMARY KEY (`id_amenities`),
  UNIQUE KEY `idamenities_UNIQUE` (`id_amenities`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO amenities (amenity) VALUES
('WIFI'),('TV'),('KITCHEN'),('WASHER'),('FREE_PARKING'),('AIR_CONDITIONING'),('DEDICATED_WORKSPACE'),
('POOL'),('PATIO'),('BBQ_GRILL'),('LAKE_ACCESS'),('BEACH_ACCESS'),('GYM');

#create address table
CREATE TABLE IF NOT EXISTS `address` (
  `id_address_info` binary(16) NOT NULL,
  `country` varchar(60) NOT NULL,
  `street_address` varchar(200) NOT NULL,
  `apartment_number` varchar(45) DEFAULT NULL,
  `postal_code` varchar(40) NOT NULL,
  `region` varchar(40) NOT NULL,
  `locality` varchar(40) NOT NULL,
  PRIMARY KEY (`id_address_info`),
  UNIQUE KEY `idaddressInfo_UNIQUE` (`id_address_info`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

#create listing table
CREATE TABLE  IF NOT EXISTS `listing` (
  `id_listing` binary(16) NOT NULL,
  `host_id` binary(16) NOT NULL,
  `title` varchar(32) NOT NULL,
  `description` varchar(500) NOT NULL,
  `address_id` binary(16) NOT NULL,
  `number_guest_allowed` int NOT NULL,
  `number_beds` int NOT NULL,
  `number_bedrooms` int NOT NULL,
  `number_bathrooms` int NOT NULL,
  `price_per_night` decimal(10,2) NOT NULL,
  `property_type_id` int NOT NULL,
  `housing_type_id` int NOT NULL,
  `reservation_type_id` int NOT NULL,
  `created_at` date NOT NULL,
  `enabled` tinyint NOT NULL,
  PRIMARY KEY (`id_listing`),
  UNIQUE KEY `id_listing_UNIQUE` (`id_listing`),
  KEY `fk_listing_account_id_idx` (`host_id`),
  KEY `fk_listing_address_address_id_idx` (`address_id`),
  KEY `fk_listing_property_type_property_type_id_idx` (`property_type_id`),
  KEY `fk_listing_housing_type_housing_type_id_idx` (`housing_type_id`),
  KEY `fk_listing_reservation_type_reservation_type_id_idx` (`reservation_type_id`),
  CONSTRAINT `fk_listing_account_host_id` FOREIGN KEY (`host_id`) REFERENCES `account` (`id_user`),
  CONSTRAINT `fk_listing_address_address_id` FOREIGN KEY (`address_id`) REFERENCES `address` (`id_address_info`),
  CONSTRAINT `fk_listing_housing_type_housing_type_id` FOREIGN KEY (`housing_type_id`) REFERENCES `housing_type` (`id_housing_type`),
  CONSTRAINT `fk_listing_property_type_property_type_id` FOREIGN KEY (`property_type_id`) REFERENCES `property_type` (`id_property_type`),
  CONSTRAINT `fk_listing_reservation_type_reservation_type_id` FOREIGN KEY (`reservation_type_id`) REFERENCES `reservation_type` (`id_reservation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

#create listing amenities table
CREATE TABLE IF NOT EXISTS `listing_amenities` (
  `id_listing_amenities` int NOT NULL AUTO_INCREMENT,
  `listing_id` binary(16) NOT NULL,
  `amenities_id` INT NOT NULL,
  PRIMARY KEY (`id_listing_amenities`),
  UNIQUE KEY `id_listing_amenities_UNIQUE` (`id_listing_amenities`),
  KEY `fk_listing_amenities_listing_listing_id_idx` (`listing_id`),
  KEY `fk_listing_amenities_amenities_amenities_id_idx` (`amenities_id`),
  CONSTRAINT `fk_listing_amenities_amenities_amenities_id` FOREIGN KEY (`amenities_id`) REFERENCES `amenities` (`id_amenities`),
  CONSTRAINT `fk_listing_amenities_listing_listing_id` FOREIGN KEY (`listing_id`) REFERENCES `listing` (`id_listing`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

#create listing images table
CREATE TABLE IF NOT EXISTS `listing_images` (
  `id_listing_images` int NOT NULL AUTO_INCREMENT,
  `filepath` varchar(200) NOT NULL,
  `listing_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id_listing_images`),
  UNIQUE KEY `id_listing_images_UNIQUE` (`id_listing_images`),
  KEY `fk_listing_images_listing_listing_id_idx` (`listing_id`),
  CONSTRAINT `fk_listing_images_listing_listing_id` FOREIGN KEY (`listing_id`) REFERENCES `listing` (`id_listing`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


END //
DELIMITER ;


DELIMITER //

CREATE PROCEDURE drop_db()
BEGIN
  -- Drop child tables first due to foreign key constraints
  DROP TABLE IF EXISTS listing_images;
  DROP TABLE IF EXISTS listing_amenities;
  DROP TABLE IF EXISTS listing;
  DROP TABLE IF EXISTS amenities;
  DROP TABLE IF EXISTS address;
  DROP TABLE IF EXISTS housing_type;
  DROP TABLE IF EXISTS reservation_type;
  DROP TABLE IF EXISTS property_type;
  DROP TABLE IF EXISTS verification_token;
  DROP TABLE IF EXISTS account;
  DROP TABLE IF EXISTS phone_number;
END //

DELIMITER ;