package com.rova.g3an.Activity_Home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rova.g3an.Base.BaseActivity;
import com.rova.g3an.OrderDetailsActivity;
import com.rova.g3an.Base.OrdersBase;
import com.rova.g3an.R;
import com.rova.g3an.Utils.PermUtil;
import com.rova.g3an.interfaces.OnCartClickListener;
import com.rova.g3an.models.SellProducts;
import com.rova.g3an.views.CartAdapter;

import java.util.List;


public class CartActivity extends BaseActivity implements OnCartClickListener {
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private String user_id;
    private FirebaseUser current_user;
    private RecyclerView recList;

    private TextView subTotal;
    private List<SellProducts> products_list;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private CartAdapter cartAdapter;

    TextView text_action_bottom2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Cart");
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            init();
        }
        else
        {
            init();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    private void init() {
        //FireBase
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        current_user = mAuth.getCurrentUser();
        //

        recList = (RecyclerView) findViewById(R.id.shoppingCartRecycleView);
        products_list = OrdersBase.getInstance().getmOrders();
        cartAdapter = new CartAdapter(products_list , this);
        products_staggeredGridLayoutManager = new StaggeredGridLayoutManager(1 , LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(products_staggeredGridLayoutManager);
        recList.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        //loadProducts();
        subTotal = findViewById(R.id.subTotal);
        double total = 0;
        for (int i = 0; i < products_list.size() ; i++)
        {
            total +=products_list.get(i).getPrice();
        }
        subTotal.setText(String.valueOf(total));
        text_action_bottom2 = findViewById(R.id.text_action_bottom2);
        text_action_bottom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, OrderDetailsActivity.class);
                //intent.putExtra("subTotal",subTotal.getText());

                startActivity(intent);
                finish();//Don't Return AnyMore TO the last page
            }
        });
    }


    @Override
    public void onProductClicked(SellProducts contact, int position) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //Toast.makeText(MultiEditorActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
