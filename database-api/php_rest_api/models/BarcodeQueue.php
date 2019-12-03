<?php
    class BarcodeQueue
    {
        private $conn;

        public $queueid;
        public $barcode;
        public $longitude;
        public $latitude;
        public $datetime;

        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function enqueue()
        {
            // Insert a barcode, coordinates, and timestamp along with an auto incrementing queueid.
            $query = "  INSERT INTO barcode_queue 
                            (barcode, longitude, latitude, datetime) 
                            VALUES 
                            (:barcode, :longitude, :latitude, CURRENT_TIMESTAMP);";

            $stmt = $this->conn->prepare($query);

            $this->barcode = htmlspecialchars(strip_tags($this->barcode));
            $this->longitude = htmlspecialchars(strip_tags($this->longitude));
            $this->latitude = htmlspecialchars(strip_tags($this->latitude));

            $stmt->bindParam(':barcode', $this->barcode);
            $stmt->bindParam(':longitude', $this->longitude);
            $stmt->bindParam(':latitude', $this->latitude);

            if($stmt->execute())
                return true;
            else
            {
                printf("Error: %s.\n", $stmt->error);
                return false;
            }
            
        }

        public function dequeue()
        {
            // Get the row with the least value in queueid
            $query = "  SELECT *
                            FROM barcode_queue bq
                        WHERE NOT EXISTS(   SELECT * 
                                                FROM barcode_queue bq1
                                            WHERE bq1.queueid < bq.queueid );";
            
            $stmt = $this->conn->prepare($query);

            if(!$stmt->execute())
            {
                printf("Error: %s.\n", $stmt->error);
                return false;
            }

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            // Store the result within the object.
            $this->queueid = $row['queueid'];
            $this->barcode = $row['barcode'];
            $this->longitude = $row['longitude'];
            $this->latitude = $row['latitude'];
            $this->datetime = $row['datetime'];


            // Pop off the row with the least queueid value.
            $query = "  DELETE 
                            FROM barcode_queue 
                        WHERE queueid = (   SELECT queueid
                                                FROM barcode_queue bq
                                            WHERE NOT EXISTS(   SELECT * 
                                                                    FROM barcode_queue bq1
                                                                WHERE bq1.queueid < bq.queueid ));";

            $stmt = $this->conn->prepare($query);

            if($stmt->execute())
                return true;
            else
            {
                printf("Error: %s.\n", $stmt->error);
                return false;
            }
        }
    }
?>