package com.jmpancini.desafiojera.mywatchlist.model;


import java.util.List;

public class Filme{

    private String id;
    private String assistido;
    private String data;
    private String titulo;
    private String imgUrl;
    private List<Genero> genres;

    public Filme() {
    }

    public List<Genero> getGenres() {
        return genres;
    }

    public void setGenres(List<Genero> genres) {
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssistido() {
        return assistido;
    }

    public void setAssistido(String assistido) {
        this.assistido = assistido;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImgUrl() {

        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
