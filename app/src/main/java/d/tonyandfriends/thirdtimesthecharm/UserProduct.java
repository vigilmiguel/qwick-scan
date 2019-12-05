package d.tonyandfriends.thirdtimesthecharm;

import com.google.gson.annotations.SerializedName;

public class UserProduct {

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("firebaseuid")
    private String firebaseUID;

    public UserProduct(String barcode, String firebaseUID) {
        this.barcode = barcode;
        this.firebaseUID = firebaseUID;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getFirebaseUID() {
        return firebaseUID;
    }
}
