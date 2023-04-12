package com.rova.g3an.Auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rova.g3an.Base.BaseActivity;
import com.rova.g3an.R;
import com.rova.g3an.WelcomeActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    //ProgressDialog
    private ProgressDialog mLoginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mLoginProgress = new ProgressDialog(this);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email" , "user_location" , "user_gender" , "user_photos" , "user_birthday" , "user_friends");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    signIn();
            }
        });
        //printKeyHash();
    }

    private void signIn() {
        mLoginProgress.setTitle("Purchasing....");
        mLoginProgress.setMessage("Please wait while we check your credentials.");
        mLoginProgress.setCanceledOnTouchOutside(false);
        mLoginProgress.show();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {


                handleFacebookAccessToken(loginResult.getAccessToken());
                /*
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.e("response: ", response + "");
                                try {
                                    String id = object.getString("id").toString();
                                    String email = object.getString("email").toString();
                                    String name = object.getString("name").toString();
                                    String profilePicUrl = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large";
                                    Log.d("imageFB", profilePicUrl);
                                    Log.d("FB_ID:", id);


                                    //checkFBLogin(id, email, name, profilePicUrl);
                                    handleFacebookAccessToken(loginResult.getAccessToken() , id, email, name, profilePicUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                finish();
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,friends,likes,hometown,education,work");
                request.setParameters(parameters);
                request.executeAsync();
                */



            }

            @Override
            public void onCancel() {
                mLoginProgress.hide();
            }

            @Override
            public void onError(FacebookException error) {
                mLoginProgress.hide();
            }
        });
    }

    private void checkFBLogin(String id, String email, String name, String profilePicUrl) {
        Toast.makeText(LoginActivity.this, "Welcome : " + name , Toast.LENGTH_SHORT).show();

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                mLoginProgress.hide();
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(final AuthResult authResult) {

                final String device_token = FirebaseInstanceId.getInstance().getToken();
                final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInf = wifiMan.getConnectionInfo();
                int ipAddress = wifiInf.getIpAddress();
                final String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
                //generic "in between two numbers"
                Random r = new Random();
                int Low = 10;
                int High = 100;
                final int random_number = r.nextInt(High-Low) + Low;
                fStore.collection("users").document(authResult.getUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(!task.getResult().exists())
                            {
                                Map<String, Object> userMap_ = new HashMap<>();
                                // ----------------------------------------
                                userMap_.put("user_id", authResult.getUser().getUid());
                                userMap_.put("username", authResult.getUser().getDisplayName() + random_number);
                                userMap_.put("email" , authResult.getUser().getEmail());
                                userMap_.put("online",  timestamp.getTime());
                                userMap_.put("token_id" , device_token);
                                userMap_.put("user_ip" , ip);
                                userMap_.put("role" , 0);//0
                                // ----------------------------------------
                                userMap_.put("following", 0);
                                userMap_.put("followers", 0);
                                userMap_.put("posts", 0);
                                userMap_.put("name", authResult.getUser().getDisplayName());
                                userMap_.put("created_date" ,timestamp.getTime());
                                userMap_.put("brith_date", "");
                                userMap_.put("status", "i'm available.");
                                //userMap_.put("image", authResult.getUser().getPhotoUrl());
                                //userMap_.put("thumb_image", authResult.getUser().getPhotoUrl());
                                fStore.collection("users").document(authResult.getUser().getUid()).set(userMap_);
                            }
                        }
                    }
                });



                mLoginProgress.dismiss();
                startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode ,data);

    }
    public ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading....");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void printKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature  : packageInfo.signatures)
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KEYHASH", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
