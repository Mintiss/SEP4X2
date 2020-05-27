package sep4x2.android.ui.temperature;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sep4x2.android.ui.network.SensorAPI;
import sep4x2.android.ui.network.SensorData;
import sep4x2.android.ui.network.SensorResponse;
import sep4x2.android.ui.network.ServiceGenerator;

public class SensorDataRepository {

    private static SensorDataRepository instance;
    private MutableLiveData<TemperatureData> temperature;
    private MutableLiveData<SensorData> sensorData;

    private SensorDataRepository()
    {
        sensorData = new MutableLiveData<>();
        temperature = new MutableLiveData<>();
    }

    public static synchronized SensorDataRepository getInstance() {
        if (instance == null) {
            instance = new SensorDataRepository();
        }
        return instance;
    }

    public MutableLiveData<SensorData> getSensorData() {
        return sensorData;
    }

    public void updateSensorData() {
        SensorAPI sensorAPI = ServiceGenerator.getSensorAPI();
        Call<SensorResponse> call = sensorAPI.getSensorData();
        call.enqueue(new Callback<SensorResponse>() {
            @Override
            public void onResponse(Call<SensorResponse> call, Response<SensorResponse> response) {
                if (response.code() == 200) {
                    sensorData.setValue(response.body().getSensorData());
                    Log.i("asshole",""+response.body().getSensorData().getMetricsId() );
                }
            }

            @Override
            public void onFailure(Call<SensorResponse> call, Throwable t) {
                Log.i("ass", "fuck");
            }
        });
    }






}
