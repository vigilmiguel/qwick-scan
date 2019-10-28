<?php
    class Scan {
        private $conn;

        public $userid;
        public $productid;
        public $datetimescanned;

        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function create()
        {
            $query = "  INSERT INTO scans   (userid, productid, datetimescanned)
                            VALUES          (:userid, :productid, CURRENT_TIMESTAMP)";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $this->userid = htmlspecialchars(strip_tags($this->userid));
            $this->productid = htmlspecialchars(strip_tags($this->productid));

            $stmt->bindParam(':userid', $this->userid);
            $stmt->bindParam(':productid', $this->productid);

            if($stmt->execute())
                return true;
            
            printf("Error: %s.\n", $stmt->error);

            return false;
        }

    }

?>