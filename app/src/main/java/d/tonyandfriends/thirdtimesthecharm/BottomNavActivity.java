package d.tonyandfriends.thirdtimesthecharm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

public class BottomNavActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_menu:
                    mTextMessage.setText("Main Menu");
                    startActivity(new Intent(BottomNavActivity.this, ScannerStartActivity.class));
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText("Profile");
                    startActivity(new Intent(BottomNavActivity.this, ProfileActivity.class));
                    return true;
                case R.id.navigation_history:
                    mTextMessage.setText("History");
                    startActivity(new Intent(BottomNavActivity.this, HistoryActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
