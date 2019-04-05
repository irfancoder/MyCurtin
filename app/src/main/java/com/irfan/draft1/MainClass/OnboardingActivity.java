package com.irfan.draft1.MainClass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.irfan.draft1.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OnboardingActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    ImageView bigCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        auth = FirebaseAuth.getInstance();

        bigCircle = findViewById(R.id.animated_background);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        //saveSharedSetting(OnboardingActivity.this, getString(R.string.IS_USER_FIRST_TIME), false);

        if (auth.getCurrentUser()!=null)
        {
            if (auth.getCurrentUser().isEmailVerified())
            {
                Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }



        TextView signUpButton = findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(view -> {

            SignupFragment signup = new SignupFragment();
            signup.setEnterTransition(new Slide(Gravity.RIGHT));
            signup.setExitTransition(new Slide(Gravity.RIGHT));
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_content, signup).commit();
        });

        TextView loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            SigninFragment signin = new SigninFragment();
            signin.setEnterTransition(new Slide(Gravity.RIGHT));
            signin.setExitTransition(new Slide(Gravity.RIGHT));
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_content, signin).commit();
        });
        TabLayout indicator = findViewById(R.id.onboard_indicator);
        indicator.setupWithViewPager(mViewPager);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private String[] onboardingText;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_onboarding, container, false);

            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            onboardingText = getResources().getStringArray(R.array.onboarding_messages);

            TextView onboardMessage = view.findViewById(R.id.onboard_text);
            onboardMessage.setText(onboardingText[position - 1]);
            //Glide.with(getActivity()).load(imageOnboarding[position-1]).into(onboardImage);
            return view;
        }

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        AnimationDrawable animatedCircle = (AnimationDrawable) bigCircle.getDrawable();
        animatedCircle.setEnterFadeDuration(4000);
        animatedCircle.setExitFadeDuration(4000);
        animatedCircle.start();
    }

    public static void saveSharedSetting(Context ctx, String settingName, boolean settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(SplashActivity.PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(settingName, settingValue);
        editor.apply();
    }


}
