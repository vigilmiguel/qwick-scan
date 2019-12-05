package d.tonyandfriends.thirdtimesthecharm;

import com.google.gson.annotations.SerializedName;

public class ProductEnqueue {

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("latitude")
    private Double latitude;

    public ProductEnqueue(String barcode, Double longitude, Double latitude) {
        this.barcode = barcode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getBarcode() {
        return barcode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
}
