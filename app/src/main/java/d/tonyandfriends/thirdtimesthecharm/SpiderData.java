package d.tonyandfriends.thirdtimesthecharm;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SpiderData implements Serializable {
    //Standard Data package class, will contain all information retrieved from a spider crawl
    String productName;
    String imgURL;
    ArrayList<Double> latitude;
    ArrayList<Double> longitude;
    ArrayList<String> localStores;
    ArrayList<String> prices;
    ArrayList<String> priceURL;
    ArrayList<String> stores;

    SpiderData()
    {
        imgURL = "";
        productName = "";
        prices = new ArrayList<String>();
        priceURL = new ArrayList<String>();
        stores = new ArrayList<String>();
        localStores = new ArrayList<String>();
        latitude = new ArrayList<Double>();
        longitude = new ArrayList<Double>();


    }
    public void setProductName(String name){productName = name;}
    public void setImgURL(String url){imgURL = url;}
    public void addPrice(String price){prices.add(price);}
    public void addURL(String url){priceURL.add(url);}
    public void addName(String name){stores.add(name);}
    public void addLocalStore(String name) {localStores.add(name);}
    public void addLatitude(Double x) {latitude.add(x);}
    public void addLongitude(Double x) {longitude.add(x);}
    public ArrayList<String> getPrices(){ return prices;}
    public ArrayList<String> getURLS(){ return priceURL;}
    public ArrayList<String> getStores(){ return stores;}
    public String getProductName(){return productName;}
    public String getImgURL(){return imgURL;}
    public ArrayList<String> getLocalStores() {return localStores;}
    public ArrayList<Double> getLatitude() {return latitude;}
    public ArrayList<Double> getLongitude() {return longitude;}
}
