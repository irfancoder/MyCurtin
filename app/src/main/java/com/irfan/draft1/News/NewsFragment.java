package com.irfan.draft1.News;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irfan.draft1.QRCodeReader.QRCodeReaderActivity;
import com.irfan.draft1.R;
import com.irfan.draft1.Schedule.Schedule;

import java.util.Objects;

/**
 * Created by irfan on 13/06/2017.
 */

public class NewsFragment extends Fragment  {

    public TabLayout.Tab tab;
    private NewsFavorites newsFavorites = new NewsFavorites();
    private View badge;
    private Animation animation;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);


        ImageView qrReader = view.findViewById(R.id.qrcode_button);
        qrReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(200);
                }
                Intent qr = new Intent(getActivity(), QRCodeReaderActivity.class);
                startActivity(qr);
            }
        });



        ViewPager viewPager = view.findViewById(R.id.viewpager);
        setNewsTabAdapter(viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tab = tabLayout.getTabAt(i);
            Objects.requireNonNull(tab).setCustomView(R.layout.badged_tab);
            TextView tabTitle;
            if (tab.getCustomView() != null) {
                tabTitle = tab.getCustomView().findViewById(android.R.id.text1);
                tabTitle.setTextColor(ContextCompat.getColorStateList(Objects.requireNonNull(getActivity()), R.color.tab_text_color_indicator));

            }
        }
        badge = Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(2)).getCustomView()).findViewById(R.id.badgeCotainer);

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==2)
                {
                    animation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_bottom);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            badge.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    badge.setAnimation(animation);


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }

    public void setAddArguments(final NewsModel args) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (args != null) {
                    newsFavorites.setUIArguments(args, getActivity());
                }
            });
        }
    }

    public void setDeleteArguments(final NewsModel args) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (args != null) {
                    newsFavorites.setUXArguments(args, getActivity());
                }
            });
        }
    }
    public void setBadge(final boolean state) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (state) {
                    badge.setVisibility(View.VISIBLE);
                    badge.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up));
                }
            });
        }
    }

    private void setNewsTabAdapter(ViewPager viewPager) {
        NewsTabAdapter newsTabAdapter = new NewsTabAdapter(getChildFragmentManager());

        newsTabAdapter.addFragments(new NewsCampus(), getString(R.string.newsfeed_tab_1));
        newsTabAdapter.addFragments(new NewsCommunity(), getString(R.string.newsfeed_tab_2));
        newsTabAdapter.addFragments(newsFavorites, getString(R.string.newsfeed_tab_3));
        viewPager.setAdapter(newsTabAdapter);
    }



}
