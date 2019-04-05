package com.irfan.draft1.News;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.irfan.draft1.R;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by irfan on 04/08/2017.
 */

public class NewsCampus extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private NewsFragmentAdapter adapter;
    private SwipeRefreshLayout refreshView;
    private List<NewsModel> newsList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Query newsReference = db.collection("CampusNews");
    private Query featuredReference = db.collection("FeaturedNews");
    private DocumentSnapshot lastIndex;
    private static final int interval = 8;
    private ProgressBar loadingBar;
    private FeaturedNewsAdapter featuredNewsAdapter;
    private List<NewsModel> featuredNewsList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.news_campus_fragment, container, false);

        ViewPager viewPager = view.findViewById(R.id.featured_news);
        viewPager.setAdapter(featuredNewsAdapter);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(32, 0, 32, 0);
        viewPager.setPageMargin(16);

        refreshView = view.findViewById(R.id.refresh_news);
        refreshView.setOnRefreshListener(this);
        loadingBar = view.findViewById(R.id.loading_bar);
        RecyclerView recyclerView = view.findViewById(R.id.news_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        final FloatingActionButton fab = view.findViewById(R.id.scroll_up_fab);

        final NestedScrollView scrollView = view.findViewById(R.id.scroll_view);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int dy = scrollY - oldScrollY;
                if (dy > 0) {
                    fab.show();
                } else {
                    fab.hide();
                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    loadNextData();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(NestedScrollView.FOCUS_UP);
            }
        });


//                .addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0) {
//                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) { //if the recycler view hits the bottom of the page
//
//
//                    }
//
//                }
//            }
//        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        featuredNewsAdapter = new FeaturedNewsAdapter(getActivity(), featuredNewsList);
        adapter = new NewsFragmentAdapter(getActivity(), newsList);
        onRefresh();


    }

    @Override
    public void onRefresh() {

        refreshData();

    }

    private void refreshData() {

        if (refreshView!=null) {
            refreshView.setRefreshing(true);
        }
        featuredReference.orderBy("date_posted", Query.Direction.DESCENDING).get().addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    featuredNewsList.clear();
                    for (DocumentSnapshot doc :
                            task.getResult()) {
                        NewsModel temp = doc.toObject(NewsModel.class);
                        featuredNewsList.add(temp);

                    }
                    featuredNewsAdapter.notifyDataSetChanged();

                }
            }
        });

        newsReference.orderBy("date_posted", Query.Direction.DESCENDING).limit(interval).get().addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    newsList.clear();
                    for (DocumentSnapshot doc :
                            task.getResult()) {
                        NewsModel temp = doc.toObject(NewsModel.class);
                        newsList.add(temp);
                        lastIndex = doc;
                    }
                    adapter.notifyDataSetChanged();
                    if (refreshView!=null) {
                        refreshView.setRefreshing(false);
                    }
                }

            }
        });


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("savedFeaturedNewsList", (ArrayList<? extends Parcelable>) featuredNewsList);
        outState.putParcelableArrayList("savedGeneralNewsList", (ArrayList<? extends Parcelable>) newsList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            newsList=savedInstanceState.getParcelableArrayList("savedGeneralNewsList");
            featuredNewsList= savedInstanceState.getParcelableArrayList("savedFeaturedNewsList");
        }

    }

    private void loadNextData() {
        loadingBar.setVisibility(View.VISIBLE);
        newsReference.orderBy("date_posted", Query.Direction.DESCENDING).startAfter(lastIndex).limit(interval).get().addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc :
                            task.getResult()) {
                        NewsModel temp = doc.toObject(NewsModel.class);
                        newsList.add(temp);
                        lastIndex = doc;
                    }
                    adapter.notifyDataSetChanged();
                    loadingBar.setVisibility(View.GONE);
                }

            }
        });
    }

    //        Iterator<DataSnapshot> dataSnapshotsChat = dataSnapshot.child("Curtin News").getChildren().iterator();
//
//        while (dataSnapshotsChat.hasNext()) {
//            DataSnapshot dataSnapshotChild = dataSnapshotsChat.next();
//            NewsModel newsModel = dataSnapshotChild.getValue(NewsModel.class);
//            newsList.add(newsModel);
//        }
//        Collections.reverse(newsList);
//        adapter.notifyDataSetChanged();
}

