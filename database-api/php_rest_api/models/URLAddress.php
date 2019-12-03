<?php
    class URLAddress {
        private $conn;

        public $addressid;
        public $storeid;
        public $address;


        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function create()
        {
            $query = "  INSERT INTO url_addresses
                            (storeID, address) VALUES (:storeid, :address)
                        ON CONFLICT DO NOTHING;";

            $stmt = $this->conn->prepare($query);

            

            // Clean the data.
            $this->storeid = htmlspecialchars(strip_tags($this->storeid));
            $this->address = htmlspecialchars(strip_tags($this->address));


            $stmt->bindParam(':storeid', $this->storeid);
            $stmt->bindParam(':address', $this->address);

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

        public function queryStoreAddressID()
        {
            $query = "  SELECT addressid 
                            FROM url_addresses
                        WHERE address = :address";

            $stmt = $this->conn->prepare($query);

            $this->address = htmlspecialchars(strip_tags($this->address));

            $stmt->bindParam(':address', $this->address);
           

            $stmt->execute();
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            $this->addressid = $row['addressid'];
        }

    }

?>