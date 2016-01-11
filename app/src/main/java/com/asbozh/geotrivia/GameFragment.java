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

    private TextView tvQuestion, tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4;
    private TextView tvUserName, tvCurrentQuestion, tvPoints;
    private FloatingActionButton fabAnswer;

    private int mChosenLevel;
    private ArrayList<Integer> mQuestionsOrder;
    private int totalQuestions = 30;
    private int totalSQLdbQuestions = 100;
    private int currentQuestion = 1;
    private int points = 0;


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
        } else if (mChosenLevel == 2) {
            shuffleQuestions();
        } else if (mChosenLevel == 3) {
            shuffleQuestions();
        } else if (mChosenLevel == 4) {
            shuffleQuestions();
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
        tvQuestion.setText("DONE");

    }

    private void initViews() {
        tvQuestion = (TextView) getActivity().findViewById(R.id.tvQuestion);
        tvAnswer1 = (TextView) getActivity().findViewById(R.id.tvAnswer1);
        tvAnswer2 = (TextView) getActivity().findViewById(R.id.tvAnswer2);
        tvAnswer3 = (TextView) getActivity().findViewById(R.id.tvAnswer3);
        tvAnswer4 = (TextView) getActivity().findViewById(R.id.tvAnswer4);
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

}
