package br.iesb.contatospos.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import br.iesb.contatospos.R;
import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.modelo.Contato;
import io.realm.Realm;
import io.realm.RealmResults;


public class ListaContatosActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton addNovoContato;
//    private EditText pesquisa;
    private Realm realm;
    private RealmResults<Contato> contatos;
    private ListView listContatos;
    private ArrayAdapter<String> adpcontatos;
    private EditText searchField;
    private ImageView searchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContatosPos.getUsuarioLogado() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            return;
        }


        searchIcon = (ImageView) findViewById(R.id.imv_tb_search);
        searchField = (EditText) findViewById(R.id.edt_tb_search);

        if(searchIcon == null){
            Toast.makeText(this, "Search icon null", Toast.LENGTH_SHORT).show();
        }

//        searchIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (searchField.getVisibility() == View.VISIBLE) {
//                    searchField.setVisibility(View.INVISIBLE);
//                } else {
//                    searchField.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_lista_contatos);

        addNovoContato = (FloatingActionButton) findViewById(R.id.addcontato);

//        pesquisa = (EditText) findViewById(R.id.Pesquisa);
        contatos = realm.where(Contato.class).findAll();
        listContatos = (ListView) findViewById(R.id.ListaContatos);


        addNovoContato.setOnClickListener(this);
        //listContatos.setOnItemClickListener((AdapterView.OnItemClickListener) this);


    }


    @Override
    public void onClick(View v) {

        Intent it = new Intent(this, CadastroContatoActivity.class);
        startActivity(it);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_contatos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.acao_altera_cad:

                break;

            case R.id.acao_sair:

                ContatosPos.logout();
                finish();
                System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }


}
