package com.irfan.draft1.News;

/**
 * Created by irfan on 28/10/2017.
 */

public interface PassFavoriteNewsListener {
    void passFavoriteNews(NewsModel newsEntry);
    void deleteFavoriteNews(NewsModel newsDelete);
    void passBadgeNotification(boolean state);
}
