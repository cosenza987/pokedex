package com.example.pokebuilder.ui.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokebuilder.R;
import com.example.pokebuilder.ui.pokedex.PokeInfo;

import java.util.ArrayList;
import java.util.List;

public class MovesAdapter extends RecyclerView.Adapter<MovesAdapter.ViewHolder> {

    private ArrayList<PokeInfo> moves;
    private Context context;

    public MovesAdapter(Context context, ArrayList<PokeInfo> moves) {
        this.context = context;
        this.moves = moves;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_move, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PokeInfo move = moves.get(position);
        holder.moveName.setText(move.getName());
        holder.moveDescription.setText(move.getDescription());
    }

    @Override
    public int getItemCount() {
        return moves.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView moveName;
        TextView moveDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moveName = itemView.findViewById(R.id.move_name);
            moveDescription = itemView.findViewById(R.id.move_description);
        }
    }
}
