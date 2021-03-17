package com.example.plank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataHistory> list = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new ViewHolderHistory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderHistory)holder).onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    void addItem(DataHistory data) {
        // 외부에서 item을 추가시킬 함수입니다.
        list.add(data);
    }
}
