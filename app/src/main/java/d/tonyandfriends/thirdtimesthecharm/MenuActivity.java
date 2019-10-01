package d.tonyandfriends.thirdtimesthecharm;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;



public class MenuActivity extends AppCompatActivity implements OnMenuSelectedListener{

    public static final int MENU_SCAN = 0;
    public static final int MENU_HISTORY = 1;
    public static final int MENU_LOGOUT = 2;


    String menuNames[] = {"Scan", "History", "Logout"};
    CircleMenu circleMenu;

    // To handle delays.
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);




        circleMenu = findViewById(R.id.circleMenu);
        circleMenu.setMainMenu(Color.parseColor("#1e1f26"), R.drawable.openmenu,
                R.drawable.closemenu)
                .addSubMenu(Color.parseColor("#d0e1f9"), R.drawable.barcode)
                //.addSubMenu(Color.parseColor("#d0e1f9"), R.drawable.maps)
                .addSubMenu(Color.parseColor("#d0e1f9"), R.drawable.history)
                .addSubMenu(Color.parseColor("#d0e1f9"), R.drawable.logout);

        circleMenu.setOnMenuSelectedListener(this);
    }


    // Separate function so it looks cleaner.
    @Override
    public void onMenuSelected(int i) {
        Toast.makeText(getApplicationContext(), "You Selected: " +
                menuNames[i], Toast.LENGTH_SHORT).show();

        // start activities based on what was selected.
        switch (i) {
            case MENU_SCAN:
                // Wait 1 second  to complete menu animation.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MenuActivity.this,
                                ScannerStartActivity.class));
                    }
                }, 1000);
                break;
            /*
            case MENU_MAPS:
                // Wait 1 second  to complete menu animation.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MenuActivity.this,
                                MapsActivity.class));
                    }
                }, 1000);
                break;
            */
            case MENU_HISTORY:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MenuActivity.this,
                                HistoryActivity.class));
                    }
                }, 1000);
                break;

            case MENU_LOGOUT:
                // Wait 1 second  to complete menu animation.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MenuActivity.this,
                                ProfileActivity.class));
                    }
                }, 1000);
                break;
        }
    }
}
