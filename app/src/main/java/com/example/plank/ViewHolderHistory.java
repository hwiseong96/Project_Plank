package com.example.plank;

import android.animation.ValueAnimator;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderHistory extends RecyclerView.ViewHolder {

    ImageView image;
    TextView month, day, nDay, plank1, plank2, memo;


    public ViewHolderHistory(@NonNull View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.check1);
        month = itemView.findViewById(R.id.mon);
        day = itemView.findViewById(R.id.il);
        nDay = itemView.findViewById(R.id.ilcha);
        plank1 = itemView.findViewById(R.id.PT);
        plank2 = itemView.findViewById(R.id.PT2);
        memo = itemView.findViewById(R.id.mon);


    }
    public void onBind(DataHistory data){

        image.setImageResource(data.getImage());
        month.setText(data.getMonth());
        day.setText(data.getDay());
        nDay.setText(data.getnDay());
        plank1.setText(data.getPlank1());
        plank2.setText(data.getPlank2());



    }

}
