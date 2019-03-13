package d.tonyandfriends.thirdtimesthecharm;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Test{
    public String test(String x)
        {
        String URL = "https://www.upcdatabase.com/item/";
        Response doc ; // = null
        String code = x;
        String description = "";


        try {
            doc = Jsoup.connect(URL + code).timeout(6000).execute();
            Document dic = doc.parse();
            // For a site with only one table this works, We will need the actual table names for sites with more than 1 table per page
            Elements elements  =  dic.select("table");
            Elements rows = elements.select("tr"); // this finds elements with tr and counts it
            Log.d("mySite", URL + code);
            Log.d("mySize", String.valueOf(rows.size()));
            if(rows.size() < 3) // Check how big the table is, if its size 2 then There is no valid Scan in their DB
            {
                description = "";
            }
            else { // Otherwise the second row gives us the Name

                description = rows.get(2).text();
                Log.d("mydesc", description); // name on barcode lookup is going to be h4
                int i =2;
                while(!description.contains("Description"))
                {
                    description = rows.get(++i).text();

                }
                Log.d("des",description);
                if(description.contains("Description"))
                {
                    description = description.substring(12); // gets rid of the description part
                }
                String temp = rows.get(++i).text();
                if(temp.contains("Size"))
                {
                    temp = temp.substring(11);
                    description += "" + temp;
                }
                Log.d("ss",temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return description;
    }

}
