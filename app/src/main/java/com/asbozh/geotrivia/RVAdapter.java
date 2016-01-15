package com.asbozh.geotrivia;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.QuestionsViewHolder>{


    private int[] mUserAnswers;
    private ArrayList<Integer> mQuestionsOrder;
    private String currentTable;

    SQLHandler rvHandler;

    public RVAdapter(Context context, String table, ArrayList<Integer> questionsOrder, int[] userAnswers){
        this.mUserAnswers = userAnswers;
        this.mQuestionsOrder = questionsOrder;
        rvHandler = new SQLHandler(context);
        this.currentTable = table;
    }

    @Override
    public RVAdapter.QuestionsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_card_view, viewGroup, false);
        QuestionsViewHolder qvh = new QuestionsViewHolder(v);
        return qvh;
    }

    @Override
    public void onBindViewHolder(RVAdapter.QuestionsViewHolder viewHolder, int position) {
        rvHandler.open();

        viewHolder.tvResultsQuestion.setText(rvHandler.getQuestion(currentTable, mQuestionsOrder.get(position)));
        viewHolder.tvResultsAnswer.setText(rvHandler.getAnswer(currentTable, mQuestionsOrder.get(position)));
        if (mUserAnswers[position] == 1) {
            viewHolder.ivResultsIcon.setImageResource(R.drawable.check);
        } else {
            viewHolder.ivResultsIcon.setImageResource(R.drawable.close);
        }

        rvHandler.close();
    }

    @Override
    public int getItemCount() {
        return mQuestionsOrder.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class QuestionsViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public TextView tvResultsQuestion;
        public TextView tvResultsAnswer;
        public ImageView ivResultsIcon;

        public QuestionsViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            tvResultsQuestion = (TextView)itemView.findViewById(R.id.tvResultsQuestion);
            tvResultsAnswer = (TextView)itemView.findViewById(R.id.tvResultsAnswer);
            ivResultsIcon = (ImageView)itemView.findViewById(R.id.ivResultsIcon);
        }
    }

}
