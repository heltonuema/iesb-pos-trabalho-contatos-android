package br.iesb.contatospos.modelo;

/**
 * Created by Helton on 09/09/16.
 */
public interface IUsuario {

    String getContatoUsuario();
    String getEmailUsuario();
    String getFacebookId();
    String getSenha();
    double getUltimaLatitude();
    double getUltimaLongitude();

    void setEmailUsuario(String email);
    void setFacebookId(String facebookId);
    void setContatoUsuario(String uuidContato);
    void setSenha(String senha);
    void setUltimaLatitude(double latitude);
    void setUltimaLongitude(double longitude);
}
