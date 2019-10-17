<?php
    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');

    include_once '../../config/Database.php';
    include_once '../../models/Products.php';
    
    $database = new Database();
    $db = $database->connect();
    
    $product = new Products($db);

    $product->productname = isset($_GET['productname']) ? $_GET['productname'] : die();

    $product->readSingle();

    $product = array(
                'productname' => $product->productname, // $variables must match the column names retured by the literal query and should be lowercase.
                'barcode' => $product->barcode,
                'productid' => $product->productid
                'imageurl' => $product->imageurl
            );

    print_r(json_encode($product));
    
