package sep4x2.android.ui.noise;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import sep4x2.android.local_database.Entity.SensorData;

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