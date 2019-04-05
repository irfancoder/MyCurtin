package com.irfan.draft1.News;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irfan.draft1.MainClass.MainActivity;
import com.irfan.draft1.R;
import com.irfan.draft1.Schedule.Schedule;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by irfan on 28/10/2017.
 */

public class NewsFavorites extends Fragment {
    private String newsFileName = "newscoo.cb";
    private RecyclerView recyclerView;
    private LinearLayout news_emptystate;
    private ImageView newsImage_emptystate;
    private TextView newsText_emptystate;
    private NewsFavoriteAdapter adapter;
    private List<NewsModel> newsList = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_tab_fragment, container, false);
        news_emptystate = view.findViewById(R.id.news_emptystate);
        newsImage_emptystate = view.findViewById(R.id.newsImage_emptystate);
        newsText_emptystate = view.findViewById(R.id.newsText_emptystate);

        try {
            readNewsFile();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        adapter = new NewsFavoriteAdapter(getActivity(), newsList,this);
        recyclerView = view.findViewById(R.id.news_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        newsIsEmpty(newsList);

        return view;
    }

    public void setUIArguments(final NewsModel args, final Context context) {
        if(context!=null){
            ((MainActivity)context).runOnUiThread(() -> {
                if (args != null) {

                    boolean check = false;
                    for (int i = 0; i < newsList.size(); i++) {
                        if (args.equals(newsList.get(i))) {
                            newsList.set(i, args);
                            check = true;
                        }

                    }
                    if (!check) {
                        newsList.add(args);
                    }

                    if (adapter!=null) {
                        adapter.notifyDataSetChanged();
                        newsIsEmpty(newsList);
                    }


                }
            });
        }
    }
    public void setUXArguments(final NewsModel args, final Context context) {
        if(context!=null){
            ((MainActivity)context).runOnUiThread(() -> {
                if (args != null) {

                    for (int i = 0; i < newsList.size(); i++) {
                        if (args.equals(newsList.get(i))) {
                            newsList.remove(i);
                        }
                    }
                    if (adapter!=null) {
                        adapter.notifyDataSetChanged();
                        newsIsEmpty(newsList);
                    }
                }
            });
        }
    }
    public void newsIsEmpty(List<NewsModel> listParameter) {
        if (listParameter.isEmpty()) {

            recyclerView.setVisibility(View.INVISIBLE);
            news_emptystate.setVisibility(View.VISIBLE);
            newsImage_emptystate.setImageResource(R.drawable.favorite_emptystate);
            newsText_emptystate.setText("This grumpy cat will go away once you start saving newsfeed");


        } else {

            recyclerView.setVisibility(View.VISIBLE);
            news_emptystate.setVisibility(View.INVISIBLE);

        }

    }
    @Override
    public void onPause() {
        super.onPause();
        saveAlarmFile();

    }

    private void saveAlarmFile() {
        try {
            FileOutputStream open = getActivity().openFileOutput(newsFileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(open);
            os.writeObject(newsList);
            os.close();


        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readNewsFile() throws ClassNotFoundException {


        try {
            FileInputStream fis = Objects.requireNonNull(getContext()).openFileInput(newsFileName);
            if (fis!=null) {
                ObjectInputStream is = new ObjectInputStream(fis);
                newsList = (List<NewsModel>) is.readObject();
                is.close();
                fis.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
