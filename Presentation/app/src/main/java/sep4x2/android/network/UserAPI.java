package sep4x2.android.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAPI {

    @GET("api/user/login?{email}&{password}")
    Call<LoginResponse> getLoginInformation(@Path("email") String email, @Path("password") String password);

    @POST("api/user/createAccount")
    Call<UserPost> createAccount(@Query(value = "userid",encoded = true) String userid,
                                 @Query(value = "productid",encoded = true)String productid,
                                 @Query(value = "token",encoded = true)String token);
}
