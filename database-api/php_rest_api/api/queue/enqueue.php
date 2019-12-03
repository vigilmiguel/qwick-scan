<?php

    /*
        JSON Input Format:
        {
            "barcode" : String,
            "longitude" : Float,
            "latitude" : Float
        }
    */

    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: POST');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Access-Control-Allow-Methods, Content-Type, ' . 
            'Authorization, X-Requested-With');

    include_once '../../config/Database.php';
    include_once '../../models/BarcodeQueue.php';

    $database = new Database();
    $db = $database->connect();
    
    $queue = new BarcodeQueue($db);

    $data = json_decode(file_get_contents("php://input"));

    $queue->barcode = $data->barcode;
    $queue->longitude = $data->longitude;
    $queue->latitude = $data->latitude;

    
    if($queue->enqueue())
    {
        echo json_encode(
            array('message' => 'Barcode Enqueued')
        );
    }
    else
    {
        echo json_encode(
            array('message' => 'ERROR: Failed to enqueue barcode.')
        );
    }
    

?>