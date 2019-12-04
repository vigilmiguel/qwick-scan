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
import android.util.ArrayMap;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MenuActivity extends AppCompatActivity {

    // To handle delays.
    Handler handler = new Handler();

    Retrofit retrofit;
    DatabaseAPI databaseAPI;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

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


        Log.i("heawhaer", "hereherehereh");

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        // API Setup
        /*
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
                */

        retrofit = new Retrofit.Builder()
                .baseUrl("http://18.216.191.20/php_rest_api/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        databaseAPI = retrofit.create(DatabaseAPI.class);

        // If the user is not in the database, it puts them in the db.
        checkIfUserInDatabase();


    }

    public void checkIfUserInDatabase()
    {
        Log.i("DB Check for", "UID: " + firebaseUser.getUid());


        User user = new User(firebaseUser.getUid(), null);

        try
        {
            //Call<Void> c = databaseAPI.createUser(user);
            Call<User> call = databaseAPI.getUser(user);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.i("API Response Code:", "" + response.code());

                    if(response.isSuccessful())
                    {
                        // Success
                        User user = new User();

                        if(response.body() != null)
                            user = response.body();


                        if(!user.exists())
                        {
                            Log.i("User Info", "Does not exist!");
                            createUserInDB();
                        }
                        else
                        {
                            Log.i("User Info", "Username: " + user.getUserName() + " UID: " + user.getFirebaseUID());
                            createToast("Welcome back, " + user.getUserName(), Toast.LENGTH_SHORT);
                        }

                    }
                    else
                    {
                        createToast("ERROR: API Response Code: " + response.code(), Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //jsonInput.put("firebaseuid", firebaseUser.getUid());


    }

    public void createUserInDB()
    {
        User user = new User(firebaseUser.getUid(), firebaseUser.getEmail());

        Call<Void> call = databaseAPI.createUser(user);

        Log.i("heawhaer", "hereherehereh");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("API Response Code:", "" + response.code());

                if(response.isSuccessful())
                {
                    // Success
                    createToast("New user created!", Toast.LENGTH_SHORT);

                }
                else
                {
                    createToast("ERROR: API Response Code: " + response.code(), Toast.LENGTH_LONG);
                    ///Log.i("Error", response.body());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("FAIL", "" + t.getMessage());
            }
        });
    }

    public void createToast(String text, int duration)
    {
        Toast.makeText(MenuActivity.this, text, duration).show();
    }
}
