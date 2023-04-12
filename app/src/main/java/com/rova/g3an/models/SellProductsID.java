package com.rova.g3an.models;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

/**
 * Created by Mohamed El Sayed
 */
public class SellProductsID {

    @Exclude
    public String SellProductsID;
    public <T extends SellProductsID> T withid (@NonNull final String id)
    {
        this.SellProductsID = id;
        return (T) this;
    }
}
