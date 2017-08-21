package com.ktds.mydiary.model;

import java.io.Serializable;

/**
 * Created by Admin on 2017-06-29.
 */

public class Diary implements Serializable {

    private String id;
    private String writedDate;
    private String descript;
    private String imagePath;
    private String thumbnailImagePath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWritedDate() {
        return writedDate;
    }

    public void setWritedDate(String writedDate) {
        this.writedDate = writedDate;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getThumbnailImagePath() {
        return thumbnailImagePath;
    }

    public void setThumbnailImagePath(String thumbnailImagePath) {
        this.thumbnailImagePath = thumbnailImagePath;
    }
}
