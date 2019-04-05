package com.irfan.draft1.MainClass;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.irfan.draft1.R;

import java.util.Arrays;
import java.util.Collections;


/**
 * Created by irfan on 15/01/2018.
 */

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    private EditText email;
    private Button submit;
    private ProgressBar progressBar;
    private TextView textStatus;
    private TextInputLayout emailWrapper;
    private FirebaseAuth auth;

    private int cx, cy;

    public static ForgotPasswordFragment newInstance(int centerX, int centerY) {
        Bundle args = new Bundle();
        args.putInt("cx", centerX);
        args.putInt("cy", centerY);
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_password_fragment, container, false);
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                cx = getArguments().getInt("cx");
                cy = getArguments().getInt("cy");

                // get the hypothenuse so the radius is from one corner to the other
                int radius = (int) Math.hypot(right, bottom);

                Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                reveal.setInterpolator(new DecelerateInterpolator(2f));
                reveal.setDuration(1000);
                reveal.start();
            }
        });

        ImageView back = view.findViewById(R.id.back_button);
        email = view.findViewById(R.id.profile_email_edit);
        submit = view.findViewById(R.id.submit_forgotpassword);
        progressBar = view.findViewById(R.id.progress_bar);
        textStatus = view.findViewById(R.id.forgot_password_status);
        emailWrapper = view.findViewById(R.id.email_wrapper);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    private void sendPasswordResetEmail(String emailAddress) {
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        textStatus.setText(getString(R.string.forgot_password_success));
                    }
                });
    }

    private boolean validateFields(String email) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailWrapper.setError("Required");
            valid = false;
        } else if (!isEmailValid(email)) {
            emailWrapper.setError("Make sure to use Curtin email");
            valid = false;
        } else {
            emailWrapper.setError(null);
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
            case R.id.submit_forgotpassword:
                String emailReceived = email.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (validateFields(emailReceived)) {
                    sendPasswordResetEmail(emailReceived);
                    submit.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);

                }
                break;

            case R.id.back_button:
                Fragment fragment = getFragmentManager().findFragmentById(R.id.main_content);

                if (fragment instanceof ForgotPasswordFragment) {
                    final ForgotPasswordFragment theFragment = (ForgotPasswordFragment) fragment;
                    Animator unreveal = theFragment.prepareUnrevealAnimator(cx, cy);


                    unreveal.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // remove the fragment only when the animation finishes
                            getFragmentManager().popBackStack();
                            //to prevent flashing the fragment before removing it, execute pending transactions inmediately
                            getFragmentManager().executePendingTransactions();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    unreveal.start();

                }

                break;
        }
    }


        public Animator prepareUnrevealAnimator ( float cx, float cy){
            int radius = getEnclosingCircleRadius(getView(), (int) cx, (int) cy);
            Animator anim = ViewAnimationUtils.createCircularReveal(getView(), (int) cx, (int) cy, radius, 0);
            anim.setInterpolator(new AccelerateInterpolator(2f));
            anim.setDuration(1000);
            return anim;
        }
        private int getEnclosingCircleRadius (View v,int cx, int cy){
            int realCenterX = cx + v.getLeft();
            int realCenterY = cy + v.getTop();
            int distanceTopLeft = (int) Math.hypot(realCenterX - v.getLeft(), realCenterY - v.getTop());
            int distanceTopRight = (int) Math.hypot(v.getRight() - realCenterX, realCenterY - v.getTop());
            int distanceBottomLeft = (int) Math.hypot(realCenterX - v.getLeft(), v.getBottom() - realCenterY);
            int distanceBottomRight = (int) Math.hypot(v.getRight() - realCenterX, v.getBottom() - realCenterY);

            Integer[] distances = new Integer[]{distanceTopLeft, distanceTopRight, distanceBottomLeft,
                    distanceBottomRight};

            return Collections.max(Arrays.asList(distances));

        }
    }
