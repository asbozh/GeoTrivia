package com.asbozh.geotrivia;


import android.app.Fragment;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutUsFragment extends Fragment {

    private FloatingActionButton fabAboutUsBack;

    public interface Listener {
        void onAboutUsBackClicked();
    }

    Listener mListener = null;

    public void setListener(Listener l) {
        mListener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fabAboutUsBack = (FloatingActionButton) getActivity().findViewById(R.id.fabAboutUsBack);
        fabAboutUsBack.show();
        fabAboutUsBack.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorAboutUsBackFAB)));
        fabAboutUsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAboutUsBackClicked();
            }
        });

    }
}
