package d.tonyandfriends.thirdtimesthecharm;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ListView;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryActivity extends AppCompatActivity {


    FirebaseUser firebaseUser;
    DatabaseReference databaseUserScanHistory;

    ValueEventListener databaseListener;
    ImageButton imageButton;
    ListView scanHistoryList;

    Retrofit retrofit;
    DatabaseAPI databaseAPI;

    List<Product> userProductHistory = new ArrayList<>();
    List<String> returnedVals = new ArrayList<>();
    //Spinner s;
    int select = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        imageButton = findViewById(R.id.imageButton4);
//        imageButton.setBackgroundDrawable(null);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Open the RegisterActivity page.
//                startActivity(new Intent(HistoryActivity.this, MenuActivity.class));
//            }
//        });

        //Bottom Navigation bar

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_menu:
                        startActivity(new Intent(HistoryActivity.this, MenuActivity.class));
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(HistoryActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.navigation_history:
                        startActivity(new Intent(HistoryActivity.this, HistoryActivity.class));
                        return true;
                }
                return false;            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //s = findViewById(R.id.spinner3);
        scanHistoryList = findViewById(R.id.historyList);
        Log.d("myFirstSTop", "ihere");
        if(firebaseUser != null) {
            String dbHistoryPath = "userScanHistory/" + firebaseUser.getUid();

            // Refer to this user's specific sub-table within the scan history table.
            databaseUserScanHistory = FirebaseDatabase.getInstance().getReference(dbHistoryPath);
        }
        returnedVals.add("");
        /*s.setSelection(0);
        //Heres our fancy clicker
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select = position; //Here we track the position using a class variable
                if (position > 0) {
                    new AlertDialog.Builder(HistoryActivity.this)

                            //your shit i didn't change
                            .setTitle("Delete")
                            .setMessage("Do you really want to delete " + userProductHistory.get(select-1).getName() + " from your history?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                // more shit of yours i didnt change, just instead of position we use select.
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    databaseUserScanHistory.child(userProductHistory.get(select-1).getBarcode()).removeValue();
                                    Log.d("myCrash2?", "or here?");
                                    Toast.makeText(HistoryActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    userProductHistory.clear();
                                    s.setAdapter(null);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        retrofit = new Retrofit.Builder()
                .baseUrl("http://18.216.191.20/php_rest_api/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        databaseAPI = retrofit.create(DatabaseAPI.class);

        scanHistoryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                select = position; //Here we track the position using a class variable
                if (position > 0) {
                    new AlertDialog.Builder(HistoryActivity.this)

                            //your shit i didn't change
                            .setTitle("Delete")
                            .setMessage("Do you really want to delete " + userProductHistory.get(select - 1).getName() + " from your history?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                // more shit of yours i didnt change, just instead of position we use select.
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    deleteProductFromHistory(userProductHistory.get(select - 1).getBarcode());

                                    getUserScanHistory();

                                    /*
                                    databaseUserScanHistory.child(userProductHistory.get(select - 1).getBarcode()).removeValue();
                                    Log.d("myCrash2?", "or here?");
                                    Toast.makeText(HistoryActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    userProductHistory.clear();
                                    scanHistoryList.setAdapter(null);
                                     */
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }

                // Means that a long click won't also count as a single click.
                return true;
            }
        });

        // Set item click to bring up item delete dialog
        scanHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                select = position; //Here we track the position using a class variable
                if (position > 0) {
                    new AlertDialog.Builder(HistoryActivity.this)

                            //your shit i didn't change
                            .setTitle("Rescan")
                            .setMessage("Do you want to rescan " + userProductHistory.get(select - 1).getName() + "?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                // more shit of yours i didnt change, just instead of position we use select.
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    Intent intent = new Intent(HistoryActivity.this, ScannerStartActivity.class);

                                    //Bundle bundle = new Bundle();

                                    String barcode = userProductHistory.get(select - 1).getBarcode();

                                    //bundle.putString();

                                    // Carry this barcode variable over to ScannerStartActivity.
                                    intent.putExtra("barcode", barcode);

                                    startActivity(intent);

                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }


            }
        });
    }



    // Called after onCreate()
    @Override
    protected void onStart() {
        super.onStart();

        getUserScanHistory();

        /*
        We don't want this to keep calling onDataChange after we've left the page.
        So, only fetch the data once. Therefore, we use addListenerForSingleValueEvent.

        OR, we can use addValueEventListener and remove the listener when the user leaves
        this page. This may be useful if we want live data to be updated as it changes.
        We can use this when deleting data so it automatically updates.


        databaseListener = databaseUserScanHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userProductHistory.clear();

                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    try
                    {
                        Product product = data.getValue(Product.class);

                        userProductHistory.add(product);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }



                }


                //Displays the user's current scan history.
                //For testing if the list actually received the products the user has scanned.




                returnedVals.clear();
                returnedVals.add("");

                for(Product p : userProductHistory) {
                    Log.i("HistoryActivity", p.getName());
                    returnedVals.add(p.getName() +"\n" + p.getDateRecentlyScanned() + "\nxfghcjvb");
                    //ArrayAdapter<String> adapter =
                    //        new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_dropdown_item_1line, returnedVals);
                    //adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, returnedVals);
                    // Do i need to set adapter here?
                    Log.d("myCrash?", "Is it here?");
                    scanHistoryList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
    }

    public void getUserScanHistory()
    {

        try
        {
            User user = new User(firebaseUser.getUid(), firebaseUser.getEmail());

            Call<List<Product>> call = databaseAPI.getUserHistory(user);

            Log.i("ScanHistory", "TRY CATCH DEEX NUTZ");

            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                    if(response.isSuccessful())
                    {
                        if(response.body() != null)
                        {
                            Log.i("ScanHistory", "Response and body success.");
                            userProductHistory = response.body();

                            returnedVals.clear();
                            returnedVals.add("");

                            for(Product p : userProductHistory) {

                                Log.i("HistoryActivity", p.getName());
                                returnedVals.add(p.getName() +"\nMost Recent Scan: " + p.getDateTimeScanned() +
                                        "\nTimes Scanned: " + p.getNumScans());

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, returnedVals);

                                scanHistoryList.setAdapter(adapter);
                            }
                        }
                        else
                        {
                            Log.i("ScanHistory", "result is empty.");
                        }
                    }
                    else
                    {
                        Log.i("ScanHistory", "response unsuccessful.");
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    Log.i("ScanHistory", "query failed.");

                    t.printStackTrace();
                }
            });

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteProductFromHistory(String barcode)
    {

        try
        {
            UserProduct userProduct = new UserProduct(barcode, firebaseUser.getUid());

            Call<Void> call = databaseAPI.deleteUserProduct(userProduct);

            Log.i("ScanHistory", "Delete history");

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if(response.isSuccessful())
                    {

                        Log.i("ScanHistory", "Delete success.");


                    }
                    else
                    {
                        Log.i("ScanHistory", "response unsuccessful.");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.i("ScanHistory", "query failed.");

                    t.printStackTrace();
                }
            });

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //databaseUserScanHistory.removeEventListener(databaseListener);
    }

}
