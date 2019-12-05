<?php

    /*
        JSON Input:
        {
            "productid" : Integer,
            "barcode" : String,
            "productname" : String,
            "imageurl" : String
        }
    */
    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: POST');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Access-Control-Allow-Methods, Content-Type, ' . 
            'Authorization, X-Requested-With');

    include_once '../../config/Database.php';
    include_once '../../models/Products.php';

    $database = new Database();
    $db = $database->connect();
    
    $product = new Products($db);

    $data = json_decode(file_get_contents("php://input"));

    $product->productid = $data->productid;
    $product->barcode = $data->barcode;
    $product->productname = $data->productname;
    $product->imageurl = $data->imageurl;

    if($product->update())
    {
        echo json_encode(
            array('message' => 'Updated')
        );
    }
    else
    {
        echo json_encode(
            array('message' => 'ERROR: User Not Created')
        );
    }

?>