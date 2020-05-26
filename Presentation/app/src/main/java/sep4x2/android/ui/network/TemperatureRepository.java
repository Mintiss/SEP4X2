package sep4x2.android.ui.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sep4x2.android.ui.login.LoginModel;
import sep4x2.android.ui.temperature.TemperatureModel;

public class TemperatureRepository {

    private static TemperatureRepository instance;
    private MutableLiveData<TemperatureModel> temperature;
    private MutableLiveData<LoginModel> login;

   private TemperatureRepository()
   {
       temperature = new MutableLiveData<>();
       login = new MutableLiveData<>();
   }

    public static synchronized TemperatureRepository getInstance() {
        if (instance == null) {
            instance = new TemperatureRepository();
        }
        return instance;
    }

    public LiveData<TemperatureModel> getTemperature() {return temperature;}
    public LiveData<LoginModel> getLogin() {return login;}


    //Getting the temperature
    public void updateTemperature()
    {
        SensorAPI sensorAPI =ServiceGenerator.getSensorAPI();
        Call<SensorResponse> call = sensorAPI.getAllMetrics();
        call.enqueue(new Callback<SensorResponse>() {
            @Override
            public void onResponse(Call<SensorResponse> call, Response<SensorResponse> response) {
                if(response.code() == 200)
                {
                   temperature.setValue(response.body().getTemperature());
                }
            }

            @Override
            public void onFailure(Call<SensorResponse> call, Throwable t) {
                Log.i("Retrofit","Something went wrong");
            }
        });
    }

    //getting the login information
    public void login(String email, String password)
    {
        UserAPI userAPI = ServiceGenerator.getUserAPI();
        Call<LoginResponse> call = userAPI.getLoginInformation(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.code() ==200)
                {
                    login.setValue(response.body().getLoginInformation());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i("Retrofit", "Something went wrong :(");
            }
        });
    }





}
