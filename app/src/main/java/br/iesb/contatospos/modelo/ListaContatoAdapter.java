package br.iesb.contatospos.modelo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by andre on 09/09/2016.
 */

public class ListaContatoAdapter extends ArrayAdapter<IContato> {

    private int rowRescourceID;


    public ListaContatoAdapter(Context context, int resource, List<IContato> contatos){
        super (context,resource,contatos);
        this.rowRescourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }


}
