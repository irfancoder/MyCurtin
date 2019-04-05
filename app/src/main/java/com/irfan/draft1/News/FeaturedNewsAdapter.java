package com.irfan.draft1.News;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
 * Created by irfan on 10/01/2018.
 */

public class FeaturedNewsAdapter extends PagerAdapter {

    private List<NewsModel> featuredNewsList = new ArrayList<>();
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
    private PassFavoriteNewsListener passFavoriteNews;
    private String newsFileName = "newscoo.cb";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageThumbnail;
    private StorageReference storageLogo;

    public FeaturedNewsAdapter(Context context, List<NewsModel> imageModelArrayList) {
        this.context = context;
        this.featuredNewsList = imageModelArrayList;
        try {
            passFavoriteNews = (PassFavoriteNewsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onPassAlarm");
        }


    }

    public void setNews(List<NewsModel> fragment) {
        featuredNewsList = fragment;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        //This acts like a viewholder in recyclerview adapter
        final NewsModel newsItem = featuredNewsList.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.news_list_interface, container, false);
        ImageView featuredImage = view.findViewById(R.id.imageNews);
        TextView datePosted = view.findViewById(R.id.datePosted);
        ImageView favorite = view.findViewById(R.id.favorite);
        TextView title = view.findViewById(R.id.titleNews);
        CircleImageView clubLogo = view.findViewById(R.id.clubLogo);
        TextView description = view.findViewById(R.id.descriptionNews);
        TextView organizer = view.findViewById(R.id.clubName);
        ImageView options = view.findViewById(R.id.optionsNews);
        CardView layout = view.findViewById(R.id.eventNews);


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!view.isSelected()) {
                    if (!similarInFavorites(newsItem)) {

                        ((ImageView) view).setImageResource(R.drawable.ic_favorite_black_24dp);
                        ((ImageView) view).setColorFilter(ContextCompat.getColor(context, R.color.colorFavorite), android.graphics.PorterDuff.Mode.SRC_IN);
                        transfer(featuredNewsList.get(position));
                        Snackbar.make(view, "Added to Favorites!", Snackbar.LENGTH_SHORT).show();
                        view.setSelected(!view.isSelected());
                    } else {
                        Snackbar.make(view, "Already in Favorites!", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    ((ImageView) view).setColorFilter(ContextCompat.getColor(context, R.color.colorBlack), android.graphics.PorterDuff.Mode.SRC_IN);
                    detransfer(featuredNewsList.get(position));
                    view.setSelected(!view.isSelected());
                    Snackbar.make(view, "Removed from Favorites!", Snackbar.LENGTH_SHORT).show();

                }
            }
        });
        options.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                PopupMenu newsOption = new PopupMenu(context, view);
                newsOption.inflate(R.menu.news_option);
                newsOption.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.shareNews:

                                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, newsItem.getUrl() + "\n\n Hey, check this out! Shared from MyCurtin");
                                sendIntent.setType("text/plain");
                                context.startActivity(sendIntent);
                                break;
                        }
                        return true;
                    }
                });
                newsOption.show();
            }
        });


//        storageThumbnail = storage.getReference().child("thumbnail/" + newsItem.getImage());
//        storageLogo = storage.getReference().child("logo/" + newsItem.getLogo());
        organizer.setText(newsItem.getOrganizer());
        title.setText(newsItem.getTitle());
        datePosted.setText(sdf.format(newsItem.getDate_posted()));
        description.setText(newsItem.getDescription());

        Glide.with(context).load(newsItem.getImage()).into(featuredImage);
        Glide.with(context).load(newsItem.getLogo()).into(clubLogo);
//        Glide.with(context).using(new FirebaseImageLoader()).load(storageThumbnail).into(featuredImage);
//        Glide.with(context).using(new FirebaseImageLoader()).load(storageLogo).into(clubLogo);


        layout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsLearnMore.class);
                intent.putExtra("URL", featuredNewsList.get(position).getUrl());
                context.startActivity(intent);
            }
        });
        container.addView(view);

        return view;

    }

    @Override
    public int getCount() {
        return featuredNewsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }


    private boolean similarInFavorites(NewsModel newsSimilar) {
        boolean check = false;
        try {
            List<NewsModel> temp = readNewsFile();
            for (int i = 0; i < temp.size(); i++) {
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
            for (NewsModel doc :
                    temp) {
                if (newsDelete.getUrl().equals(doc.getUrl())) {
                    temp.remove(doc);

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
