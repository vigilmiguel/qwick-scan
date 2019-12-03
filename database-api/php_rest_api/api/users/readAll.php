<?php
    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');

    include_once '../../config/Database.php';
    include_once '../../models/User.php';
    
    $database = new Database();
    $db = $database->connect();
    
    $user = new User($db);
    
    $result = $user->readAll();
    
    $num = $result->rowCount();
    
    if($num > 0)
    {
        $users_arr = array();
        $users_arr['data'] = array();

        while($row = $result->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);

            $user_item = array(
                'userid' => $userid, // $variables must match the column names retured by the literal query and should be lowercase.
                'firebaseuid' => $firebaseuid,
                'username' => $username
            );

            array_push($users_arr['data'], $user_item);
        }

        echo json_encode($users_arr);
    }
    else
    {
        echo json_encode(
            array('message' => 'No Posts Found')
        );
    }
    

?>