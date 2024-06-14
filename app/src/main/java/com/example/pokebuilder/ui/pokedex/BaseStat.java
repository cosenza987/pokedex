package com.example.pokebuilder.ui.pokedex;

public class BaseStat {
    String name;
    Integer value;
    public BaseStat(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}
