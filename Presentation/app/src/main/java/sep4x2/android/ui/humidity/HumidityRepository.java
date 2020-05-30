package sep4x2.android.ui.humidity;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import sep4x2.android.SharedSensors.Humidity;
import sep4x2.android.local_database.LocalDatabase;
import sep4x2.android.local_database.SensorDao;

public class HumidityRepository {

    private SensorDao sensorDao;

    private static HumidityRepository instance;

    private HumidityRepository(Application application)
    {
        LocalDatabase database = LocalDatabase.getInstance(application);
        sensorDao = database.sensorDao();
    }


    public static synchronized HumidityRepository getInstance(Application application) {
        if (instance == null) {
            instance = new HumidityRepository(application);
        }
        return instance;
    }

    public List<Humidity> getHumidityData() {
        try {
            return new getDataAsync(sensorDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getDataAsync extends AsyncTask<Void,Void, List<Humidity>>
    {
        private SensorDao sensorDao;

        private getDataAsync(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected List<Humidity> doInBackground(Void... voids) {
            return sensorDao.getAllHumidityData();
        }
    }

}
