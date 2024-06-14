package com.example.pokebuilder;

public class UrlSingleton {
    public String url = "192.168.50.223:8080/ImemonAPI_war_exploded";
    private static UrlSingleton instance;
    public static synchronized UrlSingleton getInstance() {
        if(instance == null) {
            instance = new UrlSingleton();
        }
        return instance;
    }
    protected UrlSingleton() {}
}
