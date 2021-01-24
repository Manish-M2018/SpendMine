package com.example.spendmine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Leaderboard_adapter extends RecyclerView.Adapter<Leaderboard_adapter.ViewHolder> {

    ArrayList<MyListData> listdata;

    public Leaderboard_adapter(ArrayList<MyListData> listdata) {
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public Leaderboard_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        Leaderboard_adapter.ViewHolder lead = new Leaderboard_adapter.ViewHolder(v);
        return lead;
    }

    @Override
    public void onBindViewHolder(@NonNull Leaderboard_adapter.ViewHolder holder, int position) {
        MyListData item = listdata.get(position);
        holder.name.setText(item.getName());
        holder.city.setText(item.getCity());
        holder.coins.setText(item.getCoins());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,city,coins;
        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.city = (TextView) itemView.findViewById(R.id.city);
            this.coins = (TextView) itemView.findViewById(R.id.coins);
        }
    }
}
