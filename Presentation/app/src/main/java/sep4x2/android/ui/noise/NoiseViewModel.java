package sep4x2.android.ui.noise;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoiseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NoiseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the noise fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}