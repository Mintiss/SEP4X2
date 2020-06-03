package sep4x2.android.ui.humidity;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sep4x2.android.SharedSensors.Humidity;
import sep4x2.android.SharedSensors.WeeklyConverter;
import sep4x2.android.local_database.Entity.WeeklyStatisticsAllData;
import sep4x2.android.local_database.LocalDatabase;
import sep4x2.android.local_database.SensorDao;
import sep4x2.android.network.GETResponses.WeeklyResponse;
import sep4x2.android.network.SensorAPI;
import sep4x2.android.network.ServiceGenerator;

public class HumidityRepository {

    private SensorDao sensorDao;

    private static HumidityRepository instance;

    private WeeklyStatisticsAllData weeklyStatisticsAllData;

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

    public WeeklyConverter getSpecificWeekSensors(int weekNo){

        try {
            return new getWeeklyDataAsync(sensorDao).execute(weekNo).get();
        } catch (Exception e){

            DateTime weekStartDate=new DateTime().withWeekOfWeekyear(weekNo);
            DateTime weekEndDate=new DateTime().withWeekOfWeekyear(weekNo+1);

            weekStartDate.withTime(0,0,0,0);
            weekEndDate.withTime(23,59,59,59);

            String pattern=("yyyy-MM-dd HH:mm:ss");

            String startDate = weekStartDate.toString(pattern);
            String endDate = weekEndDate.toString(pattern);

            SensorAPI sensorAPI = ServiceGenerator.getSensorAPI();
            Call<WeeklyResponse> call = sensorAPI.getWeeklySensorData(startDate,endDate);

            call.enqueue(new Callback<WeeklyResponse>()
            {
                @Override
                public void onResponse(Call<WeeklyResponse> call, Response<WeeklyResponse> response) {
                    if (response.code() == 200) {
                        weeklyStatisticsAllData=(new WeeklyStatisticsAllData(response.body()));
                        new InsertWeeklySensorDataAsync(sensorDao).execute(weeklyStatisticsAllData);

                        Log.i("THIS WEEK",""+response.body().getRoomID());
                    }
                }

                @Override
                public void onFailure(Call<WeeklyResponse> call, Throwable t) {
                    Log.i("API GET WEEKLY SENSOR", "Call failed");
                }
            });
            try{
                return new getWeeklyDataAsync(sensorDao).execute(weekNo).get();
            } catch (Exception e1){
                Log.i("API GET WEEKLY SENSOR", "NO WEEKS FOUND FOR USER");
            }
            return null;
        }
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

    private static class InsertWeeklySensorDataAsync extends AsyncTask<WeeklyStatisticsAllData,Void,Void>
    {
        private SensorDao sensorDao;

        private InsertWeeklySensorDataAsync(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected Void doInBackground(WeeklyStatisticsAllData... weeklyStatisticsAllData) {
            try {
                sensorDao.insertIntoWeekly(weeklyStatisticsAllData[0]);
            } catch (SQLiteConstraintException e)
            {
                return null;
            }
            return null;
        }
    }


}
