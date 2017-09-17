package com.bignerdranch.android.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
      new Question(R.string.question_food, false),
      new Question(R.string.question_car, true),
      new Question(R.string.question_video_games, false),
      new Question(R.string.question_Dog, true),
      new Question(R.string.question_computer, true),
      new Question(R.string.question_fishing, true),
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);

        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(false);
            }
        });
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
    }
    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId=0;
        //Toast toast = new Toast(getApplicationContext());

        if(userPressedTrue == answerIsTrue){
             messageResId = R.string.correct_toast;
             LayoutInflater inflater = getLayoutInflater();
             View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.toast_layout_correct));
             TextView text = (TextView) layout.findViewById(R.id.text_correct);
             text.setText(messageResId);
             Toast toast = new Toast(getApplicationContext());
             toast.setDuration(Toast.LENGTH_SHORT);
             toast.setGravity(Gravity.TOP,0,160);
             toast.setView(layout);
             toast.show();
        }else{
               messageResId = R.string.incorrect_toast;
               LayoutInflater inflater = getLayoutInflater();
               View layout = inflater.inflate(R.layout.custom_toast_incorrect,(ViewGroup) findViewById(R.id.toast_layout_incorrect));
               TextView text = (TextView) layout.findViewById(R.id.text_incorrect);
               text.setText(messageResId);
               Toast toast = new Toast(getApplicationContext());
               toast.setDuration(Toast.LENGTH_SHORT);
               toast.setGravity(Gravity.TOP,0,160);
               toast.setView(layout);
               toast.show();
        }


    }

}
