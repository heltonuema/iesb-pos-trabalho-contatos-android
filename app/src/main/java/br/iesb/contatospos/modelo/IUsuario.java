package br.iesb.contatospos.modelo;

/**
 * Created by Helton on 09/09/16.
 */
public interface IUsuario {

    String getNome();
    String getSobrenome();
    String getEmail();
    String getUriFoto();
    String getFacebookId();

    void setNome(String nome);
    void setSobrenome(String sobrenome);
    void setEmail(String email);
    void setUriFoto(String uriFoto);
    void setFacebookId(String facebookId);

}
