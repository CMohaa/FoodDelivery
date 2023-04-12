package com.rova.g3an;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.rova.g3an.Base.OrdersBase;
import com.rova.g3an.Utils.MultiLineRadioGroup;
import com.rova.g3an.models.SellProducts;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private List<SellProducts> products_list;
    private TextView no_of_items;
    private TextView total_amount;
    private TextView delivery_date;
    MultiLineRadioGroup main_activity_multi_line_radio_group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        total_amount = findViewById(R.id.total_amount);
        no_of_items = findViewById(R.id.no_of_items);
        delivery_date = findViewById(R.id.delivery_date);
        products_list = OrdersBase.getInstance().getmOrders();
        double total = 0;
        for (int i = 0; i < products_list.size() ; i++)
        {
            total +=products_list.get(i).getPrice();
        }
        total_amount.setText(String.valueOf(total));
        no_of_items.setText(String.valueOf(products_list.size()));
        /*
        Calendar calander = Calendar.getInstance();
        int cDay = calander.get(Calendar.DAY_OF_MONTH);
        int cMonth = calander.get(Calendar.MONTH) + 1;
        int cYear = calander.get(Calendar.YEAR);
        String Date = (cDay +" / "+ cMonth +" / "+ cYear);
        delivery_date.setText(Date);

        */

    }
}
