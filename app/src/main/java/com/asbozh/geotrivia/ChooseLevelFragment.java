package com.asbozh.geotrivia;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class ChooseLevelFragment extends Fragment implements View.OnClickListener {

    private TextView tvLvl2015, tvColorLvl2015, tvLvl2014, tvColorLvl2014, tvLvl2013, tvColorLvl2013, tvLvl2012, tvColorLvl2012;
    private FloatingActionButton fabStartLevel, fabChooseLevelBack;

    public interface Listener {
        void onChooseLevelBackButtonPressed();
    }

    Listener mListener = null;

    public void setListener(Listener l) {
        mListener = l;
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
            Toast.makeText(getActivity(), "2015 clicked..", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.tvTest2014 || v.getId() == R.id.tvColored2014) {
            Toast.makeText(getActivity(), "2014 clicked..", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.tvTest2013 || v.getId() == R.id.tvColored2013) {
            Toast.makeText(getActivity(), "2013 clicked..", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.tvTest2012 || v.getId() == R.id.tvColored2012) {
            Toast.makeText(getActivity(), "2012 clicked..", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.fabChooseLevelBack) {
            mListener.onChooseLevelBackButtonPressed();
        }
        if (v.getId() == R.id.fabStartLevel) {
            Toast.makeText(getActivity(), "Start level clicked..", Toast.LENGTH_SHORT).show();
        }
    }
}
