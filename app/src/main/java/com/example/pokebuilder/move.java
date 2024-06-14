package com.example.pokebuilder;
public class move {

    private int _id;
    private String name;

    public move(int _id, String name) {
        this._id = _id;
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public int getId() {
        return this._id;
    }
}

