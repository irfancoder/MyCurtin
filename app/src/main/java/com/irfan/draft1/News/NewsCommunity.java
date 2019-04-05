package com.irfan.draft1.News;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.irfan.draft1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by irfan on 04/08/2017.
 */

public class NewsCommunity extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private NewsFragmentAdapter adapter;
    private List<NewsModel> newsList = new ArrayList<>();
    private LinearLayout news_emptystate;
    private ProgressBar loadingBar;
    private SwipeRefreshLayout refreshView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Query newsReference = db.collection("CommunityNews");
    private DocumentSnapshot lastIndex;
    private int interval = 5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_tab_fragment, container, false);

        refreshView = view.findViewById(R.id.refresh_news);
        refreshView.setOnRefreshListener(this);
        loadingBar = view.findViewById(R.id.loading_bar);

        recyclerView = view.findViewById(R.id.news_list);
        news_emptystate = view.findViewById(R.id.news_emptystate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // Recycle view scrolling down...
                    if(!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)){ //if the recycler view hits the bottom of the page
                        loadNextData();

                    }

                }
            }
        });

        return view;
    }

//
//    private void newsIsEmpty(List<NewsModel> listParameter) {
//        if (listParameter.isEmpty()) {
//
//            recyclerView.setVisibility(View.INVISIBLE);
//            news_emptystate.setVisibility(View.VISIBLE);
//
//
//        } else {
//
//            recyclerView.setVisibility(View.VISIBLE);
//            news_emptystate.setVisibility(View.INVISIBLE);
//
//        }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NewsFragmentAdapter(getActivity(), newsList);
        refreshData();
    }
    private void refreshData()
    {

        newsReference.orderBy("date_posted", Query.Direction.DESCENDING).limit(interval).get().addOnCompleteListener(getActivity(),new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    newsList.clear();
                    for (DocumentSnapshot doc :
                            task.getResult()) {
                        NewsModel temp = doc.toObject(NewsModel.class);
                        newsList.add(temp);
                        lastIndex = doc;
                    }
                    adapter.notifyDataSetChanged();

                }

            }
        });
    }
    private void loadNextData()
    {
        loadingBar.setVisibility(View.VISIBLE);
        newsReference.orderBy("date_posted", Query.Direction.DESCENDING).startAfter(lastIndex).limit(interval).get().addOnCompleteListener(getActivity(),new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
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

    @Override
    public void onRefresh() {
        refreshView.setRefreshing(true);
        refreshData();
        refreshView.setRefreshing(false);
    }

//
//    Iterator<DataSnapshot> dataSnapshotsChat = dataSnapshot.child("Community News").getChildren().iterator();
//
//        while (dataSnapshotsChat.hasNext()) {
//            DataSnapshot dataSnapshotChild = dataSnapshotsChat.next();
//            NewsModel newsModel = dataSnapshotChild.getValue(NewsModel.class);
//            newsList.add(newsModel);
//        }
//        Collections.reverse(newsList);
//        adapter.notifyDataSetChanged();
//        newsIsEmpty(newsList);
}


/*
//    private void downloadNews() {
//        try {
//
//            newsList.clear();
//            final File tempFile = File.createTempFile("downloadNews", "txt");
//            StorageReference downloadReference = storage.getReference();
//            downloadReference.child("news.txt").getFile(tempFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    //  Toast.makeText(getContext(), tempFile.getAbsolutePath() + "", Toast.LENGTH_LONG).show();
//                    Gson gson = new Gson();
//                    try (Reader reader = new FileReader(tempFile)) {
//
//                        NewsModel[] test = gson.fromJson(reader, NewsModel[].class);
//
//                        for (int i = 0; i < test.length; i++) {
//                            newsList.add(test[i]);
//
//                        }
//                        adapter.notifyDataSetChanged();
//                        newsIsEmpty(newsList);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
*/
