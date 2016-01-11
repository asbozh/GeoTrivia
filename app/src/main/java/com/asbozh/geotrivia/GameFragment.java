package com.asbozh.geotrivia;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameFragment extends Fragment {

    private TextView tvQuestion, tvOption1, tvOption2, tvOption3, tvOption4;
    private TextView tvUserName, tvCurrentQuestion, tvPoints;
    private FloatingActionButton fabAnswer;

    private int mChosenLevel;
    private String currentTable;
    private String currentAnswer;
    private ArrayList<Integer> mQuestionsOrder;
    private int totalQuestions = 30;
    private int totalSQLdbQuestions = 100;
    private int currentQuestion;
    private int points = 0;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        handler = new SQLHandler(getActivity().getApplicationContext());
        currentQuestion = 1;
        showFullQuestion(currentQuestion);
    }

    private void initViews() {
        tvQuestion = (TextView) getActivity().findViewById(R.id.tvQuestion);
        tvOption1 = (TextView) getActivity().findViewById(R.id.tvAnswer1);
        tvOption2 = (TextView) getActivity().findViewById(R.id.tvAnswer2);
        tvOption3 = (TextView) getActivity().findViewById(R.id.tvAnswer3);
        tvOption4 = (TextView) getActivity().findViewById(R.id.tvAnswer4);
        tvUserName = (TextView) getActivity().findViewById(R.id.tvUserName);
        tvCurrentQuestion = (TextView) getActivity().findViewById(R.id.tvCurrentQuestion);
        tvPoints = (TextView) getActivity().findViewById(R.id.tvPoints);
        fabAnswer = (FloatingActionButton) getActivity().findViewById(R.id.fabAnswer);
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

    private void showFullQuestion(int currentQuestion) {
        handler.open();
        tvQuestion.setText(handler.getQuestion(currentTable, mQuestionsOrder.get(currentQuestion - 1)));
        String[] options = handler.getOptions(currentTable, mQuestionsOrder.get(currentQuestion - 1));
        tvOption1.setText(options[0]);
        tvOption2.setText(options[1]);
        tvOption3.setText(options[2]);
        tvOption4.setText(options[3]);
        currentAnswer = handler.getAnswer(currentTable, mQuestionsOrder.get(currentQuestion - 1));
        handler.close();
    }

}
