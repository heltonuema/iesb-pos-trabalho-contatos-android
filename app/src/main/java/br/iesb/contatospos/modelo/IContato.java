package br.iesb.contatospos.modelo;

/**
 * Created by andre on 09/09/2016.
 */
public interface IContato {

     void setId(final String id);

    String getId();

    String getUriFoto();

    void setUriFoto(final String uri);

    String getNome();

    void setNome(String nome);

    String getEmail();

    void setEmail(String email);

    String getTelefone();

    void setTelefone(String telefone);

}
