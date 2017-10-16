package com.bignerdranch.android.geoquiz;

/**
 * Created by Austin on 9/16/2017.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mQuestionAnswered;

    public Question(int textResId, boolean answerTrue, boolean QuestionAnswered){
        mTextResId=textResId;
        mAnswerTrue=answerTrue;
        mQuestionAnswered = QuestionAnswered;
    }

    public int getTextResId() {
        return mTextResId;
    }
    public boolean getQuestionAnswered(){
        return mQuestionAnswered;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public void setQuestionAnswered(boolean QuestionAnswered){mQuestionAnswered = QuestionAnswered;}
}
