package sep4x2.android.ui.humidity;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import sep4x2.android.SharedSensors.Humidity;

public class HumidityViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private HumidityRepository repository;

    public HumidityViewModel(Application application) {
        super(application);
        repository=HumidityRepository.getInstance(application);


        mText = new MutableLiveData<>();
        mText.setValue("This is the humidity fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public List<Humidity> getHumidityData(){
        return repository.getHumidityData();
    }
}