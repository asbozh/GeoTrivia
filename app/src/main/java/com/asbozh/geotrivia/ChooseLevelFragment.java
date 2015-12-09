package com.asbozh.geotrivia;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class ChooseLevelFragment extends Fragment implements View.OnClickListener {

    // 0 - nothing clicked, 2015 - Test 2015, 2014 - Test 2014, 2013 - Test 2013, 2012 - Test 2012
    private int levelClicked = 0;

    private ArrayList<Integer> mQuestionsOrder;

    private TextView tvLvl2015, tvColorLvl2015, tvLvl2014, tvColorLvl2014, tvLvl2013, tvColorLvl2013, tvLvl2012, tvColorLvl2012;
    private FloatingActionButton fabStartLevel, fabChooseLevelBack;

    public interface Listener {
        void onChooseLevelBackButtonPressed();
    }

    Listener mListener = null;

    public void setListener(Listener l) {
        mListener = l;
    }

    public ArrayList<Integer> getmQuestionsOrder() {
        return mQuestionsOrder;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_level, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();

    }

    private void initViews() {
        tvLvl2015 = (TextView) getActivity().findViewById(R.id.tvTest2015);
        tvColorLvl2015 = (TextView) getActivity().findViewById(R.id.tvColored2015);
        tvLvl2014 = (TextView) getActivity().findViewById(R.id.tvTest2014);
        tvColorLvl2014 = (TextView) getActivity().findViewById(R.id.tvColored2014);
        tvLvl2013 = (TextView) getActivity().findViewById(R.id.tvTest2013);
        tvColorLvl2013 = (TextView) getActivity().findViewById(R.id.tvColored2013);
        tvLvl2012 = (TextView) getActivity().findViewById(R.id.tvTest2012);
        tvColorLvl2012 = (TextView) getActivity().findViewById(R.id.tvColored2012);
        fabStartLevel = (FloatingActionButton) getActivity().findViewById(R.id.fabStartLevel);
        fabStartLevel.setVisibility(View.INVISIBLE);
        fabChooseLevelBack = (FloatingActionButton) getActivity().findViewById(R.id.fabChooseLevelBack);

        tvLvl2015.setOnClickListener(this);
        tvLvl2014.setOnClickListener(this);
        tvLvl2013.setOnClickListener(this);
        tvLvl2012.setOnClickListener(this);
        tvColorLvl2015.setOnClickListener(this);
        tvColorLvl2014.setOnClickListener(this);
        tvColorLvl2013.setOnClickListener(this);
        tvColorLvl2012.setOnClickListener(this);
        fabStartLevel.setOnClickListener(this);
        fabChooseLevelBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvTest2015 || v.getId() == R.id.tvColored2015) {
            // Toast.makeText(getActivity(), "2015 clicked..", Toast.LENGTH_SHORT).show();
            levelClicked = 2015;
            new RandomOrder().execute(25, 30);
            fabStartLevel.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.tvTest2014 || v.getId() == R.id.tvColored2014) {
            // Toast.makeText(getActivity(), "2014 clicked..", Toast.LENGTH_SHORT).show();
            levelClicked = 2014;
            new RandomOrder().execute(29, 32);
            fabStartLevel.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.tvTest2013 || v.getId() == R.id.tvColored2013) {
            // Toast.makeText(getActivity(), "2013 clicked..", Toast.LENGTH_SHORT).show();
            levelClicked = 2013;
            new RandomOrder().execute(28, 32);
            fabStartLevel.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.tvTest2012 || v.getId() == R.id.tvColored2012) {
            // Toast.makeText(getActivity(), "2012 clicked..", Toast.LENGTH_SHORT).show();
            levelClicked = 2012;
            new RandomOrder().execute(29, 32);
            fabStartLevel.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.fabChooseLevelBack) {
            mListener.onChooseLevelBackButtonPressed();
        }
        if (v.getId() == R.id.fabStartLevel) {
            Toast.makeText(getActivity(), "Start level clicked..", Toast.LENGTH_SHORT).show();
        }
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
        }
    }
}
