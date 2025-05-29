ALTER TABLE account DROP COLUMN profile_picture_path;
CREATE TABLE `profile_picture` (
  `id_pfp` BINARY(16) NOT NULL,
  `filepath` VARCHAR(255) NOT NULL,
  `account_id` BINARY(16) NOT NULL,
  PRIMARY KEY (`id_pfp`),
  UNIQUE INDEX `id_pfp_UNIQUE` (`id_pfp` ASC) VISIBLE,
  INDEX `fk_profile_picture_account_idx` (`account_id` ASC) VISIBLE,
  CONSTRAINT `fk_profile_picture_account`
    FOREIGN KEY (`account_id`)
    REFERENCES `usertestdb`.`account` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
