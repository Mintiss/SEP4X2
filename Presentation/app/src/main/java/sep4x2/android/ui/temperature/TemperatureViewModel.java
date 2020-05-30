package sep4x2.android.ui.temperature;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import sep4x2.android.ui.local_database.Entity.SensorData;

public class TemperatureViewModel extends ViewModel {

    TemperatureRepository repository;

    private MutableLiveData<String> mText;

    public TemperatureViewModel(Application application) {
        mText = new MutableLiveData<>();
        mText.setValue("This is the temperature fragment");
       // repository = TemperatureRepository.getInstance(application);
    }


   /* public LiveData<List<SensorData>> getTemperatureData() {
        return repository.getTemperatureData();
    } */


    public LiveData<String> getText() {
        return mText;
    }



}