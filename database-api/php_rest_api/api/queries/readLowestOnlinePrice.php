<?php
    /*
        GET request JSON input format
        
        {
            "barcode" : String,
            "numberOfStores" : Integer
        }
    */
    ini_set('display_errors', 1);
    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    include_once '../../config/Database.php';
    include_once '../../models/Query.php';
    
    $database = new Database();
    $db = $database->connect();
    
    $query = new Query($db);

    // Get the JSON input data.
    $data = json_decode(file_get_contents("php://input"));

    $barcode = $data->barcode;
    $numberOfStores = $data->numberOfStores;
    
    $result = $query->getLowestOnlinePrice($barcode, $numberOfStores);
    
    $numRows = $result->rowCount();
    
    if($numRows > 0)
    {
        $arr = array();
        $arr['data'] = array();
        while($row = $result->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
            $item = array(
                'productname' => $productname, // $variables must match the column names retured by the literal query and should be lowercase.
                'storename' => $storename,
                'price' => $price,
                'imageurl' => $imageurl,
                'address' => $address
            );
            array_push($arr['data'], $item);
        }
        echo json_encode($arr);
    }
    else
    {
        echo json_encode(
            array('message' => 'No rows returned')
        );
    }
    
?>