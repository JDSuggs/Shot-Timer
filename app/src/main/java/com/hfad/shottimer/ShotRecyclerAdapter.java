package com.hfad.shottimer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ShotRecyclerAdapter extends RecyclerView.Adapter<ShotRecyclerAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Shot> shotList;

    //getting the context and product list with constructor
    public ShotRecyclerAdapter(Context mCtx, List<Shot> shotList) {
        this.mCtx = mCtx;
        this.shotList = shotList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.shot_card, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the shot of the specified position
        Shot shot = shotList.get(position);

        //binding the data with the viewholder views
        holder.shotNumber.setText(String.valueOf(shot.getShotNumber()));
//        holder.shotTime.setText(String.valueOf(shot.getTime()));
        holder.shotTime.setText((shot.toString()));
        try{
            holder.splitTime.setText( Shot.getTimeStr(shot.getTime() - shotList.get(position-1).getTime() ));
            shot.setSplitTime(shot.getTime() - shotList.get(position-1).getTime());
        }catch (Exception e){
            holder.splitTime.setText(String.valueOf(0.0));
        }



    }


    @Override
    public int getItemCount() {
        return shotList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView shotNumber, shotTime,splitTime;


        public ProductViewHolder(View itemView) {
            super(itemView);
            shotNumber = itemView.findViewById(R.id.shotNumber);
            shotTime = itemView.findViewById(R.id.timeShot);
            splitTime = itemView.findViewById(R.id.splitTime   );

        }
    }
}
