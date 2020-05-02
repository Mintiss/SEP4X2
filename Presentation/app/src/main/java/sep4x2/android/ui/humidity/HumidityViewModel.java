package sep4x2.android.ui.humidity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HumidityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HumidityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the humidity fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}