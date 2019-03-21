package d.tonyandfriends.thirdtimesthecharm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Serializable {

    //private static final android.util.Log Log = ;
    private GoogleMap mMap;
    //SpiderData mydata;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        title = (TextView)findViewById(R.id.Title);
        title.setBackgroundColor(Color.WHITE);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<LatLng> myMarkers = new ArrayList<LatLng>();

        //retrieving our spiderData
        SharedPreferences mPrefs = getSharedPreferences("poop",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("mySpider",null);
        SpiderData obj = gson.fromJson(json,SpiderData.class);
        if(obj.getLatitude().size() == 0 || obj.getLongitude().size() == 0)
        {
            Toast.makeText(MapsActivity.this, "Sorry No Results!", Toast.LENGTH_LONG).show();
            LatLng errorCord = new LatLng(-77.8400829,166.6443582);
        }
        else {

            for (int i = 0; i < obj.getLatitude().size(); i++) {

                Log.d("MyLat", Double.toString(obj.getLatitude().get(i)));
                Log.d("MyLot", Double.toString(obj.getLongitude().get(i)));
                LatLng temp = new LatLng(obj.getLatitude().get(i), obj.getLongitude().get(i));
                Log.d("mylatng", temp.toString());
                myMarkers.add(new LatLng(obj.getLatitude().get(i), obj.getLongitude().get(i)));
                mMap.addMarker(new MarkerOptions().position(myMarkers.get(i)).title(obj.getLocalStores().get(i)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myMarkers.get(i)));
            }
        }

    }


}
