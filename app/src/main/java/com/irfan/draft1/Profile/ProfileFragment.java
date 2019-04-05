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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.irfan.draft1.MainClass.OnboardingActivity;
import com.irfan.draft1.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by irfan on 20/07/2017.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener  {

    private TextView profileName,profileEmail;
    private CircleImageView profilePicture;
    private FirebaseUser user;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);


        profileName = view.findViewById(R.id.profile_name);
        profilePicture = view.findViewById(R.id.profile_picture);
        profileEmail = view.findViewById(R.id.profile_email);
        Button aboutUs = view.findViewById(R.id.about_us);
        Button signout = view.findViewById(R.id.sign_out);
//        Button notification = view.findViewById(R.id.notification_settings);
        ImageView editProfile = view.findViewById(R.id.edit_profile);
//        notification.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        signout.setOnClickListener(this);
        editProfile.setOnClickListener(this);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (user!=null)
        {
            profileName.setText(user.getDisplayName());
            profileEmail.setText(user.getEmail());

            if (user.getPhotoUrl()!=null) {
                Glide.with(getActivity()).load(user.getPhotoUrl()).into(profilePicture);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.about_us:
                final AboutUsDialog dialog = new AboutUsDialog(getActivity());
                dialog.show();


                break;

            case R.id.sign_out:
                new AlertDialog.Builder(getActivity()).setTitle("Continue to sign out?").setMessage("This will log you out and will require you to sign back in again").setPositiveButton("Sign out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), OnboardingActivity.class);
                        startActivity(intent);
                        (getActivity()).finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();


                break;
            case R.id.edit_profile:
                EditProfileFragment editProfile = new EditProfileFragment();
                editProfile.setEnterTransition(new Slide(Gravity.RIGHT));
                editProfile.setExitTransition(new Slide(Gravity.RIGHT));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,editProfile).addToBackStack(null).commit();
                break;
//            case R.id.notification_settings:

//                break;
        }
    }



    public void setProfileChange(final String name, final Uri image)
    {
        getActivity().runOnUiThread(() -> {
            profileName.setText(name);
            Glide.with(getActivity()).load(image).into(profilePicture);
        });
    }
}
