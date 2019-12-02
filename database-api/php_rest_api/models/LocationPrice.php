<?php
    class LocationPrice {
        private $conn;

        public $productid;
        public $locationid;
        public $price;
        public $pricedate;

        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function create()
        {
            $query = "  INSERT INTO location_prices 
                            (productid, locationid, price, pricedate) 
                            VALUES 
                            (:productid, :locationid, :price, NOW() )
                        ON CONFLICT DO NOTHING;";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $this->productid = htmlspecialchars(strip_tags($this->productid));
            $this->locationid = htmlspecialchars(strip_tags($this->locationid));
            $this->price = htmlspecialchars(strip_tags($this->price));

            $stmt->bindParam(':productid', $this->productid);
            $stmt->bindParam(':locationid', $this->locationid);
            $stmt->bindParam(':price', $this->price);

            if($stmt->execute())
            {
                return true;
            }    
            else
            {
                printf("Error: %s.\n", $stmt->error);

                return false;
            }
            
            
        }


    }

?>