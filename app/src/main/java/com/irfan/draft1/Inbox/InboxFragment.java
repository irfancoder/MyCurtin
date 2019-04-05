package com.irfan.draft1.Inbox;

import android.os.Bundle;
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
import android.widget.TextView;

import com.irfan.draft1.News.NewsCampus;
import com.irfan.draft1.News.NewsCommunity;
import com.irfan.draft1.News.NewsFavorites;
import com.irfan.draft1.News.NewsModel;
import com.irfan.draft1.News.NewsTabAdapter;
import com.irfan.draft1.R;

/**
 * Created by irfan on 26/01/2018.
 */

public class InboxFragment extends Fragment  {

    private NewsTabAdapter newsTabAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public TabLayout.Tab tab;
    private View badge;
    private Animation animation;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inbox_fragment, container, false);

        viewPager = view.findViewById(R.id.inboxViewpager);
        setNewsTabAdapter(viewPager);

        tabLayout = view.findViewById(R.id.inboxTabs);
        tabLayout.setupWithViewPager(viewPager);


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.badged_tab);
            TextView tabTitle = tab.getCustomView().findViewById(android.R.id.text1);
            tabTitle.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.tab_text_color_indicator));
        }
        badge = tabLayout.getTabAt(0).getCustomView().findViewById(R.id.badgeCotainer);

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0)
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



    private void setNewsTabAdapter(ViewPager viewPager) {
        newsTabAdapter = new NewsTabAdapter(getChildFragmentManager());

        newsTabAdapter.addFragments(new ChatTabFragment(), getString(R.string.inbox_tab_1));
        newsTabAdapter.addFragments(new ContactsTabFragment(), getString(R.string.inbox_tab_2));
        viewPager.setAdapter(newsTabAdapter);
    }



}
