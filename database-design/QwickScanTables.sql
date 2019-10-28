
DROP TABLE location_prices;
DROP TABLE ratings;
DROP TABLE web_prices;

DROP TABLE store_locations;
DROP TABLE url_addresses;

DROP TABLE stores;
DROP TABLE scans;
DROP TABLE products;
DROP TABLE users;

CREATE TABLE users(
  userID              BIGSERIAL         NOT NULL,
  firebaseUID         VARCHAR(50)       NOT NULL,
  userName            VARCHAR(50),
  CONSTRAINT users_pk PRIMARY KEY(userID),
  CONSTRAINT users_ck UNIQUE(firebaseUID)
);

CREATE TABLE products(
  productID           BIGSERIAL         NOT NULL,
  barcode             VARCHAR(30)       NOT NULL,
  productName         VARCHAR(150),
  imageURL            VARCHAR(500),
  CONSTRAINT products_pk PRIMARY KEY(productID),
  CONSTRAINT products_ck_1 UNIQUE(barcode),
  CONSTRAINT products_ck_2 UNIQUE(productName)
);

CREATE TABLE scans(
  userID              BIGINT            NOT NULL,
  productID           BIGINT            NOT NULL,
  dateTimeScanned     TIMESTAMP         NOT NULL,
  CONSTRAINT scans_pk PRIMARY KEY(userID, productID, dateTimeScanned),
  CONSTRAINT scans_fk_1 FOREIGN KEY(userID) REFERENCES users(userID)
    ON UPDATE CASCADE ON DELETE NO ACTION,
  CONSTRAINT scans_fk_2 FOREIGN KEY(productID) REFERENCES products(productID)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE stores(
  storeID             BIGINT            NOT NULL,
  storeName           VARCHAR(30)       NOT NULL,
  CONSTRAINT stores_pk PRIMARY KEY(storeID),
  CONSTRAINT stores_ck UNIQUE(storeName)
);

CREATE TABLE store_locations(
  locationID          BIGSERIAL         NOT NULL,
  storeID             BIGINT            NOT NULL,
  point               GEOGRAPHY(POINT)  NOT NULL,
  CONSTRAINT store_locations_pk PRIMARY KEY(locationID),
  CONSTRAINT store_locations_ck UNIQUE(point),
  CONSTRAINT store_locations_fk FOREIGN KEY(storeID) REFERENCES stores(storeID)
    ON UPDATE CASCADE ON DELETE NO ACTION
);

CREATE TABLE url_addresses(
  addressID           BIGSERIAL         NOT NULL,
  storeID             BIGINT            NOT NULL,
  address             VARCHAR(500)      NOT NULL,
  CONSTRAINT url_addresses_pk PRIMARY KEY(addressID),
  CONSTRAINT url_addresses_ck UNIQUE(address),
  CONSTRAINT url_addresses_fk FOREIGN KEY(storeID) REFERENCES stores(storeID)
    ON UPDATE CASCADE ON DELETE NO ACTION
);

CREATE TABLE location_prices(
  productID           BIGINT            NOT NULL,
  locationID          BIGINT            NOT NULL,
  price               DOUBLE PRECISION  NOT NULL,
  priceDate           DATE              NOT NULL,
  CONSTRAINT location_prices_pk PRIMARY KEY(productID, locationID, priceDate),
  CONSTRAINT location_prices_fk_1 FOREIGN KEY(productID) REFERENCES products(productID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT location_prices_fk_2 FOREIGN KEY(locationID) REFERENCES store_locations(locationID)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE web_prices(
  productID           BIGINT            NOT NULL,
  addressID           BIGINT            NOT NULL,
  price               DOUBLE PRECISION  NOT NULL,
  priceDate           DATE              NOT NULL,
  CONSTRAINT web_prices_pk PRIMARY KEY(productID, addressID, priceDate),
  CONSTRAINT web_prices_fk_1 FOREIGN KEY(productID) REFERENCES products(productID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT web_prices_fk_2 FOREIGN KEY(addressID) REFERENCES url_addresses(addressID)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ratings(
  productID           BIGINT            NOT NULL,
  storeID             BIGINT            NOT NULL,
  rating              DOUBLE PRECISION  NOT NULL,
  ratingDate          DATE              NOT NULL,
  CONSTRAINT ratings_pk PRIMARY KEY(productID, storeID, ratingDate),
  CONSTRAINT ratings_fk_1 FOREIGN KEY(productID) REFERENCES products(productID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT ratings_fk_2 FOREIGN KEY(storeID) REFERENCES stores(storeID)
    ON UPDATE CASCADE ON DELETE CASCADE
);



INSERT INTO users   (firebaseUID, userName)
  VALUES            ('randomUID', 'miguelangel'),
                    ('jafarsUID', 'jafar');

SELECT * FROM users;

INSERT INTO scans   (userid, productid, datetimescanned)
    VALUES          (1, 1, CURRENT_TIMESTAMP);










