package sep4x2.android.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import sep4x2.android.network.SensorDataClient;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    SensorDataClient sensorDataClient;

    public HomeViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the Home fragment");

        sensorDataClient= SensorDataClient.getInstance(application);
    }

    public LiveData<String> getText() {
        return mText;
    }
}