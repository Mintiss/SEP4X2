package sep4x2.android.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

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

import javax.annotation.Nullable;

import sep4x2.android.R;
import sep4x2.android.ui.change_password.ChangePasswordFragmentDirections;
import sep4x2.android.ui.login.LoginFragmentDirections;

public class ProfileFragment extends Fragment implements View.OnClickListener {


    private View view;
    private ProfileViewModel profileViewModel;
    private Button changePassButton, deleteAccountButton, logOffButton, confirmDeleteButton;
    private EditText confirmPassword;
    private TextView firstName, lastName, email, productId;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        changePassButton = view.findViewById(R.id.profile_change_password_button);
        deleteAccountButton = view.findViewById(R.id.profile_delete_account_button);
        logOffButton = view.findViewById(R.id.profile_log_out);
        confirmDeleteButton = view.findViewById(R.id.profile_confirm_delete);
        confirmPassword = view.findViewById(R.id.profile_confirm_password);

        confirmDeleteButton.setVisibility(View.INVISIBLE);
        confirmPassword.setVisibility(View.INVISIBLE);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();


        DocumentReference documentReference = firestore.collection("users").document(firebaseAuth.getUid());
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                firstName.setText(documentSnapshot.getString("firstName"));
                lastName.setText(documentSnapshot.getString("lastName"));
                email.setText(documentSnapshot.getString("email"));
                productId.setText(documentSnapshot.getString("productId"));
            }
        });

        firstName = view.findViewById(R.id.profile_first_name);
        lastName = view.findViewById(R.id.profile_last_name);
        email = view.findViewById(R.id.profile_email);
        productId = view.findViewById(R.id.profile_product_id);

        logOffButton.setOnClickListener(this);
        changePassButton.setOnClickListener(this);
        deleteAccountButton.setOnClickListener(this);
        confirmDeleteButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.profile_change_password_button:
                NavDirections action;
                action = ProfileFragmentDirections.actionNavProfileToNavChangePassword();
                Navigation.findNavController(v).navigate(action);
                break;
            case R.id.profile_delete_account_button:
                confirmPassword.setVisibility(View.VISIBLE);
                confirmDeleteButton.setVisibility(View.VISIBLE);
                break;
            case R.id.profile_confirm_delete:
                try {
                    AuthCredential authCredential = EmailAuthProvider.getCredential(email.getText().toString(), confirmPassword.getText().toString());
                    user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                firestore.collection("users").document(firebaseAuth.getUid()).delete();
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        NavDirections action;
                                        FirebaseAuth.getInstance().signOut();
                                        action = ProfileFragmentDirections.actionNavProfileToNavLogin();
                                        Navigation.findNavController(v).navigate(action);
                                        Toast.makeText(getContext(),"Account Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                                Toast.makeText(getContext(),task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                }
                catch (IllegalArgumentException e){
                    Toast.makeText(getContext(), "Please enter your password.", Toast.LENGTH_SHORT).show();
                    break;
                }

            case R.id.profile_log_out:
                FirebaseAuth.getInstance().signOut();
                action = ProfileFragmentDirections.actionNavProfileToNavLogin();
                Navigation.findNavController(v).navigate(action);
                Toast.makeText(getContext(),"You have been signed off", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}