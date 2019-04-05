package com.irfan.draft1.MainClass;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.irfan.draft1.Notification.EventNotificationID;
import com.irfan.draft1.Notification.EventsNotification;
import com.irfan.draft1.R;

import java.util.Objects;

/**
 * Created by irfan on 13/01/2018.
 */

public class SigninFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "hello";
    private EditText email;
    private EditText password;
    private ProgressBar progressBar;
    private TextInputLayout userWrapper, passWrapper;

    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signin_fragment, container, false);

        ImageView back = view.findViewById(R.id.back_button);
        email = view.findViewById(R.id.profile_email_edit);
        password = view.findViewById(R.id.profile_password_edit);
        Button submit = view.findViewById(R.id.login);

        TextView forgotPassword = view.findViewById(R.id.forgot_password);
        userWrapper = view.findViewById(R.id.email_wrapper);
        passWrapper = view.findViewById(R.id.password_wrapper);
        progressBar = view.findViewById(R.id.progress_bar);
        forgotPassword.setOnClickListener(this);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();


    }

    private void signInUser(String email, final String password) {
        progressBar.setVisibility(View.VISIBLE);
//        errorAlert.setText(null);

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), task -> {
            progressBar.setVisibility(View.GONE);
            FirebaseUser user = auth.getCurrentUser();

            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                if (user != null) {
                    if (user.isEmailVerified()) {

                        // [START subscribe_topics]
                        FirebaseMessaging.getInstance().subscribeToTopic("CURTIN_NEWS")
                                .addOnCompleteListener(task1 -> {
                                    String msg = "subscription success";
                                    if (!task1.isSuccessful()) {
                                        msg = "subscription failure";
                                    }
                                    Log.d(TAG, msg);
//                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                });


                        // [END subscribe_topics]

                        SignupFragment.saveStoredPassword(Objects.requireNonNull(getActivity()), SignupFragment.USER_PASSWORD, password);
                        Toast.makeText(getActivity(), "Welcome back, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Snackbar.make(getView(), "Please verify your email before sign in", Snackbar.LENGTH_LONG).show();
                    }
                }


            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                passWrapper.setError("Password or email is incorrect");
            }
        });


    }

    private boolean validateFields(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            userWrapper.setError("Required");
            valid = false;
        } else if (!isEmailValid(email)) {
            userWrapper.setError("Make sure to use Curtin email");
            valid = false;
        } else {
            userWrapper.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passWrapper.setError("Required");
            valid = false;
        } else if (password.length() < 6) {
            passWrapper.setError("Password should at least contain 6 characters or more");
            valid = false;
        } else {
            passWrapper.setError(null);
        }

        return valid;
    }

    boolean isEmailValid(String email) {

        boolean isCurtinEmail = false;
        String domain = email.substring(email.indexOf("@") + 1);
        switch (domain) {
            case "student.curtin.edu.my":
            case "curtin.edu.my":
                isCurtinEmail = true;
                break;

        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && isCurtinEmail;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                userWrapper.setError(null);
                passWrapper.setError(null);
                if (validateFields(emailText, passwordText)) {
                    signInUser(emailText, passwordText);

                }
                break;
            case R.id.forgot_password:
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                ForgotPasswordFragment forgot = ForgotPasswordFragment.newInstance(location[0], location[1]);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_content, forgot).commit();
                break;
            case R.id.back_button:
                getFragmentManager().popBackStack();
                break;

        }
    }
}
