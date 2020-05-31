package sep4x2.android.ui.home;

import android.app.Application;
import android.app.NotificationChannel;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import sep4x2.android.R;
import sep4x2.android.local_database.Entity.SensorData;
import sep4x2.android.network.SensorDataClient;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private HomeRepository homeRepository;
    private MutableLiveData<SensorData> data;
    private double previousTemperature, previousHumidity, previousNoise, previousCo2;

    SensorDataClient sensorDataClient;

    public HomeViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the Home fragment");

        sensorDataClient= SensorDataClient.getInstance(application);
        homeRepository = HomeRepository.getInstance(application);

        //query the db for the second to last data
        previousTemperature = 0;
        previousHumidity = 0;
        previousCo2 = 0;
        previousNoise = 0;

    }

    public LiveData<SensorData> getData() {
        return homeRepository.getData();
    }

    public void checkForNotifications(SensorData  data){
        Log.i("Notif", "temp:" + data.getTemperature() + "old temp" + previousTemperature);
        if((data.getTemperature() > previousTemperature + 10) || (data.getTemperature() < previousTemperature - 10)){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication().getApplicationContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_brightness_7_black_24dp)
                    .setContentTitle("Health monitor")
                    .setContentText("Major change in temperature.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication().getApplicationContext());
            notificationManager.notify(100, builder.build());

        }
        if((data.getHumidity() > previousHumidity + 10) || (data.getHumidity() < previousHumidity - 10)){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication().getApplicationContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_invert_colors_black_24dp)
                    .setContentTitle("Health monitor")
                    .setContentText("Major change in humidity.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication().getApplicationContext());
            notificationManager.notify(101, builder.build());
        }
        if((data.getCo2() > 1000)){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication().getApplicationContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_local_florist_black_24dp)
                    .setContentTitle("Health monitor")
                    .setContentText("Co2 levels can cause health issues")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication().getApplicationContext());
            notificationManager.notify(103, builder.build());
        }
        if((data.getNoise() > previousNoise + 10) || (data.getNoise() < previousNoise - 10)) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication().getApplicationContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                    .setContentTitle("Health monitor")
                    .setContentText("Environment more noisy than recorded before.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication().getApplicationContext());
            notificationManager.notify(104, builder.build());
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}