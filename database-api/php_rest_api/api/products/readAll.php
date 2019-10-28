<?php
    ini_set('display_errors', 1);
    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    include_once '../../config/Database.php';
    include_once '../../models/Products.php';
    
    $database = new Database();
    $db = $database->connect();
    
    $products = new Products($db);
    
    $result = $products->readAll();
    
    $num = $result->rowCount();
    
    if($num > 0)
    {
        $products_arr = array();
        $products_arr['data'] = array();
        while($row = $result->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
            $products_item = array(
                'productid' => $productid, // $variables must match the column names retured by the literal query and should be lowercase.
                'barcode' => $barcode,
                'productname' => $productname
                'imageurl' => $imageurl
            );
            array_push($products_arr['data'], $products_item);
        }
        echo json_encode($products_arr);
    }
    else
    {
        echo json_encode(
            array('message' => 'No Posts Found')
        );
    }
    
?>
