package sep4x2.android.ui.temperature;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import sep4x2.android.ui.local_database.Entity.SensorData;
import sep4x2.android.ui.local_database.Entity.TemperatureData;

public class TemperatureViewModel extends AndroidViewModel {

    private TemperatureRepository repository;

    public TemperatureViewModel(Application application) {
        super(application);
        repository=TemperatureRepository.getInstance(application);
    }

    public List<SensorData> getTemperatureData() {
        return repository.getTemperatureData();
    }

}