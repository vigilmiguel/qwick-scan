DEELTE FROM location_prices WHERE locationid = 1 AND productid = SELECT productid FROM products WHERE barcode = '1';
DELETE FROM store_locations WHERE locationid = 6 AND storeid = (SELECT storeid FROM stores WHERE storename = 'Testing store for test data!!!');
DELETE FROM products WHERE barcode='1';
DELETE FROM stores WHERE storename = 'Testing store for test data!!!';

DELETE FROM users WHERE userid <> 1 AND userid <> 2;

DELETE FROM web_prices;

SELECT * FROM users;
SELECT * FROM scans;
SELECT * FROM products;
SELECT * FROM stores;
SELECT * FROM store_locations;
SELECT * FROM location_prices;
SELECT * FROM url_addresses;
SELECT * FROM web_prices;

-- Get the user's scanned barcodes with the number of scans and the most recent date scanned.
SELECT t1.barcode, t1.numscans, t2.datetimescanned, t1.productname, t1.imageurl
  FROM( SELECT barcode, productname, imageurl, COUNT(barcode) AS numscans
          FROM users u  INNER JOIN scans s ON u.userid = s.userid
                        INNER JOIN products p ON p.productid = s.productid
          WHERE u.firebaseuid = 'hknnXVPmtuNvaSflPYNZ6Fm5ELp2'
        GROUP BY barcode, productname, imageurl) t1
        
  INNER JOIN

      ( SELECT p.barcode, p.productname, s.datetimescanned
          FROM users u  INNER JOIN scans s ON u.userid = s.userid
                        INNER JOIN products p ON p.productid = s.productid
        WHERE u.firebaseuid = 'hknnXVPmtuNvaSflPYNZ6Fm5ELp2' 
            AND NOT EXISTS( SELECT *
                              FROM users u1 INNER JOIN scans s1 ON u1.userid = s1.userid
                                            INNER JOIN products p1 ON p1.productid = s1.productid
                            WHERE s1.datetimescanned > s.datetimescanned AND p1.barcode = p.barcode)) t2
  ON t1.barcode = t2.barcode
ORDER BY t2.datetimescanned DESC;                

SELECT *
  FROM products
WHERE barcode = '123456789';

INSERT INTO stores (storeName) VALUES ('random store');

SELECT storeid
    FROM stores
WHERE storename = 'random store';


SELECT * FROM store_locations
  WHERE point = 'POINT(1.7453 1.53241)';

INSERT INTO store_locations
    (storeid, point)
    VALUES
    (1, 'POINT(1.7453 1.53241)');

SELECT sl.locationid, s.storeid, s.storename, ST_X(point::geometry) AS "longitude", ST_Y(point::geometry) AS "latitude"
    FROM store_locations sl INNER JOIN stores s ON sl.storeid = s.storeid;
    
-- Get nearest x locations given barcode, longitude and latitude.
SELECT productname, storename, price, imageurl, ST_X(sl.point::geometry) AS "longitude", 
ST_Y(sl.point::geometry) AS "latitude", ST_Distance(sl.point, 'POINT(1.1 1.0)') AS "distance"
  FROM products p   INNER JOIN location_prices lp ON p.productid = lp.productid
                    INNER JOIN store_locations sl ON sl.locationid = lp.locationid
                    INNER JOIN stores s ON s.storeid = sl.storeid
WHERE barcode = '2'
ORDER BY ST_Distance(sl.point, 'POINT(1.1 1.0)')
LIMIT 30;

-- Get the websites that sell a given barcode for the cheapest price.
SELECT productname, storename, price, imageurl, address
  FROM products p INNER JOIN web_prices wp ON p.productid = wp.productid
                  INNER JOIN url_addresses ua ON ua.addressid = wp.addressid
                  INNER JOIN stores s ON s.storeid = ua.storeid
WHERE barcode = '10'
ORDER BY price
LIMIT 20;


-- Get all stores and their locations that sell the given barcode.
SELECT productname, barcode, storename, price, ST_X(point::geometry) AS "longitude", ST_Y(point::geometry) AS "latitude"
  FROM stores s INNER JOIN store_locations sl ON s.storeid = sl.storeid
                INNER JOIN location_prices lp ON lp.locationid = sl.locationid
                INNER JOIN products p ON p.productid = lp.productid
WHERE barcode = '2';

-- Get all stores and web addresses that sell this product.
SELECT *--productname, barcode, storename, price, address
  FROM stores s INNER JOIN url_addresses ua ON s.storeid = ua.storeid
                INNER JOIN web_prices wp ON wp.addressid = ua.addressid
                INNER JOIN products p ON p.productid = wp.productid
WHERE barcode = '814855028825';


INSERT INTO location_prices
  (productid, locationid, price, pricedate)
  VALUES
  (9, 1, 13.79, NOW());
  
INSERT INTO web_prices
  (productid, addressid, price, pricedate)
  VALUES
  (50, 23, 34.32, NOW() );
  
INSERT INTO products (barcode, productname, imageurl)
  VALUES ('1', '.:Headphones Test:.', NULL)
 ON CONFLICT DO NOTHING;
 

DROP TABLE barcode_queue;

CREATE TABLE barcode_queue(
  queueid         BIGSERIAL         NOT NULL,
  barcode         VARCHAR(30)       NOT NULL,
  longitude       DOUBLE PRECISION  NOT NULL,
  latitude        DOUBLE PRECISION  NOT NULL,
  dateTime        TIMESTAMP         NOT NULL,
  CONSTRAINT barcode_queue_pk PRIMARY KEY(queueid)
);

SELECT * FROM barcode_queue
ORDER BY queueid DESC;

INSERT INTO barcode_queue (barcode, longitude, latitude, dateTime) VALUES ('100', 12.31, 122.3, CURRENT_TIMESTAMP);

-- Get the row with the least value in queueid
SELECT *
  FROM barcode_queue bq
WHERE NOT EXISTS( SELECT * 
                    FROM barcode_queue bq1
                  WHERE bq1.queueid < bq.queueid );
                  
-- Pop off the row with the least queueid value.
DELETE FROM barcode_queue WHERE queueid = ( SELECT queueid
                                              FROM barcode_queue bq
                                            WHERE NOT EXISTS( SELECT * 
                                                                FROM barcode_queue bq1
                                                              WHERE bq1.queueid < bq.queueid ));
                                                              

SELECT * 
  FROM products p INNER JOIN web_prices wp ON p.productid = wp.productid
                  INNER JOIN url_addresses ua ON wp.addressid = ua.addressid
                  INNER JOIN stores s ON ua.storeid = s.storeid;

