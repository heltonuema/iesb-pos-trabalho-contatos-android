package br.iesb.contatospos.exception;

/**
 * Created by Helton on 06/09/16.
 */
public class EntradaInvalidaException extends Exception {

    public EntradaInvalidaException(final String mensagem){
        super(mensagem);
    }

    public EntradaInvalidaException(final Throwable e){
        super(e);
    }
}
