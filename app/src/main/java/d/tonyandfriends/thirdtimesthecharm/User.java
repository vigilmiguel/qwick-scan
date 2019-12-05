package d.tonyandfriends.thirdtimesthecharm;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("userid")
    private Integer userID;

    @SerializedName("firebaseuid")
    private String firebaseUID;

    @SerializedName("username")
    private String userName;

    public User()
    {
        userID = null;
        firebaseUID = null;
        userName = null;
    }

    public User(String firebaseUID, String userName)
    {
        this.firebaseUID = firebaseUID;
        this.userName = userName;
    }

    public Integer getUserID() {
        return userID;
    }

    public String getFirebaseUID() {
        return firebaseUID;
    }

    public String getUserName() {
        return userName;
    }

    public boolean exists()
    {
        if(firebaseUID == null || userName == null)
            return false;
        else
            return true;
    }
}
