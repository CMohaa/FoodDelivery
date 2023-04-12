package com.rova.g3an.Activity_Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rova.g3an.HomeActivity;
import com.rova.g3an.R;
import com.rova.g3an.Utils.FilePaths;
import com.rova.g3an.Utils.MediaSelector;
import com.rova.g3an.models.Products;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class AddProductActivity extends AppCompatActivity {
    private ImageView setup_img;
    private AutoCompleteTextView textView;
    private TextView add_to_product;
    private String user_id;
    private com.rengwuxian.materialedittext.MaterialEditText product_name;
    private com.rengwuxian.materialedittext.MaterialEditText product_price;
    private com.rengwuxian.materialedittext.MaterialEditText product_barcode;
    protected MediaSelector mediaSelector = new MediaSelector();
    //Firebase
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore fStore;
    //Picture
    private Bitmap compressedImageFile;
    private File new_image_file;

    //ProgressDialog
    private ProgressDialog mLoginProgress;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        //Progress Dialog
        /*
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
                */
        //set auto complete
       textView = (AutoCompleteTextView) findViewById(R.id.edit_ip);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.myarray));
        textView.setAdapter(adapter);
        //set spinner
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_ip);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(spinner.getSelectedItem().toString());
                textView.dismissDropDown();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText(spinner.getSelectedItem().toString());
                textView.dismissDropDown();
            }
        });
        mLoginProgress = new ProgressDialog(this);
        //Firebase
        fStore = FirebaseFirestore.getInstance();
        mAuth =FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();//Get_user_id
        storageReference = FirebaseStorage.getInstance().getReference();
        setup_img = findViewById(R.id.productimage);
        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.productprice);
        product_barcode = findViewById(R.id.productbarcode);
        add_to_product = findViewById(R.id.add_to_product);
        add_to_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = product_name.getText().toString();//name
                final String price = product_price.getText().toString();
                final String barcode = product_barcode.getText().toString();
                if (!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(price) &&!TextUtils.isEmpty(barcode) && getNew_image_file() != null) {
                    mLoginProgress.setTitle("Uploading Image");
                    mLoginProgress.setMessage("Please wait while we check your credentials.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    uploadFile(getNew_image_file() , name ,price , barcode);
                }
            }
        });
        setup_img.setOnClickListener(view -> mediaSelector.startChooseImageActivity(this, MediaSelector.CropType.Rectangle, result -> {
            Uri file = Uri.fromFile(new File(result));
            setNew_image_file(new File(file.getPath()));
            try{
                setup_img.setImageURI(Uri.fromFile(getNew_image_file()));
            }
            catch (Exception e) {
                Toast.makeText(this, "Error" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
    }
    public void uploadFile(File file , final String user_name  , final String price ,  final String barcode) {


        // random uid.
        // this is used to generate an unique folder in which
        // upload the file to preserve the filename
        Uri uri = Uri.fromFile(file);
        //Uri new_uri = ImageCompressorUltra.compressImage(getContentResolver() , uri);

        File new_image_file = new File(uri.getPath());
        try {
            compressedImageFile = new Compressor(AddProductActivity.this)//Compressor Library
                    .setMaxWidth(100)
                    .setMaxHeight(100)
                    .setQuality(2)

                    .compressToBitmap(new_image_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FilePaths filePaths = new FilePaths();
        final String randomname = UUID.randomUUID().toString();//generic randomname
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] thumb_data = baos.toByteArray();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        UploadTask uploadTask = storageReference.child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/products_pics/").child(randomname + ".jpg")
                .putBytes(thumb_data);//upload image after Compressed
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                storageReference.child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/products_pics/").child(randomname + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        String download_thumb_uri = uri.toString();
                        Products products = new Products();
                        products.setName(user_name);
                        products.setPrice(Double.parseDouble(price));
                        products.setType(textView.getText().toString());
                        products.setThumb_image(download_thumb_uri);
                        products.setBarcode(Long.parseLong(barcode));
                        /*
                        HashMap<String, Object> userMap_ = new HashMap<>();
                        userMap_.put("name", user_name);
                        userMap_.put("cost", "");
                        userMap_.put("price", price);
                        userMap_.put("type", "");
                        userMap_.put("trader", "");
                        userMap_.put("validity_start", "");
                        userMap_.put("validity_end", "");
                        userMap_.put("thumb_image", download_thumb_uri);
                        */
                        fStore.collection("products").add(products).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                if (task.isSuccessful()) {

                                    mLoginProgress.dismiss();
                                    Toast.makeText(AddProductActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(SetupActivity.this, "The User Settings Are Updated", Toast.LENGTH_SHORT).show();
                                    Intent mIntent = new Intent(AddProductActivity.this, HomeActivity.class);
                                    startActivity(mIntent);
                                    finish();
                                } else {
                                    mLoginProgress.hide();
                                    String e = task.getException().getMessage();
                                    Toast.makeText(AddProductActivity.this, "Database Error" + e, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            mediaSelector.handleResult(this, requestCode, resultCode, data);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error" +e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public File getNew_image_file() {
        return new_image_file;
    }

    public void setNew_image_file(File new_image_file) {
        this.new_image_file = new_image_file;
    }
}
