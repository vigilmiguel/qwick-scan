<?php

    /*
        JSON Input:
        {
            "userid" : Integer,
            "productid" : Integer
        }
    */

    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: POST');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Access-Control-Allow-Methods, Content-Type, ' . 
            'Authorization, X-Requested-With');

    include_once '../../config/Database.php';
    include_once '../../models/Scan.php';

    $database = new Database();
    $db = $database->connect();
    
    $scan = new Scan($db);

    $data = json_decode(file_get_contents("php://input"));

    $scan->userid = $data->userid;
    $scan->productid = $data->productid;
    

    if($scan->create())
    {
        echo json_encode(
            array('message' => 'Scan Created')
        );
    }
    else
    {
        echo json_encode(
            array('message' => 'ERROR: Scan Not Created')
        );
    }

?>