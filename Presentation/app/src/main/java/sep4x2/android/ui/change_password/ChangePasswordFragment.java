package sep4x2.android.ui.change_password;

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
import sep4x2.android.ui.register.RegisterFragmentDirections;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ChangePasswordViewModel mViewModel;
    private Button backButton;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_change_password, container, false);

        backButton = view.findViewById(R.id.change_password_back_button);

        backButton.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        NavDirections action;
        if (v.getId() == R.id.change_password_back_button) {
            action = ChangePasswordFragmentDirections.actionNavChangePasswordToNavProfile();
            Navigation.findNavController(v).navigate(action);
        }
    }
}
