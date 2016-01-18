package com.asbozh.geotrivia;


import android.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameFragment extends Fragment implements View.OnClickListener {

    private TextView tvQuestion, tvOption1, tvOption2, tvOption3, tvOption4;
    private RelativeLayout rlScoreLayout;
    private TextView tvUserName, tvCurrentQuestion, tvPoints;
    private FloatingActionButton fabAnswer;

    private int selectColor;

    private int mChosenLevel;

    private String mNickName;
    private String currentTable;
    private String correctAnswer, currentAnswer;
    private ArrayList<Integer> mQuestionsOrder;
    private int[] userAnswers = new int[30]; // array of 0 and 1 | 0 - wrong answer, 1 - correct answer
    private int totalQuestions = 30;
    private int totalSQLdbQuestions = 100;
    private int currentQuestion;
    private int points;

    SQLHandler handler;

    public interface Listener {
        void onGameFinish();
    }

    Listener mListener = null;

    public void setListener(Listener l) {
        mListener = l;
    }

    public void setChosenLevel(int chosenLevel) {
        mChosenLevel = chosenLevel;
        if (mChosenLevel == 1) {
            shuffleQuestions();
            currentTable = "TABLE_GEO_Questions";
        } else if (mChosenLevel == 2) {
            shuffleQuestions();
            currentTable = "TABLE_HIS_Questions";
        } else if (mChosenLevel == 3) {
            shuffleQuestions();
            currentTable = "TABLE_BIO_Questions";
        } else if (mChosenLevel == 4) {
            shuffleQuestions();
            currentTable = "TABLE_PHI_Questions";
        }
    }

    public void setNickName(String mNickName) {
        this.mNickName = mNickName.replace("Здравейте, ", "");
    }

    public String getCurrentTable() {
        return currentTable;
    }

    public ArrayList<Integer> getmQuestionsOrder() {
        return mQuestionsOrder;
    }

    public int[] getUserAnswers() {
        return userAnswers;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handler = new SQLHandler(getActivity().getApplicationContext());
        currentQuestion = 1;
        points = 0;
        initViews();
        setColors();
        showFullQuestion();
    }

    private void setColors() {
        switch (mChosenLevel) {
            case 1:         // Geography
                tvQuestion.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorGeography));
                rlScoreLayout.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorGeography_accent));
                selectColor = Color.parseColor("#E0F2F1");
                break;
            case 2:        // History
                tvQuestion.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorHistory));
                rlScoreLayout.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorHistory_accent));
                selectColor = Color.parseColor("#E8EAF6");
                break;
            case 3:        // Biology
                tvQuestion.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorBiology));
                rlScoreLayout.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorBiology_accent));
                selectColor = Color.parseColor("#FFEBEE");
                break;
            case 4:        // Philosophy
                tvQuestion.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPhilosophy));
                rlScoreLayout.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPhilosophy_accent));
                selectColor = Color.parseColor("#FCE4EC");
                break;
        }
    }

    private void initViews() {
        rlScoreLayout = (RelativeLayout) getActivity().findViewById(R.id.rlScoreLayout);
        tvQuestion = (TextView) getActivity().findViewById(R.id.tvQuestion);
        tvOption1 = (TextView) getActivity().findViewById(R.id.tvOption1);
        tvOption2 = (TextView) getActivity().findViewById(R.id.tvOption2);
        tvOption3 = (TextView) getActivity().findViewById(R.id.tvOption3);
        tvOption4 = (TextView) getActivity().findViewById(R.id.tvOption4);

        tvUserName = (TextView) getActivity().findViewById(R.id.tvUserName);
        tvUserName.setText(mNickName);
        tvCurrentQuestion = (TextView) getActivity().findViewById(R.id.tvCurrentQuestion);
        setCurrentNumberOfQuestion();
        tvPoints = (TextView) getActivity().findViewById(R.id.tvPoints);
        setCurrentPoints();
        fabAnswer = (FloatingActionButton) getActivity().findViewById(R.id.fabAnswer);
        fabAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white)));

        tvOption1.setOnClickListener(this);
        tvOption2.setOnClickListener(this);
        tvOption3.setOnClickListener(this);
        tvOption4.setOnClickListener(this);
        fabAnswer.setOnClickListener(this);
    }

    private void setCurrentPoints() {
        tvPoints.setText(points + " т.");
    }

    private void setCurrentNumberOfQuestion() {
        tvCurrentQuestion.setText(currentQuestion + "/" + totalQuestions);
    }

    private void shuffleQuestions() {
        Random random = new Random();
        int randomIndex, firstQuestion, randomQuestion;
        mQuestionsOrder = new ArrayList<>();
        for (int i = 1; i <= totalSQLdbQuestions; i++) {
            mQuestionsOrder.add(i); // so the number starts from 1 on position 0
        }
        Log.d("asbozh_numbers", mQuestionsOrder.toString());

        for (int i = 0; i < totalSQLdbQuestions * 3; i++) {
            randomIndex = 1 + random.nextInt(mQuestionsOrder.size() - 1);
            firstQuestion = mQuestionsOrder.get(0);
            randomQuestion = mQuestionsOrder.get(randomIndex);
            mQuestionsOrder.set(0, randomQuestion);
            mQuestionsOrder.set(randomIndex, firstQuestion);
        }

        Log.d("asbozh_numbers", mQuestionsOrder.toString());

        while (mQuestionsOrder.size() > totalQuestions) {
            mQuestionsOrder.remove(mQuestionsOrder.size() - 1);
        }
        Log.d("asbozh_numbers", mQuestionsOrder.toString());
    }

    private void showFullQuestion() {
        handler.open();
        tvQuestion.setText(handler.getQuestion(currentTable, mQuestionsOrder.get(currentQuestion - 1)));
        String[] options = handler.getOptions(currentTable, mQuestionsOrder.get(currentQuestion - 1));
        tvOption1.setText(options[0]);
        tvOption2.setText(options[1]);
        tvOption3.setText(options[2]);
        tvOption4.setText(options[3]);
        correctAnswer = handler.getAnswer(currentTable, mQuestionsOrder.get(currentQuestion - 1));
        handler.close();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tvOption1:
                markAnswer(1);
                showFAB();
                break;
            case R.id.tvOption2:
                markAnswer(2);
                showFAB();
                break;
            case R.id.tvOption3:
                markAnswer(3);
                showFAB();
                break;
            case R.id.tvOption4:
                markAnswer(4);
                showFAB();
                break;
            case R.id.fabAnswer:
                checkAnswer();
                // Execute some code after some milliseconds have passed
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        hideFAB();
                        updateUI();
                    }
                }, 700);
                break;
        }


    }

    private void updateUI() {
        currentQuestion++;
        if (currentQuestion <= totalQuestions) {
            clearViews();
            setCurrentNumberOfQuestion();
            setCurrentPoints();
            showFullQuestion();
        } else {
            mListener.onGameFinish();
        }
    }

    private void clearViews() {
        tvOption1.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
        tvOption2.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
        tvOption3.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
        tvOption4.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
        fabAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.white)));
    }

    private void checkAnswer() {
        if (correctAnswer.equals(currentAnswer)) {
            userAnswers[currentQuestion - 1] = 1;
            points++;
            fabAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.correct_answer)));
            fabAnswer.setImageResource(R.drawable.check_white);
        } else {
            fabAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.wrong_answer)));
            fabAnswer.setImageResource(R.drawable.close_white);
            userAnswers[currentQuestion - 1] = 0;
        }

    }

    private void hideFAB() {
        // animations to be implemented
        fabAnswer.hide();
    }

    private void showFAB() {
        // animations to be implemented
        fabAnswer.show();
        fabAnswer.setImageResource(R.drawable.correct);
    }

    private void markAnswer(int i) {
        if (i == 1) {
            tvOption1.setBackgroundColor(selectColor);
            tvOption2.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            tvOption3.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            tvOption4.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            currentAnswer = tvOption1.getText().toString();
        } else if (i == 2) {
            tvOption2.setBackgroundColor(selectColor);
            tvOption1.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            tvOption3.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            tvOption4.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            currentAnswer = tvOption2.getText().toString();
        } else if (i == 3) {
            tvOption3.setBackgroundColor(selectColor);
            tvOption1.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            tvOption2.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            tvOption4.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            currentAnswer = tvOption3.getText().toString();
        } else if (i == 4) {
            tvOption4.setBackgroundColor(selectColor);
            tvOption1.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            tvOption2.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            tvOption3.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white_background));
            currentAnswer = tvOption4.getText().toString();
        }
    }

}
