package com.irfan.draft1.News;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.irfan.draft1.Alarms.PassAlarmActivityListener;
import com.irfan.draft1.R;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by irfan on 28/10/2017.
 */

public class NewsFavoriteAdapter extends RecyclerView.Adapter<NewsFavoriteAdapter.NewsHolder> {

    private Context context;
    private List<NewsModel> newsList;
    private NewsModel news;
    private NewsFavorites newsFavorites;
    private String newsFileName = "newscoo.cb";

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");


    public NewsFavoriteAdapter(Context context, List<NewsModel> newsList, NewsFavorites newsFavorites) {
        this.context = context;
        this.newsList = newsList;
        this.newsFavorites = newsFavorites;


    }


    @NonNull
    @Override
    public NewsFavoriteAdapter.NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_interface, parent, false);
//        storageThumbnail = storage.getReference();
//        storageLogo = storage.getReference();
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsFavoriteAdapter.NewsHolder holder, int position) {
        news = newsList.get(position);


        holder.organizer.setText(news.getOrganizer());
        holder.title.setText(news.getTitle());
        holder.datePosted.setText(sdf.format(news.getDate_posted()));
        holder.description.setText(news.getDescription());

        Glide.with(context).load(news.getImage()).into(holder.imageNews);
        Glide.with(context).load(news.getLogo()).into(holder.imageLogo);

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
        ImageView favorite,option;
        public TextView description;
        CircleImageView imageLogo;
        TextView organizer;
        public CardView layout;


        NewsHolder(View itemView) {
            super(itemView);
            titleDescNews = itemView.findViewById(R.id.titleDescNews);
            imageNews = itemView.findViewById(R.id.imageNews);
            datePosted = itemView.findViewById(R.id.datePosted);
            favorite = itemView.findViewById(R.id.favorite);
            organizer = itemView.findViewById(R.id.clubName);
            title = itemView.findViewById(R.id.titleNews);
            imageLogo = itemView.findViewById(R.id.clubLogo);
            description = itemView.findViewById(R.id.descriptionNews);
            option = itemView.findViewById(R.id.optionsNews);
            layout = itemView.findViewById(R.id.eventNews);

            favorite.setOnClickListener(this);
            option.setOnClickListener(this);
            layout.setOnClickListener(this);

            favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            favorite.setColorFilter(ContextCompat.getColor(context, R.color.colorFavorite), android.graphics.PorterDuff.Mode.SRC_IN);


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.favorite:
                    final int lastPosition = getAdapterPosition();
                    final NewsModel toRemove = newsList.get(lastPosition);
                    delete(lastPosition);
                    Snackbar.make(view, "Removed from Favorites!", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newsList.add(lastPosition, toRemove);
                            notifyItemInserted(lastPosition);
                            newsFavorites.newsIsEmpty(newsList);

                        }
                    }).setActionTextColor(ContextCompat.getColor(context, R.color.colorPrimary)).show();
                    break;
                case R.id.optionsNews:
                    PopupMenu newsOption = new PopupMenu(context, view);
                    newsOption.inflate(R.menu.news_option);
                    newsOption.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.shareNews:
                                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, newsList.get(getAdapterPosition()).getUrl() + "\n\n Hey you, check this out!");
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

        private void delete(int position) {
            newsList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, newsList.size());
            newsFavorites.newsIsEmpty(newsList);
        }

        private void openNews() {
            Intent intent = new Intent(context, NewsLearnMore.class);
            intent.putExtra("URL", newsList.get(getAdapterPosition()).getUrl());
            context.startActivity(intent);


        }

    }


}