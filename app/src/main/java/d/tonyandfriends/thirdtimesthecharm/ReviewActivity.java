package d.tonyandfriends.thirdtimesthecharm;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Log.i("ReviewActivity", "ImageButton");
        Bundle bundle = getIntent().getExtras();

        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> starRatings = new ArrayList<>();

        ArrayList<ImageButton> imageButtons = new ArrayList<>();
        ArrayList<TextView> ratingTexts = new ArrayList<>();

        imageButtons.add((ImageButton)findViewById(R.id.logoImageButton0));
        imageButtons.add((ImageButton)findViewById(R.id.logoImageButton1));
        imageButtons.add((ImageButton)findViewById(R.id.logoImageButton2));
        imageButtons.add((ImageButton)findViewById(R.id.logoImageButton3));
        imageButtons.add((ImageButton)findViewById(R.id.logoImageButton4));
        imageButtons.add((ImageButton)findViewById(R.id.logoImageButton5));

        ratingTexts.add((TextView)findViewById(R.id.ratingText0));
        ratingTexts.add((TextView)findViewById(R.id.ratingText1));
        ratingTexts.add((TextView)findViewById(R.id.ratingText2));
        ratingTexts.add((TextView)findViewById(R.id.ratingText3));
        ratingTexts.add((TextView)findViewById(R.id.ratingText4));
        ratingTexts.add((TextView)findViewById(R.id.ratingText5));

        //Bottom Navigation bar

        //Bottom Navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_menu:
                        startActivity(new Intent(ReviewActivity.this, MenuActivity.class));
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(ReviewActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.navigation_history:
                        startActivity(new Intent(ReviewActivity.this, HistoryActivity.class));
                        return true;
                }
                return false;            }
        });

        for(int i = 0; i < imageButtons.size(); i++)
        {
            imageButtons.get(i).setVisibility(ImageButton.INVISIBLE);
        }

        for(int i = 0; i < ratingTexts.size(); i++)
        {
            ratingTexts.get(i).setVisibility(TextView.INVISIBLE);
        }

        if(bundle != null) {
            urls = bundle.getStringArrayList("urls");
            names = bundle.getStringArrayList("names");
            starRatings = bundle.getStringArrayList("starRatings");
        }

        if(urls != null && names != null && starRatings != null) {

            int size = getMin(urls.size(), names.size(), starRatings.size());

            for (int i = 0; i < size; i++) {
                // Get the company image based on their name.
                int image = getCompanyImage(names.get(i));

                imageButtons.get(i).setImageResource(image);
                imageButtons.get(i).setTag(urls.get(i));
                imageButtons.get(i).setVisibility(ImageButton.VISIBLE);
                imageButtons.get(i).setClickable(true);

                imageButtons.get(i).setOnClickListener(this);

                ratingTexts.get(i).setText(starRatings.get(i));
                ratingTexts.get(i).setVisibility(TextView.VISIBLE);


            }

            /*
            imageButtons.get(0).setImageResource(R.mipmap.best_buy);
            imageButtons.get(1).setImageResource(R.mipmap.ebay);
            imageButtons.get(2).setImageResource(R.mipmap.home_depot);
            imageButtons.get(3).setImageResource(R.mipmap.macys);
            imageButtons.get(4).setImageResource(R.mipmap.target);
            imageButtons.get(5).setImageResource(R.mipmap.walmart);
            */

        }



    }


    @Override
    public void onClick(View v) {

        String url = (String)v.getTag();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        intent.setData(Uri.parse(url));

        try
        {
            startActivity(intent);
        }
        catch(Exception e)
        {
            Toast.makeText(ReviewActivity.this, "Invalid website!", Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }


    }


    int getCompanyImage(String companyName)
    {
        int imageID;

        switch(companyName.toUpperCase())
        {
            case "BESTBUY": case "BEST_BUY": case "BEST-BUY": case "BEST BUY":
                imageID = R.mipmap.best_buy;
                break;

            case "EBAY": case "E_BAY": case "E-BAY": case "E BAY":
                imageID = R.mipmap.ebay;
                break;

            case "HOMEDEPOT": case "HOME_DEPOT": case "HOME-DEPOT": case "HOME DEPOT":
                imageID = R.mipmap.home_depot;
                break;

            case "MACYS": case "MACY'S":
                imageID = R.mipmap.macys;
                break;

            case "TARGET":
                imageID = R.mipmap.target;
                break;

            case "WALMART": case "WAL_MART": case "WAL-MART": case "WAL MART":
                imageID = R.mipmap.walmart;
                break;

            default:
                imageID = R.mipmap.no_company;
                break;
        }

        return imageID;
    }

    int getMin(int x, int y, int z)
    {
        List<Integer> list = new ArrayList<>();
        int min;

        list.add(x);
        list.add(y);
        list.add(z);

        min = list.get(0);


        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i) < min)
            {
                min = list.get(i);
            }
        }


        return min;
    }


}
