package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.example.quiz.SetsActivity.category_id;
import static java.lang.String.valueOf;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView ques, quesCount, timer;
    private Button option1, option2, option3, option4;
    private List<Question> questionList;
    int quesNumber;
    private CountDownTimer time;
    private int Score;
    private FirebaseFirestore firestore;
    private int setNo;
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        ques = findViewById(R.id.textQues);
        quesCount = findViewById(R.id.QuesNum);
        timer = findViewById(R.id.Count);

        option1 = findViewById(R.id.btnOp1);
        option2 = findViewById(R.id.btnOp2);
        option3 = findViewById(R.id.btnOp3);
        option4 = findViewById(R.id.btnOp4);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);


        questionList = new ArrayList<>();


        loadingDialog=new Dialog(QuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        firestore=FirebaseFirestore.getInstance();
        setNo=getIntent().getIntExtra("SETNO",1);

        getQuestionList();

        Score = 0;


    }

    private void getQuestionList() {
        questionList.clear();

        firestore.collection("QUIZ").document("CAT"+ valueOf(category_id))
                .collection("SET"+ valueOf(setNo)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                        {
                            docList.put(doc.getId(),doc);
                        }

                        QueryDocumentSnapshot quesListDoc  = docList.get("QUESTIONS_LIST");

                        String count = quesListDoc.getString("COUNT");

                        for(int i=0; i < Integer.valueOf(count); i++)
                        {
                            String quesID = quesListDoc.getString("Q" + valueOf(i+1) + "_ID");

                            QueryDocumentSnapshot quesDoc = docList.get(quesID);

                            questionList.add(new Question(
                                    quesDoc.getString("QUESTION"),
                                    quesDoc.getString("A"),
                                    quesDoc.getString("B"),
                                    quesDoc.getString("C"),
                                    quesDoc.getString("D"),
                                    Integer.valueOf(quesDoc.getString("ANSWER"))
                            ));

                        }

                        setQuestion();

                        loadingDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });


    }


        //firestore.collection("QUIZ").document("CAT"+ String.valueOf(category_id))
               // .collection("SET"+ String.valueOf(setNo))
               // .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

    private void setQuestion() {
try {
    timer.setText(valueOf(20));
    ques.setText(questionList.get(0).getQuestion());
    option1.setText(questionList.get(0).getOptionA());
    option2.setText(questionList.get(0).getOptionB());
    option3.setText(questionList.get(0).getOptionC());
    option4.setText(questionList.get(0).getOptionD());

    quesCount.setText(valueOf(1) + "/" + valueOf(questionList.size()));

    startTimer();

    quesNumber = 0;
}catch (ArrayIndexOutOfBoundsException e){
    Log.d("fgdfgdfg", "setQuestion: "+e);
}

    }

    private void startTimer() {

        time = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timer.setText(valueOf(millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                changeQuestion();

            }

        };
        time.start();

    }

    @Override
    public void onClick(View v) {

       int selectedOption= 0;

        switch (v.getId()) {
            case R.id.btnOp1:
                selectedOption = 1;
                break;
            case R.id.btnOp2:
                selectedOption =2;
                break;
            case R.id.btnOp3:
                selectedOption =3;
                break;
            case R.id.btnOp4:
                selectedOption = 4;
                break;
            default:
        }
        time.cancel();

       checkAnswer((selectedOption), v);


    }

    private void checkAnswer(int selectedOption, View view)
    {

        if(selectedOption == questionList.get(quesNumber).getCorrectAns())
        {
            //Right Answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            Score++;

        }
        else
        {
            //Wrong Answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

           switch (questionList.get(quesNumber).getCorrectAns())
            {

                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                   break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

            }

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();

            }
        }, 1000);


    }

    private void changeQuestion() {
        if (quesNumber < questionList.size() - 1) {

            quesNumber++;

            playAnim(ques, 0, 0);
            playAnim(option1, 0, 1);
            playAnim(option2, 0, 2);
            playAnim(option3, 0, 3);
            playAnim(option4, 0, 4);

            quesCount.setText(valueOf(quesNumber + 1) + "/" + valueOf(questionList.size()));
            timer.setText(valueOf(20));
            startTimer();


        } else {
            Intent i = new Intent(QuestionActivity.this, ScoreActivity.class);
            i.putExtra("SCORE", valueOf(Score) + "/" + valueOf(questionList.size()));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            //QuestionActivity.this.finish();
        }

    }

    private void playAnim(View v, final int value, int viewNum) {

        v.animate().alpha(value).scaleX(value).scaleY(value).setDuration(1000)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        if (value == 0) {
                            switch (viewNum) {
                                case 0:
                                    ((TextView) v).setText(questionList.get(quesNumber).getQuestion());
                                    break;
                                case 1:
                                    ((Button) v).setText(questionList.get(quesNumber).getOptionA());
                                    break;
                                case 2:
                                    ((Button) v).setText(questionList.get(quesNumber).getOptionB());
                                    break;
                                case 3:
                                    ((Button) v).setText(questionList.get(quesNumber).getOptionC());
                                    break;
                                case 4:
                                    ((Button) v).setText(questionList.get(quesNumber).getOptionD());
                                    break;
                            }

                            if (viewNum != 0)
                                ((Button) v).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6dd5ed")));


                            playAnim(v, 1, viewNum);

                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        time.cancel();
    }
}