<?php
    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: POST');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Access-Control-Allow-Methods, Content-Type, ' . 
            'Authorization, X-Requested-With');

    include_once '../../config/Database.php';
    include_once '../../models/User.php';

    $database = new Database();
    $db = $database->connect();
    
    $user = new User($db);

    $data = json_decode(file_get_contents("php://input"));

    $user->userid = $data->userid;
    $user->firebaseUID = $data->firebaseuid;
    $user->userName = $data->username;

    if($user->update())
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