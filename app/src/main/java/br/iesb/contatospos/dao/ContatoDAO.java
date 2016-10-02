package br.iesb.contatospos.dao;

import br.iesb.contatospos.modelo.Contato;
import br.iesb.contatospos.modelo.IContato;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

import android.content.ContentValues;
import android.content.Context;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Andre on 08/09/2016.
 */
public class ContatoDAO implements Closeable {

    private Realm realm;

    public ContatoDAO() {
        realm = Realm.getDefaultInstance();
    }

    public void excluir(String id) {

    }

    public void alterar(final Contato contato) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(contato);
            }
        });
    }

    public void novoContato(final IContato iContato) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Contato contato = realm.createObject(Contato.class);
                contato.setId(UUID.randomUUID().toString());
                contato.setNome(iContato.getNome());
                contato.setEmail(iContato.getEmail());
                contato.setTelefone(iContato.getTelefone());
                contato.setUriFoto(iContato.getUriFoto());
                realm.copyToRealm(contato);
            }
        });
    }

    @Override
    public void close() throws IOException {
        realm.close();
    }

}
