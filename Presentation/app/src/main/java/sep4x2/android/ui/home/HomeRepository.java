package sep4x2.android.ui.home;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutionException;
import sep4x2.android.local_database.Entity.SensorData;
import sep4x2.android.local_database.LocalDatabase;
import sep4x2.android.local_database.SensorDao;
import sep4x2.android.network.SensorDataClient;

public class HomeRepository {

    private SensorDao sensorDao;
    private SensorDataClient sensorDataClient;
    private static HomeRepository instance;

    private HomeRepository(Application application)
    {
        LocalDatabase database = LocalDatabase.getInstance(application);
        sensorDao = database.sensorDao();
        sensorDataClient= SensorDataClient.getInstance(application);
    }

    public static synchronized HomeRepository getInstance(Application application) {
        if (instance == null) {
            instance = new HomeRepository(application);
        }
        return instance;
    }

    public LiveData<SensorData> getData() {
        try {
            return new getDataAsync(sensorDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateData() {
         sensorDataClient.updateSensorData();
    }

    private static class getDataAsync extends AsyncTask<Void,Void,LiveData<SensorData>>
    {
        private SensorDao sensorDao;

        private getDataAsync(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected LiveData<SensorData> doInBackground(Void... voids) {
            return sensorDao.getLastestSensorData();
        }
    }

}
