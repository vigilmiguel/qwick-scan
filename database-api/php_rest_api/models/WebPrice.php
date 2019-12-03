<?php
    class WebPrice {
        private $conn;

        public $productid;
        public $addressid;
        public $price;
        public $pricedate;

        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function create()
        {
            $query = "  INSERT INTO web_prices 
                            (productid, addressid, price, pricedate) 
                            VALUES 
                            (:productid, :addressid, :price, NOW() )
                        ON CONFLICT DO NOTHING;";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $this->productid = htmlspecialchars(strip_tags($this->productid));
            $this->addressid = htmlspecialchars(strip_tags($this->addressid));
            $this->price = htmlspecialchars(strip_tags($this->price));

            $stmt->bindParam(':productid', $this->productid);
            $stmt->bindParam(':addressid', $this->addressid);
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