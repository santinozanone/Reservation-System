create table `listing_reservation_lock`(
    `id_listing` binary(16) NOT NULL,
    `enabled` tinyint NOT NULL,
    PRIMARY KEY(`id_listing`),
    UNIQUE KEY `id_listing_UNIQUE` (`id_listing`),
    KEY `fk_listing_lock_listing_id_listing_id_listingidx` (`id_listing`),
    CONSTRAINT `fk_listing_lock_listing_id_listing` FOREIGN KEY (`id_listing`) REFERENCES `listing` (`id_listing`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;