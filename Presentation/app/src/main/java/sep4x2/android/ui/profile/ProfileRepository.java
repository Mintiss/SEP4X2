package sep4x2.android.ui.profile;

import android.app.Application;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sep4x2.android.network.DELETE.UserDelete;
import sep4x2.android.network.ServiceGenerator;
import sep4x2.android.network.UserAPI;
import sep4x2.android.network.POST.UserPost;

public class ProfileRepository {

    private static ProfileRepository instance;

    public ProfileRepository(Application application) {
    }

    public static synchronized ProfileRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ProfileRepository(application);
        }
        return instance;
    }

    public void deleteUserInAPI(String userId) {

        UserAPI userAPI = ServiceGenerator.getUserAPI();
        Call<UserDelete> call = userAPI.deleteAccount(userId);

        call.enqueue(new Callback<UserDelete>()
        {
            @Override
            public void onResponse(Call<UserDelete> call, Response<UserDelete> response) {
                if (response.code() == 500) {
                    Log.i("DELETE USER",""+response.toString());
                }
                else {
                    Log.i("FAIL DELETE USER",""+call.request().url().toString());
                }
            }

            @Override
            public void onFailure(Call<UserDelete> call, Throwable t) {
                Log.i("API DELETE USER", "Call failed");
            }
        });
    }
}
