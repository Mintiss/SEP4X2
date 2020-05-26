package sep4x2.android.ui.temperature;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import sep4x2.android.ui.network.TemperatureRepository;

public class TemperatureViewModel extends ViewModel {

    TemperatureRepository repository;

    private MutableLiveData<String> mText;

    public TemperatureViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the temperature fragment");
        repository = TemperatureRepository.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<TemperatureData> repositoryGetTemperature()
    {
        return repository.getTemperature();
    }

    public void TemperatureUpdate()
    {
        repository.updateTemperature();
    }


}