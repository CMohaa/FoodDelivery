package com.rova.g3an;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.rova.g3an.views.DessertAdapter;


public class AboutActivity extends AppCompatActivity {

    private RecyclerView recList;
    private ImageView visitingcards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        recList = (RecyclerView) findViewById(R.id.slider);
        visitingcards = findViewById(R.id.visitingcards);
        visitingcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(AboutActivity.this, IndividualProductActivity.class);
                startActivity(loginIntent);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recList.setLayoutManager(llm);

        DessertAdapter adapter = new DessertAdapter();
        recList.setAdapter(adapter);
    }




}