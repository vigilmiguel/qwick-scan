package d.tonyandfriends.thirdtimesthecharm;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;



public class MenuActivity extends AppCompatActivity {

    // To handle delays.
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //Bottom Navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_menu:
                        startActivity(new Intent(MenuActivity.this, MenuActivity.class));
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(MenuActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.navigation_history:
                        startActivity(new Intent(MenuActivity.this, HistoryActivity.class));
                        return true;
                }
                return false;            }
        });

        //Testing Button for Auto Testers

        ImageButton scanButton2 = (ImageButton)findViewById(R.id.imageButton);

        scanButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlphaAnimation buttonClick = new AlphaAnimation(0.2F, 1F);
                buttonClick.setDuration(1000);
                buttonClick.setStartOffset(2000);
                buttonClick.setFillAfter(true);
                v.startAnimation(buttonClick);
                startActivity(new Intent(MenuActivity.this, ScannerStartActivity.class));            }
        });


        Button logoutButton = (Button)findViewById(R.id.profile_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ProfileActivity.class));            }
        });

        //End of Testing
    }
}
