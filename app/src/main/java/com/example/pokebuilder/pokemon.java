package com.example.pokebuilder;

public class pokemon {

    private int _id;
    private String image;
    private String name;

    public pokemon(int _id, String name, String imgUrl) {
        this._id = _id;
        this.name = name;
        this.image = imgUrl;
    }
    public String getName() {
        return this.name;
    }

    public String getImage() {
        return this.image;
    }

    public int getId() {
        return this._id;
    }
}
