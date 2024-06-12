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

public class AbilitiesAdapter extends RecyclerView.Adapter<AbilitiesAdapter.ViewHolder> {

    private ArrayList<PokeInfo> abilities;
    private Context context;

    public AbilitiesAdapter(Context context, ArrayList<PokeInfo> abilities) {
        this.context = context;
        this.abilities = abilities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_ability, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PokeInfo ability = abilities.get(position);
        holder.abilityName.setText(ability.getName());
        holder.abilityDescription.setText(ability.getDescription());
    }

    @Override
    public int getItemCount() {
        return abilities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView abilityName;
        TextView abilityDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            abilityName = itemView.findViewById(R.id.ability_name);
            abilityDescription = itemView.findViewById(R.id.ability_description);
        }
    }
}
