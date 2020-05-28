package sep4x2.android.ui.temperature;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

<<<<<<< HEAD
import java.util.List;

import sep4x2.android.ui.local_database.Entity.SensorData;
=======
import sep4x2.android.ui.network.TemperatureRepository;
>>>>>>> parent of f38ccbe... broken temperature

public class TemperatureViewModel extends ViewModel {

    TemperatureRepository repository;

    private MutableLiveData<String> mText;

    public TemperatureViewModel(Application application) {
        mText = new MutableLiveData<>();
        mText.setValue("This is the temperature fragment");
<<<<<<< HEAD
        repository = TemperatureRepository.getInstance(application);
=======
        repository = TemperatureRepository.getInstance();
>>>>>>> parent of f38ccbe... broken temperature
    }


<<<<<<< HEAD
    public LiveData<List<SensorData>> getTemperatureData() {
        return repository.getTemperatureData();
=======
    public LiveData<TemperatureData> repositoryGetTemperature()
    {
        return repository.getTemperature();
>>>>>>> parent of f38ccbe... broken temperature
    }


    public LiveData<String> getText() {
        return mText;
    }

   

}