package d.tonyandfriends.thirdtimesthecharm;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.simple.parser.JSONParser;
import org.json.*;


import org.json.JSONObject;
import java.net.URL;

// String url = "qwickscandb.copnww0vhd9s.us-east-2.rds.amazonaws.com"

public class ContactAPI extends AsyncTask<Void, Void, String> {
    private Context mContext;
    private String mUrl;
    public ContactAPI(Context context, String url) {
        mContext = context;
        mUrl = url;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(Void... params) {
        String resultString = null;
        resultString = getJSON(mUrl);

        return resultString;
    }

    @Override
    protected void onPostExecute(String strings) {
        super.onPostExecute(strings);
    }

    private String getJSON(String url) {
        JSONParser jParser = new JSONParser();
        // getting JSON string from URL
        JSONObject json = jParser.getJSONObject("sys");
        return json;
    }

}