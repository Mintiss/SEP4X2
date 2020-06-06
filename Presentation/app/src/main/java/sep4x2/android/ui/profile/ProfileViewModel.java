package sep4x2.android.ui.profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    private ProfileRepository repository;

    public ProfileViewModel(Application application) {
        super(application);
        repository=ProfileRepository.getInstance(application);

        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public void deleteUser(String userId){
        repository.deleteUserInAPI(userId);
    }


    public LiveData<String> getText() {
        return mText;
    }
}