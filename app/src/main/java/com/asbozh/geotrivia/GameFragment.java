package com.asbozh.geotrivia;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameFragment extends Fragment {

    private TextView tvQuestion, tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4;
    private TextView tvUserName, tvCurrentQuestion, tvPoints;
    private EditText etAnswer;
    private FloatingActionButton fabAnswer;

    private int mChosenLevel;
    private ArrayList<Integer> mQuestionsOrder;
    private int specialQuestion, endQuestion;
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
        if (mChosenLevel == 2015) {
            specialQuestion = 26;
            endQuestion = 30;
            new RandomOrder().execute(specialQuestion - 1, endQuestion);
        } else if (mChosenLevel == 2014) {
            specialQuestion = 30;
            endQuestion = 32;
            new RandomOrder().execute(specialQuestion - 1, endQuestion);
        } else if (mChosenLevel == 2013) {
            specialQuestion = 29;
            endQuestion = 32;
            new RandomOrder().execute(specialQuestion - 1, endQuestion);
        } else if (mChosenLevel == 2012) {
            specialQuestion = 30;
            endQuestion = 32;
            new RandomOrder().execute(specialQuestion - 1, endQuestion);
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
        etAnswer = (EditText) getActivity().findViewById(R.id.etAnswer);
        fabAnswer = (FloatingActionButton) getActivity().findViewById(R.id.fabAnswer);
    }


    private class RandomOrder extends AsyncTask<Integer, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(Integer... params) {
            Random random = new Random();
            int randomIndex, firstQuestion, randomQuestion;
            ArrayList<Integer> questions = new ArrayList<>();
            int n = params[0];
            int totalNumber = params[1];
            for (int i = 0; i < n; i++) {
                questions.add(i + 1); // so the number starts from 1 on position 0
            }

            Log.d("asbozh_numbers", questions.toString());

            for (int i = 0; i < n; i++) {
                randomIndex = 1 + random.nextInt(questions.size() - 1);
                firstQuestion = questions.get(0);
                randomQuestion = questions.get(randomIndex);
                questions.set(0, randomQuestion);
                questions.set(randomIndex, firstQuestion);
            }
            for (int i = n + 1; i <= totalNumber; i++) {
                questions.add(i);
            }

            Log.d("asbozh_numbers", questions.toString());

            return questions;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            // super.onPostExecute(arrayList);
            mQuestionsOrder = arrayList;
            Log.d("asbozh_numbers", mQuestionsOrder.toString());
            tvQuestion.setText(mQuestionsOrder.toString());
        }
    }
}
