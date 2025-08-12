#create account table
CREATE TABLE IF NOT EXISTS `account` (
	  `id_account` binary(16) NOT NULL,
	  `username` varchar(55) NOT NULL,
	  `name` varchar(55) NOT NULL,
	  `surname` varchar(55) NOT NULL,
	  `email` varchar(255) NOT NULL,
	  `enabled` tinyint(1) NOT NULL,
	  PRIMARY KEY (`id_account`),
	  UNIQUE KEY `username` (`username`),
	  UNIQUE KEY `email` (`email`),
	  UNIQUE KEY `id_account_UNIQUE` (`id_account`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



#create property type
CREATE TABLE IF NOT EXISTS `property_type` (
  `id_property_type` INT NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id_property_type`),
  UNIQUE KEY `idproperty_type_UNIQUE` (`id_property_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


#create reservation type table
CREATE TABLE IF NOT EXISTS `reservation_type` (
  `id_reservation_type`INT NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id_reservation_type`),
  UNIQUE KEY `idreservation_type_UNIQUE` (`id_reservation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

#create reservation status table
CREATE TABLE IF NOT EXISTS `reservation_status` (
  `id_reservation_status`INT NOT NULL AUTO_INCREMENT,
  `status` varchar(45) NOT NULL,
  PRIMARY KEY (`id_reservation_status`),
  UNIQUE KEY `idreservation_status_UNIQUE` (`id_reservation_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


#create housing type table
CREATE TABLE IF NOT EXISTS `housing_type` (
  `id_housing_type` INT NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id_housing_type`),
  UNIQUE KEY `idhousing_type_UNIQUE` (`id_housing_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


# create amenities table
CREATE TABLE IF NOT EXISTS `amenities` (
  `id_amenities` INT NOT NULL AUTO_INCREMENT,
  `amenity` varchar(45) NOT NULL,
  PRIMARY KEY (`id_amenities`),
  UNIQUE KEY `idamenities_UNIQUE` (`id_amenities`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


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
  CONSTRAINT `fk_listing_account_host_id` FOREIGN KEY (`host_id`) REFERENCES `account` (`id_account`),
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
`id_listing_images` binary(16) NOT NULL,
`filepath` varchar(200) NOT NULL,
`listing_id` binary(16) DEFAULT NULL,
PRIMARY KEY (`id_listing_images`),
UNIQUE KEY `id_listing_images_UNIQUE` (`id_listing_images`),
KEY `fk_listing_images_listing_listing_id_idx` (`listing_id`),
CONSTRAINT `fk_listing_images_listing_listing_id` FOREIGN KEY (`listing_id`)
REFERENCES `listing` (`id_listing`))
 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


#create reservation table
CREATE TABLE IF NOT EXISTS `reservation`(
    `id_reservation` binary(16) NOT NULL,
    `id_host` binary(16) not null,
    `id_guest` binary(16) not null,
    `id_listing` binary(16) not null,
    `check_in_date` date not null,
    `check_out_date` date not null,
    `number_guests` int not null,
    `total_price` decimal(10,2) not null,
    `id_status` int not null,
    PRIMARY KEY(`id_reservation`),
    UNIQUE KEY `id_reservation_UNIQUE` (`id_reservation`),
    KEY `fk_reservation_account_id_host_idx` (`id_host`),
    KEY `fk_reservation_account_id_guest_idx` (`id_guest`),
    KEY `fk_reservation_listing_id_listing_idx` (`id_listing`),
    KEY `fk_reservation_reservation_type_id_status_idx` (`id_status`),
    CONSTRAINT `fk_reservation_account_id_host` FOREIGN KEY (`id_host`) REFERENCES `account` (`id_account`),
    CONSTRAINT `fk_reservation_account_id_guest` FOREIGN KEY (`id_guest`) REFERENCES `account` (`id_account`),
    CONSTRAINT `fk_reservation_listing_id_listing` FOREIGN KEY (`id_listing`) REFERENCES `listing` (`id_listing`),
    CONSTRAINT `fk_reservation_reservation_type_id_status` FOREIGN KEY (`id_status`) REFERENCES `reservation_status` (`id_reservation_status`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



)
