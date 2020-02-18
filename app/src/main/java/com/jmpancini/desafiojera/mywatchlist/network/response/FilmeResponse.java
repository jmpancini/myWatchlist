package com.jmpancini.desafiojera.mywatchlist.network.response;

import java.util.List;

public class FilmeResponse {

    private final String id;

    private final String title;

    private final String poster_path;

    private final String original_title;

    private final String original_language;

    private final String overview;

    private final List<GenreResult> genres;

    public FilmeResponse(String id, String title, String poster_path, String original_title, String original_language, String overview, List<GenreResult> genres) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.original_language = original_language;
        this.overview = overview;
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOverview() {
        return overview;
    }

    public List<GenreResult> getGenres() {
        return genres;
    }
}
