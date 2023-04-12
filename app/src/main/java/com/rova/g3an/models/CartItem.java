package com.rova.g3an.models;

import android.graphics.Bitmap;

/**
 * Created by leeyipfung on 3/16/2018.
 */

public class CartItem {
    public CartItem(String prodVariant, String prodprofURL, String prodName, String prodPrice, String cartQty, String retailerProfPicURL, String retailerShopName, String cartID, String itemStatus, String realPhoto1, String realPhoto2, String photodate, String expdate, String limitqty, String discount, String discountedPrice, String prodcode, Bitmap profBitmap, Bitmap coverBitmap) {
        this.prodVariant = prodVariant;
        this.prodprofURL = prodprofURL;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.cartQty = cartQty;
        this.retailerProfPicURL = retailerProfPicURL;
        this.retailerShopName = retailerShopName;
        this.cartID = cartID;
        this.itemStatus = itemStatus;
        this.realPhoto1 = realPhoto1;
        this.realPhoto2 = realPhoto2;
        this.photodate = photodate;
        this.expdate = expdate;
        this.limitqty = limitqty;
        this.discount = discount;
        this.discountedPrice = discountedPrice;
        this.prodcode = prodcode;
        this.profBitmap = profBitmap;
        this.coverBitmap = coverBitmap;
    }

    private String prodVariant, prodprofURL, prodName, prodPrice, cartQty, retailerProfPicURL, retailerShopName,cartID,itemStatus,realPhoto1,realPhoto2,photodate,expdate,limitqty,discount,discountedPrice,prodcode;
    private Bitmap profBitmap,coverBitmap;

    public CartItem(String prodName, String prodPrice, String cartID) {
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.cartID = cartID;
    }

    public String getProdcode() {
        return prodcode;
    }

    public void setProdcode(String prodcode) {
        this.prodcode = prodcode;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getLimitqty() {
        return limitqty;
    }

    public void setLimitqty(String limitqty) {
        this.limitqty = limitqty;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getPhotodate() {
        return photodate;
    }

    public void setPhotodate(String photodate) {
        this.photodate = photodate;
    }

    public String getRealPhoto1() {
        return realPhoto1;
    }

    public void setRealPhoto1(String realPhoto1) {
        this.realPhoto1 = realPhoto1;
    }

    public String getRealPhoto2() {
        return realPhoto2;
    }

    public void setRealPhoto2(String realPhoto2) {
        this.realPhoto2 = realPhoto2;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public Bitmap getProfBitmap() {
        return profBitmap;
    }

    public void setProfBitmap(Bitmap profBitmap) {
        this.profBitmap = profBitmap;
    }

    public Bitmap getCoverBitmap() {
        return coverBitmap;
    }

    public void setCoverBitmap(Bitmap coverBitmap) {
        this.coverBitmap = coverBitmap;
    }

    public Bitmap getprofBitmap() {
        return profBitmap;
    }

    public void setprofBitmap(Bitmap profBitmap) {
        this.profBitmap = profBitmap;
    }

    public String getProdVariant() {
        return prodVariant;
    }

    public void setProdVariant(String prodVariant) {
        this.prodVariant = prodVariant;
    }

    public String getProdprofURL() {
        return prodprofURL;
    }

    public void setProdprofURL(String prodprofURL) {
        this.prodprofURL = prodprofURL;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getCartQty() {
        return cartQty;
    }

    public void setCartQty(String cartQty) {
        this.cartQty = cartQty;
    }

    public String getRetailerProfPicURL() {
        return retailerProfPicURL;
    }

    public void setRetailerProfPicURL(String retailerProfPicURL) {
        this.retailerProfPicURL = retailerProfPicURL;
    }

    public String getRetailerShopName() {
        return retailerShopName;
    }

    public void setRetailerShopName(String retailerShopName) {
        this.retailerShopName = retailerShopName;
    }
}

