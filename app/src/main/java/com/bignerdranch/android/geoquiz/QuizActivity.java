package com.bignerdranch.android.geoquiz;


import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private Button mTrueButton;//reference to true button
    private Button mFalseButton;//reference to false button
    private ImageButton mNextButton;//reference to next button
    private ImageButton mPrevButton;//reference to previous button
    private ImageButton mT2SButton;
    private TextView mQuestionTextView;//reference to text view of the question
    private TextView mQuizScoreView;
    private Button mViewScoreButton;
    private Button mResetQuiz;
    private double numCorrect = 0;
    private double userScore = 0;

    private TextToSpeech mQuestionTTS;
    private int DATA_CHECK_CODE = 0;


    //the following holds an array of question objects, each question has 2 pieces of data, a text for the question and whether the answer is true or false
    private Question[] mQuestionBank = new Question[] {
      new Question(R.string.question_food, false, false),
      new Question(R.string.question_car, true, false),
      new Question(R.string.question_video_games, false, false),
      new Question(R.string.question_Dog, true, false),
      new Question(R.string.question_computer, true, false),
      new Question(R.string.question_fishing, true,false),
    };


    //initialized variable to keep track of array of objects
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);//view the user sees when application is open

        if(savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);//holds the physical reference to question in activit_quiz.xml
        mQuizScoreView = (TextView) findViewById(R.id.score_quiz);
        mViewScoreButton = (Button) findViewById(R.id.ViewScore);
        mViewScoreButton.setVisibility(View.GONE);
        mResetQuiz = (Button) findViewById(R.id.ResetQuiz);
        mResetQuiz.setVisibility(View.GONE);
        mTrueButton = (Button) findViewById(R.id.true_button);//holds the physical reference to the true button in main activity
        mFalseButton = (Button) findViewById(R.id.false_button);//physical reference to false button in main activity
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mFalseButton.setEnabled(false);
                mQuestionBank[mCurrentIndex].setQuestionAnswered(true);
                checkAnswer(true);//if true button is clicked, check if answer is true
                if(mCurrentIndex == 5){
                    if(mQuestionBank[mCurrentIndex].getQuestionAnswered() == true) {
                        mTrueButton.setVisibility(View.GONE);
                        mFalseButton.setVisibility(View.GONE);
                        mNextButton.setVisibility(View.GONE);
                        mPrevButton.setVisibility(View.GONE);
                        mViewScoreButton.setVisibility(View.VISIBLE);
                        mResetQuiz.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mTrueButton.setEnabled(false);
                mQuestionBank[mCurrentIndex].setQuestionAnswered(true);
                checkAnswer(false);//if false button is clicked, check if answer is false
                if(mCurrentIndex == 5){
                    if(mQuestionBank[mCurrentIndex].getQuestionAnswered() == true) {
                        mTrueButton.setVisibility(View.GONE);
                        mFalseButton.setVisibility(View.GONE);
                        mNextButton.setVisibility(View.GONE);
                        mPrevButton.setVisibility(View.GONE);
                        mViewScoreButton.setVisibility(View.VISIBLE);
                        mResetQuiz.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mNextButton = (ImageButton) findViewById(R.id.next_button);//physical reference to next button in main activity
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;//if next button clicked, change index to match that of next in array

                if(mQuestionBank[mCurrentIndex].getQuestionAnswered() == true){
                    mFalseButton.setEnabled(false);
                    mTrueButton.setEnabled(false);
                }else{
                    mFalseButton.setEnabled(true);
                    mTrueButton.setEnabled(true);
                }
                updateQuestion();//update the user interface
            }
        });
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);//holds physical reference to previous button
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mCurrentIndex==0){//for some reason it wasnt reading modulus of negative numbers properly so i had to check if current index was 0, if index is 0, set previous index to last element in array
                    mCurrentIndex=mQuestionBank.length-1;
                }else {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;//navigates to the previous index for any index not 0
                }

                if(mQuestionBank[mCurrentIndex].getQuestionAnswered() == true){
                    mFalseButton.setEnabled(false);
                    mTrueButton.setEnabled(false);
                }else{
                    mFalseButton.setEnabled(true);
                    mTrueButton.setEnabled(true);
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
        mViewScoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                userScore = (numCorrect / mQuestionBank.length)*100;
                mT2SButton.setVisibility(View.GONE);
                viewScore();
            }
        });
        mResetQuiz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                mT2SButton.setVisibility(View.VISIBLE);

                for(int i = 0; i<mQuestionBank.length;i++){
                    mQuestionBank[i].setQuestionAnswered(false);
                }
                mTrueButton.setVisibility(View.VISIBLE);
                mFalseButton.setVisibility(View.VISIBLE);
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
                mNextButton.setVisibility(View.VISIBLE);
                mPrevButton.setVisibility(View.VISIBLE);
                mViewScoreButton.setVisibility(View.GONE);
                mResetQuiz.setVisibility(View.GONE);
                mQuestionTextView.setVisibility(View.VISIBLE);
                mQuizScoreView.setVisibility(View.GONE);
                mCurrentIndex = 0;
                numCorrect = 0;
                userScore = 0;
                updateQuestion();
            }
        });
        mT2SButton = (ImageButton) findViewById(R.id.Text2Speech);
        mT2SButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mQuestionTTS.speak(getString(mQuestionBank[mCurrentIndex].getTextResId()), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, DATA_CHECK_CODE);
        updateQuestion();// so that the first question shows without clicking any buttons.
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"onStart() called");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"onResume() called");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() called");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop() called");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }
    //updates the user interface
    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();

        mQuestionTextView.setText(question);
    }
    private void viewScore(){
        String score = Double.toString(userScore);
        mQuestionTextView.setVisibility(View.GONE);
        mFalseButton.setEnabled(false);
        mQuizScoreView.setVisibility(View.VISIBLE);
        mQuizScoreView.setText("Your score is: "+score+"%");
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId=0;
        //Toast toast = new Toast(getApplicationContext());

        if(userPressedTrue == answerIsTrue){//everything below is for my custom toast messages.
             numCorrect++;
             messageResId = R.string.correct_toast;
            mQuestionTTS.speak("Correct, Great Job", TextToSpeech.QUEUE_FLUSH, null);
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
               mQuestionTTS.speak("Incorrect, terrible", TextToSpeech.QUEUE_FLUSH, null);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                mQuestionTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            mQuestionTTS.setLanguage(Locale.US);
        }else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}
