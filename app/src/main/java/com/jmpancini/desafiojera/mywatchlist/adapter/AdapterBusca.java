package com.jmpancini.desafiojera.mywatchlist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.jmpancini.desafiojera.mywatchlist.R;
import com.jmpancini.desafiojera.mywatchlist.activity.BuscaActivity;
import com.jmpancini.desafiojera.mywatchlist.config.ConfiguracaoFirebase;
import com.jmpancini.desafiojera.mywatchlist.model.Filme;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterBusca extends RecyclerView.Adapter<AdapterBusca.MyViewHolder> {

    private List<Filme> filmes;

    public AdapterBusca() {
        this.filmes = new ArrayList<>();
    }

    public void setFilmes(List<Filme> filmes) {
        this.filmes = filmes;
        notifyDataSetChanged();
    }

    public List<Filme> getFilmes() {
        return filmes;
    }

    @NonNull
    @Override
    public AdapterBusca.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_busca, parent, false);
        return new AdapterBusca.MyViewHolder( item );
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterBusca.MyViewHolder holder, int position) {

        Filme filme = filmes.get(position);

        holder.id = filme.getId();
        holder.titulo.setText(filme.getTitulo());
        Picasso.get().load(filme.getImgUrl()).into(holder.poster);

        holder.botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = ConfiguracaoFirebase.getInstanciaFirebase().getReference(ConfiguracaoFirebase.GetFilmeDataPath(holder.id)).child("assistido").setValue("false").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(holder.itemView.getContext(), holder.titulo.getText() + " foi adicionado a sua Watchlist", Toast.LENGTH_SHORT).show();
                        filmes.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return filmes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView titulo;
        ImageView poster;

        ImageButton botaoAdicionar;

        public MyViewHolder(View itemView){

            super(itemView);
            id = "";
            titulo = itemView.findViewById(R.id.textTitulo);
            poster = itemView.findViewById(R.id.imagePoster);
            botaoAdicionar = itemView.findViewById(R.id.buttonAdicionar);
        }

    }
}
