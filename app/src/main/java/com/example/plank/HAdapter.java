package com.example.plank;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataHistory> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    Button btn;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new ViewHolderHistory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        ViewHolderHistory viewHolderHistory =(ViewHolderHistory)holder;
        viewHolderHistory.onBind(list.get(position));
        viewHolderHistory.getBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });

        //((ViewHolderHistory)holder).onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void addItem(DataHistory data) {
        // 외부에서 item을 추가시킬 함수입니다.
        list.add(data);
    }
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    public HAdapter(){
    }

    public HAdapter(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


}
