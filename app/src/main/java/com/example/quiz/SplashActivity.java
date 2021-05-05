package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private ImageView img1, img2;
    private TextView textView;
    Animation animation1, animation2, animation3;

    String state = "";

    public static List<String> catList = new ArrayList<>();
    private FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        textView = findViewById(R.id.textQuiz);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enetr_from_right);
        img2.setAnimation(animation2);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter_from_left);
        img1.setAnimation(animation1);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_animation_right);
        img2.setAnimation(animation2);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_animation_left);
        img1.setAnimation(animation1);
        animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_to_bottom);
        textView.setAnimation(animation3);


        fireStore = FirebaseFirestore.getInstance();

       new Thread() {
            public void run() {

                // Thread will sleep for 5 seconds
                loadData();

                // After 5 seconds redirect to another intent

            }
        }.start();


    }
    private void loadData() {


        catList.clear();

        fireStore.collection("QUIZ").document("Catagories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    DocumentSnapshot doc=task.getResult();

                    if(doc.exists()){
                        long count=(long)doc.get("COUNT");

                        for (int i=1;i<=count;i++){
                          String catName=doc.getString("CAT"+String.valueOf(i));

                          catList.add(catName);
                        }


                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        SplashActivity.this.finish();

                    }else{
                        Toast.makeText(SplashActivity.this, "No Catagory Document Exist!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }else{

                    Toast.makeText(SplashActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

}