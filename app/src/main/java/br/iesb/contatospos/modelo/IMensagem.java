package br.iesb.contatospos.modelo;

/**
 * Created by Helton on 12/09/16.
 */
public interface IMensagem {

    void setId(final String id);
    void setMensagem(final String mensagem);
    void setSender(final String sender);
    String getMensagem();
    String getSender();
    String getId();

}
