package com.irfan.draft1.MainClass;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.firebase.messaging.FirebaseMessaging;
import com.irfan.draft1.Alarms.PassAlarmActivityListener;
import com.irfan.draft1.Inbox.InboxFragment;
import com.irfan.draft1.Maps.MapTabFragment;
import com.irfan.draft1.News.NewsFragment;
import com.irfan.draft1.News.NewsLearnMore;
import com.irfan.draft1.News.NewsModel;
import com.irfan.draft1.News.PassFavoriteNewsListener;
import com.irfan.draft1.Profile.EditProfileFragment;
import com.irfan.draft1.Profile.ProfileFragment;
import com.irfan.draft1.QRCodeReader.QRCodeReaderActivity;
import com.irfan.draft1.QRCodeReader.Shaker;
import com.irfan.draft1.R;
import com.irfan.draft1.Schedule.Schedule;
import com.irfan.draft1.Schedule.TimetableTabFragment;
import com.irfan.draft1.Studies.StudiesFragment;
import com.irfan.draft1.Utilities.PatchUpdateDialog;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements PassFavoriteNewsListener,EditProfileFragment.OnProfileChange
{

    private AHBottomNavigation bottomNavigationBar;
    private AHBottomNavigationViewPager viewPager;
    private AHBottomNavigationItem news;
    private AHBottomNavigationItem schedule;
    private AHBottomNavigationItem studies;
    private AHBottomNavigationItem profile;
    private AHBottomNavigationItem inbox;

    //private MapTabFragment mapTabFragment = new MapTabFragment();
    private TimetableTabFragment timetableTabFragment = new TimetableTabFragment();
    private NewsFragment newsFragment = new NewsFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private StudiesFragment studiesFragment = new StudiesFragment();
   // private InboxFragment inboxFragment = new InboxFragment();

    private Shaker shaker;

    private int alarmCounter = 0;
//    private TextView tabTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            setContentView(R.layout.content_main);

            //FirebaseMessaging.getInstance().subscribeToTopic("CURTIN_NEWS");



            if(getIntent()!=null)
            {
                String url = getIntent().getStringExtra("URL");
                if (getIntent().hasExtra("URL"))
                {
                    Intent intent = new Intent(this,NewsLearnMore.class);
                    intent.putExtra("URL",url);
                    startActivity(intent);
                }
            }


//            AdView adView = (AdView) findViewById(R.id.adview);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            adView.loadAd(adRequest);

            bottomNavigationBar = findViewById(R.id.bottom_navigation);
            loadFragments lf = new loadFragments(newsFragment, timetableTabFragment, studiesFragment,profileFragment);
            lf.execute();

            SharedPreferences settings = this.getSharedPreferences(PatchUpdateDialog.PREFS_NAME,0);


            if (!settings.getBoolean("skipMessage",false)) {
                PatchUpdateDialog patchDialog = new PatchUpdateDialog(this);
                patchDialog.show();
            }
            news = new AHBottomNavigationItem(R.string.tab_1, R.drawable.balloons, R.color.colorBlack);
            //location = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_map, R.color.colorPrimary);
            schedule = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_bus, R.color.colorBlack);
            studies = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_studies, R.color.colorBlack);
//            inbox = new AHBottomNavigationItem(R.string.tab_4,R.drawable.mailbox,R.color.colorBlack);
            profile = new AHBottomNavigationItem(R.string.tab_5, R.drawable.forest, R.color.colorBlack);
            viewPager = findViewById(R.id.container);


            setupViewPager();
            setBottomNavigationBar();
            shaker = new Shaker(this);
            shaker.setOnShakeListener(() -> {
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(200);
                Intent qr = new Intent(getApplicationContext(), QRCodeReaderActivity.class);
                startActivity(qr);
                shaker.pause();
            });

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        shaker.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shaker.pause();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void setupViewPager() {
        //bottomNavigationBar.addItem(location);
        bottomNavigationBar.addItem(news);
        bottomNavigationBar.addItem(schedule);
        bottomNavigationBar.addItem(studies);
//        bottomNavigationBar.addItem(inbox);
        bottomNavigationBar.addItem(profile);
    }


    private void setBottomNavigationBar() {
        bottomNavigationBar.setBehaviorTranslationEnabled(false);
        bottomNavigationBar.setUseElevation(true);
        bottomNavigationBar.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        bottomNavigationBar.setAccentColor(ContextCompat.getColor(this, R.color.colorAccent));
        bottomNavigationBar.setInactiveColor(ContextCompat.getColor(this, R.color.colorDefaultText));
        bottomNavigationBar.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottomNavigationBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        bottomNavigationBar.setNotification("", 0);
                        break;
                    case 1:
                        bottomNavigationBar.setNotification("", 1);
                        break;
                    case 2:
                        bottomNavigationBar.setNotification("", 2);

                        break;
                    case 3:
                        bottomNavigationBar.setNotification("", 3);
                        break;
//                    case 4:
//                    bottomNavigationBar.setNotification("", 4);
//                        break;

                }



                viewPager.setCurrentItem(position, false);


                Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                viewPager.startAnimation(fadeIn);
                return true;
            }
        });
    }


    public boolean googleServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (apiAvailability.isUserResolvableError(isAvailable)) {
            Dialog dialog = apiAvailability.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to GooglePlay services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void passFavoriteNews(NewsModel newsEntry) {
        newsFragment.setAddArguments(newsEntry);
    }

    @Override
    public void deleteFavoriteNews(NewsModel newsDelete) {
        newsFragment.setDeleteArguments(newsDelete);
    }

    @Override
    public void passBadgeNotification(boolean state) {
        newsFragment.setBadge(state);
    }

    @Override
    public void updateProfile(String name, Uri image) {
        profileFragment.setProfileChange(name,image);
    }


    @SuppressLint("StaticFieldLeak")
    public class loadFragments extends AsyncTask<Void, Void, TabAdapter> {
        NewsFragment newsFragment;
        //        MapTabFragment mapTabFragment;
        TimetableTabFragment timetableTabFragment;
        StudiesFragment studiesFragment;
        ProfileFragment profileFragment;
//        InboxFragment inboxFragment;


        public loadFragments(NewsFragment newsFragment, TimetableTabFragment timeFragment, StudiesFragment studiesFragment, ProfileFragment profileFragment) {
            this.newsFragment = newsFragment;
//            this.mapTabFragment = mapFragment;
            this.timetableTabFragment = timeFragment;
            this.studiesFragment = studiesFragment;
            this.profileFragment = profileFragment;
//            this.inboxFragment = inboxFragment;

        }

        @Override
        protected TabAdapter doInBackground(Void... TabAdapter) {
            com.irfan.draft1.MainClass.TabAdapter adapter = new TabAdapter(getSupportFragmentManager());


            adapter.addFragment(0, newsFragment);
            adapter.addFragment(1, timetableTabFragment);
            adapter.addFragment(2, studiesFragment);
//            adapter.addFragment(3, inboxFragment);
            adapter.addFragment(3, profileFragment);

            return adapter;
        }

        @Override
        protected void onPostExecute(TabAdapter adapter) {
            super.onPostExecute(adapter);

            viewPager.setAdapter(adapter);
            bottomNavigationBar.setCurrentItem(0);

        }


    }


}




