package com.example.plank;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderHistory extends RecyclerView.ViewHolder {

    ImageView image, image2;
    TextView month, day, nDay, plank1, plank2, memo;
    ConstraintLayout constraintLayout;
    Button button;

    public ViewHolderHistory(@NonNull final View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.check1);
        image2 = itemView.findViewById(R.id.check2);
        month = itemView.findViewById(R.id.mon);
        day = itemView.findViewById(R.id.il);
        nDay = itemView.findViewById(R.id.ilcha);
        plank1 = itemView.findViewById(R.id.PT);
        plank2 = itemView.findViewById(R.id.PT2);
        memo = itemView.findViewById(R.id.memo);
        constraintLayout = itemView.findViewById(R.id.memoLayout);
        button = itemView.findViewById(R.id.hisBtn);

    }

    public void onBind(DataHistory data){
        image.setImageResource(data.getImage());
        image2.setImageResource(data.getImage2());
        month.setText(data.getMonth());
        day.setText(data.getDay());
        nDay.setText(data.getnDay());
        plank1.setText(data.getPlank1());
        plank2.setText(data.getPlank2());
        if(data.getMemo() == null || data.getMemo().length() == 0){
            constraintLayout.setVisibility(View.GONE);
        }else{
            memo.setText(data.getMemo());
        }
        if(data.isClear() == false){
            button.setVisibility(View.GONE);
        }

    }

    public Button getBtn(){
        return button;
    }


}
