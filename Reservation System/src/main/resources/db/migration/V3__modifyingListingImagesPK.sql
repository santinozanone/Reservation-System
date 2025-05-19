DROP TABLE IF EXISTS listing_images;
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