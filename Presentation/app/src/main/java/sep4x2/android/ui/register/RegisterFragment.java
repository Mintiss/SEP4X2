package sep4x2.android.ui.register;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sep4x2.android.R;
import sep4x2.android.ui.login.LoginFragmentDirections;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Button registerButton, backButton;
    private RegisterViewModel mViewModel;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        registerButton = view.findViewById(R.id.register_registerButton);
        backButton = view.findViewById(R.id.register_backButton);
        registerButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        NavDirections action;
        switch (v.getId()) {
            case R.id.register_registerButton:
                action = RegisterFragmentDirections.actionNavRegisterToNavHome();
                Navigation.findNavController(v).navigate(action);
                break;
            case R.id.register_backButton:
                action = RegisterFragmentDirections.actionNavRegisterToNavLogin();
                Navigation.findNavController(v).navigate(action);
                break;
            default:
                break;
        }
    }
}
