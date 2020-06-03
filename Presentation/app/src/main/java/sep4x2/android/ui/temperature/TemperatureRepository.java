package sep4x2.android.ui.temperature;

import android.app.Application;
import android.os.AsyncTask;
import java.util.List;
import java.util.concurrent.ExecutionException;
import sep4x2.android.SharedSensors.Temperature;
import sep4x2.android.SharedSensors.WeeklyConverter;
import sep4x2.android.local_database.LocalDatabase;
import sep4x2.android.local_database.SensorDao;


public class TemperatureRepository {

    private SensorDao sensorDao;

    private static TemperatureRepository instance;

    private TemperatureRepository(Application application)
    {
       LocalDatabase database = LocalDatabase.getInstance(application);
       sensorDao = database.sensorDao();
    }


    public static synchronized TemperatureRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TemperatureRepository(application);
        }
        return instance;
    }

    public List<Temperature> getTemperatureData() {
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

    private static class getDataAsync extends AsyncTask<Void,Void,List<Temperature>>
    {
        private SensorDao sensorDao;

        private getDataAsync(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected List<Temperature> doInBackground(Void... voids) {
            return sensorDao.getAllTemperatureData();
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
