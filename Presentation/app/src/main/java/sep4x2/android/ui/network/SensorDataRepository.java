package sep4x2.android.ui.network;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sep4x2.android.ui.local_database.Entity.SensorData;
import sep4x2.android.ui.local_database.LocalDatabase;
import sep4x2.android.ui.local_database.SensorDao;


public class SensorDataRepository {

    private static SensorDataRepository instance;

    private MutableLiveData<SensorData> sensorData;

    //Database
    private SensorDao sensorDao;



    private SensorDataRepository(Application application)
    {
       // LocalDatabase database = LocalDatabase.getInstance(application);
        //sensorDao = database.sensorDao();

        sensorData = new MutableLiveData<>();

    }

    public static synchronized SensorDataRepository getInstance(Application application) {
        if (instance == null) {
            instance = new SensorDataRepository(application);
        }
        return instance;
    }


    public MutableLiveData<SensorData> getSensorData() {
        return sensorData;
    }



   /* public void updateSensorData() {
        SensorAPI sensorAPI = ServiceGenerator.getSensorAPI();
        Call<SensorData> call = sensorAPI.getSensorData();
        call.enqueue(new Callback<SensorData>() {
            @Override
            public void onResponse(Call<SensorData> call, Response<SensorData> response) {
                if (response.code() == 200) {
                    sensorData.setValue(response.body().getSensorData());
                    sensorDao.insert(sensorData);
                    Log.i("asshole",""+response.body().getSensorData().getMetricsId() );
                }
            }

            @Override
            public void onFailure(Call<SensorData> call, Throwable t) {
                Log.i("ass", "fuck");
            }
        });
    } */






}
