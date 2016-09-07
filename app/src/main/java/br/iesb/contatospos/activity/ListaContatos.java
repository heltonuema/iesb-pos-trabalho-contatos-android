package br.iesb.contatospos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import br.iesb.contatospos.R;
import br.iesb.contatospos.modelo.Contato;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class ListaContatos extends AppCompatActivity implements  View.OnClickListener {

    private FloatingActionButton add_novo_contato;
    private EditText Pesquisa;
    private ListView ListContatos;
    private Realm realm;
    private RealmResults<Contato> Contatos;
    private ArrayAdapter<String> adpcontatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        add_novo_contato = (FloatingActionButton)findViewById(R.id.add_novo_contato);
        Pesquisa = (EditText)findViewById(R.id.Pesquisa);
        ListContatos = (ListView) findViewById(R.id.ListaContatos);

        add_novo_contato.setOnClickListener(this);
      //  ListContatos.setOnItemClickListener();








            }


    @Override
    public void onClick(View v) {

        Intent it = new Intent(this, CadastraNovoContato.class);
        startActivity (it);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_contatos, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.acao_altera_cad:

                break;

            case R.id.acao_sair:

                break;
        }

        return super.onOptionsItemSelected(item);
    }



}
