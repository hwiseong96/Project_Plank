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
    TextView month, day, nDay, plank1, plank2, memo, diffi, ment;
    ConstraintLayout constraintLayout;
    Button button;

    public ViewHolderHistory(@NonNull final View itemView){
        super(itemView);
        diffi = itemView.findViewById(R.id.diffi);
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
        ment = itemView.findViewById(R.id.ment);

    }

    public void onBind(DataHistory data){
        image.setImageResource(data.getImage());
        image2.setImageResource(data.getImage2());
        month.setText(data.getMonth());
        day.setText(data.getDay());
        nDay.setText(data.getnDay());
        plank1.setText(data.getPlank1());
        plank2.setText(data.getPlank2());
        diffi.setText(data.getDiffi() + "30일 챌린지 도전 중");
        ment.setText(data.getMent());
        if(data.getMemo() == null || data.getMemo().length() == 0){
            constraintLayout.setVisibility(View.GONE);
        }else{
            memo.setText(data.getMemo());
        }
        if(data.isClear() == false){
            button.setVisibility(View.GONE);
        }
        if (data.getPlank1().equals("휴식")) {
            ment.setText("오늘은 휴식날! 내일 봐요 \uD83D\uDE09");
            image.setVisibility(View.GONE);
            image2.setVisibility(View.GONE);
            plank1.setVisibility(View.GONE);
            plank2.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
        }

    }

    public Button getBtn(){
        return button;
    }


}
