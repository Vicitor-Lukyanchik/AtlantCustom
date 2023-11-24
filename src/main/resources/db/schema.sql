DROP TABLE IF EXISTS import CASCADE;
DROP TABLE IF EXISTS export CASCADE;
DROP TABLE IF EXISTS unit CASCADE;
DROP TABLE IF EXISTS declaration CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id       BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    username VARCHAR(50)           NOT NULL,
    password VARCHAR(60)           NOT NULL
);


INSERT INTO users (username, password)
VALUES ('user', 'user'),
       ('admin', 'admin');


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

CREATE TABLE export(
      id       BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
      declaration_id BIGINT NOT NULL REFERENCES declaration(id),

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

CREATE TABLE import(
      id       BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
      declaration_id BIGINT NOT NULL REFERENCES declaration(id),

      tnved VARCHAR(10) NOT NULL
);
