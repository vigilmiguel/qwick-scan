package d.tonyandfriends.thirdtimesthecharm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
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

public class HistoryActivity extends AppCompatActivity {


    FirebaseUser firebaseUser;
    DatabaseReference databaseUserScanHistory;

    ValueEventListener databaseListener;

    List<Product> userProductHistory = new ArrayList<>();
    List<String> returnedVals = new ArrayList<>();

    Spinner s = (Spinner) findViewById(R.id.spinner3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null) {
            String dbHistoryPath = "userScanHistory/" + firebaseUser.getUid();

            // Refer to this user's specific sub-table within the scan history table.
            databaseUserScanHistory = FirebaseDatabase.getInstance().getReference(dbHistoryPath);
        }
    }

    // Called after onCreate()
    @Override
    protected void onStart() {
        super.onStart();

        /*
        We don't want this to keep calling onDataChange after we've left the page.
        So, only fetch the data once. Therefore, we use addListenerForSingleValueEvent.

        OR, we can use addValueEventListener and remove the listener when the user leaves
        this page. This may be useful if we want live data to be updated as it changes.
        We can use this when deleting data so it automatically updates.
         */

        databaseListener = databaseUserScanHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Product product = data.getValue(Product.class);

                    userProductHistory.add(product);
                }

                /*
                Displays the user's current scan history.
                For testing if the list actually received the products the user has scanned.
                */
                for(Product p : userProductHistory) {
                    Log.i("HistoryActivity", p.getName());
                    returnedVals.add(p.getName() +"\n" + p.getDateRecentlyScanned());
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, returnedVals);
                    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                    s.setAdapter(adapter);
                }
                s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        new AlertDialog.Builder(HistoryActivity.this)
                                .setTitle("Delete")
                                .setMessage("Do you really want to delete " + userProductHistory.get(position)+" from your history?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        databaseUserScanHistory.child(userProductHistory.get(position).getBarcode()).removeValue();
                                        Toast.makeText(HistoryActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        databaseUserScanHistory.removeEventListener(databaseListener);



    }


}
