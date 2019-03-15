package d.tonyandfriends.thirdtimesthecharm;
//import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.*;



class Spider extends AsyncTask<String,Void,ArrayList<String>> {
    public DataTransporter myVessel = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected ArrayList<String> doInBackground(String... Code) {
        ArrayList<String> product = new ArrayList<String>();
        String url = "";
        String description = "";
        String code = Code[0];

        description = FirstDB(code);

        if (description == "") {
            description = SecondDB(code);
        }

        url = image(code);
        product.add(description);
        product.add(url);
        Log.d("firstd",description);
        Log.d("firsturl",url);
        return product;
    }

    public void onPostExecute(ArrayList<String> result) {

        // Only way I found to transfer Data from Async task to other places, using an interface
        myVessel.onProcessDone(result);
    }

    public String FirstDB(String x) {
        String URL = "https://www.upcdatabase.com/item/";
        Response doc; // = null
        String code = x;
        String description = "";


        try {
            doc = Jsoup.connect(URL + code).timeout(6000).execute();
            Document dic = doc.parse();
            // For a site with only one table this works, We will need the actual table names for sites with more than 1 table per page
            Elements elements = dic.select("table");
            Elements rows = elements.select("tr"); // this finds elements with tr and counts it
            Log.d("mySite", URL + code);
            Log.d("mySize", String.valueOf(rows.size()));
            if (rows.size() < 3) // Check how big the table is, if its size 2 then There is no valid Scan in their DB
            {
                description = "";
            } else { // Otherwise the second row gives us the Name

                description = rows.get(2).text();
                Log.d("mydesc", description); // name on barcode lookup is going to be h4
                int i = 2;
                while (!description.contains("Description")) {
                    description = rows.get(++i).text();

                }
                Log.d("des", description);
                if (description.contains("Description")) {
                    description = description.substring(12); // gets rid of the description part
                }
                String temp = rows.get(++i).text();
                if (temp.contains("Size")) {
                    temp = temp.substring(11);
                    description += "" + temp;
                }
                Log.d("ss", temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return description;

    }

    public String SecondDB(String x) {
        String URL = "https://www.barcodelookup.com/";
        Response doc; // = null
        String code = x;
        String description = "";

        try {
            doc = Jsoup.connect(URL + code).timeout(6000).execute();
            Document dic = doc.parse();
            Elements elements = dic.select("h4"); // finds all h4 headers and add them together
            //String y = elements.eachText().get(0); //eachText returns an list of strings so we just need the first one
            if (elements.size() == 0) // if there were no values found with the header then return empty string and try next database
            {
                description = "Sorry we couldn't find that item";
                return description;
            }
            Log.d("code", code);
            description = elements.eachText().get(0);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return description;
    }

    public String image(String x)
    {
        //List temp = new ArrayList();
        //boolean temp = false;
        //Log.d("Hi","hi");
        String URL = "https://www.barcodelookup.com/";
        Response doc; // = null
        String code = x;
        String imageurl = "";
        try {
            doc = Jsoup.connect(URL + code).timeout(6000).execute();
            Document dic = doc.parse();
            Elements elements = dic.select("div#images"); // finds all h4 headers and add them together
            //Log.d("myelements",elements.toString());
           // temp = elements.hasAttr("img src");
            Elements ss = dic.getElementsByAttribute("src");
            imageurl = ss.eq(6).toString();
            imageurl = imageurl.substring(10,62);
            Log.d("firstone",imageurl);
            //Log.d("tester", ss.toString());

            //Log.d("xxxxx",Boolean.toString(temp));
            String aa = dic.getElementsByAttribute("src").first().toString();
            //String y = elements.eachText().get(0); //eachText returns an list of strings so we just need the first one
            if (elements.size() == 0) // if there were no values found with the header then return empty string and try next database
            {
                return imageurl;
            }
            //Log.d("myurl", (String)temp.get(0));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageurl;
    }


}


