package sep4x2.android.ui.temperature;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import sep4x2.android.SharedSensors.Temperature;
import sep4x2.android.SharedSensors.WeeklyConverter;

public class TemperatureViewModel extends AndroidViewModel {

    private TemperatureRepository repository;

    public TemperatureViewModel(Application application) {
        super(application);
        repository = TemperatureRepository.getInstance(application);
    }

    public List<Temperature> getTemperatureData() {
        return repository.getTemperatureData();
    }

    public WeeklyConverter getWeeklyData(int weekNo)
    {
        return repository.getSpecificWeekSensors(weekNo);
    }
}