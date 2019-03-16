package d.tonyandfriends.thirdtimesthecharm;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;



class Spider extends AsyncTask<String,Void,String>
{
    public DataTransporter myVessel = null;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected String doInBackground(String... Code) {
        //URL + Code will give us our product lookup
        String URL = "https://www.upcdatabase.com/item/";
        Response doc = null;
        String code = Code[0];
        String description = "poop";

        try {
            // How we connect and do other things
            doc = Jsoup.connect(URL + code).timeout(6000).execute();
            Document dic = doc.parse();
            // For a site with only one table this works, We will need the actual table names for sites with more than 1 table per page
            Elements elements  =  dic.select("table");
            Elements rows = elements.select("tr");
            Log.d("mySite", URL + code);
            Log.d("mySize", String.valueOf(rows.size()));
            if(rows.size() < 3) // Check how big the table is, if its size 2 then There is no valid Scan in their DB
            {
                description = "Sorry we couldn't find that item";
            }
            else { // Otherwise the second row gives us the Name

                description = rows.get(2).text();
                Log.d("mydesc", description);
                int i =2;
                while(!description.contains("Description"))
                {
                    description = rows.get(++i).text();

                }
                String temp = rows.get(++i).text();
                if(temp.contains("Size"))
                {
                    temp = temp.substring(11);
                    description += "" + temp;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return description;
    }

    public void onPostExecute(String result) {

        // Only way I found to transfer Data from Async task to other places, using an interface
        myVessel.onProcessDone(result);
    }
}


