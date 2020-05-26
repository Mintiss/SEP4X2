package sep4x2.android.ui.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sep4x2.android.ui.temperature.TemperatureModel;

public class TemperatureRepository {

    private static TemperatureRepository instance;
    private MutableLiveData<TemperatureModel> temperature;

   private TemperatureRepository() {temperature = new MutableLiveData<>();}

    public static synchronized TemperatureRepository getInstance() {
        if (instance == null) {
            instance = new TemperatureRepository();
        }
        return instance;
    }

    public LiveData<TemperatureModel> getTemperature() {return temperature;}

    public void updateTemperature()
    {
        SensorAPI sensorAPI =ServiceGenerator.getAllMetrics();
        Call<MetricsResponse> call = sensorAPI.getAllMetrics();
        call.enqueue(new Callback<MetricsResponse>() {
            @Override
            public void onResponse(Call<MetricsResponse> call, Response<MetricsResponse> response) {
                if(response.code() == 200)
                {
                   temperature.setValue(response.body().getTemperature());
                }
            }

            @Override
            public void onFailure(Call<MetricsResponse> call, Throwable t) {
                Log.i("Retrofit","Something went wrong");
            }
        });
    }

}
