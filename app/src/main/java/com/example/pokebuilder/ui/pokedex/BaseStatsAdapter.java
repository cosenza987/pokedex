package com.example.pokebuilder.ui.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokebuilder.R;
import com.example.pokebuilder.ui.pokedex.BaseStat;

import java.util.ArrayList;
import java.util.List;

public class BaseStatsAdapter extends RecyclerView.Adapter<BaseStatsAdapter.ViewHolder> {

    private ArrayList<BaseStat> baseStats;
    private Context context;

    public BaseStatsAdapter(Context context, ArrayList<BaseStat> baseStats) {
        this.context = context;
        this.baseStats = baseStats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_stats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaseStat baseStat = baseStats.get(position);
        holder.baseStatName.setText(baseStat.getName());
        holder.baseStatValue.setText(String.valueOf(baseStat.getValue()));
        holder.baseStatProgress.setProgress(baseStat.getValue());
    }

    @Override
    public int getItemCount() {
        return baseStats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView baseStatName, baseStatValue;
        ProgressBar baseStatProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            baseStatName = itemView.findViewById(R.id.base_stat_name);
            baseStatValue = itemView.findViewById(R.id.base_stat_value);
            baseStatProgress = itemView.findViewById(R.id.base_stat_progress);
        }
    }
}
