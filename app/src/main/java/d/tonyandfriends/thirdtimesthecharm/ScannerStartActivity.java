package d.tonyandfriends.thirdtimesthecharm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.*;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;



/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class ScannerStartActivity extends Activity implements DataTransporter, Serializable {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView contactname, contacttitle, contactorganization;
    private ProgressBar pBar;
    private TextView Progress;
    //private TextView Title;

    //private ArrayList<TextView> priceButtons;
    private ArrayList<TextView> storeTextViews;
    private ArrayList<Button> priceButtons;

    /*
    private TextView P1;
    private TextView P2;
    private TextView P3;
    private TextView S1;
    private TextView S2;
    private TextView S3;
    */
    private TextView L1;
    private TextView L2;
    private TextView L3;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    Spider spidey = new Spider();
    ImageView productImageView;
    Button mapButton;
    //Button scanButton;
    //Button menuButton;
    Button shareButton;
    Button reviewButton;

    String productName = "";
    String productImage = "";
    String productBarcode = "";
    String historyBarcode = null;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseUserScanHistory;
    DatabaseReference databaseProductsScanned;

    Retrofit retrofit;
    DatabaseAPI databaseAPI;


    Location location;
    LocationManager locationManager;
    Double longitude, latitude;


    User userInfo;
    Product productInfo;

    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_start);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null) {
            String dbHistoryPath = "userScanHistory/" + firebaseUser.getUid();

            // Refer to this user's specific sub-table within the scan history table.
            databaseUserScanHistory = FirebaseDatabase.getInstance()
                    .getReference(dbHistoryPath);
        }



        // Refers to the productsScanned table. This will store all products scanned
        // by all users.
        databaseProductsScanned = FirebaseDatabase.getInstance()
                .getReference("productsScanned");


        statusMessage = (TextView)findViewById(R.id.status_message);

        productImageView = findViewById(R.id.ProductPicture);

        storeTextViews = new ArrayList<>();
        priceButtons = new ArrayList<>();

        storeTextViews.add((TextView)findViewById(R.id.S1));
        storeTextViews.add((TextView)findViewById(R.id.S2));
        storeTextViews.add((TextView)findViewById(R.id.S3));

        priceButtons.add((Button)findViewById(R.id.P1));
        priceButtons.add((Button)findViewById(R.id.P2));
        priceButtons.add((Button)findViewById(R.id.P3));



        /*
        S1 = (TextView)findViewById(R.id.S1);
        S2 = (TextView)findViewById(R.id.S2);
        S3 = (TextView)findViewById(R.id.S3);
        P1 = (TextView)findViewById(R.id.P1);
        P2 = (TextView)findViewById(R.id.P2);
        P3 = (TextView)findViewById(R.id.P3);
        */
        Progress = (TextView)findViewById(R.id.Progress);


        //Title = (TextView)findViewById(R.id.Title);
        pBar = (ProgressBar)findViewById(R.id.progressBar);
        mapButton = (Button)findViewById(R.id.map_button);
        //scanButton = (Button)findViewById(R.id.scan_button);
        //menuButton = (Button)findViewById(R.id.menu_button);
        shareButton = (Button)findViewById(R.id.share_button);
        reviewButton = findViewById(R.id.review_button);

        Progress.setVisibility(TextView.VISIBLE);
        //menuButton.setVisibility(Button.INVISIBLE);
        //scanButton.setVisibility(Button.INVISIBLE);
        pBar.setVisibility(ProgressBar.VISIBLE);
        //Title.setVisibility(TextView.INVISIBLE);
        mapButton.setVisibility(Button.INVISIBLE);
        shareButton.setVisibility(Button.INVISIBLE);
        reviewButton.setVisibility(Button.INVISIBLE);


        for(int i = 0; i < storeTextViews.size() && i < priceButtons.size(); i++) {
            storeTextViews.get(i).setVisibility(TextView.INVISIBLE);
            priceButtons.get(i).setVisibility(TextView.INVISIBLE);
        }

        /*
        S1.setVisibility(S1.INVISIBLE);
        S2.setVisibility(S2.INVISIBLE);
        S3.setVisibility(S3.INVISIBLE);
        P1.setVisibility(P1.INVISIBLE);
        P2.setVisibility(P2.INVISIBLE);
        P3.setVisibility(P3.INVISIBLE);
        */




        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_menu:
                        startActivity(new Intent(ScannerStartActivity.this, MenuActivity.class));
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(ScannerStartActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.navigation_history:
                        startActivity(new Intent(ScannerStartActivity.this, HistoryActivity.class));
                        return true;
                }
                return false;
            }

        });


        retrofit = new Retrofit.Builder()
                .baseUrl("http://18.216.191.20/php_rest_api/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        databaseAPI = retrofit.create(DatabaseAPI.class);


        // Check if location is enabled.
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)
        {
            // Get the user's gps coordinates.
            Criteria criteria = new Criteria();

            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

            String locationProvider = locationManager.getBestProvider(criteria, true);

            Log.i("Product Info", "location enabled.");

            locationManager.requestLocationUpdates(locationProvider, 1000, 0,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            locationManager.removeUpdates(this);

                            Log.i("Product Info", "location changed.");

                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });

        }
        else // Request permission from the user.
        {

            Log.i("Product Info", "location disabled.");

            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    0);

        }

        // We need the user's info in order to store their scan history.
        // All we really need is their userid in the DB.
        getUserInfo();

        //Bundle bundle = getIntent().getExtras();
        historyBarcode = getIntent().getStringExtra("barcode");

        // If we didn't get a barcode from the history activity....
        if(historyBarcode == null)
        {
            // Activate the camera for barcode capture.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
        else // IF we did...
        {
            productBarcode = historyBarcode;

            Log.i("Product Info", "Barcode Value: " + productBarcode);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    findBarcodeInDatabase();
                }
            }, 1000);


        }

    }


    @Override
    // This is our Adapter implementation
    // We take the result from the instance of our Spider object, which is a Name string that we parsed from some HTML
    public void onProcessDone(final SpiderData result) {

        Log.d("mymymymymymymym",Integer.toString(result.prices.size()));
        // Using sharedPreferences and json/gson files, I can transfer an object from one activity to another.
        //this is one of the only ways to transfer an object between activites
        SharedPreferences mPrefs = getSharedPreferences("poop",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit = mPrefs.edit();
        Gson gson = new Gson();
        String jsonData = gson.toJson(result);
        prefEdit.putString("mySpider",jsonData);
        prefEdit.commit();



        for(int i =0; i<result.getPrices().size();i++)
        {
             //Log.d("myprice " +i, result.getPrices().get(i));
             //Log.d("myURL " +i, result.getURLS().get(i)); // Can be ignnored for now (doesnt work)
             //Log.d("myStoreName " +i, result.getStores().get(i));
        }
        final ArrayList<String> store = result.getStores();
        final ArrayList<String> price = result.getPrices();
        ArrayList<String> links = result.getURLS();
        /*
        for(int k =0; k < store.size() && k < storeTextViews.size(); k++)
        {
            if(store.get(k) == NULL || store.get(k)== "")
            {
                store.set(k,"");
            }

            storeTextViews.get(k).setText(store.get(k));
        }
        for(int j =0; j < price.size() && j < priceButtons.size(); j++)
        {
            if(price.get(j) == NULL || price.get(j)== "")
            {
                price.set(j,"");
            }

            priceButtons.get(j).setText(price.get(j));
        }
        for(int j =0; j < links.size(); j++)
        {
            if(links.get(j) == NULL || links.get(j)== "")
            {
                links.set(j,"");
            }
        }
        */

        /*
        S1.setText(store.get(0));
        S2.setText(store.get(1));
        S3.setText(store.get(2));
        P1.setText(price.get(0));
        P2.setText(price.get(1));
        P3.setText(price.get(2));
        */


        //pBar.setVisibility(ProgressBar.INVISIBLE);
        //Progress.setVisibility(Progress.INVISIBLE);
        //Title.setVisibility(TextView.VISIBLE);

        /*
        for(int i = 0; i < storeTextViews.size() && i < priceButtons.size(); i++) {
            storeTextViews.get(i).setVisibility(TextView.VISIBLE);
            priceButtons.get(i).setVisibility(TextView.VISIBLE);
        }
        */

        spidey.cancel(true); // May not be needed, someday I may even test it
        /*
        S1.setVisibility(S1.VISIBLE);
        S2.setVisibility(S2.VISIBLE);
        S3.setVisibility(S3.VISIBLE);
        P1.setVisibility(P1.VISIBLE);
        P2.setVisibility(P2.VISIBLE);
        P3.setVisibility(P3.VISIBLE);
        */

        productName = "";

        // The name will return "Description $itemName", I dont want it to say Description, so this is a quickfix until we find a better way to parse the HTML
        // If we find a result...
        String pname = result.getProductName();
        String purl = result.getImgURL();
        if(spidey.foundProduct) {

            Log.i("SCANNERSTARTACTIVITY","PRODUCT_FOUND");
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                    .format(Calendar.getInstance().getTime());

            productName = pname;

            //Product product = new Product(productBarcode, productName, purl, currentTime, 1);

            // Store it in the database
            //storeInDatabase(product);

            //menuButton.setVisibility(Button.VISIBLE);

            //scanButton.setVisibility(Button.VISIBLE);





        }


        // If we don't find a result...
        else {
            Log.i("SCANNERSTARTACTIVITY","PRODUCT_NOT_FOUND");
            productName = "Sorry, we could not find that product!";
        }

        /*
        if(purl.compareTo("https://www.barcodelookup.com/assets/images/no-image-available.jpg") == 0
                || purl.isEmpty())
        {
            //Here we will add default cannot find image thing
            Glide.with(this )
                    .load("https://www.barcodelookup.com/assets/images/no-image-available.jpg")
                    .into(productImageView);


        }
        else
        {
            Glide.with(this ).load(purl).into(productImageView);
        }
        */
        //new ContactAPI().execute("http://18.216.191.20/php_rest_api/api/products/readBarcode.php?barcode="+"0787364460199");
        //new ContactAPI().execute("http://18.216.191.20/php_rest_api/api/products/readBarcode.php?barcode="+productName);
    }


    /*
    public void storeInDatabase(Product newProduct)
    {

        // Attempt to insert the new product in both tables.
        Log.i("ScannerStartActivity", "Before products scanned insertion");
        insertProductIntoTable(databaseProductsScanned, newProduct);
        Log.i("ScannerStartActivity", "After products scanned insertion");

        Log.i("ScannerStartActivity", "Before user scan history insertion");
        insertProductIntoTable(databaseUserScanHistory, newProduct);
        Log.i("ScannerStartActivity", "After user scan history insertion");


    }
     */

    // If the product is already in the given table, it will just update the date and count.
    /*
    public void insertProductIntoTable(final DatabaseReference reference,
                                       final Product scannedProduct) {

        // Run a query to return all products with the same productName.
        // Trim the results to only 1. (it should only be one anyway)
        Query queryResult = reference.orderByChild("barcode")
                .equalTo(scannedProduct.getBarcode())
                .limitToFirst(1);

        // Make the following only happen once.
        queryResult.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            //
                This is called either when initialized(which is now) or when the data is changed.
                Because its under a listener for a single value event, it will only be called now
                and not at a later time when the data changes again. (The shit will hit the fan
                if it does! xD)
             //
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(reference == databaseProductsScanned)
                    Log.i("ScannerStartActivity", "Beginning products scanned insertion....");
                else
                    Log.i("ScannerStartActivity", "Beginning user scan history insertion....");

                // Put all children in a list.
                // Should have only one child.
                Iterable<DataSnapshot> dataList = dataSnapshot.getChildren();

                // If the scanned product already exists in this table...
                if (dataList.iterator().hasNext()) {

                    // Store the first(only) product snapshot.
                    DataSnapshot data = dataList.iterator().next();

                    //
                    Toast.makeText(ScannerStartActivity.this,
                            "This is already in the database!", Toast.LENGTH_SHORT).show();
                    //

                    // Extract the data of the existing product.
                    Product existingProduct = data.getValue(Product.class);

                    if(existingProduct != null) {

                        // Update the time and increment scan count of the existing product.
                        existingProduct.setDateRecentlyScanned(scannedProduct.getDateRecentlyScanned());
                        existingProduct.setScanCount(existingProduct.getScanCount() + 1);

                        // Write it to the database.
                        Log.i("ScannerStartActivity", "Existing: " +
                                existingProduct.getBarcode());
                        reference.child(existingProduct.getBarcode())
                                .setValue(existingProduct);

                    }
                }
                // If the scanned product is not in this table...
                else {

                    //
                    Toast.makeText(ScannerStartActivity.this,
                            "Never seen this one before!", Toast.LENGTH_SHORT).show();
                    //

                    if(scannedProduct.getBarcode() != null) {

                        // insert the new product with the given key.
                        reference.child(scannedProduct.getBarcode()).setValue(scannedProduct);


                    }

                }

                Log.i("ScannerStartActivity", "Ending insertion....!!!");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
     */

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String poop = "";
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    int type = barcode.valueFormat;


                    switch (type) {
                        case Barcode.CONTACT_INFO:
                            Log.i(TAG, barcode.contactInfo.title);
                            Barcode.Email[] set = barcode.contactInfo.emails;
                            List<String> answers = new ArrayList<String>();
                            for (int i = 0; i < set.length; i++){
                                answers.add(set[i].address);

                                Toast.makeText(this, "emails " + answers, Toast.LENGTH_LONG).show();
                            }
                            contactorganization.setText(barcode.contactInfo.organization);
                            contacttitle.setText(barcode.contactInfo.title);
                            Barcode.PersonName name = barcode.contactInfo.name;
                            String contactName = name.first + " " + name.last;
                            contactname.setText(contactName);
                            break;
                        case Barcode.EMAIL:
                            Log.i(TAG, barcode.email.address);
                            break;
                        case Barcode.ISBN:
                            Log.i(TAG, barcode.rawValue);
                            break;
                        case Barcode.PHONE:
                            Log.i(TAG, barcode.phone.number);
                            break;
                        case Barcode.PRODUCT:
                            Log.i(TAG, barcode.rawValue);
                            // Get the ID, only thing we are concerned with as far as I'm concerned
                            poop = barcode.rawValue;
                            break;
                        case Barcode.SMS:
                            Log.i(TAG, barcode.sms.message);
                            break;
                        case Barcode.TEXT:
                            Log.i(TAG, barcode.rawValue);
                            break;
                        case Barcode.URL:
                            Log.i(TAG, "url: " + barcode.url.url);
                            break;
                        case Barcode.WIFI:
                            Log.i(TAG, barcode.wifi.ssid);
                            break;
                        case Barcode.GEO:
                            Log.i(TAG, barcode.geoPoint.lat + ":" + barcode.geoPoint.lng);
                            break;
                        case Barcode.CALENDAR_EVENT:
                            Log.i(TAG, barcode.calendarEvent.description);
                            break;
                        case Barcode.DRIVER_LICENSE:
                            Log.i(TAG, barcode.driverLicense.licenseNumber);
                            break;
                        default:
                            Log.i(TAG, barcode.rawValue);
                            break;
                    }
                    //For Jsoup or Async classes there are three Array fields <1,2,3>
                    //Field one will be for data we want to pass, field 2 is unimporant as far as I know, keep it void. Field 3 is our return data
                    String [] container = new String[1]; // Here we are passing String, so we need a String Array
                    container[0] = poop; // one day I may make this a legit name, we assign our ID we get from barcode into our Array
                    productBarcode = poop;
                    try { // Async threads can only run once, so if we want multiple scans we need new objects. This may not be the best way, but it works for now
                        spidey = spidey.getClass().newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    spidey.myVessel = this; // Assign the our instance to Spider
                    spidey.execute(container); // Jesus take the wheel

                    //Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    // IDK if these are ever even used, I've tried to get them to work, but nothing happens
//                    pBar.setVisibility(ProgressBar.INVISIBLE);
//                    Progress.setText(R.string.barcode_failure);
//                    Log.d(TAG, "No barcode captured, intent data is null");

                    //Testing Purpose Code Only


                    String testUPC = "811571018420";
                    String [] container = new String[1]; // Here we are passing String, so we need a String Array
                    container[0] = testUPC; // one day I may make this a legit name, we assign our ID we get from barcode into our Array
                    productBarcode = testUPC;
                    try { // Async threads can only run once, so if we want multiple scans we need new objects. This may not be the best way, but it works for now
                        spidey = spidey.getClass().newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    spidey.myVessel = this; // Assign the our instance to Spider
                    try {
                        spidey.execute(container); // Jesus take the wheel
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();;
                    }


                    //End of Testing Code

                }
            } else {

                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        
        
        // Up until this point, we have the barcode we have scanned.
        
        Log.i("Product Info", "Barcode Value: " + productBarcode);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findBarcodeInDatabase();
            }
        }, 1000);


    }


    public void findBarcodeInDatabase()
    {
        //productBarcode = "10";


        // Check to see if the barcode is in the database.
        Product product = new Product(productBarcode);

        try
        {
            Call<Product> call = databaseAPI.getProduct(product);

            productInfo = new Product();

            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Log.i("API Response Code:", "" + response.code());

                    if(response.isSuccessful())
                    {
                        if(response.body() != null)
                            productInfo = response.body();

                        if(!productInfo.exists())
                        {
                            // The barcode returned from the db is null.
                            Log.i("Product Info", "Not in database!");

                            // Now, we need to enqueue the barcode onto the queue so that Jame's
                            // crawler scans it and adds it to the DB.

                            enqueueToDB();

                        }
                        else
                        {
                            // The barcode was found and we need to display it.
                            Log.i("Product Info", "Found in database!");

                            // Add to user scan history.
                            addProductToUserHistory();

                            findLowestPrices();
                        }
                    }
                    else
                    {
                        showError("No API Response.");
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    t.printStackTrace();
                    Log.i("Product Info", "FAIL products");
                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void addProductToUserHistory()
    {
        if(userInfo.getUserID() == null)
            Log.i("Product Info", "userID = null");

        if(productInfo.getProductID() == null)
            Log.i("Product Info", "productID = null");


        UserScan userScan = new UserScan(userInfo.getUserID(), productInfo.getProductID());

        try
        {
            Call<Void> call = databaseAPI.createScan(userScan);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(!response.isSuccessful())
                        showError("No API Response.");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showError("Create Scan Query Failed.");
                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void enqueueToDB()
    {
        Log.i("Product Info", "Long: " + longitude + " Lat: " + latitude);

        if(longitude == null || latitude == null)
        {
            Log.i("Product Info", "Defaulting coords to Long Beach, CA");
            longitude = -118.193741;
            latitude = 33.770050;
        }

        ProductEnqueue product = new ProductEnqueue(productBarcode, longitude, latitude);

        try
        {
            Call<Void> call = databaseAPI.enqueueDB(product);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if(response.isSuccessful())
                    {
                        createToast("Could not find this product. Please " +
                                "try again later...", Toast.LENGTH_LONG);

                        showResults(null);
                    }
                    else
                    {
                        showError("No API Response.");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    Log.i("Product Info", "FAIL enqueue");
                    showError("Enqueue query failed.");
                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getUserInfo()
    {
        userInfo = new User(firebaseUser.getUid(), firebaseUser.getEmail());

        try
        {
            Call<User> call = databaseAPI.getUser(userInfo);



            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if(response.isSuccessful()) {
                        Log.i("Product Info", "Response Successful");
                        if (response.body() != null)
                        {
                            userInfo = new User();
                            Log.i("Product Info", "Emoty user");

                            Log.i("Product Info", "Response Body not empty");
                            userInfo = response.body();
                        }
                        else
                            Log.i("Product Info", "Response Body empty.");

                    }
                    else
                    {
                        Log.i("Product Info", "Response Unsuccessful");
                        showError("No API Response.");
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    showError("User query failed.");
                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void findLowestPrices()
    {
        ProductWebPrices input = new ProductWebPrices(productBarcode, 3);
        Log.i("Product Info", "" + productBarcode);

        try
        {
            Call<List<ProductWebPrices>> callWeb = databaseAPI.getLowestWebPrices(input);

            callWeb.enqueue(new Callback<List<ProductWebPrices>>() {
                @Override
                public void onResponse(Call<List<ProductWebPrices>> call, Response<List<ProductWebPrices>> response) {
                    if(response.isSuccessful())
                    {
                        List<ProductWebPrices> result = new ArrayList<>();

                        if(response.body() != null)
                            result = response.body();

                        if(!result.get(0).exists())
                        {
                            // Product exists but no prices found...
                            Log.i("Product Info", "Exists but no prices.");
                            showError("Found barcode but no prices.");
                        }
                        else
                        {
                            // Display the prices.
                            Log.i("Product Info", "Found product and prices!");
                            /*
                            Log.i("Product Info", "Name: " + result.get(0).getProductName()
                                        + "Store: " + result.get(0).getStoreName()
                                        + "Price: " + result.get(0).getPrice()
                                        + "address " + result.get(0).getUrlAddress());

                             */

                            showResults(result);
                        }
                    }
                    else
                    {
                        showError("No API Response");
                    }
                }

                @Override
                public void onFailure(Call<List<ProductWebPrices>> call, Throwable t) {
                    t.printStackTrace();
                    Log.i("Product Info", "FAIL prices");
                    showError("Price query failed.");

                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }



    public void showResults(List<ProductWebPrices> result)
    {
        pBar.setVisibility(ProgressBar.INVISIBLE);
        Progress.setVisibility(TextView.INVISIBLE);

        // Set to no image found by default.
        Glide.with(ScannerStartActivity.this)
                .load("https://www.barcodelookup.com/assets/images/no-image-available.jpg")
                .into(productImageView);

        String notFoundText = "Product Not Found: Try again later.";
        statusMessage.setText(notFoundText);



        for(int i = 0; result != null && i < result.size(); i++)
        {
            // If there is at least 1 result...
            if(i == 0)
            {
                // Get the image url of the first result.
                String purl = result.get(i).getImageURL();

                if(purl.compareTo("https://www.barcodelookup.com/assets/images/no-image-available.jpg") != 0
                        && !purl.isEmpty())
                {
                    // Set the imageurl to whatever is in the db.
                    Glide.with(ScannerStartActivity.this)
                            .load(purl)
                            .into(productImageView);


                }

                statusMessage.setText(result.get(i).getProductName());

            }

            storeTextViews.get(i).setText(result.get(i).getStoreName());
            String set_text = "$" + String.valueOf(result.get(i).getPrice());
            //priceButtons.get(i).setText(String.valueOf(result.get(i).getPrice()));
            priceButtons.get(i).setText(set_text);
            priceButtons.get(i).setTag(result.get(i).getUrlAddress());

            storeTextViews.get(i).setVisibility(TextView.VISIBLE);
            priceButtons.get(i).setVisibility(TextView.VISIBLE);

            mapButton.setVisibility(Button.VISIBLE);
            shareButton.setVisibility(Button.VISIBLE);
            reviewButton.setVisibility(Button.VISIBLE);




            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ScannerStartActivity.this, MapsActivity.class));
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ScannerStartActivity.this, shareActivity.class);

                    // Create a bundle to store the info we want to send to shareActivity.
                    Bundle bundle = new Bundle();

                    /*
                    // Store the name, stores and prices for the product in the bundle.
                    bundle.putString("productName", productName);
                    bundle.putStringArrayList("stores", store);
                    bundle.putStringArrayList("prices", price);




                    // Add it to the intent.
                    intent.putExtras(bundle);


                    // Fire her up!!
                    startActivity(intent);

                     */
                }
            });

            reviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ScannerStartActivity.this, ReviewActivity.class);

                    Bundle bundle = new Bundle();

                    /*
                    // Assuming the array lists have the respective names: urls, names, starRatings
                    ArrayList<String> urls = result.reviewSitesURl;
                    ArrayList<String> names = result.reviewSitesName;
                    ArrayList<String> starRatings = result.starRating;

                    String size = Integer.toString(urls.size());
                    Log.i("cheekESTER", size);


                    size = Integer.toString(names.size());
                    Log.i("cheekESTER", size);


                    size = Integer.toString(starRatings.size());
                    Log.i("cheekESTER", size);

                    /*
                    names.add("Best Buy");
                    starRatings.add("4.5");
                    urls.add("https://popgoestheweek.com/wp-content/uploads/2019/03/Momo-Vincent-Marcus-1.jpg");

                    names.add("E-Bay");
                    starRatings.add("3.1");
                    urls.add("https://www.ebay.com/");

                    names.add("Macy's");
                    starRatings.add("4.9");
                    urls.add("https://www.macys.com/");

                    names.add("Macy");
                    starRatings.add("4.9");
                    urls.add("https://popgoestheweek.com/wp-content/uploads/2019/03/Momo-Vincent-Marcus-1.jpg");
                    /

                    bundle.putStringArrayList("urls", urls);
                    bundle.putStringArrayList("names", names);
                    bundle.putStringArrayList("starRatings", starRatings);


                    intent.putExtras(bundle);


                    startActivity(intent);
                    */
                }
            });
        }
    }

    public void showNavBar()
    {

    }


    public class ContactAPI extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                String productName = parentObject.getString("productname");
                if (productName.equals("null")) return "Product not in database!";
                int barcode = parentObject.getInt("barcode");
                int productID = parentObject.getInt("productid");
                String imageURL = parentObject.getString("imageurl");
                String result = productName + "  " + imageURL + "  " + barcode + "  " + productID;
                //Log.d("whaHappen", result);
                return result;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
      
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //String arr[] = result.split("  ", 2);
            //statusMessage.setText(arr[0]);
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent i = new Intent(ScannerStartActivity.this, MenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void createToast(String text, int duration)
    {
        Toast.makeText(ScannerStartActivity.this, text, duration).show();
    }

    public void showError(String text)
    {
        createToast("ERROR: " + text, Toast.LENGTH_LONG);
    }

   
}


