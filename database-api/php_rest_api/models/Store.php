<?php
    class Store {
        private $conn;

        public $storeid;
        public $storename;

        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function create()
        {
            $query = "  INSERT INTO stores (storename) VALUES (:storename);";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $this->storename = htmlspecialchars(strip_tags($this->storename));

            $stmt->bindParam(':storename', $this->storename);


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

        public function queryStoreID()
        {
            $query = "  SELECT storeid 
                            FROM stores
                        WHERE storename=?";

            $stmt = $this->conn->prepare($query);

            $this->storename = htmlspecialchars(strip_tags($this->storename));

            $stmt->bindParam(1, $this->storename);

            $stmt->execute();
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            $this->storeid = $row['storeid'];
        }

    }

?>