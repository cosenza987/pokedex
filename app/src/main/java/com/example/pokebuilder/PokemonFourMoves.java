package com.example.pokebuilder;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PokemonFourMoves implements Serializable {
    private pokemon poke;
    private ArrayList<move> moves;

    public PokemonFourMoves(pokemon poke, ArrayList<move> moves) {
        this.poke = poke;
        this.moves = moves;
    }
    public pokemon getPokemon() {
        return this.poke;
    }

    public move getMove(int i) {
        return this.moves.get(i);
    }

}
