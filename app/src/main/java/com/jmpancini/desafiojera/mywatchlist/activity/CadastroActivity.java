package com.jmpancini.desafiojera.mywatchlist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.jmpancini.desafiojera.mywatchlist.R;
import com.jmpancini.desafiojera.mywatchlist.config.ConfiguracaoFirebase;
import com.jmpancini.desafiojera.mywatchlist.model.Usuario;
import com.vicmikhailau.maskededittext.MaskedEditText;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private MaskedEditText campoDataDeNascimeto;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoDataDeNascimeto = findViewById(R.id.editDataDeNascimento);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);

        botaoCadastrar = findViewById(R.id.buttonCadastrar);

        botaoCadastrar.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String textoNome = campoNome.getText().toString();
                String textoDataDeNascimento = campoDataDeNascimeto.getText().toString().replaceAll(" ","");
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if(!textoNome.isEmpty()){
                    if(textoDataDeNascimento.length()==10){
                        if(!textoEmail.isEmpty()){
                            if(!textoSenha.isEmpty()){

                                usuario = new Usuario();
                                usuario.setNome(textoNome);
                                usuario.setDataDeNascimento(textoDataDeNascimento);
                                usuario.setEmail(textoEmail);
                                usuario.setSenha(textoSenha);

                                cadastrarUsuario();

                            }else{
                                Toast.makeText(CadastroActivity.this,"Senha deve ter ao menos 6 caracteres!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(CadastroActivity.this,"Preencha o Email!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,"Data de Nascimento Invalida!" , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,"Preencha o Nome!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    ConfiguracaoFirebase.SetValue(ConfiguracaoFirebase.GetUsuarioDataPath()+"/nome", usuario.getNome());
                    ConfiguracaoFirebase.SetValue(ConfiguracaoFirebase.GetUsuarioDataPath()+"/email", usuario.getEmail());
                    ConfiguracaoFirebase.SetValue(ConfiguracaoFirebase.GetUsuarioDataPath()+"/dataDeNascimento", usuario.getDataDeNascimento());

                    finish();

                }else{

                    String excecao = "";

                    try{
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e ){
                        excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e ){
                        excecao = "E-mail Inválido";
                    }catch ( FirebaseAuthUserCollisionException e ){
                        excecao = "E-mail já cadastrado";
                    }catch ( Exception e ){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                    }

                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
