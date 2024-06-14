package com.example.pokebuilder.ui.teams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pokebuilder.R;
import com.example.pokebuilder.pokemon;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamsFragmentRecyclerAdapter extends RecyclerView.Adapter<TeamsFragmentRecyclerAdapter.ViewHolder> {

    private ArrayList<Team> teams;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    TeamsFragmentRecyclerAdapter(Context context, ArrayList<Team> teams) {
        this.mInflater = LayoutInflater.from(context);
        this.teams = teams;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.team_recycler_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Team team = teams.get(position);
        holder.teamNameTextView.setText(team.getName());
        for (int i=1;i<=6;i++){
            String imgUrl = teams.get(position).getPokemon(i-1).getPokemon().getImage();
            Picasso.get().load(imgUrl).into(holder.pokemonImageViews.get(i-1));
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return teams.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView teamNameTextView;
        ArrayList<ImageView> pokemonImageViews = new ArrayList<>();

        ViewHolder(View itemView) {
            super(itemView);
            teamNameTextView = itemView.findViewById(R.id.recyclerRowTeamName);
            pokemonImageViews = new ArrayList<>(Arrays.asList(
                itemView.findViewById(R.id.recyclerRowPokemon1),
                itemView.findViewById(R.id.recyclerRowPokemon2),
                itemView.findViewById(R.id.recyclerRowPokemon3),
                itemView.findViewById(R.id.recyclerRowPokemon4),
                itemView.findViewById(R.id.recyclerRowPokemon5),
                itemView.findViewById(R.id.recyclerRowPokemon6)
            ));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Team getItem(int id) {
        return teams.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
