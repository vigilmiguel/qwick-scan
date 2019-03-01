package d.tonyandfriends.thirdtimesthecharm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;



public class MenuActivity extends AppCompatActivity {

    public static final int MENU_SCAN = 0;
    public static final int MENU_LOGOUT = 1;

    String menuNames[] = {"Scan", "Logout"};
    CircleMenu circleMenu;

    // To handle delays.
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);




        circleMenu = (CircleMenu)findViewById(R.id.circleMenu);
        circleMenu.setMainMenu(Color.parseColor("#ADADAD"), R.drawable.openmenu,
                R.drawable.closemenu)
                .addSubMenu(Color.parseColor("#2F37FF"), R.mipmap.barcode)
                .addSubMenu(Color.parseColor("#7E0B16"), R.drawable.logout)

                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int i) {
                        Toast.makeText(getApplicationContext(), "You Selected: " +
                                menuNames[i], Toast.LENGTH_SHORT).show();

                        //
                        switch(i)
                        {
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
                });

        /*
        findViewById(R.id.settingsicon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this   , ProfileActivity.class));
            }
        });
        findViewById(R.id.scannericon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this   , ScannerStartActivity.class));
            }
        });
        */
    }
}
