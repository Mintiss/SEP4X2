package sep4x2.android.ui.noise;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import sep4x2.android.ui.local_database.Entity.SensorData;
import sep4x2.android.ui.local_database.SensorDao;

public class NoiseViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    private NoiseRepository repository;

    public NoiseViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the noise fragment");

       repository = NoiseRepository.getInstance(application);
    }

    public List<SensorData> getNoiseData() {
        return repository.getNoiseData();
    }

    public LiveData<String> getText() {
        return mText;
    }


}