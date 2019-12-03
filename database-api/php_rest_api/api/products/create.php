<?php
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

    $product->barcode = $data->barcode;
    $product->productname = $data->productname;
    $product->imageurl = $data->imageurl;

    
    if($product->create())
    {
        echo json_encode(
            array('message' => 'Product Created')
        );
    }
    else
    {
        echo json_encode(
            array('message' => 'ERROR: Product Not Created')
        );
    }
    

?>