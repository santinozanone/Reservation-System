#create phone_number table
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;