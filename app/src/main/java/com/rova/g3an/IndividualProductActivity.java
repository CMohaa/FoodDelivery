package com.rova.g3an;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.rova.g3an.Activity_Home.NotificationActivity;
import com.rova.g3an.Base.BaseActivity;
import com.rova.g3an.Base.OrdersBase;
import com.rova.g3an.Activity_Products.ProductActivity;
import com.rova.g3an.Utils.ProductsUI;
import com.rova.g3an.models.Orders;
import com.rova.g3an.models.Products;
import com.rova.g3an.models.SellProducts;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class IndividualProductActivity extends BaseActivity {
    private int quantity = 1;
    EditText quantityProductPage;
    LinearLayout btn_notification;
    TextView btn_buy;
    TextView btn_add_to_cart;
    private ImageView productimage;
    private TextView productname;
    private TextView productprice;
    private TextView productdesc;
    private Products sellProducts;
    private String thumb_image;

    //Firebase
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore fStore;
    private AuthResult authResult;
    private String user_id;
    List<Products> sellCART;
    //ProgressDialog
    private ProgressDialog mLoginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product);

        sellProducts = (Products) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_PRODUCTS_LIST);
        thumb_image = getIntent().getStringExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mLoginProgress = new ProgressDialog(this);
        //Firebase
        fStore = FirebaseFirestore.getInstance();
        mAuth =FirebaseAuth.getInstance();
        FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        user_id = CurrentUser.getUid();
        productimage = findViewById(R.id.productimage);
        productname = findViewById(R.id.productname);
        productprice = findViewById(R.id.productprice);
        productdesc = findViewById(R.id.productdesc);

        quantityProductPage = findViewById(R.id.quantityProductPage);
        quantityProductPage.setText("1");
        setDetails();
        btn_buy = findViewById(R.id.buy_now);
        btn_add_to_cart = findViewById(R.id.add_to_cart);
        btn_notification = findViewById(R.id.btn_notification);
        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(IndividualProductActivity.this, NotificationActivity.class);
                startActivity(loginIntent);
                finish();//Don't Return AnyMore TO the last page
            }
        });
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SellProducts products = new SellProducts();
                products.setId(OrdersBase.getInstance().getmOrders().size());
                products.setName(sellProducts.getName());
                products.setPrice(sellProducts.getPrice());
                products.setQuantity(Double.parseDouble(quantityProductPage.getText().toString()));
                products.setType(sellProducts.getType());
                products.setThumb_image(sellProducts.getThumb_image());
                products.setBarcode(sellProducts.getBarcode());
                boolean inserted =  OrdersBase.getInstance().InsertOrder(products);
                if(inserted)
                {
                    Toast.makeText(IndividualProductActivity.this, "Succ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(IndividualProductActivity.this, "Fall", Toast.LENGTH_SHORT).show();
                }
                //sellCART = OrdersBase.getInstance().getmOrders();
                //Toast.makeText(IndividualProductActivity.this, String.valueOf( sellCART.size()), Toast.LENGTH_SHORT).show();
                /*
                Intent loginIntent = new Intent(IndividualProductActivity.this, CartActivity.class);
                startActivity(loginIntent);
                finish();//Don't Return AnyMore TO the last page
                */
            }
        });
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLoginProgress.setTitle("Purchasing....");
                mLoginProgress.setMessage("Please wait while we check your credentials.");
                mLoginProgress.setCanceledOnTouchOutside(false);
                mLoginProgress.show();
                fStore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String name = task.getResult().getString("name");
                        Calendar calander = Calendar.getInstance();
                        int cDay = calander.get(Calendar.DAY_OF_MONTH);
                        int cMonth = calander.get(Calendar.MONTH) + 1;
                        int cYear = calander.get(Calendar.YEAR);
                        //String Date = (cDay +" / "+ cMonth +" / "+ cYear).toString();

                        DateFormat df = new SimpleDateFormat("dd@MM@yyyy");
                        String date = df.format(Calendar.getInstance().getTime());
                        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        String time = String.valueOf(timestamp.getTime());
                        SellProducts products = new SellProducts();
                        products.setName(sellProducts.getName());
                        products.setPrice(sellProducts.getPrice());
                        products.setQuantity(Double.parseDouble(quantityProductPage.getText().toString()));
                        products.setType(sellProducts.getType());
                        products.setThumb_image(sellProducts.getThumb_image());
                        products.setOwner_name(name);
                        products.setOwnder_id(user_id);

                        Orders orders = new Orders();
                        //orders.setId(Integer.parseInt(user_id));
                        orders.setName(name);
                        fStore.collection("orders").document(user_id).collection("Daily_orders").document(time).set(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fStore.collection("orders").document(user_id).
                                        collection("Daily_orders").document(time).collection("products").add(products).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        mLoginProgress.dismiss();
                                        //Toast.makeText(SetupActivity.this, "The User Settings Are Updated", Toast.LENGTH_SHORT).show();
                                        Intent loginIntent = new Intent(IndividualProductActivity.this, ProductActivity.class);
                                        startActivity(loginIntent);
                                        finish();//Don't Return AnyMore TO the last page

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mLoginProgress.hide();
                                        Toast.makeText(IndividualProductActivity.this, "Database Error" + e, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                    }
                });


                /*
                  if (task.isSuccessful()) {

                            mLoginProgress.dismiss();
                            Toast.makeText(IndividualProductActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(SetupActivity.this, "The User Settings Are Updated", Toast.LENGTH_SHORT).show();
                            Intent loginIntent = new Intent(IndividualProductActivity.this, ProductActivity.class);
                            startActivity(loginIntent);
                            finish();//Don't Return AnyMore TO the last page
                        } else {
                            mLoginProgress.hide();
                            String e = task.getException().getMessage();
                            Toast.makeText(IndividualProductActivity.this, "Database Error" + e, Toast.LENGTH_SHORT).show();
                        }
                 */

            }
        });

    }

    private void setDetails() {
        //Toast.makeText(this, thumb_image, Toast.LENGTH_SHORT).show();
        Glide.with(this)
                .load(thumb_image) // image url
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_shop) // any placeholder to load at start
                        .error(R.drawable.ic_shop)  // any image in case of error
                        .override(200, 200) // resizing
                        .centerCrop())
                .into(productimage);  // imageview object
        productname.setText(sellProducts.getName());
        productprice.setText(String.valueOf(sellProducts.getPrice()));
        //productdesc.setText(sellProducts.getName());

    }

    public void decrement(View view) {
        if (quantity > 1) {
            quantity--;
            quantityProductPage.setText(String.valueOf(quantity));
        }
    }

    public void increment(View view) {
        if (quantity < 500) {
            quantity++;
            quantityProductPage.setText(String.valueOf(quantity));
        } else {

        }
    }
    //check that product count must not exceed 500
    TextWatcher productcount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (quantityProductPage.getText().toString().equals("")) {
                quantityProductPage.setText("0");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //none
            if (Integer.parseInt(quantityProductPage.getText().toString()) >= 500) {

            }
        }

    };
}
