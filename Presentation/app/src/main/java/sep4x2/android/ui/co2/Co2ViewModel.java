package sep4x2.android.ui.co2;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import sep4x2.android.SharedSensors.CO2;

public class Co2ViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    private Co2Repository repository;

    public Co2ViewModel(Application application) {
        super(application);
        repository=Co2Repository.getInstance(application);

        mText = new MutableLiveData<>();
        mText.setValue("This is the CO2 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public List<CO2> getCo2Data(){
        return repository.getCO2Data();
    }
}