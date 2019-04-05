package com.irfan.draft1.News;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.icu.util.DateInterval;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.irfan.draft1.MainClass.MainActivity;
import com.irfan.draft1.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by irfan on 03/08/2017.
 */

public class NewsFragmentAdapter extends RecyclerView.Adapter<NewsFragmentAdapter.NewsHolder>
{

    private Context context;
    private List<NewsModel> newsList;
    private NewsModel news;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageThumbnail;
    private StorageReference storageLogo;
    private PassFavoriteNewsListener passFavoriteNews;
    private String newsFileName = "newscoo.cb";
    private SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");



    public NewsFragmentAdapter(Context context, List<NewsModel> newsList) {
        this.context = context;
        this.newsList = newsList;
        try {
            passFavoriteNews = (PassFavoriteNewsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onPassAlarm");
        }

    }


    @Override
    public NewsFragmentAdapter.NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_interface, parent, false);

        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsFragmentAdapter.NewsHolder holder, int position) {
        news = newsList.get(position);

//        storageThumbnail = storage.getReference().child("thumbnail/"+news.getImage());
//        storageLogo = storage.getReference().child("logo/"+news.getLogo());
        holder.organizer.setText(news.getOrganizer());
        holder.title.setText(news.getTitle());
        holder.datePosted.setText(sdf.format(news.getDate_posted()));
        holder.description.setText(news.getDescription());
        Glide.with(context).load(news.getImage()).into(holder.imageNews);
        Glide.with(context).load(news.getLogo()).into(holder.clubLogo);
//        Glide.with(context).using(new FirebaseImageLoader()).load(storageThumbnail).into(holder.imageNews);
//        Glide.with(context)
//                .using(new FirebaseImageLoader())
//                .load(storageLogo)
//                .into(holder.clubLogo);

    }




    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageNews;
        public TextView datePosted;
        public TextView title;
        public LinearLayout titleDescNews;
        public ImageView favorite;
        public CircleImageView clubLogo;
        public TextView description;
        public TextView organizer;
        public ImageView options;
        public CardView eventNews;

        public NewsHolder(View itemView) {
            super(itemView);
            titleDescNews = itemView.findViewById(R.id.titleDescNews);
            imageNews = itemView.findViewById(R.id.imageNews);
            datePosted = itemView.findViewById(R.id.datePosted);
            favorite = itemView.findViewById(R.id.favorite);
            title = itemView.findViewById(R.id.titleNews);
            clubLogo = itemView.findViewById(R.id.clubLogo);
            description = itemView.findViewById(R.id.descriptionNews);
            organizer = itemView.findViewById(R.id.clubName);
            eventNews = itemView.findViewById(R.id.eventNews);
            options = itemView.findViewById(R.id.optionsNews);

            eventNews.setOnClickListener(this);
            favorite.setOnClickListener(this);
            options.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.favorite:
                    if (!view.isSelected()) {
                        if (!similarInFavorites(newsList.get(getAdapterPosition()))) {
                            ((ImageView)view).setImageResource(R.drawable.ic_favorite_black_24dp);
                            ((ImageView)view).setColorFilter(ContextCompat.getColor(context, R.color.colorFavorite), android.graphics.PorterDuff.Mode.SRC_IN);
                            transfer(newsList.get(getAdapterPosition()));
                            Snackbar.make(view, "Added to Favorites!", Snackbar.LENGTH_SHORT).show();

                            view.setSelected(!view.isSelected());
                        }else
                        {
                            Snackbar.make(view, "Already in Favorites!", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        ((ImageView)view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        ((ImageView)view).setColorFilter(ContextCompat.getColor(context, R.color.colorBlack), android.graphics.PorterDuff.Mode.SRC_IN);
                        detransfer(newsList.get(getAdapterPosition()));
                        view.setSelected(!view.isSelected());
                        Snackbar.make(view, "Removed from Favorites!", Snackbar.LENGTH_SHORT).show();

                    }
                    break;
                case R.id.optionsNews:
                    PopupMenu newsOption = new PopupMenu(context,view);
                    newsOption.inflate(R.menu.news_option);
                    newsOption.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId())
                            {
                                case R.id.shareNews:
                                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, newsList.get(getAdapterPosition()).getUrl()+"\n\n Hey, check this out! Share from MyCurtin");
                                    sendIntent.setType("text/plain");
                                    context.startActivity(sendIntent);
                                    break;
                            }
                        return true;
                        }
                    });
                    newsOption.show();
                    break;
                case R.id.eventNews:
                    openNews();
                    break;


            }
        }



        private boolean similarInFavorites(NewsModel newsSimilar)
        {
            boolean check = false;
            try {
                List<NewsModel> temp = readNewsFile();
                for (int i = 0;i<temp.size();i++) {
                    if (newsSimilar.getUrl().equals(temp.get(i).getUrl())) {
                        check = true;
                    }
                }
                saveNewsFile(temp);



            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return check;
        }
        private void detransfer(NewsModel newsDelete) {
            try {
                List<NewsModel> temp = readNewsFile();
                for (int i = 0;i<temp.size();i++) {
                    if (newsDelete.getUrl().equals(temp.get(i).getUrl())) {
                        temp.remove(i);

                    }
                }
                saveNewsFile(temp);
                passFavoriteNews.deleteFavoriteNews(newsDelete);



            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void transfer(final NewsModel newsEntry) {

            try {
                List<NewsModel> temp = readNewsFile();
                temp.add(newsEntry);
                passFavoriteNews.passFavoriteNews(newsEntry);
                passFavoriteNews.passBadgeNotification(true);
                saveNewsFile(temp);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

        private void openNews() {
            Intent intent = new Intent(context, NewsLearnMore.class);
            intent.putExtra("URL", newsList.get(getAdapterPosition()).getUrl());
            context.startActivity(intent);
//            Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(eventNews,eventNews.getLeft(),eventNews.getTop(),eventNews.getWidth(),eventNews.getHeight()).toBundle();
//            ActivityCompat.startActivity(context,intent,options);
//            //((MainActivity)context).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        }

    }

    private void saveNewsFile(List<NewsModel> savedFavorites) {
        try {
            FileOutputStream open = context.openFileOutput(newsFileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(open);
            os.writeObject(savedFavorites);
            os.close();


        } catch (FileNotFoundException ignored) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<NewsModel> readNewsFile() throws ClassNotFoundException {

        List<NewsModel> savedFavorites = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(newsFileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            savedFavorites = (List<NewsModel>) is.readObject();
            is.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return savedFavorites;
    }


}
