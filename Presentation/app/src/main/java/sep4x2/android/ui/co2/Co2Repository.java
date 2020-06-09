package sep4x2.android.ui.co2;

import android.app.Application;
import android.os.AsyncTask;
import java.util.List;
import java.util.concurrent.ExecutionException;
import sep4x2.android.SharedSensors.CO2;
import sep4x2.android.SharedSensors.WeeklyConverter;
import sep4x2.android.local_database.LocalDatabase;
import sep4x2.android.local_database.SensorDao;

public class Co2Repository {

    private SensorDao sensorDao;

    private static Co2Repository instance;

    private Co2Repository(Application application)
    {
        LocalDatabase database = LocalDatabase.getInstance(application);
        sensorDao = database.sensorDao();
    }

    public static synchronized Co2Repository getInstance(Application application) {
        if (instance == null) {
            instance = new Co2Repository(application);
        }
        return instance;
    }

    public List<CO2> getCO2Data() {
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


    private static class getDataAsync extends AsyncTask<Void,Void, List<CO2>>
    {
        private SensorDao sensorDao;

        private getDataAsync(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected List<CO2> doInBackground(Void... voids) {
            return sensorDao.getAllCO2Data();
        }
    }


    private static class getWeeklyDataAsync extends AsyncTask<Integer,Void, WeeklyConverter>
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
