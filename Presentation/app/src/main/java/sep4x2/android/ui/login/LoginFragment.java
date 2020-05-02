package sep4x2.android.ui.login;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sep4x2.android.R;

public class LoginFragment extends Fragment implements View.OnClickListener {


    private View view;
    private Button loginButton, registerButton;
    private LoginViewModel mViewModel;

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
        return view;
    }


    @Override
    public void onClick(View v) {
        NavDirections action;
        switch (v.getId()) {
            case R.id.login_loginButton:
                action = LoginFragmentDirections.actionNavLoginToNavHome();
                Navigation.findNavController(v).navigate(action);
                break;
            case R.id.login_registerButton:
                action = LoginFragmentDirections.actionNavLoginToNavRegister();
                Navigation.findNavController(v).navigate(action);
                break;
            default:
                break;
        }
    }
}
