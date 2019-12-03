<?php
    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');

    include_once '../../config/Database.php';
    include_once '../../models/BarcodeQueue.php';
    
    $database = new Database();
    $db = $database->connect();
    
    $queue = new BarcodeQueue($db);

    if($queue->dequeue())
    {
        $queue = array(
            'queueid' => $queue->queueid,
            'barcode' => $queue->barcode,
            'longitude' => $queue->longitude,
            'latitude' => $queue->latitude,
            'datetime' => $queue->datetime
        );

        print_r(json_encode($queue));
    }
    else
    {
        echo json_encode(
            array('message' => 'ERROR: Failed to dequeue barcode.')
        );
    }

    
    
?>