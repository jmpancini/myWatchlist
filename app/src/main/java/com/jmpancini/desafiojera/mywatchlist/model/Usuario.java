package com.jmpancini.desafiojera.mywatchlist.model;

public class Usuario {
    private String Nome;
    private String Email;
    private String Senha;
    private String DataDeNascimento;


    public Usuario() {
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public String getDataDeNascimento() {
        return DataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        DataDeNascimento = dataDeNascimento;
    }
}
