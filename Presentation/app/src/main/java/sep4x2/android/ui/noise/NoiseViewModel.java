package sep4x2.android.ui.noise;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import sep4x2.android.SharedSensors.Noise;
import sep4x2.android.SharedSensors.WeeklyConverter;

public class NoiseViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    private NoiseRepository repository;

    public NoiseViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the noise fragment");

       repository = NoiseRepository.getInstance(application);
    }

    public List<Noise> getNoiseData() {
        return repository.getNoiseData();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public WeeklyConverter getWeeklyData(int weekNo){
        return repository.getSpecificWeekSensorsTemperature(weekNo);
    }
}