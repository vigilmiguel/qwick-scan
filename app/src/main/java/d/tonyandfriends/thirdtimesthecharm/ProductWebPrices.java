package d.tonyandfriends.thirdtimesthecharm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductWebPrices {

    @SerializedName("productname")
    private String productName;

    @SerializedName("storename")
    private String storeName;

    @SerializedName("price")
    private Double price;

    @SerializedName("imageurl")
    private String imageURL;

    @SerializedName("address")
    private String urlAddress;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("numberOfStores")
    private Integer numStores;

    public ProductWebPrices()
    {
        this.productName = null;
    }

    public ProductWebPrices(String barcode, Integer numStores)
    {
        this.productName = null;
        this.barcode = barcode;
        this.numStores = numStores;
    }



    public String getProductName() {
        return productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public String getBarcode() {
        return barcode;
    }

    public Integer getNumStores() {
        return numStores;
    }

    public boolean exists()
    {
        if(productName == null)
            return false;
        else
            return true;
    }
}
