<?php
    class StoreLocation {
        private $conn;

        public $locationid;
        public $storeid;
        public $longitude;
        public $latitude;

        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function create()
        {
            $query = "  INSERT INTO store_locations 
                            (storeID, point) VALUES (:storeid, :point)
                        ON CONFLICT DO NOTHING;";

            $stmt = $this->conn->prepare($query);

            

            // Clean the data.
            $this->storeid = htmlspecialchars(strip_tags($this->storeid));
            $this->longitude = htmlspecialchars(strip_tags($this->longitude));
            $this->latitude = htmlspecialchars(strip_tags($this->latitude));

            $querypoint = 'POINT(' . $this->longitude . ' ' . $this->latitude . ')';

            $stmt->bindParam(':storeid', $this->storeid);
            $stmt->bindParam(':point', $querypoint);

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

        public function queryStoreLocationID()
        {
            $query = "  SELECT locationid 
                            FROM store_locations
                        WHERE point = :point";

            $stmt = $this->conn->prepare($query);

            $this->longitude = htmlspecialchars(strip_tags($this->longitude));
            $this->latitude = htmlspecialchars(strip_tags($this->latitude));

            $querypoint = 'POINT(' . $this->longitude . ' ' . $this->latitude . ')';

            $stmt->bindParam(':point', $querypoint);
           

            $stmt->execute();
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            $this->locationid = $row['locationid'];
        }

    }

?>