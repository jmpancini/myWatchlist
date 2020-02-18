package com.jmpancini.desafiojera.mywatchlist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.jmpancini.desafiojera.mywatchlist.R;
import com.jmpancini.desafiojera.mywatchlist.config.ConfiguracaoFirebase;
import com.jmpancini.desafiojera.mywatchlist.model.Filme;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterFilmes extends RecyclerView.Adapter<AdapterFilmes.MyViewHolder> {

    private List<Filme> filmes;

    public AdapterFilmes() {
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filme, parent, false);
        return new MyViewHolder( item );
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Filme filme = filmes.get(position);

        holder.id = filme.getId();
        holder.titulo.setText(filme.getTitulo());
        Picasso.get().load(filme.getImgUrl()).into(holder.poster);

        holder.botaoRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = ConfiguracaoFirebase.RemoveValue(ConfiguracaoFirebase.GetFilmeDataPath(holder.id));
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

        ImageButton botaoRemover;

        public MyViewHolder(View itemView){

            super(itemView);
            id = "";
            titulo = itemView.findViewById(R.id.textTitulo);
            poster = itemView.findViewById(R.id.imagePoster);
            botaoRemover = itemView.findViewById(R.id.buttonRemover);
        }

    }

}
