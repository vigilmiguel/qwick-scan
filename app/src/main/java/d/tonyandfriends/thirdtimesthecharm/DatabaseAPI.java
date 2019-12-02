package d.tonyandfriends.thirdtimesthecharm;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DatabaseAPI {

    @GET("users/readSingle.php")
    Call<User> getUser(@Query("firebaseuid") String firebaseUID);

    @POST("users/create.php")
    Call<Void> createUser(@Body User user);
}
