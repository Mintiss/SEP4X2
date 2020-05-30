package sep4x2.android.ui.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserAPI {

    @GET("api/user/login?{email}&{password}")
    Call<LoginResponse> getLoginInformation(@Path("email") String email, @Path("password") String password);
}
