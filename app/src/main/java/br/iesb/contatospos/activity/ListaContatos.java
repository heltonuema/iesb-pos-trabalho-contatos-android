package br.iesb.contatospos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import br.iesb.contatospos.R;


public class ListaContatos extends AppCompatActivity implements  View.OnClickListener {

    private FloatingActionButton add_novo_contato;
    private ImageButton pesquisar_contato;
    private EditText Pequisa;
    private RecyclerView ListaContatos;
    private ArrayAdapter<String> Listcontatos;

    @Override
    public void onClick(View v) {

        Intent it = new Intent(this, Cadastra_novo_contato.class);
        startActivity (it);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lista_contatos);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_novo_contato);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


}
