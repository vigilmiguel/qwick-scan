<?php
    class Database
    {
        private $host = 'qwickscandb.copnww0vhd9s.us-east-2.rds.amazonaws.com';
        private $db_name = 'qsdb';
        private $username = 'qwickscan';
        private $password = '#bringbackjafar';
        private $conn;

        public function connect()
        {
            $this->conn = null;

            try
            {
                $this->conn = new PDO('pgsql:host=' . $this->host . ';dbname=' . $this->db_name, $this->username, $this->password);

                //echo 'Connection successful!' . "\n\n";

                $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            }
            catch(PDOException $e)
            {
                echo 'Connection Error: ' . $e->getMessage() . "\n\n";
            }

            return $this->conn;
        }
    }
?>