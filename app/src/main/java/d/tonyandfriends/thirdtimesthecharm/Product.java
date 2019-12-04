package d.tonyandfriends.thirdtimesthecharm;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
public class Product {
    @SerializedName("productid")
    private Integer productID;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("productname")
    private String name;

    @SerializedName("imageurl")
    private String pictureURL;


    private String dateRecentlyScanned;
    private int scanCount;

    public Product() {
        this.productID = null;
        this.barcode = null;
    }

    public Product(String barcode, String name, String pictureURL, String dateRecentlyScanned,
                   int scanCount) {
        this.barcode = barcode;
        this.name = name;
        this.pictureURL = pictureURL;
        this.dateRecentlyScanned = dateRecentlyScanned;
        this.scanCount = scanCount;
    }

    // Database uses these functions when storing and receiving products.


    public Integer getProductID() {
        return productID;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getDateRecentlyScanned() {
        return dateRecentlyScanned;
    }

    public int getScanCount() {
        return scanCount;
    }



    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public void setDateRecentlyScanned(String dateRecentlyScanned) {
        this.dateRecentlyScanned = dateRecentlyScanned;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public boolean exists()
    {
        if(barcode == null)
            return false;
        else
            return true;
    }


}
