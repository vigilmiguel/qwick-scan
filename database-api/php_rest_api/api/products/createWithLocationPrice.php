<?php
    ini_set('display_errors', 1);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: POST');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Access-Control-Allow-Methods, Content-Type, ' . 
            'Authorization, X-Requested-With');

    include_once '../../config/Database.php';
    include_once '../../models/Products.php';
    include_once '../../models/Store.php';
    include_once '../../models/StoreLocation.php';
    include_once '../../models/LocationPrice.php';

    $database = new Database();
    $db = $database->connect();
    
    // Create the product.
    $product = new Products($db);

    $data = json_decode(file_get_contents("php://input"));

    $product->barcode = $data->barcode;
    $product->productname = $data->productname;
    $product->imageurl = $data->imageurl;

    if($product->create())
    {
        echo json_encode(
            array('message' => 'Product Created')
        );

        // Store the product ID within its class variables.
        $product->queryProductID();
    }
    else
    {
        echo json_encode(
            array('message' => 'ERROR: Product Not Created')
        );

        die();
    }

    // Create the store.
    $store = new Store($db);

    $store->storename = $data->storename;

    if($store->create())
    {
        echo json_encode(
            array('message' => 'Store created.')
        );

        // Store the store ID within its class variables.
        $store->queryStoreID();
    }
    else
    {
        echo json_encode(
            array('message' => "ERROR: Failed to create a store.")
        );

        die();
    }

    // Create StoreLocation
    $storelocation = new StoreLocation($db);

    $storelocation->storeid = $store->storeid;
    $storelocation->longitude = $data->longitude;
    $storelocation->latitude = $data->latitude;

    if($storelocation->create())
    {
        echo json_encode(
            array('message' => 'Store Location created.')
        );

        // Store the location ID within its class variables.
        $storelocation->queryStoreLocationID();
    }
    else
    {
        echo json_encode(
            array('message' => 'ERROR: Failed to create store location.')
        );

        die();
    }

    // Create LocationPrice
    $locationprice = new LocationPrice($db);

    $locationprice->productid = $product->productid;
    $locationprice->locationid = $storelocation->locationid;
    $locationprice->price = $data->price;

    if($locationprice->create())
    {
        echo json_encode(
            array('message' => 'Location Price created.')
        );
    }
    else
    {
        echo json_encode(
            array('message' => 'ERROR: Failed to create location price.')
        );

        die();
    }

?>