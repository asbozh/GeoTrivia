package com.asbozh.geotrivia;


import android.app.Fragment;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class ChooseLevelFragment extends Fragment implements View.OnClickListener {


    // 0 - nothing clicked, 1 - Geography, 2 - History, 3 - Biology, 4 - Philosophy
    private int levelClicked = 0;


    private TextView tvHeaderChooseLevel, tvTestGeo, tvColoredGeo, tvTestHis, tvColoredHis, tvTestBio, tvColoredBio, tvTestPhi, tvColoredPhi;
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
        unSelectOthers(0);
    }

    private void initViews() {
        tvHeaderChooseLevel = (TextView) getActivity().findViewById(R.id.tvChooseLevel);
        tvHeaderChooseLevel.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary));
        tvTestGeo = (TextView) getActivity().findViewById(R.id.tvTestGeo);
        tvColoredGeo = (TextView) getActivity().findViewById(R.id.tvColoredGeo);
        tvTestHis = (TextView) getActivity().findViewById(R.id.tvTestHis);
        tvColoredHis = (TextView) getActivity().findViewById(R.id.tvColoredHis);
        tvTestBio = (TextView) getActivity().findViewById(R.id.tvTestBio);
        tvColoredBio = (TextView) getActivity().findViewById(R.id.tvColored2013);
        tvTestPhi = (TextView) getActivity().findViewById(R.id.tvTestPhi);
        tvColoredPhi = (TextView) getActivity().findViewById(R.id.tvColoredPhi);
        fabStartLevel = (FloatingActionButton) getActivity().findViewById(R.id.fabStartLevel);
        fabStartLevel.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white)));
        fabChooseLevelBack = (FloatingActionButton) getActivity().findViewById(R.id.fabChooseLevelBack);
        fabChooseLevelBack.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white)));


        tvTestGeo.setOnClickListener(this);
        tvTestHis.setOnClickListener(this);
        tvTestBio.setOnClickListener(this);
        tvTestPhi.setOnClickListener(this);
        tvColoredGeo.setOnClickListener(this);
        tvColoredHis.setOnClickListener(this);
        tvColoredBio.setOnClickListener(this);
        tvColoredPhi.setOnClickListener(this);
        fabStartLevel.setOnClickListener(this);
        fabChooseLevelBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvTestGeo || v.getId() == R.id.tvColoredGeo) {
            levelClicked = 1;
            tvTestGeo.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorGeography));
            tvTestGeo.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white));
            unSelectOthers(levelClicked);
            fabStartLevel.show();
        }
        if (v.getId() == R.id.tvTestHis || v.getId() == R.id.tvColoredHis) {
            levelClicked = 2;
            tvTestHis.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorHistory));
            tvTestHis.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white));
            unSelectOthers(levelClicked);
            fabStartLevel.show();
        }
        if (v.getId() == R.id.tvTestBio || v.getId() == R.id.tvColored2013) {
            levelClicked = 3;
            tvTestBio.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorBiology));
            tvTestBio.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white));
            unSelectOthers(levelClicked);
            fabStartLevel.show();
        }
        if (v.getId() == R.id.tvTestPhi || v.getId() == R.id.tvColoredPhi) {
            levelClicked = 4;
            tvTestPhi.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPhilosophy));
            tvTestPhi.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white));
            unSelectOthers(levelClicked);
            fabStartLevel.show();
        }
        if (v.getId() == R.id.fabChooseLevelBack) {
            mListener.onChooseLevelBackButtonPressed();
        }
        if (v.getId() == R.id.fabStartLevel) {
            mListener.onStartLevelClicked();
        }
    }

    private void unSelectOthers(int levelClicked) {
        if (levelClicked != 1) {
            tvTestGeo.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorGeography_50));
            tvTestGeo.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.black_text));
        }
        if (levelClicked != 2) {
            tvTestHis.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorHistory_50));
            tvTestHis.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.black_text));
        }
        if (levelClicked != 3) {
            tvTestBio.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorBiology_50));
            tvTestBio.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.black_text));
        }
        if (levelClicked != 4) {
            tvTestPhi.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPhilosophy_50));
            tvTestPhi.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.black_text));
        }
    }
}
