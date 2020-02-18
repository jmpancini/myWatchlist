package com.jmpancini.desafiojera.mywatchlist.network.response;



public class GenreResult {

    private final String id;

    private final String name;

    public GenreResult(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
