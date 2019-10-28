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

        publc function readBarcode()
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

        publc function productName()
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
            $stmt->bindParam(':imageurl', $this->productid);

            if($stmt->execute())
                return true;
            
            printf("Error: %s.\n", $stmt->error);

            return false;

        }



        //public function readBarcode() //Return Row based on Barcode
        //public function readProductName() // Return Row Based on Product Name

        //Write Functions
        //public function createProduct() // Create Row 
        //public function editProduct() // Edit an existing row, (maybe based on Barcode, or productName)
    }

?