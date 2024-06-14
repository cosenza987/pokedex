package com.example.pokebuilder;

import java.io.Serializable;

public class move implements Serializable {

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

