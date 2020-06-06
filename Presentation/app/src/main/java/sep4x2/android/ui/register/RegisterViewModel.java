package sep4x2.android.ui.register;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class RegisterViewModel extends AndroidViewModel {

    private RegisterRepository repository;

    public RegisterViewModel(Application application) {
        super(application);
        repository = RegisterRepository.getInstance(application);
    }

    public void postUserToDatabase(String id, String productId, String token){
        repository.postUserToAPI(id, productId, token);
    }
}
