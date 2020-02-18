package com.jmpancini.desafiojera.mywatchlist.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jmpancini.desafiojera.mywatchlist.R;
import com.jmpancini.desafiojera.mywatchlist.adapter.AdapterBusca;
import com.jmpancini.desafiojera.mywatchlist.config.ConfiguracaoFirebase;
import com.jmpancini.desafiojera.mywatchlist.model.Filme;
import com.jmpancini.desafiojera.mywatchlist.model.Genero;
import com.jmpancini.desafiojera.mywatchlist.network.ApiService;
import com.jmpancini.desafiojera.mywatchlist.network.response.FilmeResponse;
import com.jmpancini.desafiojera.mywatchlist.network.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscaActivity extends AppCompatActivity {

    private TextView textFonteDoConteudo;
    private RecyclerView recyclerBusca;
    private List<Filme> filmes = new ArrayList<>();
    private AdapterBusca adapterBusca;

    private String textQuery;

    private int currentQueryPage;
    private int totalQueryPages;
    private int totalQueryItems;

    private boolean buscando;

    public boolean isBuscando() {
        return buscando;
    }

    public void setBuscando(boolean buscando) {
        this.buscando = buscando;
    }



    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) recyclerBusca.getLayoutManager();
    }


    public String getTextQuery() {
        return textQuery;
    }

    public void setTextQuery(String textQuery) {
        this.textQuery = textQuery;
        filmes.clear();
        povoaRecyclerVier(1);
    }

    public int getTotalQueryItems() {
        return totalQueryItems;
    }

    public void setTotalQueryItems(int totalQueryItems) {
        this.totalQueryItems = totalQueryItems;
    }

    public int getCurrentQueryPage() {
        return currentQueryPage;
    }

    public void setCurrentQueryPage(int currentQueryPage) {
        this.currentQueryPage = currentQueryPage;
    }

    public int getTotalQueryPages() {
        return totalQueryPages;
    }

    public void setTotalQueryPages(int totalQueryPages) {
        this.totalQueryPages = totalQueryPages;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Adicionar Filmes");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textFonteDoConteudo = findViewById(R.id.textFonteDoConteudo);
        configuraAdapter();

        povoaRecyclerVier(1);



    }

    private void configuraAdapter(){

        recyclerBusca = findViewById(R.id.recyclerBuscaFilmes);

        recyclerBusca.setLayoutManager(new LinearLayoutManager(this));
        recyclerBusca.setHasFixedSize(true);

        adapterBusca = new AdapterBusca();
        adapterBusca.setFilmes(filmes);
        recyclerBusca.setAdapter(adapterBusca);


        recyclerBusca.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                notificaMudança();
                //Toast.makeText(BuscaActivity.this, "Scroll "+getLayoutManager().findFirstVisibleItemPosition()+" de "+recyclerBusca.getAdapter().getItemCount(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void notificaMudança(){
        if(!isBuscando() && getLayoutManager().findFirstVisibleItemPosition()>recyclerBusca.getAdapter().getItemCount()-10 && getCurrentQueryPage()<getTotalQueryPages()){
            povoaRecyclerVier(getCurrentQueryPage()+1);
        }
    }

    private void povoaRecyclerVier(final int page){
        setBuscando(true);
        setCurrentQueryPage(page);

        if(getTextQuery()==null || getTextQuery().isEmpty()){
            textFonteDoConteudo.setText("Recomendados para você");
            realizarDiscovery();

        }else{
            textFonteDoConteudo.setText("Resultado da pesquisa");
            realizarBusca();
        }

    }

    private void realizarBusca(){
        ApiService.getInstance()
                .buscarFilmes( getTextQuery(), getCurrentQueryPage())
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        if(response.isSuccessful()){
                            if(getCurrentQueryPage() == 1){
                                setTotalQueryPages(response.body().getTotal_pages());
                                setTotalQueryItems(response.body().getTotal_results());
                            }

                            trataResultados(response.body().getResults());
                        }
                        setBuscando(false);
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        setBuscando(false);
                        Toast.makeText(BuscaActivity.this, "Erro ao buscar filmes na Api", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void realizarDiscovery(){
        String teste = Genero.getIdMaior();

        ApiService.getInstance()
                .discoverFilmes( getCurrentQueryPage(), Genero.getIdMaior())
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        if(response.isSuccessful()){
                            if(getCurrentQueryPage() == 1){
                                setTotalQueryPages(response.body().getTotal_pages());
                                setTotalQueryItems(response.body().getTotal_results());
                            }

                            trataResultados(response.body().getResults());
                        }
                        setBuscando(false);
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        setBuscando(false);
                        Toast.makeText(BuscaActivity.this, "Erro ao descobrir filmes na Api", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void trataResultados(List<FilmeResponse> results){

        for(FilmeResponse item : results) {
            filtraNaoAdicionado(item);
        }
    }

    private void filtraNaoAdicionado(final FilmeResponse filmeResponse){
        ConfiguracaoFirebase.getInstanciaFirebase().getReference(ConfiguracaoFirebase.GetFilmeDataPath(filmeResponse.getId())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    Filme filme = new Filme();
                    filme.setId(filmeResponse.getId());
                    filme.setTitulo(filmeResponse.getTitle());
                    filme.setImgUrl("https://image.tmdb.org/t/p/w500" + filmeResponse.getPoster_path());
                    filmes.add(filme);
                    adapterBusca.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                setTextQuery(query);
                searchView.clearFocus();

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText==null || newText.isEmpty()){
                    setTextQuery("");
                }
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
