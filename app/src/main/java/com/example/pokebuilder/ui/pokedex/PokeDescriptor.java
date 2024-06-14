package com.example.pokebuilder.ui.pokedex;

public class PokeDescriptor {
    protected String name, type1, type2, pokeid;
    protected String imgUrl;
    public PokeDescriptor(String name, String type1, String type2, String imgUrl, String pokeid) {
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.imgUrl = imgUrl;
        this.pokeid = pokeid;
    }
    public String getName() {
        return name;
    }
    public String getType1() {
        return type1;
    }
    public String getType2() {
        return type2;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public String getPokeid() { return pokeid; }
}
