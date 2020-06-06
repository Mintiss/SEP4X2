package sep4x2.android.ui.register;

import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {

    private RegisterRepository repository;


    public RegisterViewModel() {
    }

    public void postUserToDatabase(String id, String productId, String token){
        repository.postUserToDatabase(id, productId, token);

    }
}
