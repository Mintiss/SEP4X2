package sep4x2.android.ui.register;

import android.app.Application;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sep4x2.android.network.ServiceGenerator;
import sep4x2.android.network.UserAPI;
import sep4x2.android.network.POST.UserPost;


public class RegisterRepository {

    private static RegisterRepository instance;

    public RegisterRepository(Application application) {
    }

    public static synchronized RegisterRepository getInstance(Application application) {
        if (instance == null) {
            instance = new RegisterRepository(application);
        }
        return instance;
    }

    public void postUserToAPI(String userid, String productid, String token) {

        UserAPI userAPI = ServiceGenerator.getUserAPI();
        Call<UserPost> call = userAPI.createAccount(userid, productid, token);

        call.enqueue(new Callback<UserPost>()
        {
            @Override
            public void onResponse(Call<UserPost> call, Response<UserPost> response) {
                if (response.code() == 500) {
                    Log.i("CREATE USER",""+response.toString());
                }
                else {
                    Log.i("FAIL CREATE USER",""+call.request().url().toString());
                }
            }

            @Override
            public void onFailure(Call<UserPost> call, Throwable t) {
                Log.i("API POST USER", "Call failed");
            }
        });
    }


}
