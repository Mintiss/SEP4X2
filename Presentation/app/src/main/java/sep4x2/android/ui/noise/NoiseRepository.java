package sep4x2.android.ui.noise;

import android.app.Application;
import android.os.AsyncTask;
import java.util.List;
import java.util.concurrent.ExecutionException;
import sep4x2.android.SharedSensors.Noise;
import sep4x2.android.SharedSensors.WeeklyConverter;
import sep4x2.android.local_database.LocalDatabase;
import sep4x2.android.local_database.SensorDao;

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

    public List<Noise> getNoiseData() {
        try {
            return new getDataAsync(sensorDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public WeeklyConverter getSpecificWeekSensors(int weekNo){
        try {
            return new getWeeklyDataAsync(sensorDao).execute(weekNo).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getDataAsync extends AsyncTask<Void,Void,List<Noise>>
    {
        private SensorDao sensorDao;

        private getDataAsync(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected List<Noise> doInBackground(Void... voids) {
            return sensorDao.getAllNoiseData();
        }
    }



    private static class getWeeklyDataAsync extends AsyncTask<Integer,Void,WeeklyConverter>
    {
        private SensorDao sensorDao;

        private getWeeklyDataAsync(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected WeeklyConverter doInBackground(Integer... integers) {
            return sensorDao.getWeeklyData(integers[0]);
        }
    }
}
