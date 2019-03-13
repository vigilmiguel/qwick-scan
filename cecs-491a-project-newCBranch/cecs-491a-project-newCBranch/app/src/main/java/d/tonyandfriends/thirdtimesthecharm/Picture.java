package d.tonyandfriends.thirdtimesthecharm;
import android.util.Log;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
public class Picture {
    public String image(String x)
    {
        String URL = "https://www.barcodelookup.com/";
        Response doc ; // = null
        String code = x;
        String description = "";

        try {
            doc = Jsoup.connect(URL + code).timeout(6000).execute();
            Document dic = doc.parse();
            Elements elements = dic.select("src"); // finds all h4 headers and add them together
            //String y = elements.eachText().get(0); //eachText returns an list of strings so we just need the first one
            if(elements.size() == 0) // if there were no values found with the header then return empty string and try next database
            {
                description = "Sorry we couldn't find that item";
                return description;
            }
            Log.d("code",code);
            description =  elements.eachText().get(0);



        } catch (IOException e) {
            e.printStackTrace();
        }




        return "";
    }
}
