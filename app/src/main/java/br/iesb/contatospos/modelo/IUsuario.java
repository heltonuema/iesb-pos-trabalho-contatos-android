package br.iesb.contatospos.modelo;

/**
 * Created by Helton on 09/09/16.
 */
public interface IUsuario {

    //TODO excluir apos os testes
    String getNome();
    String getSobrenome();
    String getUriFoto();

    String getContatoUsuario();
    String getEmailUsuario();
    String getFacebookId();
    String getSenha();
    double getUltimaLatitude();
    double getUltimaLongitude();

    //TODO excluir apos os testes
    void setNome(String nome);
    void setSobrenome(String sobrenome);
    void setUriFoto(String uriFoto);

    void setEmailUsuario(String email);
    void setFacebookId(String facebookId);
    void setContatoUsuario(String uuidContato);
    void setSenha(String senha);
    void setUltimaLatitude(double latitude);
    void setUltimaLongitude(double longitude);
}
