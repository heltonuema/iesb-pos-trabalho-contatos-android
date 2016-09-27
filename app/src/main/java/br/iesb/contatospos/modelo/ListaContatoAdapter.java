package br.iesb.contatospos.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;
import android.widget.TextView;
import java.util.List;
import br.iesb.contatospos.modelo.Contato;

/**
 * Created by andre on 09/09/2016.
 */

public class ListaContatoAdapter extends ArrayAdapter<IContato> implements ListAdapter {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;


    public ListaContatoAdapter(Context context, int resource){
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null ;
    }


}
