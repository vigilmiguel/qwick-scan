<?php
    ini_set('display_errors', 1);
    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    include_once '../../config/Database.php';
    include_once '../../models/Scan.php';
    
    $database = new Database();
    $db = $database->connect();
    
    $scan = new Scan($db);

    $numberOfScans = isset($_GET['numScans']) ? $_GET['numScans'] : die();
    
    $result = $scan->getRecentScans($numberOfScans);
    
    $num = $result->rowCount();
    
    if($num > 0)
    {
        $scans_arr = array();
        $scans_arr['data'] = array();
        while($row = $result->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
            $scan_item = array(
                'productname' => $productname, // $variables must match the column names retured by the literal query and should be lowercase.
                'imageurl' => $imageurl,
                'datetimescanned' => $datetimescanned,
            );
            array_push($scans_arr['data'], $scan_item);
        }
        echo json_encode($scans_arr);
    }
    else
    {
        echo json_encode(
            array('message' => 'No Scans Found')
        );
    }
    
?>