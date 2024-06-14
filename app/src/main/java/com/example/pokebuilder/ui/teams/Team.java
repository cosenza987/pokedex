package com.example.pokebuilder.ui.teams;

import com.example.pokebuilder.PokemonFourMoves;
import com.example.pokebuilder.pokemon;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Team {
    private int _id;
    private String name;
    private ArrayList<PokemonFourMoves> pokemons;

    public Team(int _id, String name, ArrayList<PokemonFourMoves> pokemons) {
        this._id = _id;
        this.name = name;
        this.pokemons = pokemons;
    }
    public String getName() {
        return this.name;
    }

    public int getId() {
        return this._id;
    }

    public PokemonFourMoves getPokemon(int i) {
        return this.pokemons.get(i);
    }
}
