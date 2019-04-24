package d.tonyandfriends.thirdtimesthecharm;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderProto{
    public SpiderData myInfo = new SpiderData();


    public void BestBuy()
    {
        String product = myInfo.getProductName();
        product = product.replace("'","");
        product = product.replace("(","");
        product = product.replace(")","");
        product = product.replaceAll("\\s","+");
        //https://www.bestbuy.com/site/searchpage.jsp?st=google+home+slate&_dyncharset=UTF-8&id=pcat17071&type=page&sc=Global&cp=1&nrp=&sp=&qp=&list=n&af=true&iht=y&usc=All+Categories&ks=960&keys=keys
        String Search = "https://www.bestbuy.com/site/searchpage.jsp?st=" + product +"&_dyncharset=UTF-8&id=pcat17071&type=page&sc=Global&cp=1&nrp=&sp=&qp=&list=n&af=true&iht=y&usc=All+Categories&ks=960&keys=keys";
        Log.d("myserach", Search);

        try {
            Document dic = Jsoup.connect(Search).userAgent("Opera").timeout(0).get();
            Elements element = dic.select("div.image-column");
            Log.d("myTest", element.toString());


        } catch (IOException e) {
            Log.d("myBestBuyError",e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    //99% working function, Parse Frys and grab the URL/Price
    //This one was kind of easy, because they had a search by UPC # option
    public void Frys()
    {
        String barcode = myInfo.getBarcodeNumber();
        String productURL = "https://www.frys.com/";
        String productPrice = "";
        String search ="https://www.frys.com/search?search_type=regular&sqxts=1&cat=&query_string=" + barcode + "&nearbyStoreName=false";

        try {
            Document dic = Jsoup.connect(search).userAgent("Opera").get(); //How we connect to our site
            Elements element = dic.select("p#prodDescp"); // Here I had html that looked like this:  <p class="font_reg productDescp" id="prodDescp" >
            // My url info was here, so using the select query p#prodDescp, it is saying select elements p with id = prodDescp
            // https://jsoup.org/cookbook/extracting-data/selector-syntax This is where I've been getting most of my syntax from

            if(element.size() == 0) return; // Make sure to check if we have a match or not.

            //Ez way to grab the data we want, if its always in the same spot in the string, just start there and grab it until you get to a terminating char of your choosing
            int i = 86;
            while(element.toString().charAt(i) != '"') productURL += element.toString().charAt(i++);

            element = dic.select("li#did_price1valuediv");
            i = 305;
            while(element.toString().charAt(i) != '<') productPrice += element.toString().charAt(i++);


            //Here we will save Store Name: Frys Electronics, our URL we found, and the price we found
            // I'm not sure how we should hold the data? maybe a map of some sort. Or we cna continue just using 3 array lists.


        } catch (IOException e) {
            Log.d("myBestBuyError",e.getLocalizedMessage());
            e.printStackTrace();
        }



    }
    public void NewEgg()
    {
        String barcode = myInfo.getBarcodeNumber();
        String productURL = "";
        String productPrice = "";
        String search ="https://www.newegg.com/Product/ProductList.aspx?Submit=ENE&DEPA=0&Order=BESTMATCH&Description=" + barcode + "++&N=-1&isNodeId=1";
        Log.d("mySearch",search);

        try {
            Document dic = Jsoup.connect(search).userAgent("Opera").get(); //How we connect to our site
            Elements element = dic.select(".item-button-area");

            if(element.size() == 0) return; // Make sure to check if we have a match or not.

            int i = 145;
            while(element.toString().charAt(i) != '\'') productURL += element.toString().charAt(i++);

            //Finding price on the first page is really difficult, so I have to go to the next page to get it, makes this function a bit slower than the others
            dic = Jsoup.connect(productURL).userAgent("Opera").get();

            element = dic.select("meta[itemprop = price]");
            i = 32;
            while(element.toString().charAt(i) != '"') productPrice += element.toString().charAt(i++);


        } catch (IOException e) {
            Log.d("myBestBuyError",e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void MicroCenter()
    {
        String barcode = myInfo.getBarcodeNumber();
        String productURL = "https://www.microcenter.com";
        String productPrice = "";
        String search ="https://www.microcenter.com/search/search_results.aspx?Ntt=" + barcode + "++";

        try {
            Document dic = Jsoup.connect(search).userAgent("Opera").get(); //How we connect to our site
            Elements element = dic.select(".quick");

            if(element.size() == 0) return; // Make sure to check if we have a match or not.

            int i = 84;
            while(element.toString().charAt(i) != '"') productURL += element.toString().charAt(i++);

            element = dic.select("span[itemprop = price]");
            i = 51;
            while(element.toString().charAt(i) != '<') productPrice += element.toString().charAt(i++);


            //Here we will save Store Name: Frys Electronics, our URL we found, and the price we found
            // I'm not sure how we should hold the data? maybe a map of some sort. Or we cna continue just using 3 array lists.

        } catch (IOException e) {
            Log.d("myBestBuyError",e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    //General Querys
    public void Walmart()
    {
        //Can be used for all querys
        String barcode = myInfo.getBarcodeNumber();
        String productURL = "https://walmart.com";
        String productPrice = "";
        String search ="https://www.walmart.com/search/?cat_id=0&query=" + barcode + "++";
        Log.d("mySearch",search);

        try {
            Document dic = Jsoup.connect(search).userAgent("Opera").get(); //How we connect to our site

            //URL
            Elements element = dic.select(".product-title-link");
            if(element.size() == 0) return; // Make sure to check if we have a match or not.
            int i = 60;
            while(element.toString().charAt(i) != '"') productURL += element.toString().charAt(i++);


            //Price
            element = dic.select(".price-group");
            if(element.size() == 0) return;
            i = 51;
            while(element.toString().charAt(i) != '"') productPrice += element.toString().charAt(i++);

            try
            {
                URL oracle = new URL(search); //this is the link that we pass in for the product
                URLConnection hi = oracle.openConnection();	 //this gives us a connection im pretty sure
                BufferedReader in = new BufferedReader(new InputStreamReader(hi.getInputStream())); // gets some input
                String inputLine; // our empty string
                String patternString = "<div class=\"collapsable-content-container\"><div>"; //this is the start of the review section so that's what we look for
                String start = "<div class=\"collapsable-content-container\"><div>";
                int whereweare = 0;

                String end = "</div>";
                //String kk;
                //Pattern ending = Pattern.compile(end);
                //Pattern begin = Pattern.compile(start);
                Pattern pattern = Pattern.compile(patternString);  // this basically creates the string we are looking for, needs to be pattern object
                //int count = 0;
                while((inputLine = in.readLine()) != null)		//goes through the entire html page line by line
                {
                    Matcher matcher = pattern.matcher(inputLine);		//creates a matcher that looks for if the line contains our string
                    if(matcher.find())									// if it finds it then we move in
                    {
					/*Matcher first = begin.matcher(inputLine);
					Matcher last = ending.matcher(inputLine);
					if(first.find() && last.find())
						{
							System.out.println(first.start()  + " " + last.end());
						}*/
                        int startingval = inputLine.indexOf("<div class=\"collapsable-content-container\"><div>") + 48;  // we get the index of the substring where it starts and then add 48 to get to the end of this string
                        int endingval = inputLine.indexOf("</div><div style=\"position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;z-index:-1;visibility:hidden;pointer-events:none\">");	// this is basically the end of the reviews on the page
                        if(startingval <0 || endingval <0)
                        {
                            break;
                        }

                        String newstring = inputLine.substring(startingval,endingval);	// scrape the substring which is our review
                        newstring = newstring.replace("&#34;","\"");	// changes the string into quotation marks cuz html weird
                        newstring = newstring.replace("&#39;","'");		// changes the string into appostraphe cuz html weird
                        System.out.println(newstring);				//prints our string out now
                        //System.out.println(inputLine.substring(startingval,endingval));	//how it used to look
                        // this was just to test
					/*System.out.println(inputLine);
					count++;
					System.out.println(count + " " + matcher.start() + " " + matcher.end());
					System.out.println(inputLine.substring(matcher.start(),matcher.end()));
					*/
                    }
				/*if(inputLine.contains("<script>"))
				{
					System.out.println(inputLine);
				}*/
                    //System.out.println(inputLine);
                }

                in.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Not working, trying some stuff here
    public void Target()
    {
        //Can be used for all querys
        String barcode = myInfo.getBarcodeNumber();
        String productURL = "https://walmart.com";
        String productPrice = "";
        String search ="https://www.target.com/p/google-home-smart-speaker-with-google-assistant/-/A-51513049";
        Log.d("mySearch",search);

        try {
            Document dic = Jsoup.connect(search).userAgent("Opera").get(); //How we connect to our site
            Elements element = dic.select("*");
            Log.d("mysize", Integer.toString(element.size()));
            //Log.d("myshit",element.toString());
            for(int i =0; i < element.size(); i++)
            {
                if(element.get(i).toString().contains("129.99"))
                {
                    Log.d("myHit", Integer.toString(i));
                    Log.d("my " + i, element.get(i).toString());
                }
                //Log.d("my " + i, element.get(i).toString());
            }

            //URL
            /*
            Elements element = dic.select(".product-title-link");
            if(element.size() == 0) return; // Make sure to check if we have a match or not.
            int i = 60;
            while(element.toString().charAt(i) != '"') productURL += element.toString().charAt(i++);
            */


            //Price
            /*
            element = dic.select(".price-group");
            if(element.size() == 0) return;
            i = 51;
            while(element.toString().charAt(i) != '"') productPrice += element.toString().charAt(i++);
            */


        } catch (IOException e) {
            Log.d("myBestBuyError",e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void Amazon()
    {
        String prodname = myInfo.getProductName();  //gets the product name
        //Log.d("names",prodname);
        String[] splitname = prodname.split(" ");   // gets rid of the whitespace in the product name
        String search ="https://www.amazon.com/s?k=";

        for(int i = 0; i < splitname.length; i++)                   // adds the %20 that is needed for the search
        {
            if(i == splitname.length - 1)
            {
                search = search +splitname[i];
                break;
            }
            search = search + splitname[i] + "+";
        }
        search += "&ref=nb_sb_noss_2";
        Log.d("searching", search);
        String productPrice = "";
        String productUrl= "https://www.amazon.com";
        try {
            Document dic = Jsoup.connect(search).userAgent("Opera").get();
            Elements element = dic.select("span.a-offscreen");          // need this to make the rest still work
            if(element.size() == 0)
            {
                return;
            }
            Element price = dic.select("span.a-offscreen").first();  // takes the first span class
            productPrice = price.text();        // takes the text of the span class
            Log.d("Price",productPrice);
            if(element.size() == 0 )
            {
                return;
            }

            Elements each= dic.select(".a-link-normal");
            if(each.size() ==0 )
            {
                return;
            }
            Element url = dic.select("h5").first();         //gets the first h5 where the url is located
            String url2 = url.toString();                           //convert to a string
            url2 = url2.replace("&amp;","&");       // replaces the words with an & symbol
            for(int last = url2.lastIndexOf("href=") +6; last<1000;last++)      //gets the url from the class
            {
                if(url2.charAt(last) == '"')        //once it finds teh end o f the url then it breaks
                {
                    break;
                }
                productUrl += url2.charAt(last);
            }
            String xx = productUrl;



            try {                       // its not getting to the bottom of the doc
                Log.d("xx",xx);
                Document amazon = Jsoup.connect(xx).userAgent("Opera").get();
                Elements reviews = amazon.select("h3");
                if(reviews.size() == 0)
                {
                    Log.d("fail","try again");
                    return;
                }
                /*Element price = dic.select("span.a-offscreen").first();  // takes the first span class
                productPrice = price.text();        // takes the text of the span class
                Log.d("Price",productPrice);
                if(element.size() == 0 )
                {
                    return;
                }

                Elements each= dic.select(".a-link-normal");
                if(each.size() ==0 )
                {
                    return;
                }
                Element url = dic.select("h5").first();         //gets the first h5 where the url is located
                String url2 = url.toString();                           //convert to a string
                url2 = url2.replace("&amp;","&");       // replaces the words with an & symbol
                for(int last = url2.lastIndexOf("href=") +6; last<1000;last++)      //gets the url from the class
                {
                    if(url2.charAt(last) == '"')        //once it finds teh end o f the url then it breaks
                    {
                        break;
                    }
                    productUrl += url2.charAt(last);
                }
                */



                Log.d("reviews",reviews.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }





            Log.d("url",productUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Food Query
    public void FreshDirect()
    {

    }

    //Clothes Section (Maybe we can replace this witha  different category?
    public void Macys()
    {
        //Log.d("heyeheyehiahsdfkjahsfkahskj", "what");
        String prodname = myInfo.getProductName();
        prodname = prodname.replaceAll(" ","-");
        String search ="https://www.macys.com/shop/featured/";
        search += prodname;
        try {
            Document dic = Jsoup.connect(search).userAgent("Opera").get(); //How we connect to our site

            //URL
            Elements element = dic.select(".productThumbnailImage");
            if(element.size() == 0)
            {
                Log.d("hey","doesnt work");
                return; // Make sure to check if we have a match or not.
            }
            Log.d("productLink", element.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Kohls()
    {

    }

    public void Nordstrom()
    {
        String prodname = myInfo.getProductName();  //gets the product name
        //Log.d("names",prodname);
        String[] splitname = prodname.split(" ");   // gets rid of the whitespace in the product name
        String search ="https://shop.nordstrom.com/sr?origin=keywordsearch&keyword=";

        for(int i = 0; i < splitname.length; i++)                   // adds the %20 that is needed for the search
        {
            if(i == splitname.length - 1)
            {
                search = search +splitname[i];
                break;
            }
            search = search + splitname[i] + "%20";
        }
        Log.d("searching", search);
        String productPrice = "";
        String productUrl= "";
        try {
            Document dic = Jsoup.connect(search).userAgent("Opera").get();
            Elements element = dic.select("h3");
            Log.d("myTest", element.toString());
            if(element.size() == 0 )
            {
                Log.d("myTest", "wrong");
                return;
            }

            for(int x = 1; x < element.size(); x++)
            {
                productPrice += element.toString().charAt(x);
            }

            Log.d("myTest", element.toString());

            Elements each= dic.select("k9jiU");
            if(each.size() == 0)
            {
                return;
            }

            for(int x = 0; x < each.size(); x++)
            {
                productUrl += each.toString().charAt(x);
            }
            Log.d("testingurl", each.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void JcPennys()
    {
        String prodname = myInfo.getProductName();  //gets the product name
        String[] splitname = prodname.split(" ");   // gets rid of the whitespace in the product name
        String search ="https://www.jcpenney.com/s?searchTerm=";
        for(int i = 0; i < splitname.length; i++)                   // adds the %20 that is needed for the search
        {
            if(i == splitname.length - 1)
            {
                search = search +splitname[i];
                break;
            }
            search = search + splitname[i] + "+";
        }
        String productPrice = "";
        String productUrl= "";

        try {
            Document dic = Jsoup.connect(search).userAgent("Opera").get();
            Elements element = dic.select(" ");
            Log.d("myTest", element.toString());
            if(element.size() == 0 )
            {
                Log.d("myTest", "wrong");
                return;
            }

            for(int x = 1; x < element.size(); x++)
            {
                productPrice += element.toString().charAt(x);
            }

            Log.d("myTest", element.toString());

            Elements each= dic.select("k9jiU");
            if(each.size() == 0)
            {
                return;
            }

            for(int x = 0; x < each.size(); x++)
            {
                productUrl += each.toString().charAt(x);
            }
            Log.d("testingurl", each.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void Ebay()
    {
        String prodname = myInfo.getProductName();
        prodname = prodname.replaceAll(" ","+");

        String search ="https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2334524.m570.l1313.TR12.TRC2.A0.H0.X";
        String secondpart = ".TRS0&_nkw=";

        search = search + prodname + secondpart + prodname;
        // not done you. you still need to get the new url and then you need to pass it into this next method
        try {
            ArrayList<String> hello = new ArrayList<String>();
            URL oracle = new URL("https://www.ebay.com/itm/Genuine-Original-OEM-Apple-iPhone-X-8-7-6S-plus-5-Lightning-USB-Cable-Charger-1M/191482438168?epid=134466773&hash=item2c953e2218:g:MR8AAOSwMmBVonp6:sc:USPSFirstClass!91214!US!-1"); //this is the link that we pass in for the product
            URLConnection hi = oracle.openConnection();	 //this gives us a connection im pretty sure
            BufferedReader in = new BufferedReader(new InputStreamReader(hi.getInputStream())); // gets some input
            String inputLine; // our empty string
            String patternString = "<p itemprop=\"reviewBody\" class=\"review-item-content wrap-spaces\">"; //this is the start of the review section so that's what we look for

            Pattern pattern = Pattern.compile(patternString);  // this basically creates the string we are looking for, needs to be pattern object

            while((inputLine = in.readLine()) != null)		//goes through the entire html page line by line
            {

                Matcher matcher = pattern.matcher(inputLine);		//creates a matcher that looks for if the line contains our string
                if(matcher.find())									// if it finds it then we move in
                {

                    inputLine = inputLine.substring(69,inputLine.length()-4);
                    hello.add(inputLine);
                }

            }
            //myInfo.setEbayreviews(hello);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
