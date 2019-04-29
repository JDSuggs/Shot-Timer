package com.hfad.shottimer;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class StatRecyclerAdapter extends RecyclerView.Adapter<StatRecyclerAdapter.StatViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Stats> statList;

    //getting the context and product list with constructor
    public StatRecyclerAdapter(Context mCtx, List<Stats> statList) {
        this.mCtx = mCtx;
        this.statList = statList;
    }

    @Override
    public StatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.content_historical, null);
        return new StatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StatViewHolder holder, int position) {
        try{
            //getting the stats of the specified position
            final Stats stats = statList.get(position);
            final CardView shotCard = holder.cardViewStat;
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    shotCard.setBackgroundColor(Color.BLUE);
                    //stats.setMissedShots(1);

                }
            });

            //binding the data with the viewholder views
            holder.statNumber.setText(String.valueOf(stats.getStatNumber()));

            holder.statTime.setText(String.valueOf(stats.getDate()));

            try{
                holder.sessionLength.setText("Calculate session time!!!!");
            }catch (Exception e){
                holder.sessionLength.setText(String.valueOf(0.0));
            }

        }catch (Exception e){
            ;
        }




    }


    @Override
    public int getItemCount() {
        return statList.size();
    }


    class StatViewHolder extends RecyclerView.ViewHolder {

        TextView statNumber, statTime, sessionLength;
        CardView cardViewStat;


        public StatViewHolder(View itemView) {
            super(itemView);

            statNumber = itemView.findViewById(R.id.statSession);
            statTime = itemView.findViewById(R.id.statDate);
            sessionLength = itemView.findViewById(R.id.sessionLength);

            cardViewStat = itemView.findViewById(R.id.stat_card);

        }
    }
}
