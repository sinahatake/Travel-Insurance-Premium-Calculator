CREATE TABLE classifiers
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    title       VARCHAR(200) NOT NULL,
    description VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_classifiers_title ON classifiers (title);

CREATE TABLE classifier_values
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    classifier_id BIGINT       NOT NULL,
    ic            VARCHAR(200) NOT NULL,
    description   VARCHAR(500) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (classifier_id) REFERENCES classifiers (id)
);

CREATE UNIQUE INDEX ix_classifier_values_ic ON classifier_values (ic);

CREATE TABLE country_default_day_rate
(
    id               BIGINT         NOT NULL AUTO_INCREMENT,
    country_ic       VARCHAR(200)   NOT NULL,
    default_day_rate NUMERIC(10, 2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_country_default_day_rate_country_ic
    ON country_default_day_rate (country_ic);

CREATE TABLE age_coefficient
(
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    age_from    INT           NOT NULL,
    age_to      INT           NOT NULL,
    coefficient DECIMAL(3, 1) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE medical_risk_limit_level
(
    id                          BIGINT         NOT NULL AUTO_INCREMENT,
    medical_risk_limit_level_ic VARCHAR(50),
    coefficient                 DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_medical_risk_limit_level_ic
    ON medical_risk_limit_level (medical_risk_limit_level_ic);

CREATE TABLE persons
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    person_code VARCHAR(50)  NOT NULL,
    birth_date  DATE         NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_persons_unique
    ON persons (first_name, last_name, person_code);

CREATE TABLE agreements
(
    id        BIGINT         NOT NULL AUTO_INCREMENT,
    uuid      VARCHAR(255)   NOT NULL,
    date_from DATE           NOT NULL,
    date_to   DATE           NOT NULL,
    country   VARCHAR(255)   NOT NULL,
    premium   DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ux_agreements_uuid UNIQUE (uuid)
);


CREATE TABLE selected_risks
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    agreement_id BIGINT       NOT NULL,
    risk_ic      VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_selected_risks_agreement
        FOREIGN KEY (agreement_id) REFERENCES agreements (id)
);

CREATE UNIQUE INDEX ix_selected_risks_agreement_id_risk_ic
    ON selected_risks (agreement_id, risk_ic);

CREATE TABLE agreement_persons
(
    id                       BIGINT       NOT NULL AUTO_INCREMENT,
    agreement_id             BIGINT       NOT NULL,
    person_id                BIGINT       NOT NULL,
    medical_risk_limit_level VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_agreement_person_agreement
        FOREIGN KEY (agreement_id) REFERENCES agreements (id),
    CONSTRAINT fk_agreement_person_person
        FOREIGN KEY (person_id) REFERENCES persons (id)
);

CREATE UNIQUE INDEX ix_agreement_person_agreement_id_person_id
    ON agreement_persons (agreement_id, person_id);

CREATE TABLE agreement_person_risks
(
    id                  BIGINT         NOT NULL AUTO_INCREMENT,
    agreement_person_id BIGINT         NOT NULL,
    risk_ic             VARCHAR(100)   NOT NULL,
    premium             DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_agreement_person_risks_agreement_person_id
        FOREIGN KEY (agreement_person_id) REFERENCES agreement_persons (id)
);

CREATE UNIQUE INDEX ix_agreement_person_risks_person_risk_ic
    ON agreement_person_risks (agreement_person_id, risk_ic);

CREATE TABLE agreements_xml_export
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    agreement_uuid   VARCHAR(255) NOT NULL,
    already_exported BOOLEAN      NOT NULL,
    CONSTRAINT ux_agreements_xml_export_uuid UNIQUE (agreement_uuid),
    CONSTRAINT fk_agreements_xml_export_agreement_uuid
        FOREIGN KEY (agreement_uuid) REFERENCES agreements (uuid)
);

CREATE TABLE agreement_proposals
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    agreement_uuid     VARCHAR(255) NOT NULL,
    already_generated  CHAR(1)      NOT NULL,
    proposal_file_path VARCHAR(255) NOT NULL,
    CONSTRAINT fk_agreement_proposals_agreement_uuid
        FOREIGN KEY (agreement_uuid) REFERENCES agreements (uuid)
);


