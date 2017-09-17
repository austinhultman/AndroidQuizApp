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
    private Button mTrueButton;//reference to true button
    private Button mFalseButton;//reference to false button
    private Button mNextButton;//reference to next button
    private Button mPrevButton;//reference to previous button
    private TextView mQuestionTextView;//reference to text view of the question

    //the following holds an array of question objects, each question has 2 pieces of data, a text for the question and whether the answer is true or false
    private Question[] mQuestionBank = new Question[] {
      new Question(R.string.question_food, false),
      new Question(R.string.question_car, true),
      new Question(R.string.question_video_games, false),
      new Question(R.string.question_Dog, true),
      new Question(R.string.question_computer, true),
      new Question(R.string.question_fishing, true),
    };

    //initialized variable to keep track of array of objects
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);//view the user sees when application is open

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);//holds the physical reference to question in activit_quiz.xml

        mTrueButton = (Button) findViewById(R.id.true_button);//holds the physical reference to the true button in main activity

        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkAnswer(true);//if true button is clicked, check if answer is true
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);//physical reference to false button in main activity
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(false);//if false button is clicked, check if answer is false
            }
        });
        mNextButton = (Button) findViewById(R.id.next_button);//physical reference to next button in main activity
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;//if next button clicked, change index to match that of next in array
                updateQuestion();//update the user interface
            }
        });
        mPrevButton = (Button) findViewById(R.id.prev_button);//holds physical reference to previous button
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mCurrentIndex==0){//for some reason it wasnt reading modulus of negative numbers properly so i had to check if current index was 0, if index is 0, set previous index to last element in array
                    mCurrentIndex=mQuestionBank.length-1;
                }else {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;//navigates to the previous index for any index not 0
                }
                updateQuestion();

            }
        });
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;// navigates to next index of question array
                updateQuestion();
            }
        });
        updateQuestion();// so that the first question shows without clicking any buttons.
    }
    //updates the user interface
    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId=0;
        //Toast toast = new Toast(getApplicationContext());

        if(userPressedTrue == answerIsTrue){//everything below is for my custom toast messages.
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
