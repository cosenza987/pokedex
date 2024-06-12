package com.example.pokebuilder.ui.pokedex;

public class PokeInfo {
    private String name, description;
    public PokeInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
}
