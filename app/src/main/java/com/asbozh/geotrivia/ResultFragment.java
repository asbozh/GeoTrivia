package com.asbozh.geotrivia;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultFragment extends Fragment {

    private TextView tvHeader;
    private RelativeLayout rlHeader;
    private RecyclerView rvQuestions;
    private FloatingActionButton fabHome;

    private String currentTable;
    private int[] mUserAnswers;
    private ArrayList<Integer> mQuestionsOrder;
    private int points;

    public interface Listener {
        void onPlayAgain();
    }

    Listener mListener = null;

    public void setListener(Listener l) {
        mListener = l;
    }

    public void setCurrentTable(String currentTable) {
        this.currentTable = currentTable;
    }

    public void setmUserAnswers(int[] mUserAnswers) {
        this.mUserAnswers = mUserAnswers;
    }

    public void setmQuestionsOrder(ArrayList<Integer> mQuestionsOrder) {
        this.mQuestionsOrder = mQuestionsOrder;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        setColors();
        RVAdapter adapter = new RVAdapter(getActivity().getApplicationContext(), currentTable, mQuestionsOrder, mUserAnswers);
        rvQuestions.setAdapter(adapter);
    }

    private void setColors() {
        if (currentTable.contains("GEO")) {
            rlHeader.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorGeography));
        } else if (currentTable.contains("HIS")) {
            rlHeader.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorHistory));
        } else if (currentTable.contains("BIO")) {
            rlHeader.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorBiology));
        } else if (currentTable.contains("PHI")) {
            rlHeader.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPhilosophy));
        }
    }

    private void initViews() {
        rlHeader = (RelativeLayout) getActivity().findViewById(R.id.rlHeader);
        tvHeader = (TextView) getActivity().findViewById(R.id.tvHeader);
        String header = generateHeader();
        tvHeader.setText(header);

        rvQuestions = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvQuestions.setLayoutManager(layoutManager);
        rvQuestions.setHasFixedSize(true);

        fabHome = (FloatingActionButton) getActivity().findViewById(R.id.fabHome);
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlayAgain();
            }
        });
    }

    private String generateHeader() {

        StringBuilder builder = new StringBuilder();
        if (currentTable.contains("GEO")) {
            builder.append("Област: География\n");
        } else if (currentTable.contains("HIS")) {
            builder.append("Област: История\n");
        } else if (currentTable.contains("BIO")) {
            builder.append("Област: Биология\n");
        } else if (currentTable.contains("PHI")) {
            builder.append("Област: Философия\n");
        } else {
            builder.append("Област: --- \n");
        }
        builder.append("Точки: ").append(points).append(" от ").append(mQuestionsOrder.size()).append("\n");
        String grade = generateGrade();
        builder.append("Оценка: ").append(grade);

        return builder.toString();
    }

    private String generateGrade() {
        if (points <= 10) {
            return "Слаб";
        } else if (points > 10 && points <= 15) {
            return "Среден";
        } else if (points > 15 && points <= 20) {
            return "Добър";
        } else if (points > 20 && points <= 25) {
            return "Много добър";
        } else if (points > 25) {
            return "Отличен";
        }
        return "---";
    }


}
