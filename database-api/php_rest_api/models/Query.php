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
    }
?>