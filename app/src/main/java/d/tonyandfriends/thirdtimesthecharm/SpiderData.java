package d.tonyandfriends.thirdtimesthecharm;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SpiderData implements Serializable {
    //Standard Data package class, will contain all information retrieved from a spider crawl
    String productName;
    String imgURL;
    String barcodeNumber;
    ArrayList<Double> latitude;
    ArrayList<Double> longitude;
    ArrayList<String> localStores;
    ArrayList<String> prices;
    ArrayList<String> priceURL;
    ArrayList<String> stores;
    ArrayList<String> reviewSitesURl;
    ArrayList<String> reviewSitesName;
    ArrayList<String> starRating;

    SpiderData()
    {
        imgURL = "";
        productName = "";
        barcodeNumber = "";
        prices = new ArrayList<String>();
        priceURL = new ArrayList<String>();
        stores = new ArrayList<String>();
        localStores = new ArrayList<String>();
        latitude = new ArrayList<Double>();
        longitude = new ArrayList<Double>();
        reviewSitesName = new ArrayList<String>();
        reviewSitesURl = new ArrayList<String>();
        starRating = new ArrayList<String>();


    }
    public void setProductName(String name){productName = name;}
    public void setBarcodeNumber(String name) {barcodeNumber = name;}
    public void setImgURL(String url){imgURL = url;}
    public void addPrice(String price){prices.add(price);}
    public void addURL(String url){priceURL.add(url);}
    public void addName(String name){stores.add(name);}
    public void addLocalStore(String name) {localStores.add(name);}
    public void addLatitude(Double x) {latitude.add(x);}
    public void addReviewURL(String url) {reviewSitesURl.add(url);}
    public void addReviewName(String name) {reviewSitesName.add(name);}
    public void addStarRating(String x) {starRating.add(x);}
    public void addLongitude(Double x) {longitude.add(x);}
    public ArrayList<String> getPrices(){ return prices;}
    public ArrayList<String> getURLS(){ return priceURL;}
    public ArrayList<String> getStores(){ return stores;}
    public String getProductName(){return productName;}
    public String getBarcodeNumber(){return barcodeNumber;}
    public String getImgURL(){return imgURL;}
    public ArrayList<String> getLocalStores() {return localStores;}
    public ArrayList<Double> getLatitude() {return latitude;}
    public ArrayList<Double> getLongitude() {return longitude;}
    public ArrayList<String> getReviewSitesURl() {return reviewSitesURl;}
    public ArrayList<String> getReviewSitesName() {return reviewSitesName;}
    public ArrayList<String> getStarRating() {return starRating;}
}
