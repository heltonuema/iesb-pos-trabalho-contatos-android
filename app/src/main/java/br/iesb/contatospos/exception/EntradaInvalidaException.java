package br.iesb.contatospos.exception;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

/**
 * Created by Helton on 06/09/16.
 */
public class EntradaInvalidaException extends Exception {

    private final EditText autoCompleteTextView;
    public EntradaInvalidaException(final String mensagem){
        super(mensagem);
        autoCompleteTextView = null;
    }

    public EntradaInvalidaException(final Throwable e){
        super(e);
        autoCompleteTextView = null;
    }

    public EntradaInvalidaException(final String mensagem, final EditText editText){
        super(mensagem);
        this.autoCompleteTextView = editText;
    }

    public EntradaInvalidaException(final Throwable e, final EditText editText){
        super(e);
        this.autoCompleteTextView = editText;
    }

    public EditText getAutoCompleteTextView(){
        return this.autoCompleteTextView;
    }

}
