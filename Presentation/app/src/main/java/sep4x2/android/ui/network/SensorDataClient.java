package sep4x2.android.ui.network;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sep4x2.android.ui.local_database.Entity.SensorData;
import sep4x2.android.ui.local_database.LocalDatabase;
import sep4x2.android.ui.local_database.SensorDao;


public class SensorDataClient extends Application{

    private static SensorDataClient instance;
    private SensorData sensorData;

    //Database
    private SensorDao sensorDao;

    public SensorDataClient(Application application)
    {
        LocalDatabase database = LocalDatabase.getInstance(application);
        sensorDao = database.sensorDao();
    }

    public static synchronized SensorDataClient getInstance(Application application) {
        if (instance == null) {
            instance = new SensorDataClient(application);
        }
        return instance;
    }

    public void updateSensorData() {
        SensorAPI sensorAPI = ServiceGenerator.getSensorAPI();
        Call<SensorResponse> call = sensorAPI.getSensorData();

        call.enqueue(new Callback<SensorResponse>()
        {
            @Override
            public void onResponse(Call<SensorResponse> call, Response<SensorResponse> response) {
                if (response.code() == 200) {
                    sensorData=(new SensorData(response.body()));
                    new NukeData(sensorDao).execute();
                    new InsertSensorDataAsync(sensorDao).execute(sensorData);
                    Log.i("work",""+response.body().getMetricsID());
                }
            }

            @Override
            public void onFailure(Call<SensorResponse> call, Throwable t) {
                Log.i("API GET SENSOR", "Call failed");
            }
        });
    }

    private static class InsertSensorDataAsync extends AsyncTask<SensorData,Void,Void>
    {
        private SensorDao sensorDao;

        private InsertSensorDataAsync(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected Void doInBackground(SensorData... sensorDatas) {
            try {
                sensorDao.insert(sensorDatas[0]);
            } catch (SQLiteConstraintException e)
            {
                return null;
            }
            return null;
        }
    }

    private static class NukeData extends AsyncTask<Void,Void,Void>
    {
        private SensorDao sensorDao;

        private NukeData(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sensorDao.nukeTable();
            return null;
        }
    }
}
