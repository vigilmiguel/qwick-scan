package d.tonyandfriends.thirdtimesthecharm;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("firebaseuid")
    private String firebaseUID;


    @SerializedName("username")
    private String userName;

    public User()
    {
        firebaseUID = null;
        userName = null;
    }

    public User(String firebaseUID, String userName)
    {
        this.firebaseUID = firebaseUID;
        this.userName = userName;
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
