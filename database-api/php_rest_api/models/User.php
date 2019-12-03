<?php
    class User {
        private $conn;

        public $userid;
        public $firebaseuid;
        public $username;

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
                        WHERE firebaseuid=?";

            $stmt = $this->conn->prepare($query);

            $stmt->bindParam(1, $this->firebaseuid);

            $stmt->execute();
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            $this->userid = $row['userid'];
            $this->firebaseuid = $row['firebaseuid'];
            $this->username = $row['username'];
        }

        public function create()
        {
            $query = "  INSERT INTO users   (firebaseuid, username)
                            VALUES          (:firebaseuid, :username)";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $this->firebaseuid = htmlspecialchars(strip_tags($this->firebaseuid));
            $this->username = htmlspecialchars(strip_tags($this->username));

            $stmt->bindParam(':firebaseuid', $this->firebaseuid);
            $stmt->bindParam(':username', $this->username);

            if($stmt->execute())
                return true;
            
            printf("Error: %s.\n", $stmt->error);

            return false;
        }

        public function update()
        {

            $query = "UPDATE users
                SET
                  firebaseuid = :firebaseuid,
                  username = :username
                  WHERE
                    userid = :userid";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $this->firebaseuid = htmlspecialchars(strip_tags($this->firebaseuid));
            $this->username = htmlspecialchars(strip_tags($this->username));
            $this->userid = htmlspecialchars(strip_tags($this->userid));

            $stmt->bindParam(':firebaseuid', $this->firebaseuid);
            $stmt->bindParam(':username', $this->username);
            $stmt->bindParam(':userid', $this->userid);

            if($stmt->execute())
                return true;
            
            printf("Error: %s.\n", $stmt->error);

            return false;
        }

        public function delete()
        {
            $query = "DELETE FROM USERS 
                WHERE userid = :userid";

            $stmt = $this->conn->prepare($query);

            $this->userid = htmlspecialchars(strip_tags($this->userid));
            $stmt->bindParam(':userid', $this->userid);

            if($stmt->execute())
                return true;
            
            printf("Error: %s.\n", $stmt->error);

            return false;
        }


    }


?>