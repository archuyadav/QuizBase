package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.GridView;

import com.google.firebase.firestore.core.View;

import java.util.ArrayList;
import java.util.List;

import static com.example.quiz.SplashActivity.catList;


public class CatagoryActivity extends AppCompatActivity {

    private GridView gridView;
    private RippleDrawable rippleDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CATAGORIES");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView=findViewById(R.id.gridView);






        GridAdapter adapter=new GridAdapter(catList);
        gridView.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            CatagoryActivity.this.finish();

        }
        return super.onOptionsItemSelected(item);
    }

}