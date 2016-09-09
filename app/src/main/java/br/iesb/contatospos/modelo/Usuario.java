package br.iesb.contatospos.modelo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Helton on 07/09/16.
 */
public class Usuario extends RealmObject implements IUsuario{

    @PrimaryKey
    private String email;

    private String nome;
    private String sobrenome;
    private String senha;
    private String uriFoto;

    @Override
    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    private String facebookId;



    public String getUriFoto(){
        return uriFoto;
    }

    public void setUriFoto(final String uri){
        this.uriFoto = uri;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
