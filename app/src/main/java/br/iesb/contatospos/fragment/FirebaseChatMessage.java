package br.iesb.contatospos.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

import br.iesb.contatospos.R;
import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.modelo.Mensagem;

/**
 * Created by Helton on 12/10/16.
 */

public class FirebaseChatMessage extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enviar Mensagem");
        final View msgView = getActivity().getLayoutInflater().inflate(R.layout.fragment_imensagem, null);
        final EditText edtMensagem =(EditText) msgView.findViewById(R.id.mensagemEditText);
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(edtMensagem != null) {

                    Mensagem mensagem = new
                            Mensagem(edtMensagem.getText().toString(),
                            ContatosPos.getUsuarioLogado(getActivity()).getEmailUsuario(), ContatosPos.getUsuarioLogado(getActivity()).getContatoUsuario());
                    FirebaseDatabase.getInstance().getReference().child("messages")
                            .push().setValue(mensagem);
                    dismiss();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setView(msgView);

        return builder.create();
    }
}
