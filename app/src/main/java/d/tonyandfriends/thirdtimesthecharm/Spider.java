package d.tonyandfriends.thirdtimesthecharm;
//import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.*;



class Spider extends AsyncTask<String,Void,SpiderData> {
    public DataTransporter myVessel = null;
    public SpiderData myInfo = new SpiderData();
    public boolean foundProduct = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected SpiderData doInBackground(String... Code) {
        ArrayList<String> product = new ArrayList<String>();
        String url = "";
        String description = "";
        String code = Code[0];
        Log.d("myCiode",code);

        FirstDB(code);
        if(!foundProduct)  SecondDB(code);
        if(foundProduct)
        {
            //crawling time
            image(code);
            getPrice(myInfo.getProductName());
            locationScraper(myInfo.getProductName());
        }

        //url = image(code);
        //product.add(description);
        //product.add(url);
        Log.d("firstd",description);
        Log.d("firsturl",url);
        return myInfo;
    }

    public void onPostExecute(SpiderData result) {

        // Only way I found to transfer Data from Async task to other places, using an interface
        myVessel.onProcessDone(result);
    }

    public void FirstDB(String x) {
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
                Log.d("MyfirstFailure", "a true fail");
                return;
            } else { // Otherwise the second row gives us the Name

                foundProduct = true;
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
                myInfo.setProductName(description);
                Log.d("ss", temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void SecondDB(String x) {
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
                Log.d("mySecond Failure","wowowow");
                return;
            }

            Log.d("code", code);
            description = elements.eachText().get(0);
            Log.d("MyBS", description);
            if(description.length() == 0)
            {
                Log.d("myLength","ezfix");
                return;
            }
            foundProduct = true;
            myInfo.setProductName(description);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void image(String x)
    {
        String URL = "https://www.barcodelookup.com/";
        String bigURL ="";
        String finalURL ="";
        try {
            Document dic = Jsoup.connect(URL + x).get();
            Elements elements = dic.select("div#images"); // go straight to Div Images
            // Here we use the ID to select the string, it has a bunch of junk in addition to the img url we want. thats why I call it bigURL
            for(Element e: elements)
            {
                bigURL = e.getElementById("img_preview").toString();
                //Log.d("whats happening", e.getElementById(""))
            }
            int i = 10; // The string we wants always starts at index 10 <img src="   then after that our url is found

            // Check if bigURL has a length of at least i+1 before checking indices.
            if(bigURL.length() > i) {

                while(bigURL.charAt(i) != '"') // extract until we gt to the terminating quote at the end
                {
                    finalURL += bigURL.charAt(i++);
                }
            }

            myInfo.setImgURL(finalURL);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //A complicated function full of wonder and mystery
    public void getPrice(String product)
    {

        // add "+" to make a google search
        Log.d("myPRoduict",product);
        product = product.replace("(","");
        product = product.replace(")","");
        product = product.replaceAll("\\s","+");
        //SpiderData myInfo = new SpiderData();
        String secondVisit = "";
        // our first hop, google shopping query
        String googleQuery = "https://www.google.com/search?q=" + product + "&hl=en&tbm=shop&tbs=p_ord:rv&ei=73uNXKbSEeHi9APDlJ64Bw&ved=0ahUKEwjmheCC3ofhAhVhMX0KHUOKB3cQuw0IkAMoAQ";
        try {
            Document dic = Jsoup.connect(googleQuery).get();
            Elements elements = dic.select("div.eIuuYe");
            Log.d("MyGoogleQuery",googleQuery);
            // This is our link we need to follow to get to the prices page
            if(elements.size() == 0) return;
            String firstURL = elements.get(0).toString();
            int i = 45;
            while(firstURL.charAt(i) != '"')
            {
                secondVisit += firstURL.charAt(i++);
            }
            String letsTryit = "https://www.google.com" + secondVisit;
            dic = Jsoup.connect(letsTryit).get();

            //This selects all the base prices
            elements = dic.select(".tiOgyd");
            int Size = elements.size();
            //For now im going with only 3 results, we can change this later if we so choose
            if(Size > 3) Size = 3;

            //First loop grabs the prices
            for(i=0; i<Size;i++)
            {
                int j = 21;
                String temp = "";
                while(elements.get(i).toString().charAt(j) != '<') temp += elements.get(i).toString().charAt(j++);

                temp = temp.replace("&amp;","&");
                Log.d("myReplace",temp);
                myInfo.addPrice(temp); // add to our data
            }

            //This displays name and URL info
            elements = dic.select(".os-seller-name");
            Log.d("myConfuse",elements.toString());
            for(i=0; i<Size;i++)
            {
                Log.d("myNameSite",elements.get(i).toString());
                //First part of loop gets URL(Not working atm, the url is a special google one and it wont work unless directly clicked, so parsing is out of the question)
                //I'll try to find a solution one day.
                int j = 90;
                String temp = "";
                while(elements.get(i).toString().charAt(j) != '"')  temp += elements.get(i).toString().charAt(j++);
                temp = "https://www.google.com" + temp;
                myInfo.addURL(temp); //add to our data

                // Second loop grabs the name of the website.
                j+= 40;
                //j += 18;
                String temp2 ="";
                while(elements.get(i).toString().charAt(j) != '<') temp2 += elements.get(i).toString().charAt(j++);
                temp2 = temp2.replace("\u0026amp;","&");

                Log.d("myRealReplace",temp2);
                myInfo.addName(temp2); //Add to our data
            }


        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d("myPatience","isrunning out");
        }
    }
    //An n^2 function, with an nlogn kind of feel
    public void locationScraper(String product) // Get address and coordinates to local stores that may sell our product
    {
        // Priming our product String for a search
       Double longSign = 1.0;
       Double latSign = 1.0;
       product = product.replace("(","");
       product = product.replace(")","");
       product = product.replace("&","and");
       product = product.replaceAll("\\s","+");
       String googleQuery = "https://www.google.com/search?q=" + product + "+near+me"; // the query
       ArrayList<String> localStores = new ArrayList<String>(); // Two ArrayLists that will store the data we parse
       ArrayList<String> localCords = new ArrayList<String>();

        try {
            Document dic = Jsoup.connect(googleQuery).get();
            Elements elements = dic.select("div.dbg0pd"); // This retrieves the names of our stores
            //Elements elements2 = dic.select(".rllt__details");
            Log.d("myLocationQuery",googleQuery);
            Log.d("MyTestSize", Integer.toString(elements.size()));
            //Log.d("MyTestSize", Integer.toString(elements2.size()));
            //Log.d("MyLoop1", elements.get(0).toString());
            //Log.d("MyLoop1", Character.toString(elements.get(0).toString().charAt(58)));

            // Again I limit to 3 results, we can change in the future
            int Size = elements.size();
            if(Size == 0) return;
            if(Size > 3) Size = 3;

            //How we get our name from the Parsed Div, its pretty big
            int j =58;
            for(int i =0; i<Size; i++)
            {
               String temp  ="";
               while(elements.get(i).toString().charAt(j) != '<') temp += elements.get(i).toString().charAt(j++);
               localStores.add(temp);
               j = 58;
               Log.d("myFinalStore",temp);
            }
            for(int i=0; i<localStores.size(); i++) Log.d("myLocals:", localStores.get(i));


            elements = dic.select(".rllt__details");

            //This has the address data, split into two spans within it
            elements = dic.select(".rllt__details span");
            Log.d("myRRLLY", Integer.toString(elements.size()));
            for(int i =0; i<elements.size(); i++) Log.d("myGG", elements.get(i).toString());
            Log.d("MySizeee", Integer.toString(Size));


            int n =0;
            j = 6;
            //boolean continueScan = false;
            int continueScan = 0;
            Log.d("myReally?", elements.toString());

            //The loop to parse our address
            for(int i=0; i<Size; i++)
            {
                Log.d("myi", Integer.toString(i));
                // For each store we have, we need to find its corresponding address in a sea of useless span information
                while (n < elements.size()) {

                    Log.d("myHUH?", elements.get(n).toString());
                    char validChar = elements.get(n++).toString().charAt(j); //The char we should start at
                    int validInt = validChar - '0';
                    if(continueScan > 1)
                    {
                        continueScan = 0;
                        break;
                    }
                    if(continueScan ==1 &&(validInt <0 || validInt >9))
                    {
                        continueScan = 0;
                        break;
                    }
                    //A disgusting if loop that checks (I think) all unvalid chars. if it works it doesnt matter how ugly it is
                    if(validChar != '<' && validChar != '>' && validChar != '"' && validChar != ' '
                    && validChar != '$' &&(validChar != 'T' && elements.get(n-1).toString().charAt(j+1) != 'h'
                    && elements.get(n-1).toString().charAt(j+2) != 'e' && elements.get(n-1).toString().charAt(j+3) != 'i')
                    & elements.get(n-1).toString().charAt(j-1) != ' ')
                    {
                        Log.d("myValid", Character.toString(validChar));
                        //Appending the address to our store Name
                        String temp = "";
                        temp += localStores.get(i) + " ";
                        while(elements.get(n-1).toString().charAt(j) != '<') temp += elements.get(n-1).toString().charAt(j++);
                        localStores.set(i,temp);
                        Log.d("myFinalSet",localStores.get(i));
                        j=6;
                        continueScan += 1;
                        //break;
                    }
                    else if(continueScan == 1)
                    {
                        continueScan = 0;
                        j=6;
                        break;
                    }

                    // A relic of the past, but one day I might revert to it.
                    /*
                    int peter = elements.get(n++).toString().charAt(j) - '0';
                    if (peter >= 0 && peter <= 9) {
                        String temp = "";
                        temp += localStores.get(i) + " ";
                        Log.d("myChar", Integer.toString(peter));
                        while(elements.get(n-1).toString().charAt(j) != '<') temp += elements.get(n-1).toString().charAt(j++);
                        localStores.set(i,temp);
                        Log.d("MyFinalSet",localStores.get(i));
                        j = 6;
                        break;
                    }
                    */
                }
            }


            //Coordinate time
            for(int i =0; i<localStores.size(); i++)
            {
                Log.d("myWHAT", localStores.get(i));
                //The next netcall, finding coordinates
                String tempStore = localStores.get(i);
                tempStore = tempStore.replace("&","and");
                tempStore = tempStore.replace("-","");
                tempStore = tempStore.replaceAll("\\s","+");
                googleQuery = "https://www.google.com/search?q=" + tempStore + "coordinates";
                //googleQuery = googleQuery.replace("","+");
                dic = Jsoup.connect(googleQuery).get();
                Log.d("myLocalQuery",googleQuery);
                elements = dic.select("div.Z0LcW"); // should get us straight to the cords
                Log.d("myCords", elements.toString());
                localCords.add(elements.toString()); // Add them to our local
                myInfo.addLocalStore(localStores.get(i)); //while we're here add our final stores to our SpiderData
                Log.d("myFirst cords" +i, elements.toString());
            }
            //String manipulation stuff
            for(int i=0; i<localCords.size();i++)
            {
                Log.i("TESTLOOP", "" + i);
                String temp = localCords.get(i);

                // Sometimes getLocalStores wouldn't have an ith element.
                if(temp.length() == 0 && myInfo.getLocalStores().size() > i) {
                    Log.d("mySac", "i get here");
                    myInfo.getLocalStores().remove(i);
                    continue;
                }

                // Sometimes the string was too short.
                if(temp.length() > 45)
                    temp = temp.substring(21,45); //rough guess of where our cords are
                Log.d("myyyy",temp);
                if(temp.contains("W")) longSign = -1.0;
                if(temp.contains("S")) latSign = -1.0;
                // get rid of that bullstuff
                temp = temp.replaceAll("N","");
                temp = temp.replaceAll("E","");
                temp = temp.replaceAll("S","");
                temp  =temp.replaceAll("W","");
                temp = temp.replaceAll(",","");
                temp  = temp.replaceAll("\u00B0","");
                Log.d("myFinalCordTemp", temp);
                //Split our cords into latitude and longitude
                String [] tempCords = temp.split(" ",2);

                // It sometimes only has a length of 1.
                if(tempCords.length == 2) {
                    Log.d("myzzzz", tempCords[0]);
                    Log.d("myjjjjjj", tempCords[1]);

                    //probably should just use a hashmap, but its amateur hour tonight
                    myInfo.addLatitude(Double.parseDouble(tempCords[0]) * latSign);
                    myInfo.addLongitude(Double.parseDouble(tempCords[1]) * longSign);
                    latSign = 1.0;
                    longSign = 1.0;
                }

                Log.i("TESTLOOP", "" + i);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


