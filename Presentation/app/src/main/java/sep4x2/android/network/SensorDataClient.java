package sep4x2.android.network;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sep4x2.android.local_database.Entity.SensorData;
import sep4x2.android.local_database.Entity.WeeklyStatisticsAllData;
import sep4x2.android.local_database.LocalDatabase;
import sep4x2.android.local_database.SensorDao;
import sep4x2.android.network.GETResponses.SensorResponse;
import sep4x2.android.network.GETResponses.WeeklyResponse;


public class SensorDataClient extends Application{

    private static SensorDataClient instance;
    private SensorData sensorData;
    private WeeklyStatisticsAllData weeklyStatisticsAllData;

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

    //new NukeData(sensorDao).execute();

    public void updateSensorData(String productID) {
        SensorAPI sensorAPI = ServiceGenerator.getSensorAPI();
        Call<SensorResponse> call = sensorAPI.getSensorData(productID);

        call.enqueue(new Callback<SensorResponse>()
        {
            @Override
            public void onResponse(Call<SensorResponse> call, Response<SensorResponse> response) {
                if (response.code() == 200) {
                    sensorData=(new SensorData(response.body()));
                    new InsertSensorDataAsync(sensorDao).execute(sensorData);
                    Log.i("SENSOR DATA",""+response.body().getMetricsID());
                    Log.i("SENSOR DATA",""+response.body().toString());
                }
                else
                    Log.i("SENSOR DATA", call.request().url().toString());
            }

            @Override
            public void onFailure(Call<SensorResponse> call, Throwable t) {
                Log.i("API GET SENSOR", "Call failed");
        }
        });
    }

    public List<SensorData> getDataForPoke(){
        try {
            return new getDataForPoke(sensorDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateThisWeekSensors(int weekNo,String productID) {
        DateTime weekStartDate=new DateTime().withWeekOfWeekyear(weekNo).withTime(new LocalTime(0,0,0)).minusDays(2);
        DateTime weekEndDate=new DateTime().withWeekOfWeekyear(weekNo+1).withTime(new LocalTime(23,59,59)).minusDays(3);

        String pattern=("yyyy-MM-dd HH:mm:ss");

        Log.i("WEEKLY SENSOR", "CALLED IN HOME");

        String startDate = weekStartDate.toString(pattern);
        String endDate = weekEndDate.toString(pattern);

        SensorAPI sensorAPI = ServiceGenerator.getSensorAPI();
        Call<WeeklyResponse> call = sensorAPI.getWeeklySensorData(startDate,endDate,productID);

        call.enqueue(new Callback<WeeklyResponse>() {
            @Override
            public void onResponse(Call<WeeklyResponse> call, Response<WeeklyResponse> response) {
                if (response.code() == 200) {
                    weeklyStatisticsAllData = (new WeeklyStatisticsAllData(response.body()));
                    new InsertWeeklySensorDataAsync(sensorDao).execute(weeklyStatisticsAllData);
                    Log.i("WEEKLY SENSOR", "ON INIT"+weeklyStatisticsAllData.getWeekNo());
                }
                else {
                    Log.i("WEEKLY SENSOR", call.request().url().toString());
                }
            }

            @Override
            public void onFailure(Call<WeeklyResponse> call, Throwable t) {
                Log.i("WEEKLY SENSOR", call.request().url().toString());
            }

        });
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

    private static class NukeDataWeekly extends AsyncTask<Void,Void,Void>
    {
        private SensorDao sensorDao;

        private NukeDataWeekly(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sensorDao.nukeWeeklyTable();
            return null;
        }
    }

    private static class NukeDataNotToday extends AsyncTask<Void,Void,Void> {
        private SensorDao sensorDao;

        private NukeDataNotToday(SensorDao sensorDao) {
            this.sensorDao = sensorDao;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            sensorDao.deleteAllSensorDataNotToday();
            return null;
        }
    }

    private static class getDataForPoke extends AsyncTask<Void,Void, List<SensorData>>
    {
        private SensorDao sensorDao;


        private getDataForPoke(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected List<SensorData> doInBackground(Void... voids) {
            return sensorDao.getAllSensorData();
        }
    }
}
