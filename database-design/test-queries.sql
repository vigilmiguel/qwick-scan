DEELTE FROM location_prices WHERE locationid = 1 AND productid = SELECT productid FROM products WHERE barcode = '1';
DELETE FROM store_locations WHERE locationid = 6 AND storeid = (SELECT storeid FROM stores WHERE storename = 'Testing store for test data!!!');
DELETE FROM products WHERE barcode='1';
DELETE FROM stores WHERE storename = 'Testing store for test data!!!';


SELECT * FROM users;
SELECT * FROM products;
SELECT * FROM stores;
SELECT * FROM store_locations;
SELECT * FROM location_prices;

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


-- Get all stores and their locations that sell the given barcode.
SELECT productname, barcode, storename, price, ST_X(point::geometry) AS "longitude", ST_Y(point::geometry) AS "latitude"
  FROM stores s INNER JOIN store_locations sl ON s.storeid = sl.storeid
                INNER JOIN location_prices lp ON lp.locationid = sl.locationid
                INNER JOIN products p ON p.productid = lp.productid
WHERE barcode = '2';


INSERT INTO location_prices
  (productid, locationid, price, pricedate)
  VALUES
  (9, 1, 13.79, NOW());
  
INSERT INTO products (barcode, productname, imageurl)
  VALUES ('1', '.:Headphones Test:.', NULL)
 ON CONFLICT DO NOTHING;
 



