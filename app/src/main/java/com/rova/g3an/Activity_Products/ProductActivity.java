package com.rova.g3an.Activity_Products;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rova.g3an.Base.BaseActivity;
import com.rova.g3an.IndividualProductActivity;

import com.rova.g3an.Utils.ProductsUI;
import com.rova.g3an.R;
import com.rova.g3an.interfaces.OnProductClickListener;
import com.rova.g3an.models.Products;
import com.rova.g3an.views.SellProductsAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductActivity extends BaseActivity implements OnProductClickListener {

    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private String user_id;
    private FirebaseUser current_user;
    private RecyclerView recList;
    private String type;

    private int products_num = 5;
    private ArrayList<Products> products_list;
    private RecyclerView products_recyclerView;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private SellProductsAdapter productsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Product Type");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            init();
        }
        else
        {
            init();
        }
    }
    private void init() {
        //makeFullScreen();
        //FireBase
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        current_user = mAuth.getCurrentUser();
        //

        type = getIntent().getStringExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE);
        recList = (RecyclerView) findViewById(R.id.recyclerview);

        products_list = new ArrayList<>();
        productsAdapter = new SellProductsAdapter(products_list , this);
        products_staggeredGridLayoutManager = new StaggeredGridLayoutManager(1 , LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(products_staggeredGridLayoutManager);
        recList.setAdapter(productsAdapter);
       // loadProducts();
        getData();
        //

    }
    //Responsible For Adding the 3 tabs : Camera  , Home , Messages

    public void getAllPosts()
    {
        products_list.clear();

        if(mAuth.getCurrentUser() != null)
        {
            recList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                    super.onScrolled(recyclerView, dx, dy);


                }
            });

            Query f_query = fStore.collection("products" ).whereEqualTo("type" , type);
            f_query.addSnapshotListener(this ,new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {


                        if (doc.getType() == DocumentChange.Type.ADDED) {


                            final String blogPostId = doc.getDocument().getId();
                            /*
                            double price =  Double.parseDouble(doc.getDocument().getString("price"));
                            String name =  doc.getDocument().getString("name");
                            String thumb_image =  doc.getDocument().getString("thumb_image");
                            products_list.add(new SellProducts(name, 0 , thumb_image));
                            */
                            //thumb_image
                            Products blogPost = doc.getDocument().toObject(Products.class).withid(blogPostId);
                            products_list.add(blogPost);
                            productsAdapter.notifyDataSetChanged();


                        }
                    }


                }
            });


        }









    }

    public void getData(){
        try {
            //swipeRefreshLayout.setRefreshing(true);
            getAllPosts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProductClicked(Products products, int position) {
        Intent loginIntent = new Intent(this, IndividualProductActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) products);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, products.getThumb_image());
        startActivity(loginIntent);
    }
}
