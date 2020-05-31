package sep4x2.android.network;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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

        updateThisWeekSensors();
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
                    new InsertSensorDataAsync(sensorDao).execute(sensorData);

                    Log.i("SENSOR DATA",""+response.body().getMetricsID());
                }
            }

            @Override
            public void onFailure(Call<SensorResponse> call, Throwable t) {
                Log.i("API GET SENSOR", "Call failed");
        }
        });
    }

    public void updateThisWeekSensors() {
        LocalDate now = new LocalDate();
        LocalDate monday = (now.withDayOfWeek(DateTimeConstants.MONDAY));
        LocalDate sunday = (now.withDayOfWeek(DateTimeConstants.SUNDAY));

        DateTime mondayAtMidnight = monday.toDateTimeAtStartOfDay();
        DateTime sundayAtEndOfDay = sunday.toDateTime(new LocalTime(23, 59, 59));

        String pattern = ("yyyy-MM-dd HH:mm:ss");

        String startDate = mondayAtMidnight.toString(pattern);
        String endDate = sundayAtEndOfDay.toString(pattern);

        SensorAPI sensorAPI = ServiceGenerator.getSensorAPI();
        Call<WeeklyResponse> call = sensorAPI.getWeeklySensorData("2020-04-27 00:00:00", "2020-05-03 23:59:59");

        call.enqueue(new Callback<WeeklyResponse>() {
            @Override
            public void onResponse(Call<WeeklyResponse> call, Response<WeeklyResponse> response) {
                if (response.code() == 200) {
                    weeklyStatisticsAllData = (new WeeklyStatisticsAllData(response.body()));
                    new InsertWeeklySensorDataAsync(sensorDao).execute(weeklyStatisticsAllData);
                    Log.i("WEEKLY SENSOR", "WORKS");
                }
                if (response.code() == 400){
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
}
