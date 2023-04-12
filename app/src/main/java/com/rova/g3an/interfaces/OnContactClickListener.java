package com.rova.g3an.interfaces;

import com.rova.g3an.models.Products;


import java.io.Serializable;

public interface OnContactClickListener extends Serializable {
    void onContactClicked(Products contact, int position);
}
