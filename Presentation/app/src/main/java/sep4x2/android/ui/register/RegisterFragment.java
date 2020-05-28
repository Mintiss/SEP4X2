package sep4x2.android.ui.register;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

import sep4x2.android.R;
import sep4x2.android.ui.login.LoginFragmentDirections;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Button registerButton, backButton;
    private EditText email, firstName, lastName, productId, password, confirmPassword;
    private RegisterViewModel mViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String userID;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        registerButton = view.findViewById(R.id.register_registerButton);
        backButton = view.findViewById(R.id.register_backButton);

        email = view.findViewById(R.id.register_email);
        firstName = view.findViewById(R.id.register_first_name);
        lastName = view.findViewById(R.id.register_last_name);
        productId = view.findViewById(R.id.register_product_id);
        password = view.findViewById(R.id.register_password);
        confirmPassword = view.findViewById(R.id.register_passwordConfirm);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.register_registerButton:

                final String email = this.email.getText().toString();
                final String firstName = this.firstName.getText().toString();
                final String lastName = this.lastName.getText().toString();
                final String productId = this.productId.getText().toString();
                String password = this.password.getText().toString();
                String confirmPassword = this.confirmPassword.getText().toString();

                if(!TextUtils.equals(password, confirmPassword)) {
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(productId) || TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Can not have empty fields", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(password.length() < 8){
                    Toast.makeText(getContext(), "Can not have empty fields", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getContext(), "User Created!", Toast.LENGTH_SHORT).show();
                                userID = firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = firestore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                user.put("firstName", firstName);
                                user.put("lastName", lastName);
                                user.put("productId", productId);
                                documentReference.set(user);
                                NavDirections action;
                                action = RegisterFragmentDirections.actionNavRegisterToNavHome();
                                Navigation.findNavController(v).navigate(action);
                            }
                            else{
                                Toast.makeText(getContext(), "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                }
            case R.id.register_backButton:
                NavDirections action;
                action = RegisterFragmentDirections.actionNavRegisterToNavLogin();
                Navigation.findNavController(v).navigate(action);
                break;
            default:
                break;
        }
    }
}
