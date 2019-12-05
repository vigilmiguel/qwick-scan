package d.tonyandfriends.thirdtimesthecharm;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DatabaseAPI {

    //@GET("users/readSingle.php")
    //Call<User> getUser(@Query("firebaseuid") String firebaseUID);

    // For some reason this needs to be a post request. Otherwise, retrofit starts bitching.
    @POST("users/readSingle.php")
    Call<User> getUser(@Body User user);

    @POST("users/create.php")
    Call<Void> createUser(@Body User user);

    @POST("products/readBarcode.php")
    Call<Product> getProduct(@Body Product product);

    @POST("queries/readLowestOnlinePrice.php")
    Call<List<ProductWebPrices>> getLowestWebPrices(@Body ProductWebPrices productWebPrices);

    @POST("queue/enqueue.php")
    Call<Void> enqueueDB(@Body ProductEnqueue product);

    @POST("scans/create.php")
    Call<Void> createScan(@Body UserScan userScan);

    @POST("queries/readUserHistory.php")
    Call<List<Product>> getUserHistory(@Body User user);

    @POST("queries/deleteUserProductFromHistory.php")
    Call<Void> deleteUserProduct(@Body UserProduct userProduct);
}
