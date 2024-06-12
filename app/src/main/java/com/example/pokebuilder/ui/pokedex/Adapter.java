package com.example.pokebuilder.ui.pokedex;

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

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList pokemonList;
    Context context;
    public Adapter(Context context, ArrayList pokemonList) {
        this.context = context;
        this.pokemonList = pokemonList;
    }
    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        PokeDescriptor item = (PokeDescriptor) pokemonList.get(position);
        Picasso.get().load(item.getImgUrl()).into(holder.image);
        holder.name.setText(item.getName());
        holder.type1.setText(item.getType1());
        holder.type2.setText(item.getType2());
    }
    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, type1, type2;
        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.img);
            name = (TextView) view.findViewById(R.id.name);
            type1 = (TextView) view.findViewById(R.id.type1);
            type2 = (TextView) view.findViewById(R.id.type2);
        }
    }

}
