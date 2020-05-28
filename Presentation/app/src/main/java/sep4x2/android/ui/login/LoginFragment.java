package sep4x2.android.ui.login;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
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

import sep4x2.android.R;

public class LoginFragment extends Fragment implements View.OnClickListener {


    private View view;
    private Button loginButton, registerButton;
    private LoginViewModel mViewModel;
    private FirebaseAuth firebaseAuth;
    private EditText email, password;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = view.findViewById(R.id.login_loginButton);
        registerButton = view.findViewById(R.id.login_registerButton);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);

        firebaseAuth = FirebaseAuth.getInstance();

        
        return view;
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.login_loginButton:
                String email = this.email.getText().toString();
                String password = this.password.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(getContext(), "Can not have empty fields", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                NavDirections action;
                                action = LoginFragmentDirections.actionNavLoginToNavHome();
                                Navigation.findNavController(v).navigate(action);
                            }
                            else
                                Toast.makeText(getContext(), "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                }
            case R.id.login_registerButton:
                NavDirections action;
                action = LoginFragmentDirections.actionNavLoginToNavRegister();
                Navigation.findNavController(v).navigate(action);
                break;
            default:
                break;
        }
    }
}
