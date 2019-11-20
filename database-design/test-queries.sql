DEELTE FROM location_prices WHERE locationid = 1 AND productid = SELECT productid FROM products WHERE barcode = '1';
DELETE FROM store_locations WHERE locationid = 4 AND stoeid = (SELECT storeid FROM stores WHERE storename = 'Testing store for test data!!!');
DELETE FROM products WHERE barcode='1';
DELETE FROM stores WHERE storename = 'Testing store for test data!!!';


SELECT * FROM users;
SELECT * FROM products;

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

SELECT locationid, storeid, ST_X(point::geometry) AS "longitude", ST_Y(point::geometry) AS "latitude"
    FROM store_locations;

SELECT * FROM stores;
SELECT * FROM store_locations;
SELECT * FROM location_prices;

INSERT INTO location_prices
  (productid, locationid, price, pricedate)
  VALUES
  (9, 1, 13.79, NOW());


