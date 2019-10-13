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

        // Functions To implement in products

        //Read Functions
        //public function readBarcode() //Return Row based on Barcode
        //public function readProductName() // Return Row Based on Product Name

        //Write Functions
        //public function createProduct() // Create Row 
        //public function editProduct() // Edit an existing row, (maybe based on Barcode, or productName)
    }

?