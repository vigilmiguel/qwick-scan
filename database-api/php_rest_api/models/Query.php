<?php
    class Query
    {
        private $conn;

        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function getNearestStores($barcode, $longitude, $latitude, $numberOfStores)
        {
            // Get the nearest x stores from a point that sell a product given a barcode, longitude, latitude and number of stores.
            // Returns a distance in meters.
            $query = "  SELECT productname, storename, price, imageurl, ST_X(sl.point::geometry) AS \"longitude\", 
                        ST_Y(sl.point::geometry) AS \"latitude\", ST_Distance(sl.point, :querypoint) AS \"distance\"
                        FROM products p INNER JOIN location_prices lp ON p.productid = lp.productid
                                        INNER JOIN store_locations sl ON sl.locationid = lp.locationid
                                        INNER JOIN stores s ON s.storeid = sl.storeid
                        WHERE barcode = :barcode
                        ORDER BY ST_Distance(sl.point, :querypoint)
                        LIMIT :numstores;";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $barcode = htmlspecialchars(strip_tags($barcode));
            $longitude = htmlspecialchars(strip_tags($longitude));
            $latitude = htmlspecialchars(strip_tags($latitude));
            $numstores = htmlspecialchars(strip_tags($numberOfStores));

            $querypoint = 'POINT(' . $longitude . ' ' . $latitude . ')';

            $stmt->bindParam(':barcode', $barcode);
            $stmt->bindParam(':querypoint', $querypoint);
            $stmt->bindParam(':numstores', $numstores);
            
            $stmt->execute();

            return $stmt;
        }

        public function getAllBarcodeLocations($barcode)
        {
            // Given a barcode, get all stores and locations that sell this product.
            $query = "  SELECT productname, barcode, storename, price,
                        ST_X(point::geometry) AS \"longitude\", 
                        ST_Y(point::geometry) AS \"latitude\"
                            FROM stores s   INNER JOIN store_locations sl ON s.storeid = sl.storeid
                                            INNER JOIN location_prices lp ON lp.locationid = sl.locationid
                                            INNER JOIN products p ON p.productid = lp.productid
                        WHERE barcode = :barcode;";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $barcode = htmlspecialchars(strip_tags($barcode));

            $stmt->bindParam(':barcode', $barcode);

            $stmt->execute();

            return $stmt;
        }

        public function getLowestOnlinePrice($barcode, $numberOfStores)
        {
            // Get the websites that sell the given barcode for the cheapest price.
            $query = "  SELECT productname, storename, price, imageurl, address
                            FROM products p INNER JOIN web_prices wp ON p.productid = wp.productid
                                            INNER JOIN url_addresses ua ON ua.addressid = wp.addressid
                                            INNER JOIN stores s ON s.storeid = ua.storeid
                        WHERE barcode = :barcode
                        ORDER BY price
                        LIMIT :numstores;";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $barcode = htmlspecialchars(strip_tags($barcode));
            $numstores = htmlspecialchars(strip_tags($numberOfStores));

            $stmt->bindParam(':barcode', $barcode);
            $stmt->bindParam(':numstores', $numstores);
            
            $stmt->execute();

            return $stmt;
        }

        public function getAllBarcodeWebAddresses($barcode)
        {
            // Given a barcode, get all stores and web addresses that sell this product.
            $query = "  SELECT productname, barcode, storename, price, address
                            FROM stores s   INNER JOIN url_addresses ua ON s.storeid = ua.storeid
                                            INNER JOIN web_prices wp ON wp.addressid = ua.addressid
                                            INNER JOIN products p ON p.productid = wp.productid
                        WHERE barcode = :barcode;";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $barcode = htmlspecialchars(strip_tags($barcode));

            $stmt->bindParam(':barcode', $barcode);

            $stmt->execute();

            return $stmt;
        }

        public function getUserScanHistory($firebaseuid)
        {
            // Get the user's scanned barcodes with the number of scans and the most recent date scanned for each unique barcode.

            $query = "  SELECT t1.barcode, t1.numscans, t2.datetimescanned, t1.productname, t1.imageurl
                            FROM(   SELECT barcode, productname, imageurl, COUNT(barcode) AS numscans
                                        FROM users u    INNER JOIN scans s ON u.userid = s.userid
                                                        INNER JOIN products p ON p.productid = s.productid
                                        WHERE u.firebaseuid = :firebaseuid
                                    GROUP BY barcode, productname, imageurl
                                ) t1
                                
                            INNER JOIN
                        
                                (   SELECT p.barcode, p.productname, s.datetimescanned
                                        FROM users u    INNER JOIN scans s ON u.userid = s.userid
                                                        INNER JOIN products p ON p.productid = s.productid
                                    WHERE u.firebaseuid = :firebaseuid 
                                        AND NOT EXISTS( SELECT *
                                                            FROM users u1 INNER JOIN scans s1 ON u1.userid = s1.userid
                                                                        INNER JOIN products p1 ON p1.productid = s1.productid
                                                        WHERE s1.datetimescanned > s.datetimescanned AND p1.barcode = p.barcode)
                                ) t2
                            ON t1.barcode = t2.barcode
                        ORDER BY t2.datetimescanned DESC;";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $firebaseuid = htmlspecialchars(strip_tags($firebaseuid));

            $stmt->bindParam(':firebaseuid', $firebaseuid);

            $stmt->execute();

            return $stmt;
        }

        public function deleteUserProduct($firebaseuid, $barcode)
        {
            $query = "  DELETE
                            FROM scans
                        WHERE userid = ( SELECT userid FROM users WHERE firebaseuid = :firebaseuid )
                            AND productid = (SELECT productid FROM products WHERE barcode = :barcode);";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $firebaseuid = htmlspecialchars(strip_tags($firebaseuid));
            $barcode = htmlspecialchars(strip_tags($barcode));

            $stmt->bindParam(':firebaseuid', $firebaseuid);
            $stmt->bindParam(':barcode', $barcode);

            if($stmt->execute())
                return true;
            else
                return false;

        
        }
    }
?>