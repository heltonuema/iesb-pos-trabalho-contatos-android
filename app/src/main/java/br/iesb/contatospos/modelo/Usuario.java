package br.iesb.contatospos.modelo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Helton on 07/09/16.
 */
public class Usuario extends RealmObject implements IUsuario{

    @PrimaryKey
    private String emailUsuario;

    private String facebookId;
    private String senha;
    private String contatoUsuario;
    private double ultimaLongitude;
    private double ultimaLatitude;

    //TODO excluir apos os testes
    private String nome;
    private String sobrenome;
    private String uriFoto;

    @Override
    public String getFacebookId() {
        return facebookId;
    }

    @Override
    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    public double getUltimaLatitude() {
        return ultimaLatitude;
    }

    @Override
    public double getUltimaLongitude() {
        return ultimaLongitude;
    }

    @Override
    public void setUltimaLatitude(double latitude) {
        this.ultimaLatitude = latitude;
    }

    @Override
    public void setUltimaLongitude(double longitude) {
        this.ultimaLongitude = longitude;
    }

    @Override
    public String getContatoUsuario() {
        return contatoUsuario;
    }

    @Override
    public void setContatoUsuario(String uuidContato) {
        this.contatoUsuario = uuidContato;
    }

    @Override
    public String getUriFoto(){
        return uriFoto;
    }

    @Override
    public void setUriFoto(final String uri){
        this.uriFoto = uri;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getSobrenome() {
        return sobrenome;
    }

    @Override
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    @Override
    public String getEmailUsuario() {
        return emailUsuario;
    }

    @Override
    public void setEmailUsuario(String email) {
        this.emailUsuario = email;
    }

    @Override
    public String getSenha() {
        return senha;
    }

    @Override
    public void setSenha(String senha) {
        this.senha = senha;
    }

}
