package com.jmpancini.desafiojera.mywatchlist.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jmpancini.desafiojera.mywatchlist.R;
import com.jmpancini.desafiojera.mywatchlist.adapter.AdapterFilmes;
import com.jmpancini.desafiojera.mywatchlist.config.ConfiguracaoFirebase;
import com.jmpancini.desafiojera.mywatchlist.model.Filme;
import com.jmpancini.desafiojera.mywatchlist.model.Genero;
import com.jmpancini.desafiojera.mywatchlist.network.ApiService;
import com.jmpancini.desafiojera.mywatchlist.network.response.FilmeResponse;
import com.jmpancini.desafiojera.mywatchlist.network.response.GenreResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference filmesPerfilRef;

    private RecyclerView recyclerFilmes;
    private List<Filme> filmes = new ArrayList<>();
    private AdapterFilmes adapterFilmes;

    private static Genero generoPredileto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Watchlist");
        setSupportActionBar(toolbar);



    //configurações iniciais
        //configura botão de adicionar filme
        configuraFloatingActionButton();
        //configura adapter de filmes
        configuraAdapter();
        //configura autenticação e referencia firebase
        configuraFirebaseRefAuth();
        //recupera filmes de usuário
        recuperarFilmes();



    }

    private void configuraFloatingActionButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BuscaActivity.class));
            }
        });
    }

    private void recuperarFilmes(){
        filmesPerfilRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Filme filme;
                filmes.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    filme = ds.getValue(Filme.class);
                    filme.setId(ds.getKey());
                    buscaDetalhes(filme);
                    adicionarFilme(filme);
                }

                adapterFilmes.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WatchlistActivity.this, "Erro ao buscar no Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configuraAdapter(){

        recyclerFilmes = findViewById(R.id.recyclerWatchlist);

        recyclerFilmes.setLayoutManager(new LinearLayoutManager(this));
        recyclerFilmes.setHasFixedSize(true);

        adapterFilmes = new AdapterFilmes();
        adapterFilmes.setFilmes(filmes);
        recyclerFilmes.setAdapter(adapterFilmes);
    }

    private void configuraFirebaseRefAuth(){
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        filmesPerfilRef = ConfiguracaoFirebase.getReferenciaFirebase(ConfiguracaoFirebase.GetPerfilDataPath()).child("filmes");
    }

    private void adicionarFilme(Filme filme){
        filmes.add(filme);
        adapterFilmes.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_watchlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_deslogar:
                autenticacao.signOut();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buscaDetalhes(final Filme filme){
        Genero.resetContador();
        ApiService.getInstance()
                .buscarDetalhes( filme.getId())
                .enqueue(new Callback<FilmeResponse>() {
                    @Override
                    public void onResponse(Call<FilmeResponse> call, Response<FilmeResponse> response) {
                        if(response.isSuccessful()){
                            filme.setTitulo(response.body().getTitle());
                            filme.setImgUrl("https://image.tmdb.org/t/p/w500" + response.body().getPoster_path());
                            filme.setGenres(new ArrayList<Genero>());

                            for(GenreResult g : response.body().getGenres()){
                                filme.getGenres().add(new Genero());
                                filme.getGenres().get(response.body().getGenres().indexOf(g)).setId(g.getId());
                                filme.getGenres().get(response.body().getGenres().indexOf(g)).setName(g.getName());
                            }
                            adapterFilmes.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(Call<FilmeResponse> call, Throwable t) {
                        Toast.makeText(WatchlistActivity.this, "Erro ao buscar filme na Api", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
