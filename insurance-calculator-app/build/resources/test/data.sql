INSERT INTO classifiers(title, description)
VALUES ('RISK_TYPE', 'Risk type classifier'),
       ('COUNTRY', 'Country classifier'),
       ('MEDICAL_RISK_LIMIT_LEVEL', 'Medical risk limit level classifier');

INSERT INTO classifier_values(classifier_id, ic, description)
VALUES ((SELECT id FROM classifiers WHERE title = 'RISK_TYPE'), 'TRAVEL_MEDICAL', 'Travel policy medical risk type'),
       ((SELECT id FROM classifiers WHERE title = 'RISK_TYPE'), 'TRAVEL_CANCELLATION',
        'Travel policy trip cancellation risk type'),
       ((SELECT id FROM classifiers WHERE title = 'RISK_TYPE'), 'TRAVEL_LOSS_BAGGAGE',
        'Travel policy baggage loss risk type'),
       ((SELECT id FROM classifiers WHERE title = 'RISK_TYPE'), 'TRAVEL_THIRD_PARTY_LIABILITY',
        'Travel policy third party liability risk type'),
       ((SELECT id FROM classifiers WHERE title = 'RISK_TYPE'), 'TRAVEL_EVACUATION',
        'Travel policy evacuation risk type'),
       ((SELECT id FROM classifiers WHERE title = 'RISK_TYPE'), 'TRAVEL_SPORT_ACTIVITIES',
        'Travel policy sport activities risk type');

INSERT INTO classifier_values(classifier_id, ic, description)
VALUES ((SELECT id FROM classifiers WHERE title = 'COUNTRY'), 'LATVIA', 'Country Latvia'),
       ((SELECT id FROM classifiers WHERE title = 'COUNTRY'), 'SPAIN', 'Country Spain'),
       ((SELECT id FROM classifiers WHERE title = 'COUNTRY'), 'JAPAN', 'Country Japan');

INSERT INTO classifier_values(classifier_id, ic, description)
VALUES ((SELECT id FROM classifiers WHERE title = 'MEDICAL_RISK_LIMIT_LEVEL'), 'LEVEL_10000',
        'Medical risk 10000 euro limit level'),
       ((SELECT id FROM classifiers WHERE title = 'MEDICAL_RISK_LIMIT_LEVEL'), 'LEVEL_15000',
        'Medical risk 15000 euro limit level'),
       ((SELECT id FROM classifiers WHERE title = 'MEDICAL_RISK_LIMIT_LEVEL'), 'LEVEL_20000',
        'Medical risk 20000 euro limit level'),
       ((SELECT id FROM classifiers WHERE title = 'MEDICAL_RISK_LIMIT_LEVEL'), 'LEVEL_50000',
        'Medical risk 50000 euro limit level');

INSERT INTO country_default_day_rate(country_ic, default_day_rate)
VALUES ('LATVIA', 1.00),
       ('SPAIN', 2.50),
       ('JAPAN', 3.50);

INSERT INTO age_coefficient(age_from, age_to, coefficient)
VALUES (0, 5, 1.1),
       (6, 10, 0.7),
       (11, 17, 1.0),
       (18, 40, 1.1),
       (41, 65, 1.2),
       (66, 150, 1.5);

INSERT INTO medical_risk_limit_level(medical_risk_limit_level_ic, coefficient)
VALUES ('LEVEL_10000', 1.0),
       ('LEVEL_15000', 1.2),
       ('LEVEL_20000', 1.5),
       ('LEVEL_50000', 2.0);
