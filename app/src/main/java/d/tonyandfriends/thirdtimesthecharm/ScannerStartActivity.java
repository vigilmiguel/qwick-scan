package d.tonyandfriends.thirdtimesthecharm;

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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
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

    private ArrayList<TextView> priceTextViews;
    private ArrayList<TextView> storeTextViews;

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
    Button scanButton;
    Button menuButton;
    Button shareButton;
    Button reviewButton;
    Button apiButton;

    String productName = "";
    String productImage = "";
    String productBarode = "";

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseUserScanHistory;
    DatabaseReference databaseProductsScanned;
    


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

        storeTextViews = new ArrayList<>();
        priceTextViews = new ArrayList<>();

        storeTextViews.add((TextView)findViewById(R.id.S1));
        storeTextViews.add((TextView)findViewById(R.id.S2));
        storeTextViews.add((TextView)findViewById(R.id.S3));

        priceTextViews.add((TextView)findViewById(R.id.P1));
        priceTextViews.add((TextView)findViewById(R.id.P2));
        priceTextViews.add((TextView)findViewById(R.id.P3));



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
        scanButton = (Button)findViewById(R.id.scan_button);
        menuButton = (Button)findViewById(R.id.menu_button);
        shareButton = (Button)findViewById(R.id.share_button);
        reviewButton = findViewById(R.id.review_button);
        apiButton = findViewById(R.id.api_button);


        menuButton.setVisibility(Button.INVISIBLE);
        scanButton.setVisibility(Button.INVISIBLE);
        pBar.setVisibility(ProgressBar.VISIBLE);
        //Title.setVisibility(TextView.INVISIBLE);
        mapButton.setVisibility(Button.INVISIBLE);
        shareButton.setVisibility(Button.INVISIBLE);
        reviewButton.setVisibility(Button.INVISIBLE);
        apiButton.setVisibility(Button.INVISIBLE);

        for(int i = 0; i < storeTextViews.size() && i < priceTextViews.size(); i++) {
            storeTextViews.get(i).setVisibility(TextView.INVISIBLE);
            priceTextViews.get(i).setVisibility(TextView.INVISIBLE);
        }

        /*
        S1.setVisibility(S1.INVISIBLE);
        S2.setVisibility(S2.INVISIBLE);
        S3.setVisibility(S3.INVISIBLE);
        P1.setVisibility(P1.INVISIBLE);
        P2.setVisibility(P2.INVISIBLE);
        P3.setVisibility(P3.INVISIBLE);
        */
        Progress.setVisibility(Progress.VISIBLE);
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
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

        for(int k =0; k < store.size() && k < storeTextViews.size(); k++)
        {
            if(store.get(k) == NULL || store.get(k)== "")
            {
                store.set(k,"");
            }

            storeTextViews.get(k).setText(store.get(k));
        }
        for(int j =0; j < price.size() && j < priceTextViews.size(); j++)
        {
            if(price.get(j) == NULL || price.get(j)== "")
            {
                price.set(j,"");
            }

            priceTextViews.get(j).setText(price.get(j));
        }
        for(int j =0; j < links.size(); j++)
        {
            if(links.get(j) == NULL || links.get(j)== "")
            {
                links.set(j,"");
            }
        }

        /*
        S1.setText(store.get(0));
        S2.setText(store.get(1));
        S3.setText(store.get(2));
        P1.setText(price.get(0));
        P2.setText(price.get(1));
        P3.setText(price.get(2));
        */


        pBar.setVisibility(ProgressBar.INVISIBLE);
        Progress.setVisibility(Progress.INVISIBLE);
        //Title.setVisibility(TextView.VISIBLE);

        for(int i = 0; i < storeTextViews.size() && i < priceTextViews.size(); i++) {
            storeTextViews.get(i).setVisibility(TextView.VISIBLE);
            priceTextViews.get(i).setVisibility(TextView.VISIBLE);
        }

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
        productImageView = findViewById(R.id.ProductPicture);
        // The name will return "Description $itemName", I dont want it to say Description, so this is a quickfix until we find a better way to parse the HTML
        // If we find a result...
        String pname = result.getProductName();
        String purl = result.getImgURL();
        if(spidey.foundProduct) {

            Log.i("SCANNERSTARTACTIVITY","PRODUCT_FOUND");
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                    .format(Calendar.getInstance().getTime());

            productName = pname;

            Product product = new Product(productBarode, productName, purl, currentTime, 1);

            // Store it in the database
            storeInDatabase(product);

            menuButton.setVisibility(Button.VISIBLE);
            mapButton.setVisibility(Button.VISIBLE);
            scanButton.setVisibility(Button.VISIBLE);
            shareButton.setVisibility(Button.VISIBLE);
            reviewButton.setVisibility(Button.VISIBLE);
            apiButton.setVisibility(Button.VISIBLE);



            menuButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(ScannerStartActivity.this, MenuActivity.class));
                }
            });
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ScannerStartActivity.this, MapsActivity.class));
                }
            });
            scanButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(ScannerStartActivity.this, ScannerStartActivity.class));
                }
            });
            shareButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ScannerStartActivity.this, shareActivity.class);

                    // Create a bundle to store the info we want to send to shareActivity.
                    Bundle bundle = new Bundle();

                    // Store the name, stores and prices for the product in the bundle.
                    bundle.putString("productName", productName);
                    bundle.putStringArrayList("stores", store);
                    bundle.putStringArrayList("prices", price);




                    // Add it to the intent.
                    intent.putExtras(bundle);


                    // Fire her up!!
                    startActivity(intent);
                }
            });
            apiButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("CheckDisOut", productBarode);
                    //new ContactAPI().execute("http://18.216.191.20/php_rest_api/api/users/readAll.php");
                    new ContactAPI().execute("http://18.216.191.20/php_rest_api/api/products/readBarcode.php?barcode="+productBarode);

                }
            });
            reviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ScannerStartActivity.this, ReviewActivity.class);

                    Bundle bundle = new Bundle();

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
                    */

                    bundle.putStringArrayList("urls", urls);
                    bundle.putStringArrayList("names", names);
                    bundle.putStringArrayList("starRatings", starRatings);


                    intent.putExtras(bundle);


                    startActivity(intent);
                }
            });
        }


        // If we don't find a result...
        else {
            Log.i("SCANNERSTARTACTIVITY","PRODUCT_NOT_FOUND");
            productName = "Sorry, we could not find that product!";
        }


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


        statusMessage.setText(productName);
    }



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

    // If the product is already in the given table, it will just update the date and count.
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
            /*
                This is called either when initialized(which is now) or when the data is changed.
                Because its under a listener for a single value event, it will only be called now
                and not at a later time when the data changes again. (The shit will hit the fan
                if it does! xD)
             */
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

                    /*
                    Toast.makeText(ScannerStartActivity.this,
                            "This is already in the database!", Toast.LENGTH_SHORT).show();
                    */

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

                    /*
                    Toast.makeText(ScannerStartActivity.this,
                            "Never seen this one before!", Toast.LENGTH_SHORT).show();
                    */

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
                    productBarode = poop;
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
                    productBarode = testUPC;
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
                int barcode = parentObject.getInt("barcode");
                int productID = parentObject.getInt("productid");
                String imageURL = parentObject.getString("imageurl");
                String result = productName + " " + barcode + " " + productID + " " + imageURL;
                Log.d("whaHappen", result);
                return result;
                /*JSONArray parentArray = parentObject.getJSONArray("data");
                JSONObject finalObject = parentArray.getJSONObject(0);
                int userID = finalObject.getInt("userid");
                String firebaseUID = finalObject.getString("firebaseUID");
                String userName = finalObject.getString("userName");
                return userID + " " + firebaseUID + " " + userName;*/
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
            statusMessage.setText(result);
        }
    }
}

