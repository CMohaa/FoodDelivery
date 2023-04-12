package com.rova.g3an.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Products extends ProductsID implements Serializable , Parcelable , Cloneable {


    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY= "quantity";
    public static final String COLUMN_COST= "cost";
    public static final String COLUMN_PRICE= "price";
    public static final String COLUMN_SRC= "src";
    public static final String COLUMN_TRADER= "trader";
    public static final String COLUMN_VALIDITY_START= "validity_start";
    public static final String COLUMN_VALIDITY_END= "validity_end";
    public static final String COLUMN_TYPE= "type";
    public static final String COLUMN_TIME_STAMP= "time_stamp";

    public Products() {

    }

    public static String CREATE_TABLE(String TABLE_NAME)
    {
        return
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_NAME +" TEXT ,"
                        + COLUMN_QUANTITY +" INTEGER ,"
                        + COLUMN_COST +" REAL,"
                        + COLUMN_PRICE +" REAL,"
                        + COLUMN_SRC +" TEXT,"
                        + COLUMN_TRADER +" TEXT,"
                        + COLUMN_VALIDITY_START +" REAL,"
                        + COLUMN_VALIDITY_END +" REAL,"
                        + COLUMN_TYPE +" TEXT,"
                        + COLUMN_TIME_STAMP +" INTEGER"
                        + ")";
    }
    public static String CREATE_TABLE_EXIST(String TABLE_NAME)
    {
        return
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_NAME +" TEXT ,"
                        + COLUMN_QUANTITY +" INTEGER ,"
                        + COLUMN_COST +" REAL,"
                        + COLUMN_PRICE +" REAL,"
                        + COLUMN_SRC +" TEXT,"
                        + COLUMN_TRADER +" TEXT,"
                        + COLUMN_VALIDITY_START +" REAL,"
                        + COLUMN_VALIDITY_END +" REAL,"
                        + COLUMN_TYPE +" TEXT,"
                        + COLUMN_TIME_STAMP +" INTEGER"
                        + ")";
    }
    private static final String TAG = "Products";
    private int id;
    private String name;
    private double quantity;


    private double cost;
    private double price;
    private String src;
    private String trader;
    private String validity_start;
    private String validity_end;
    private String type;
    private String thumb_image;
    private long time_stamp;
    private long barcode;


    public Products(int id, String name, double quantity, double cost, double price, String src, String trader, String validity_start, String validity_end, String type, long time_stamp) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.price = price;
        this.src = src;
        this.trader = trader;
        this.validity_start = validity_start;
        this.validity_end = validity_end;
        this.type = type;
        this.time_stamp = time_stamp;
    }

    protected Products(Parcel in) {
        /*
           private int id;
    private String name;
    private int quantity;
    private float cost;
    private float price;
    private String src;
    private String trader;
    private long validity_start;
    private long validity_end;
    private String type;
    private Map<Integer, String> products;
         */
        id = in.readInt();
        name = in.readString();
        quantity = in.readDouble();
        cost = in.readDouble();
        price = in.readDouble();
        src = in.readString();
        trader = in.readString();
        validity_start = in.readString();
        validity_end = in.readString();
        type = in.readString();
        time_stamp = in.readLong();
        barcode = in.readLong();
        //products = in.readMap();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(quantity);
        dest.writeDouble(cost);
        dest.writeDouble(price);
        dest.writeString(src);
        dest.writeString(trader);
        dest.writeString(validity_start);
        dest.writeString(validity_end);
        dest.writeString(type);
        dest.writeLong(barcode);
        dest.writeLong(time_stamp);

    }
    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", cost='" + cost + '\'' +
                ", price='" + price + '\'' +
                ", src='" + src + '\'' +
                ", trader='" + trader + '\'' +
                ", validity_start='" + validity_start + '\'' +
                ", validity_end='" + validity_end + '\'' +
                ", type='" + type + '\'' +
                ", barcode='" + barcode + '\'' +
                ", time_stamp='" + time_stamp + '\'' +
                '}';
    }
    public static final Creator<Products> CREATOR
            = new Creator<Products>() {
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    public Products(String name, double price, String thumb_image) {
        this.name = name;
        this.price = price;
        this.thumb_image = thumb_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTrader() {
        return trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public String getValidity_start() {
        return validity_start;
    }

    public void setValidity_start(String validity_start) {
        this.validity_start = validity_start;
    }

    public String getValidity_end() {
        return validity_end;
    }

    public void setValidity_end(String validity_end) {
        this.validity_end = validity_end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }
}
