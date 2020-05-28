package sep4x2.android.ui.change_password;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import sep4x2.android.R;
import sep4x2.android.ui.register.RegisterFragmentDirections;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ChangePasswordViewModel mViewModel;
    private Button backButton, changePasswordButton;
    private EditText oldPassword, password, confirmPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private String email;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_change_password, container, false);

        backButton = view.findViewById(R.id.change_password_back_button);
        changePasswordButton = view.findViewById(R.id.change_password_button);
        oldPassword = view.findViewById(R.id.change_password_old_password);
        password = view.findViewById(R.id.change_password_new_password);
        confirmPassword = view.findViewById(R.id.change_password_confirm_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firestore.collection("users").document(firebaseAuth.getUid());
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                email = documentSnapshot.getString("email");
            }
        });

        user = firebaseAuth.getCurrentUser();

        changePasswordButton.setOnClickListener(this);
        backButton.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        NavDirections action;
        switch (v.getId()){
            case R.id.change_password_button:
                if(TextUtils.isEmpty(oldPassword.getText().toString()) || TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(confirmPassword.getText().toString())){
                    Toast.makeText(getContext(), "Cannot have empty Fields", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if (password.getText().toString().length() < 8){
                    Toast.makeText(getContext(), "Password mut be at least 8 characters", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(!TextUtils.equals(password.getText().toString(), confirmPassword.getText().toString())){
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    AuthCredential authCredential = EmailAuthProvider.getCredential(email, oldPassword.getText().toString());
                    user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Password Reset Successful", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(getContext(), "Error:" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else
                                Toast.makeText(getContext(), "Error:" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.change_password_back_button:
                action = ChangePasswordFragmentDirections.actionNavChangePasswordToNavProfile();
                Navigation.findNavController(v).navigate(action);

        }

    }
}
