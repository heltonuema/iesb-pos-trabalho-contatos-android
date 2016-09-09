package br.iesb.contatospos.dao;

import br.iesb.contatospos.modelo.Contato;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import android.content.ContentValues;
import android.content.Context;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Andre on 08/09/2016.
 */
public class ContatoDAO implements Closeable{

    private Realm realm;

    public ContatoDAO(final Contato contato){

    }

    private ContentValues preencheContentValues(Contato contato)
    {
        ContentValues values = new ContentValues();

//        values.put(Contato.Nome    , contato.getNome());
//        values.put(Contato.Telefone    , contato.getTelefone());
//        values.put(Contato.Email    , contato.getEmail());

        return values;

    }

    public static void excluir(String id)
    {

    }

    public static void alterar(Contato contato)
    {


    }

    public static void inserir(Contato contato)
    {

    }

    @Override
    public void close() throws IOException {

    }


//    public ContatoArrayAdapter buscaContatos(Context context)
//    {
//
//        ContatoArrayAdapter adpContatos = new ContatoArrayAdapter(context, R.layout.item_contato );
//
//        Cursor cursor  =  realm.query(Contato, null, null, null, null, null, null);
//
//        if (cursor.getCount() > 0 )
//        {
//
//            cursor.moveToFirst();
//
//            do {
//
//                Contato contato = new Contato();
//                contato.setId( cursor.getLong( cursor.getColumnIndex(Contato.ID) ) );
//                contato.setNome( cursor.getString( cursor.getColumnIndex(Contato.NOME ) ) );
//                contato.setTelefone( cursor.getString( cursor.getColumnIndex(Contato.TELEFONE ) ) );
//                contato.setEmail(cursor.getString( cursor.getColumnIndex(Contato.EMAIL ) ));
//
//
//                adpContatos.add(contato);
//
//            }while (cursor.moveToNext());
//
//        }
//
//        return adpContatos;
//
//    }

}
