package com.irfan.draft1.MainClass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.irfan.draft1.R;

public class VerifyEmailFragment extends Fragment
{

     public static VerifyEmailFragment newInstance(FirebaseUser user)
     {
         String name = user.getDisplayName();
         String email = user.getEmail();
         Bundle args = new Bundle();
         args.putString("name", name);
         args.putString("email", email);
         VerifyEmailFragment emailFragment = new VerifyEmailFragment();
         emailFragment.setArguments(args);
         return emailFragment;
     }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.success_signup_fragment,container,false);
        TextView  welcomeMessage = view.findViewById(R.id.welcome_message);
        TextView welcomeInstruction = view.findViewById(R.id.welcome_instruction);

        welcomeMessage.setText("Welcome, "+getArguments().getString("name")+"!\nNow check your inbox.");
        welcomeInstruction.setText("We have sent an email to \n"+getArguments().getString("email")+" \nto verify your account.");
        welcomeInstruction.setMovementMethod(LinkMovementMethod.getInstance());


//        AnimationDrawable animatedCircle = (AnimationDrawable) featureCircleBig.getDrawable();
//        animatedCircle.setEnterFadeDuration(2000);
//        animatedCircle.setExitFadeDuration(2000);
//        animatedCircle.start();

        Button openInbox = view.findViewById(R.id.openInbox);
        openInbox.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            getActivity().startActivity(intent);
        });

        Button returnHome = view.findViewById(R.id.backtoLogin);
        returnHome.setOnClickListener(view12 -> {
            getFragmentManager().beginTransaction().remove(this).commit();
        });

        return view;
    }
}
