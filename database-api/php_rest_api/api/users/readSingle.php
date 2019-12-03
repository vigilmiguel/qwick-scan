<?php
    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');

    include_once '../../config/Database.php';
    include_once '../../models/User.php';
    
    $database = new Database();
    $db = $database->connect();
    
    $user = new User($db);

    $user->firebaseuid = isset($_GET['firebaseuid']) ? $_GET['firebaseuid'] : die();

    $user->readSingle();

    $user_item = array(
                'userid' => $user->userid, // $variables must match the column names retured by the literal query and should be lowercase.
                'firebaseuid' => $user->firebaseuid,
                'username' => $user->username
            );

    print_r(json_encode($user_item));
    
