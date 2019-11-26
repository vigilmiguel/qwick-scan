<?php
    class Products {
        private $conn;
        private $table = 'products';

        public $productid;
        public $barcode;
        public $productname;
        public $imageurl;

        public function __construct($db)
        {
            $this->conn = $db;
        }

        public function readAll()
        {
            $query = "SELECT * FROM products;";

            $stmt = $this->conn->prepare($query);
            
            $stmt->execute();

            return $stmt;
        }

        public function readSingleByBarcode()
        {
            $query = "  SELECT * 
                            FROM products
                        WHERE barcode=?";

            $stmt = $this->conn->prepare($query);

            $stmt->bindParam(1, $this->barcode);

            $stmt->execute();
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            $this->productid = $row['productid'];
            $this->barcode = $row['barcode'];
            $this->productname = $row['productname'];
            $this->imageurl = $row['imageurl'];
        }

        public function readSingleByProductName()
        {
            $query = "  SELECT * 
                            FROM products
                        WHERE productname=?";

            $stmt = $this->conn->prepare($query);

            $stmt->bindParam(1, $this->productname);

            $stmt->execute();
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            $this->productid = $row['productid'];
            $this->barcode = $row['barcode'];
            $this->productname = $row['productname'];
            $this->imageurl = $row['imageurl'];
        }


        public function delete()
        {
            $query = "  DELETE 
                            FROM products
                        WHERE barcode = ?";

            $stmt = $this->conn->prepare($query);

            $stmt->bindParam(1, $this->barcode);

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

        public function update()
        {
            $query = "UPDATE products
                SET
                  barcode = :barcode,
                  productName = :productName
                  imageurl = :imageurl
                  WHERE
                    productid = :productid";

            $stmt = $this->conn->prepare($query);

            // Clean the data.
            $this->barcode= htmlspecialchars(strip_tags($this->barcode));
            $this->productname = htmlspecialchars(strip_tags($this->productname));
            $this->imageurl = htmlspecialchars(strip_tags($this->imageurl));
            $this->productid = htmlspecialchars(strip_tags($this->productid));


            $stmt->bindParam(':barcode', $this->barcode);
            $stmt->bindParam(':productname', $this->productname);
            $stmt->bindParam(':imageurl', $this->imageurl);
            $stmt->bindParam(':productid', $this->productid);

            if($stmt->execute())
                return true;
            
            printf("Error: %s.\n", $stmt->error);

            return false;

        }

        public function create()
        {
            $query = "  INSERT INTO products    (barcode, productname, imageurl)
                            VALUES 
                                                (:barcode, :productname, :imageurl)
                        ON CONFLICT DO NOTHING;";

            $stmt = $this->conn->prepare($query);

            $this->barcode = htmlspecialchars(strip_tags($this->barcode));
            $this->productname = htmlspecialchars(strip_tags($this->productname));
            $this->imageurl = htmlspecialchars(strip_tags($this->imageurl));

            $stmt->bindParam(':barcode', $this->barcode);
            $stmt->bindParam(':productname', $this->productname);
            $stmt->bindParam(':imageurl', $this->imageurl);

            if($stmt->execute())
                return true;
            
            printf("Error: %s.\n", $stmt->error);

            return false;
        }

        public function queryProductID()
        {
            $query = "  SELECT productid 
                            FROM products
                        WHERE barcode = :barcode";

            $stmt = $this->conn->prepare($query);

            $this->barcode = htmlspecialchars(strip_tags($this->barcode));
            

            $stmt->bindParam(':barcode', $this->barcode);

            $stmt->execute();
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            $this->productid = $row['productid'];
        }

        



        //public function readBarcode() //Return Row based on Barcode
        //public function readProductName() // Return Row Based on Product Name

        //Write Functions
        //public function createProduct() // Create Row 
        //public function editProduct() // Edit an existing row, (maybe based on Barcode, or productName)
    }

?>