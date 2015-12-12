package com.asbozh.geotrivia;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class ChooseLevelFragment extends Fragment implements View.OnClickListener {


    // 0 - nothing clicked, 2015 - Test 2015, 2014 - Test 2014, 2013 - Test 2013, 2012 - Test 2012
    private int levelClicked = 0;


    private TextView tvLvl2015, tvColorLvl2015, tvLvl2014, tvColorLvl2014, tvLvl2013, tvColorLvl2013, tvLvl2012, tvColorLvl2012;
    private FloatingActionButton fabStartLevel, fabChooseLevelBack;

    public interface Listener {
        void onChooseLevelBackButtonPressed();
        void onStartLevelClicked();
    }

    Listener mListener = null;

    public void setListener(Listener l) {
        mListener = l;
    }

    public int getLevelClicked() {
        return levelClicked;
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
            levelClicked = 2015;
            fabStartLevel.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.tvTest2014 || v.getId() == R.id.tvColored2014) {
            levelClicked = 2014;
            fabStartLevel.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.tvTest2013 || v.getId() == R.id.tvColored2013) {
            levelClicked = 2013;
            fabStartLevel.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.tvTest2012 || v.getId() == R.id.tvColored2012) {
            levelClicked = 2012;
            fabStartLevel.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.fabChooseLevelBack) {
            mListener.onChooseLevelBackButtonPressed();
        }
        if (v.getId() == R.id.fabStartLevel) {
            mListener.onStartLevelClicked();
        }
    }
}
