package com.ktds.noodletimer.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Admin on 2017-07-12.
 */

public class Noodle {

    private String noodleName;
    private Drawable image;
    private int cookingTime;

    public String getNoodleName() {
        return noodleName;
    }

    public void setNoodleName(String noodleName) {
        this.noodleName = noodleName;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }
}
