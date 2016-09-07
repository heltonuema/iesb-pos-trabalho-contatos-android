package br.iesb.contatospos.exception;

import android.view.View;
import android.widget.AutoCompleteTextView;

/**
 * Created by Helton on 06/09/16.
 */
public class EntradaInvalidaException extends Exception {

    private final AutoCompleteTextView autoCompleteTextView;
    public EntradaInvalidaException(final String mensagem){
        super(mensagem);
        autoCompleteTextView = null;
    }

    public EntradaInvalidaException(final Throwable e){
        super(e);
        autoCompleteTextView = null;
    }

    public EntradaInvalidaException(final String mensagem, final AutoCompleteTextView autoCompleteTextView){
        super(mensagem);
        this.autoCompleteTextView = autoCompleteTextView;
    }

    public EntradaInvalidaException(final Throwable e, final AutoCompleteTextView autoCompleteTextView){
        super(e);
        this.autoCompleteTextView = autoCompleteTextView;
    }

    public AutoCompleteTextView getAutoCompleteTextView(){
        return this.autoCompleteTextView;
    }

}
