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


    public LiveData<String> getText() {
        return mText;
    }
}