package sep4x2.android.ui.noise;

import android.app.Application;
import android.os.AsyncTask;
import java.util.List;
import java.util.concurrent.ExecutionException;
import sep4x2.android.ui.local_database.Entity.SensorData;
import sep4x2.android.ui.local_database.LocalDatabase;
import sep4x2.android.ui.local_database.SensorDao;

public class NoiseRepository {

    private SensorDao sensorDao;

    private static NoiseRepository instance;

    private NoiseRepository(Application application)
    {
        LocalDatabase database = LocalDatabase.getInstance(application);
        sensorDao = database.sensorDao();
    }


    public static synchronized NoiseRepository getInstance(Application application) {
        if (instance == null) {
            instance = new NoiseRepository(application);
        }
        return instance;
    }

    public List<SensorData> getNoiseData() {
        try {
            return new getDataAsync(sensorDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getDataAsync extends AsyncTask<Void,Void,List<SensorData>>
    {
        private SensorDao sensorDao;

        private getDataAsync(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected List<SensorData> doInBackground(Void... voids) {
            return sensorDao.getAllSensorData();
        }
    }

}
