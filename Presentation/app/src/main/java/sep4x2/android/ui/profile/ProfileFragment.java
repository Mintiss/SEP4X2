package sep4x2.android.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import sep4x2.android.R;
import sep4x2.android.ui.change_password.ChangePasswordFragmentDirections;
import sep4x2.android.ui.login.LoginFragmentDirections;

public class ProfileFragment extends Fragment implements View.OnClickListener {


    private View view;
    private ProfileViewModel profileViewModel;
    private Button changePassButton, deleteAccountButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        changePassButton = view.findViewById(R.id.profile_change_password_button);
        deleteAccountButton = view.findViewById(R.id.profile_delete_account_button);


        changePassButton.setOnClickListener(this);
        deleteAccountButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        NavDirections action;
        switch (v.getId()) {
            case R.id.profile_change_password_button:
                action = ProfileFragmentDirections.actionNavProfileToNavChangePassword();
                Navigation.findNavController(v).navigate(action);
                break;
            case R.id.profile_delete_account_button:
                //do stufff
                break;
            default:
                break;
        }
    }
}