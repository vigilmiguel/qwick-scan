<?php
    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');

    include_once '../../config/Database.php';
    include_once '../../models/Products.php';
    
    $database = new Database();
    $db = $database->connect();
    
    $product = new Products($db);

    $product->barcode = isset($_GET['barcode']) ? $_GET['barcode'] : die();

    if($product->delete())
    {
        echo json_encode(
            array('message' => 'Product deleted.')
        );
    }
    else
    {
        
        echo json_encode(
            array('message' => 'ERROR: Failed to delete product.')
        );
    }
    
?>