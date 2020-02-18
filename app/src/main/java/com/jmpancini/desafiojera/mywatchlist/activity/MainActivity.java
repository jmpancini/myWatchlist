package com.jmpancini.desafiojera.mywatchlist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jmpancini.desafiojera.mywatchlist.R;
import com.jmpancini.desafiojera.mywatchlist.config.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    //public static final int REQUEST_EXIT = 1250;

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth usuario = FirebaseAuth.getInstance();
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        verificarUsuarioLogado();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void btEntrar(View view){
        startActivity(new Intent(this, LoginActivity.class));
        //startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_EXIT);
    }
    public void btCadastrar(View view){
        startActivity(new Intent( this, CadastroActivity.class));
        //startActivityForResult(new Intent( this, CadastroActivity.class), REQUEST_EXIT);
    }
    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        if( autenticacao.getCurrentUser() != null){
            startActivity(new Intent(this, WatchlistActivity.class));
        }
    }
}
