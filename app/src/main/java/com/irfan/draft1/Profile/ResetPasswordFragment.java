package com.irfan.draft1.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.irfan.draft1.MainClass.SignupFragment;
import com.irfan.draft1.R;

/**
 * Created by irfan on 13/01/2018.
 */

public class ResetPasswordFragment extends Fragment
{
    private static final String TAG = "hello";

    private EditText oldPassword;
    private EditText newPassword;
    private ProgressBar progressBar;
    private TextView errorAlert;
    private TextInputLayout oldPasswordWrapper,newPasswordWrapper;

    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_resetpassword_fragment,container,false);

        ImageView back_button = view.findViewById(R.id.back_button);
        back_button.setOnClickListener(view1 -> {
            getActivity().onBackPressed();
        });



        oldPassword = view.findViewById(R.id.profile_old_password);
        newPassword = view.findViewById(R.id.profile_new_password);
        Button submit = view.findViewById(R.id.reset_password);
        errorAlert = view.findViewById(R.id.error_alert);
        oldPasswordWrapper = view.findViewById(R.id.old_password_wrapper);
        newPasswordWrapper = view.findViewById(R.id.new_password_wrapper);
        progressBar = view.findViewById(R.id.progress_bar);

        submit.setOnClickListener(view1 -> {
            String oldPass = oldPassword.getText().toString();
            String newPass = newPassword.getText().toString();

            oldPasswordWrapper.setError(null);
            newPasswordWrapper.setError(null);

            Toast.makeText(getActivity(),readStoredPassword(getActivity(),SignupFragment.USER_PASSWORD,"default"),Toast.LENGTH_LONG).show();
            if (validateFields(oldPass,newPass))
            {
                resetPassword(newPass);

            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


    }

    private boolean validateFields(String oldPassword, String newPassword) {
        boolean valid = true;

        if (TextUtils.isEmpty(oldPassword)) {
            oldPasswordWrapper.setError("Required");
            valid = false;
        }
        else if (oldPassword.length()<6)
        {
            oldPasswordWrapper.setError("Password should at least contain 6 characters or more");
            valid = false;
        }

        else {
            oldPasswordWrapper.setError(null);
        }

        if (TextUtils.isEmpty(newPassword)) {
            newPasswordWrapper.setError("Required");
            valid = false;
        } else if (newPassword.length()<6)
        {
            newPasswordWrapper.setError("Password should at least contain 6 characters or more");
            valid = false;
        } else {
            newPasswordWrapper.setError(null);
        }

        if (oldPassword.equals(newPassword))
        {
            errorAlert.setText("Old and new passwords should not be the same");
            valid= false;
        }

        if (!oldPassword.equals(readStoredPassword(getActivity(),SignupFragment.USER_PASSWORD,"")))
        {
            oldPasswordWrapper.setError("Your old password doesn't match");
            valid = false;
        }

        return valid;
    }

    private void resetPassword(String newPass)
    {
        progressBar.setVisibility(View.VISIBLE);
        user.updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"Password reset successful",Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().beginTransaction().remove(ResetPasswordFragment.this).commit();

                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            errorAlert.setText("Password reset unsuccessful. Please try again");
                        }
                    }
                });
    }

    public static String readStoredPassword(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(SignupFragment.USER_PASSWORD, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }
}
