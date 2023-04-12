package com.rova.g3an.models;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

/**
 * Created by Mohamed El Sayed
 */
public class ProductsID {

    @Exclude
    public String ProductsID;
    public <T extends ProductsID> T withid (@NonNull final String id)
    {
        this.ProductsID = id;
        return (T) this;
    }
}
