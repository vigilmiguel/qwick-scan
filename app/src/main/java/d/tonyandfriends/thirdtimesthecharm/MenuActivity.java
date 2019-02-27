package d.tonyandfriends.thirdtimesthecharm;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class MenuActivity extends AppCompatActivity {

    String menuNames[] = {"Scan"};
    CircleMenu circleMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);




        circleMenu = (CircleMenu)findViewById(R.id.circleMenu);
        circleMenu.setMainMenu(Color.parseColor("#ADADAD"), R.drawable.openmenu, R.drawable.closemenu)
                .addSubMenu(Color.parseColor("#2F37FF"), R.mipmap.barcode)

                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int i) {
                        Toast.makeText(getApplicationContext(), "You Selected: " + menuNames[i], Toast.LENGTH_SHORT).show();
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
