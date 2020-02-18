package com.jmpancini.desafiojera.mywatchlist.network;

import com.jmpancini.desafiojera.mywatchlist.network.response.FilmeResponse;
import com.jmpancini.desafiojera.mywatchlist.network.response.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FilmeService {


    @GET("movie/{movieId}?api_key=fff6820549291d8f03648ce827885017&language=pt-BR")
    Call<FilmeResponse> buscarDetalhes(@Path("movieId") String movieId);

    @GET("search/movie?api_key=fff6820549291d8f03648ce827885017&language=pt-BR&include_adult=false")
    Call<SearchResponse> buscarFilmes(@Query("query") String query, @Query("page") int page);

    @GET("discover/movie?api_key=fff6820549291d8f03648ce827885017&language=pt-BR&sort_by=popularity.desc&include_adult=false&include_video=false")
    Call<SearchResponse> discoverFilmes(@Query("page") int page, @Query("with_genres") String with_genres);
}
