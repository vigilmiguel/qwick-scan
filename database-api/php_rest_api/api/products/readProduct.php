<?php

    /*
        JSON Input
        {
            "productname" : String
        }
    */

    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');

    include_once '../../config/Database.php';
    include_once '../../models/Products.php';
    
    $database = new Database();
    $db = $database->connect();
    
    $product = new Products($db);

    // Get the JSON input data.
    $data = json_decode(file_get_contents("php://input"));

    $product->productname = $data->productname;

    $product->readSingleByProductName();

    $product = array(
                'productname' => $product->productname, // $variables must match the column names retured by the literal query and should be lowercase.
                'barcode' => $product->barcode,
                'productid' => $product->productid,
                'imageurl' => $product->imageurl
            );

    print_r(json_encode($product));
    
?>