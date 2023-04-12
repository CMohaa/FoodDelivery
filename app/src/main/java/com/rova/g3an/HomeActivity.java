package com.rova.g3an;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rova.g3an.Activity_Admin.AddProductActivity;
import com.rova.g3an.Activity_Home.CartActivity;
import com.rova.g3an.Activity_Products.ProductTActivity;
import com.rova.g3an.Base.BaseActivity;
import com.rova.g3an.Activity_Products.ProductActivity;
import com.rova.g3an.Utils.PermUtil;
import com.rova.g3an.Utils.ProductsUI;
import com.rova.g3an.interfaces.OnContactClickListener;
import com.rova.g3an.models.Products;
import com.rova.g3an.views.ProductsAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends BaseActivity implements OnContactClickListener {

    private static final String TAG = "HomeActivity";
    //FireBase
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private String user_id;
    private FirebaseUser current_user;
    private RecyclerView recList;
    private ImageView visitingcards;
    private ImageView T_Shirt;
    private ImageView view_profile;
    private ImageView cart;

    private int products_num = 5;
    private ArrayList<Products> products_list;
    private RecyclerView products_recyclerView;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private ProductsAdapter productsAdapter;
    //



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            init();
        }
        else
        {
            init();
        }
    }
    /*
    @Override
    public void onPostCreate(@Nullable @Nullable Bundle savedInstanceState, @Nullable @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }
*/
    private void init() {


        //makeFullScreen();
        //FireBase
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        current_user = mAuth.getCurrentUser();
        //


        recList = (RecyclerView) findViewById(R.id.slider);
        view_profile = findViewById(R.id.view_profile);
        view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(loginIntent);
            }
        });
        cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(loginIntent);
            }
        });
        visitingcards = findViewById(R.id.visitingcards);
        visitingcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(HomeActivity.this, ProductActivity.class);
                productIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, "البيتزا");
                startActivity(productIntent);
            }
        });
        T_Shirt = findViewById(R.id.tshirts);
        T_Shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(HomeActivity.this , ProductActivity.class);
                productIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, "المشروبات");
                startActivity(productIntent);
            }
        });
        products_list = new ArrayList<>();
        productsAdapter = new ProductsAdapter(products_list , this);
        products_staggeredGridLayoutManager = new StaggeredGridLayoutManager(1 , LinearLayoutManager.HORIZONTAL);
        recList.setLayoutManager(products_staggeredGridLayoutManager);
        recList.setAdapter(productsAdapter);
        loadProducts();
        //




    }
    //Responsible For Adding the 3 tabs : Camera  , Home , Messages

    @Override
    public void onStart() {
        super.onStart();
        if(current_user != null) {
            HashMap<String, Object> online_map = new HashMap<>();
            online_map.put("online", (long)1);
            //online_map.put("online" , 1);
            fStore.collection("users").document(user_id).update(online_map);
        }
    }
    HashMap<String, Object> timestampCreated;
    @Override
    public void onStop() {
        super.onStop();
        if(current_user != null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            HashMap<String, Object> online_map = new HashMap<>();
            online_map.put("online" , timestamp.getTime());
            // this.timestampCreated = online_map;
            fStore.collection("users").document(user_id).update(online_map);
        }
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
    @Override
    public void onBackPressed() {

        attemptToExitIfRoot(null);

    }

    // convert the list of contact to a map of members

    @Override
    public void onContactClicked(Products contact, int position) {
        Intent loginIntent = new Intent(this, AddProductActivity.class);
        startActivity(loginIntent);
    }

    private void loadProducts() {
        products_list.clear();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //int id, String name, int quantity, float cost, float price, String src, String trader, String validity_start, String validity_end
        products_list.add(new Products(1 , "Test1" , 100 ,111 , 111,"" , "Mohamed" , "100" , "100" , "food" ,timestamp.getTime()));
        products_list.add(new Products(2 , "Test2" , 100 ,111 , 111,"" , "Mohamed" , "100" , "100", "food",timestamp.getTime()));

        productsAdapter.notifyDataSetChanged();
        //mRefreshLayout.setRefreshing(false);

    }

}
