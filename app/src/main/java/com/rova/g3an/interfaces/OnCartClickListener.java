package com.rova.g3an.interfaces;

import com.rova.g3an.models.Products;
import com.rova.g3an.models.SellProducts;

import java.io.Serializable;

public interface OnCartClickListener extends Serializable {
    void onProductClicked(SellProducts contact, int position);
}
