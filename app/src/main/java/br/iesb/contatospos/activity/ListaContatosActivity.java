package br.iesb.contatospos.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

import br.iesb.contatospos.R;
import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.fragment.IContatoFragment;
import br.iesb.contatospos.fragment.IMensagemFragment;
import br.iesb.contatospos.fragment.ListaContatoPagerAdapter;
import br.iesb.contatospos.fragment.dummy.DummyContent;
import br.iesb.contatospos.fragment.dummy.DummyContentContato;
import br.iesb.contatospos.modelo.Contato;
import io.realm.Realm;
import io.realm.RealmResults;


public class ListaContatosActivity extends AppCompatActivity implements View.OnClickListener, IContatoFragment.OnListFragmentInteractionListener, IMensagemFragment.OnListFragmentInteractionListener {

    private FloatingActionButton addNovoContato;
//    private EditText pesquisa;
    private Realm realm;
    private RealmResults<Contato> contatos;
//    private ListView listContatos;
    private ArrayAdapter<String> adpcontatos;
    private EditText searchField;
    private ImageView searchIcon;
    private ViewPager mainPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_contatos);

        toolbar = (Toolbar) findViewById(R.id.toolbar_lista_contato);
        setSupportActionBar(toolbar);

        if (ContatosPos.getUsuarioLogado() == null) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            return;
        }

        realm = Realm.getDefaultInstance();

        addNovoContato = (FloatingActionButton) findViewById(R.id.addcontato);

        contatos = realm.where(Contato.class).findAll();


        addNovoContato.setOnClickListener(this);

        mainPager = (ViewPager) findViewById(R.id.pagerListaContatos);
        if (mainPager != null) {
            setUpPager(mainPager);
        }

        tabLayout = (TabLayout) findViewById(R.id.tabsListaContatos);
        tabLayout.setupWithViewPager(mainPager);


    }

    private void setUpPager(ViewPager mainPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        mainPager.setAdapter(adapter);
    }

    @Override
    public void onListFragmentInteraction(DummyContentContato.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }


    static class Adapter extends ListaContatoPagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.addcontato:
                if(tabLayout.getSelectedTabPosition() == 0 ) {
                    Intent intent = new Intent(this, CadastroContatoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityForResult(intent, RequestCode.ANY_ACTION);
                }
                break;
        }

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
