package com.irfan.draft1.MainClass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.irfan.draft1.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.lang.Object.*;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by irfan on 13/01/2018.
 */

public class SignupFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "hello";
    public static final String USER_PASSWORD = "userpassword";

    private EditText email;
    private EditText password;
    private EditText name;
    private ProgressBar progressBar;

    private TextInputLayout userWrapper, passWrapper, nameWrapper;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

//    private Uri imageFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);

        ImageView back_button = view.findViewById(R.id.back_button);
        back_button.setOnClickListener(this);

        email = view.findViewById(R.id.profile_email_edit);
        password = view.findViewById(R.id.profile_password_edit);
        name = view.findViewById(R.id.profile_name_edit);
        Button submit = view.findViewById(R.id.sign_up);
        userWrapper = view.findViewById(R.id.email_wrapper);
        passWrapper = view.findViewById(R.id.password_wrapper);
        nameWrapper = view.findViewById(R.id.name_wrapper);
        progressBar = view.findViewById(R.id.progress_bar);
        TextView tnc_text = view.findViewById(R.id.tnc_text);
        tnc_text.setMovementMethod(LinkMovementMethod.getInstance());

        submit.setOnClickListener(this);


        return view;

    }

    private void signUpUser(final String name, String email, final String userPassword) {

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, userPassword).addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    UserProfileChangeRequest request;
                    FirebaseUser user = auth.getCurrentUser();
//                    if (imageFile!=null) {
//                         request= new UserProfileChangeRequest.Builder()
//                                .setDisplayName(name).setPhotoUri(imageFile).build();
//                    }else
//                   {
                    request = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();
//                    }
                    if (user != null) {
                        user.updateProfile(request);
                        Map<String, Object> userDetail = new HashMap<>();
                        userDetail.put("userName", name);
                        userDetail.put("userToken", FirebaseInstanceId.getInstance().getToken());
                        saveStoredPassword(getActivity(), USER_PASSWORD, userPassword);
                        db.collection("UserDatabase").document(user.getUid()).set(userDetail);



                        user.sendEmailVerification().addOnCompleteListener(task1 -> {
                           if (task1.isSuccessful())
                           {
                               VerifyEmailFragment verifyEmailFragment = VerifyEmailFragment.newInstance(user);
                               verifyEmailFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                               verifyEmailFragment.setExitTransition(new Slide(Gravity.RIGHT));
                               getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_content, verifyEmailFragment).commit();
                           }
                        });


                    }


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Snackbar.make(getView(),getString(R.string.invalid_signup_message),Snackbar.LENGTH_LONG).show();
//                    errorAlert.setText(getString(R.string.invalid_signup_message));
                }

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EasyImage.configuration(getActivity())
                .setImagesFolderName("MyCurtinFolder") // images folder name, default is "EasyImage"
                .saveInRootPicturesDirectory(); // if you want to use internal memory for storying images - default

    }

    private boolean validateFields(String name, String email, String password) {
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

        if (TextUtils.isEmpty(name)) {
            nameWrapper.setError("Name is required");
            valid = false;
        } else {
            nameWrapper.setError(null);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
//                setUserImage(imageFile);
            }


        });
    }
//    private void setUserImage(File imageFile) {
//        this.imageFile = Uri.fromFile(imageFile);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                String nameText = name.getText().toString();
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if (validateFields(nameText, emailText, passwordText)) {
                    signUpUser(nameText, emailText, passwordText);

                }
                break;
            case R.id.profile_picture_edit:
                new AlertDialog.Builder(getActivity()).setTitle("Take Picture").setMessage("Take a new photo or select one from your gallery.").setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EasyImage.openGallery(SignupFragment.this, 0);

                    }
                }).setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EasyImage.openCamera(SignupFragment.this, 0);
                    }
                }).show();
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;

            case R.id.back_button:
                getFragmentManager().popBackStack();
                break;
        }
    }


    public static void saveStoredPassword(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(USER_PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }
}
