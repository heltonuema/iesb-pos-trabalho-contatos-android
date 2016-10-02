package br.iesb.contatospos.modelo;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Helton on 03/09/16.
 */
public class Contato extends RealmObject implements IContato {


    @PrimaryKey
    private String id;

    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String uriFoto;

    public void setId(final String id){
        this.id = new String(id);
    }

    public String getId(){
        return this.id;
    }

    public String getUriFoto(){


        return uriFoto;
    }

    @Override
    public String getSobrenome() {
        return sobrenome;
    }

    @Override
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setUriFoto(final String uri){
        this.uriFoto = uri;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = new String(nome);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = new String(email);
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = new String(telefone);
    }

}
