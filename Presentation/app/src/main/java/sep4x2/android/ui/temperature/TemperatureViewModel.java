package sep4x2.android.ui.temperature;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TemperatureViewModel extends ViewModel {

    sep4x2.android.ui.temperature.SensorDataRepository repository;

    private MutableLiveData<String> mText;

    public TemperatureViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the temperature fragment");
        repository = sep4x2.android.ui.temperature.SensorDataRepository.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<TemperatureData> repositoryGetTemperature()
    {
        MutableLiveData<TemperatureData> temperatureData;
        temperatureData = new MutableLiveData<>();
        temperatureData.setValue(new TemperatureData(repository.getSensorData().getValue().getTemperature(), repository.getSensorData().getValue().getUpdateTime()));
        return temperatureData;
    }

    public void TemperatureUpdate()
    {
        repository.updateTemperature();
    }


}