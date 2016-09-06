package br.iesb.contatospos.modelo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Helton on 03/09/16.
 */
public class Contato extends RealmObject {

    @PrimaryKey
    private String email;

    private String nome;
    private String sobrenome;
    private String senha;
    private String uriFoto;

    public String getUriFoto(){
        return new String(uriFoto);
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
