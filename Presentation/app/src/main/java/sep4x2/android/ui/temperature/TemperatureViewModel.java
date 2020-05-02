package sep4x2.android.ui.temperature;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TemperatureViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TemperatureViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the temperature fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}