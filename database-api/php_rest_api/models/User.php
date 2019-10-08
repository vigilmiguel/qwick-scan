<?php
    class User {
        private $conn;
        private $table = 'users';

        public $userid;
        public $username;
        public $hashpass;
        public $location;

        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function read()
        {
            $query = "SELECT userid, username, hashpass, ST_X(location::geometry) AS longitude, ST_Y(location::geometry) AS latitude
                        FROM users;";

            $stmt = $this->conn->prepare($query);
            
            $stmt->execute();

            return $stmt;
        }
    }

?>