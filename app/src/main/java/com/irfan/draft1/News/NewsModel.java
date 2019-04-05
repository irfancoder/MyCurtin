package com.irfan.draft1.News;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by irfan on 25/10/2017.
 */

public class NewsModel implements Parcelable,Serializable {

    private String title;
    private String image;
    private Date date_posted;
    private String url;
    private String logo;
    private String organizer;
    private String description;

    public NewsModel()
    {

    }

    public NewsModel(String title, String image,String url,Date date_posted,String logo,String description,String organizer) {
        this.title = title;
        this.image = image;
        this.url = url;
        this.date_posted = date_posted;
        this.logo = logo;
        this.description = description;
        this.organizer = organizer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public Date getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(Date date_posted) {
        this.date_posted = date_posted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    protected NewsModel(Parcel in) {
        title = in.readString();
        image = in.readString();
        long tmpDate_posted = in.readLong();
        date_posted = tmpDate_posted != -1 ? new Date(tmpDate_posted) : null;
        url = in.readString();
        logo = in.readString();
        organizer = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(image);
        dest.writeLong(date_posted != null ? date_posted.getTime() : -1L);
        dest.writeString(url);
        dest.writeString(logo);
        dest.writeString(organizer);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NewsModel> CREATOR = new Parcelable.Creator<NewsModel>() {
        @Override
        public NewsModel createFromParcel(Parcel in) {
            return new NewsModel(in);
        }

        @Override
        public NewsModel[] newArray(int size) {
            return new NewsModel[size];
        }
    };
}