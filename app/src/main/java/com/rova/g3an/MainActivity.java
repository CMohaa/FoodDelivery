package com.rova.g3an;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;
import com.rova.g3an.Auth.LoginActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static int TIME_OUT = 3000; //Time to launch the another activity
    private FirebaseAuth mAuth;

    private FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void sendtoLogin()
    {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();//Don't Return AnyMore TO the last page



    }

    @Override
    protected void onStart() {
        super.onStart();


        fStore = FirebaseFirestore.getInstance();
        mAuth =FirebaseAuth.getInstance();
        FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        /*
        final ComponentName onBootReceiver = new ComponentName(getApplication().getPackageName(), MyBroadcastReceiver.class.getName());
        if(getPackageManager().getComponentEnabledSetting(onBootReceiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
            getPackageManager().setComponentEnabledSetting(onBootReceiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        Log.e(TAG, "Notification :  getPackageManager().setComponentEnabledSetting(onBootReceiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED");
        */
        if (CurrentUser == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendtoLogin();
                }
            }, TIME_OUT);// Time Out >> Declare A Function After countdown end

        } else {


            //Start
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    sendtoHome();
                }
            }, TIME_OUT);
            //End


        }
    }

    private void sendtoHome() {

        final String device_token = FirebaseInstanceId.getInstance().getToken();//Device Token For Each Device
        String current_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid());//Current_user_id
        FirebaseMessaging.getInstance().subscribeToTopic("promotional_messages");
        Map<String , Object > tokenMap = new HashMap<>();//Map For put the data
        tokenMap.put("token_id" , device_token);

        fStore.collection("users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(loginIntent);
                finish();//Don't Return AnyMore TO the last page
            }
        });

    }
}
