package com.jmpancini.desafiojera.mywatchlist.config;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth referenciaAutenticacao;
    private static FirebaseDatabase instanciaFirebase;
    private static String idPerfil;


    public static String getIdPerfil() {
        idPerfil = "1";
        return idPerfil;
    }

    public static void setIdPerfil(String idPerfil) {
        ConfiguracaoFirebase.idPerfil = idPerfil;
    }

    public static String getIdUsuario(){
        FirebaseAuth autenticacao = getAutenticacaoFirebase();
        return autenticacao.getCurrentUser().getUid();
    }

    public static FirebaseAuth getAutenticacaoFirebase(){
        if(referenciaAutenticacao == null){
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;
    }

    public static FirebaseDatabase getInstanciaFirebase(){
        if(instanciaFirebase == null){
            instanciaFirebase = FirebaseDatabase.getInstance();
        }
        return instanciaFirebase;
    }

    public static DatabaseReference getReferenciaFirebase(String pathString){

        return getInstanciaFirebase().getReference(pathString);
    }

    public static Task SetValue(String pathString, String value){
        return FirebaseDatabase.getInstance().getReference(pathString).setValue(value);
    }

    public static Task RemoveValue(String pathString){
        return FirebaseDatabase.getInstance().getReference(pathString).removeValue();
    }

    public static String GetUsuarioDataPath(){
        return "usuário/" + getIdUsuario();
    }

    public static String GetPerfilDataPath(){
        return GetUsuarioDataPath() + "/perfil/" + getIdPerfil();
    }

    public static String GetFilmeDataPath(String filmeId){
        return GetPerfilDataPath() + "/filmes/" + filmeId;
    }


}
