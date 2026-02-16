CREATE SCHEMA IF NOT EXISTS `java-real-practice-insurance`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `java-real-practice-insurance`;

CREATE TABLE `classifiers`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `title`       VARCHAR(200) NOT NULL,
    `description` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `ix_classifiers_title` (`title`)
) ENGINE = InnoDB;

CREATE TABLE `classifier_values`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `classifier_id` BIGINT       NOT NULL,
    `ic`            VARCHAR(200) NOT NULL,
    `description`   VARCHAR(500) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `ix_classifier_values_ic` (`ic`),
    CONSTRAINT `fk_classifier_values_classifiers`
        FOREIGN KEY (`classifier_id`) REFERENCES `classifiers` (`id`)
) ENGINE = InnoDB;

CREATE TABLE `country_default_day_rate`
(
    `id`               BIGINT         NOT NULL AUTO_INCREMENT,
    `country_ic`       VARCHAR(200)   NOT NULL,
    `default_day_rate` DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `ix_country_default_day_rate_country_ic` (`country_ic`)
) ENGINE = InnoDB;

CREATE TABLE `age_coefficient`
(
    `id`          BIGINT         NOT NULL AUTO_INCREMENT,
    `age_from`    INT            NOT NULL,
    `age_to`      INT            NOT NULL,
    `coefficient` DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE `medical_risk_limit_level`
(
    `id`                          BIGINT         NOT NULL AUTO_INCREMENT,
    `medical_risk_limit_level_ic` VARCHAR(50),
    `coefficient`                 DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `ix_medical_risk_limit_level_ic` (`medical_risk_limit_level_ic`)
) ENGINE = InnoDB;
