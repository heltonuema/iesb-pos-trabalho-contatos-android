package br.iesb.contatospos.dao;

import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.modelo.Contato;
import br.iesb.contatospos.modelo.IContato;
import br.iesb.contatospos.modelo.Usuario;
import br.iesb.contatospos.util.InputUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

import android.content.ContentValues;
import android.content.Context;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Andre on 08/09/2016.
 */
public class ContatoDAO {

    public void excluir(String id) {

    }

    public UUID incluiOuAltera(final IContato iContato) {

        final UUID retorno = (iContato.getId() != null) ? UUID.fromString(iContato.getId()) : UUID.randomUUID();
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Contato contato = null;
                    if (InputUtils.notNullOrEmpty(iContato.getId())) {
                        RealmQuery<Contato> query = realm.where(Contato.class);
                        query.equalTo("id", iContato.getId());
                        contato = query.findAll().first();
                    }
                    if(contato == null){
                        contato = new Contato();
                    }
                    contato = new Contato();
                    contato.setId(retorno.toString());
                    contato.setNome(iContato.getNome());
                    contato.setEmail(iContato.getEmail());
                    contato.setTelefone(iContato.getTelefone());
                    contato.setUriFoto(iContato.getUriFoto());

                    realm.copyToRealmOrUpdate(contato);
                }
            });
        }
        return retorno;
    }

    public Contato findContatoOnRealm(final String field, final String value) {

        Contato retorno = null;
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmQuery<Contato> where = realm.where(Contato.class);
            where.equalTo(field, value);
            RealmResults<Contato> contatos = where.findAll();
            if (contatos.size() == 1) {
                retorno = ContatosPos.copyFromContato(contatos.first());
            }
        }
        return retorno;
    }

}
