package d.tonyandfriends.thirdtimesthecharm;

import com.google.gson.annotations.SerializedName;

public class UserScan {

    @SerializedName("userid")
    private int userID;

    @SerializedName("productid")
    private int productID;

    @SerializedName("datetimescanned")
    private String dateTimeScanned;

    public UserScan(int userID, int productID) {
        this.userID = userID;
        this.productID = productID;
    }

    public int getUserID() {
        return userID;
    }

    public int getProductID() {
        return productID;
    }

    public String getDateTimeScanned() {
        return dateTimeScanned;
    }
}
