package com.jmpancini.desafiojera.mywatchlist.network;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiService {

    private static FilmeService instance;

    public static FilmeService getInstance(){
        if(instance == null){
            Retrofit.Builder teste = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/");

            Retrofit retrofit = teste
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();

            instance = retrofit.create(FilmeService.class);
        }
        return instance;
    }
}
