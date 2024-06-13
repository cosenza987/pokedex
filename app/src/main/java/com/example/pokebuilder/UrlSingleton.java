package com.example.pokebuilder;

public class UrlSingleton {
    public String url = "lolcraft.servebeer.com";
    private static UrlSingleton instance;
    public static synchronized UrlSingleton getInstance() {
        if(instance == null) {
            instance = new UrlSingleton();
        }
        return instance;
    }
    protected UrlSingleton() {}
}
