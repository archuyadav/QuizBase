package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView score;
    private Button Done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score=findViewById(R.id.textScore);
        Done=findViewById(R.id.btnDone);

        String scoreString=getIntent().getStringExtra("SCORE");
        score.setText(scoreString);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ScoreActivity.this,MainActivity.class);
                ScoreActivity.this.startActivity(i);
                ScoreActivity.this.finish();
            }
        });

    }

    public static class Question {

        String questions;
        String optionA;
        String optionB;
        String optionC;
        String optionD;
      String correctAns;

        public Question(String questions, String optionA, String optionB, String optionC, String optionD, String correctAns) {
            this.questions = questions;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
            this.correctAns = correctAns;
        }

        public String getQuestions() {
            return questions;
        }

        public String getOptionA() {
            return optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public String getCorrectAns() {
            return correctAns;
        }
    }
}