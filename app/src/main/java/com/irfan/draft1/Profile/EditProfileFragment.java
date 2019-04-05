package com.irfan.draft1.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.irfan.draft1.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by irfan on 12/01/2018.
 */

public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private CircleImageView editProfilePicture;
    private EditText editName, editEmail;
    private Uri imageFile;
    private FirebaseUser user;
    private OnProfileChange listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_edit_fragment, container, false);
        ImageView back_button = view.findViewById(R.id.back_button);
        back_button.setOnClickListener(this);


        editProfilePicture = view.findViewById(R.id.profile_picture_edit);
        editName = view.findViewById(R.id.profile_name_edit);
        editEmail = view.findViewById(R.id.profile_email_edit);
        editEmail.setEnabled(false);
        editProfilePicture.setOnClickListener(this);
        Button resetPassword = view.findViewById(R.id.reset_password);
        resetPassword.setOnClickListener(this);

        Button submit = view.findViewById(R.id.submit_changes);
        submit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        try {
            listener = (OnProfileChange) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement onPassAlarm");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (user != null) {
            getUserInfo(user);

        }
    }

    private void getUserInfo(FirebaseUser user) {
        editName.setText(user.getDisplayName());
        imageFile = user.getPhotoUrl();
        if (imageFile != null) {
            Glide.with(getActivity()).load(imageFile).into(editProfilePicture);
        }
        editEmail.setText(user.getEmail());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_picture_edit:
                new AlertDialog.Builder(getActivity()).setTitle("Take Picture").setMessage("Take a new photo or select one from your gallery.").setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EasyImage.openGallery(EditProfileFragment.this, 0);

                    }
                }).setNegativeButton("Camera", (dialogInterface, i) -> EasyImage.openCamera(EditProfileFragment.this, 0)).show();
                break;
            case R.id.submit_changes:
                String name = editName.getText().toString();
                if (imageFile != null) {
                    setUserInfo(name, imageFile);
                }
                break;
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
            case R.id.reset_password:
                ResetPasswordFragment resetPassword = new ResetPasswordFragment();
                resetPassword.setEnterTransition(new Slide(Gravity.RIGHT));
                resetPassword.setExitTransition(new Slide(Gravity.RIGHT));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,resetPassword).addToBackStack(null).commit();

                break;
        }

    }

    private void setUserInfo(String name, Uri image) {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).setPhotoUri(image).build();
        user.updateProfile(request);
        listener.updateProfile(name, image);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                setUserImage(imageFile);
            }


        });
    }

    private void setUserImage(File imageFile) {
        this.imageFile = Uri.fromFile(imageFile);
        Glide.with(this).load(imageFile.getAbsolutePath()).into(editProfilePicture);
    }


    public interface OnProfileChange {
        void updateProfile(String name, Uri image);
    }
}
