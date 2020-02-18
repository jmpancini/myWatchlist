package com.jmpancini.desafiojera.mywatchlist.network.response;

import java.util.List;

public class SearchResponse {

    private final int page;

    private final int total_pages;

    private final int total_results;

    private final List<FilmeResponse> results;

    public SearchResponse(int page, int total_pages, int total_results, List<FilmeResponse> results) {
        this.page = page;
        this.total_pages = total_pages;
        this.total_results = total_results;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public List<FilmeResponse> getResults() {
        return results;
    }
}
