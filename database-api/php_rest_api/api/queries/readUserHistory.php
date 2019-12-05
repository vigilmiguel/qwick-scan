<?php
    /*
        GET request JSON input format
        {
            "firebaseuid" : String
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

    $firebaseuid = $data->firebaseuid;
    
    $result = $query->getUserScanHistory($firebaseuid);
    
    $numRows = $result->rowCount();
    
    if($numRows > 0)
    {
        $arr = array();
        
        while($row = $result->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
            $item = array(
                'barcode' => $barcode, 
                'productname' => $productname,
                'imageurl' => $imageurl,
                'numscans' => $numscans,
                'datetimescanned' => $datetimescanned
            );
            array_push($arr, $item);
        }
        echo json_encode($arr);
    }
    else
    {
        echo json_encode(
            array(
                'barcode' => null,
                'productname' => null,
                'imageurl' => null,
                'numscans' => null,
                'datetimescanned' => null
                )
        );
    }
    
?>