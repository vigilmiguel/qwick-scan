<?php
    class User {
        private $conn;

        public $userid;
        public $firebaseUID;
        public $userName;

        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function readAll()
        {

            /*
            $query = "SELECT userid, username, hashpass, ST_X(location::geometry) AS longitude, ST_Y(location::geometry) AS latitude
                        FROM users;";
            */


            $query = "SELECT * FROM users;";

            $stmt = $this->conn->prepare($query);
            
            $stmt->execute();

            return $stmt;
        }

        public function readSingle()
        {
            $query = "  SELECT * 
                            FROM users
                        WHERE firebaseUID=?";

            $stmt = $this->conn->prepare($query);

            $stmt->bindParam(1, $this->id);

            $stmt->execute();
        }

        public function create()
        {
            $query = "  INSERT INTO users   (firebaseUID, userName)
                            VALUES          (:firebaseUID, :userName)";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $this->firebaseUID = htmlspecialchars(strip_tags($this->firebaseUID));
            $this->userName = htmlspecialchars(strip_tags($this->userName));

            $stmt->bindParam(':firebaseUID', $this->firebaseUID);
            $stmt->bindParam(':userName', $this->userName);

            if($stmt->execute())
                return true;
            
            printf("Error: %s.\n", $stmt->error);

            return false;
        }
    }


?>