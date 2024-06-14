package com.example.pokebuilder.ui.teams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokebuilder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamViewAdapter extends RecyclerView.Adapter<TeamViewAdapter.ViewHolder>{
    private Team team;
    private LayoutInflater mInflater;
    TeamViewAdapter(Context context, Team team) {
        this.mInflater = LayoutInflater.from(context);
        this.team = team;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.teamview_recycler_row, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String pokemonName = team.getPokemon(position).getPokemon().getName();
        String imgUrl = team.getPokemon(position).getPokemon().getImage();
        holder.recyclerRowPokemonName.setText(pokemonName);
        Picasso.get().load(imgUrl).into(holder.recyclerRowPokemonImage);
        for(int i=1;i<=4;i++){
            String moveName = team.getPokemon(position).getMove(i-1).getName();
            holder.recyclerRowMoves.get(i-1).setText(moveName);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return 6;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recyclerRowPokemonName;
        ImageView recyclerRowPokemonImage;
        ArrayList<TextView> recyclerRowMoves = new ArrayList<TextView>();

        ViewHolder(View itemView) {
            super(itemView);
            recyclerRowPokemonName = itemView.findViewById(R.id.recyclerRowPokemonName);
            recyclerRowPokemonImage = itemView.findViewById(R.id.recyclerRowPokemonImage);
            recyclerRowMoves = new ArrayList<>(Arrays.asList(
                    itemView.findViewById(R.id.recyclerRowMove1),
                    itemView.findViewById(R.id.recyclerRowMove2),
                    itemView.findViewById(R.id.recyclerRowMove3),
                    itemView.findViewById(R.id.recyclerRowMove4)
            ));
        }

    }

}
