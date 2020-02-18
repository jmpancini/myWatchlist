package com.jmpancini.desafiojera.mywatchlist.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jmpancini.desafiojera.mywatchlist.R;
import com.jmpancini.desafiojera.mywatchlist.adapter.AdapterBusca;
import com.jmpancini.desafiojera.mywatchlist.model.Filme;
import com.jmpancini.desafiojera.mywatchlist.network.ApiService;
import com.jmpancini.desafiojera.mywatchlist.network.response.FilmeResponse;
import com.jmpancini.desafiojera.mywatchlist.network.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscaActivity extends AppCompatActivity {

    private RecyclerView recyclerBusca;
    private List<Filme> filmes = new ArrayList<>();
    private AdapterBusca adapterBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configuraAdapter();
    }

    private void configuraAdapter(){

        recyclerBusca = findViewById(R.id.recyclerBuscaFilmes);

        recyclerBusca.setLayoutManager(new LinearLayoutManager(this));
        recyclerBusca.setHasFixedSize(true);

        adapterBusca = new AdapterBusca();
        adapterBusca.setFilmes(filmes);
        recyclerBusca.setAdapter(adapterBusca);
    }

    private void realizaBusca(String query){
        ApiService.getInstance()
                .buscarFilmes( query, 1)
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        if(response.isSuccessful()){
                            Filme temp;
                            for(FilmeResponse item : response.body().getResults()){
                                temp = new Filme();
                                temp.setId(item.getId());
                                temp.setTitulo(item.getTitle());
                                temp.setImgUrl("https://image.tmdb.org/t/p/w500" + item.getPoster_path());
                                filmes.add(temp);
                                adapterBusca.notifyDataSetChanged();
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        Toast.makeText(BuscaActivity.this, "Erro ao buscar lista na Api", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //configuração da toolbar e do searchview
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_busca, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return false;
            }
        });


        searchView.setOnSearchClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filmes.clear();
                realizaBusca(searchView.getQuery().toString());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
