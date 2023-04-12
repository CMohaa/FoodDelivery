package com.rova.g3an.Base;

import com.rova.g3an.models.SellProducts;

import java.util.ArrayList;
import java.util.List;

public class OrdersBase {

    private List<SellProducts> mOrders;
    private static volatile OrdersBase instance = new OrdersBase();
    private OrdersBase(){
        mOrders = new ArrayList<>();
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
    private OrdersBase(SellProducts products)
    {
        mOrders.add(products);
    }

    public List<SellProducts> getmOrders() {
        return mOrders;
    }

    public boolean InsertOrder(SellProducts products) {

        if (mOrders.size() > 0) {
            if (this.mOrders.get(products.getId() - 1).getName().equals(products.getName())) {
                return false;
            } else {

                this.mOrders.add(products.getId(), products);
                return true;
            }
        }
        else
        {
            this.mOrders.add(products.getId(), products);
            return true;
        }

    }


    public static OrdersBase getInstance(){
        if(instance == null)
        {
            instance = new OrdersBase();
        }
        return instance;
    }

    public void dispose() {
        clearTempUser();
    }

    private void clearTempUser() {
        mOrders.clear();

    }

}
