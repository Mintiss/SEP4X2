package sep4x2.android.ui.co2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Co2ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Co2ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the CO2 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}