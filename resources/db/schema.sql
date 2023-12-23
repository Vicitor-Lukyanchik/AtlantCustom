DROP TABLE IF EXISTS consumption CASCADE;
DROP TABLE IF EXISTS arrival CASCADE;
DROP TABLE IF EXISTS export CASCADE;
DROP TABLE IF EXISTS unit CASCADE;
DROP TABLE IF EXISTS currency CASCADE;
DROP TABLE IF EXISTS declaration CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id       BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    username VARCHAR(50)           NOT NULL,
    password VARCHAR(60)           NOT NULL
);

INSERT INTO users (username, password)
VALUES ('admin', '$2a$12$uRrIMv/hpgOYRogVKrJLV.4ev0RVUlYUHV0bgwVzKxV15Ko5i44lO'),
       ('user', '$2a$12$5FFM8eTRdp1AFEyvimVWG.iNv5kIYvARy2bzb/bB4S9E4YTWENdsa');

CREATE TABLE declaration
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_kod BIGINT NOT NULL,
    is_import BOOLEAN           NOT NULL,
    declaration_date DATE NOT NULL,
    number VARCHAR(23) NOT NULL,
    import_application_number  VARCHAR(23),
    contract_number VARCHAR(32),
    contract_date DATE,
    firm_name VARCHAR(60)
);

CREATE TABLE unit(
      id       BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
      unit_code INTEGER,
      unit_name VARCHAR(12)
);

CREATE TABLE currency(
      id       BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
      currency_code INTEGER,
      currency_name VARCHAR(25)
);

CREATE TABLE export(
      id       BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
      declaration_id BIGINT REFERENCES declaration(id),

      tnved VARCHAR(10) NOT NULL,

      ttn_series VARCHAR(4) NOT NULL,
      ttn_number VARCHAR(8) NOT NULL,
      ttn_date DATE NOT NULL,

      product_name VARCHAR(180),
      product_weight DOUBLE NOT NULL,
      product_count INTEGER NOT NULL,
      product_sum DECIMAL NOT NULL,

      unit_id BIGINT REFERENCES unit(id),

      date_time TIMESTAMP NOT NULL,
      fio VARCHAR(15)
);

CREATE TABLE consumption(
    id       BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    declaration_id BIGINT REFERENCES declaration(id),
    storehouse_id BIGINT,

    storehouse_number VARCHAR(2),

    product_code VARCHAR(15),
    product_name VARCHAR(80),
    turnover_count DOUBLE ,
    product_price DECIMAL,
    arrival_date DATE ,
    unit_id BIGINT REFERENCES unit(id),
    currency_id BIGINT REFERENCES currency(id),


    external_receiver_code INTEGER,
    external_receiver_name VARCHAR(80),


    document_type INTEGER,
    document_number INTEGER ,
    reporting_month INTEGER ,
    reporting_year INTEGER ,

    accompanying_document_number VARCHAR(8),
    accompanying_document_series VARCHAR(4),
    accompanying_document_date DATE,

    nom_f VARCHAR(20),
    dat_f DATE,


    workshop_receiver VARCHAR(2),
    application_number VARCHAR(10)
);

CREATE TABLE arrival(
    id       BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    declaration_id BIGINT  REFERENCES declaration(id),
    storehouse_id BIGINT ,

    storehouse_number VARCHAR(2),

    product_code VARCHAR(15) ,
    product_name VARCHAR(80) ,
    turnover_count DOUBLE ,
    product_price DECIMAL ,
    arrival_date DATE ,
    unit_id BIGINT REFERENCES unit(id),
    currency_id BIGINT REFERENCES currency(id),


    external_receiver_code INTEGER,
    external_receiver_name VARCHAR(80),


    document_type INTEGER,
    document_number INTEGER  ,
    reporting_month INTEGER  ,
    reporting_year INTEGER  ,

    accompanying_document_number VARCHAR(8),
    accompanying_document_series VARCHAR(4),
    accompanying_document_date DATE,

    nom_f VARCHAR(20),
    dat_f DATE,


    workshop_receiver VARCHAR(2),
    application_number VARCHAR(10)
);
