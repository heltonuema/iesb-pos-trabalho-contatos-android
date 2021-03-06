package br.iesb.contatospos.activity;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import br.iesb.contatospos.R;
import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.fragment.FirebaseChatMessage;
import br.iesb.contatospos.fragment.IContatoFragment;
import br.iesb.contatospos.fragment.IMensagemFragment;
import br.iesb.contatospos.fragment.ListaContatoPagerAdapter;
import br.iesb.contatospos.fragment.dummy.DummyContent;
import br.iesb.contatospos.fragment.dummy.DummyContentContato;
import br.iesb.contatospos.modelo.Contato;
import br.iesb.contatospos.modelo.ContatoImpl;
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

        if (ContatosPos.getUsuarioLogado(this) == null) {
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
            mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPageSelected(int position) {
                    Log.i("page", String.valueOf(position));
                    if(position == 1){
                        addNovoContato.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_send, addNovoContato.getContext().getTheme()));
                    }else {
                        addNovoContato.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_name_add, addNovoContato.getContext().getTheme()));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        tabLayout = (TabLayout) findViewById(R.id.tabsListaContatos);

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mainPager);
        }

    }

    private void setUpPager(ViewPager mainPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        mainPager.setAdapter(adapter);
    }

    @Override
    public void onListFragmentInteraction(Contato item) {
        Toast.makeText(this, "Contato: " + item.getEmail(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, CadastroContatoActivity.class);
        ContatoImpl contatoItem = new ContatoImpl(item);
        intent.putExtra("contato", new Gson().toJson(contatoItem));
        startActivity(intent);
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

        switch (v.getId()) {
            case R.id.addcontato:
                if (tabLayout.getSelectedTabPosition() == 0) {
                    Intent intent = new Intent(this, CadastroContatoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("flags", CadastroContatoActivity.FLAG_EDITA_CAMPOS |
                            CadastroContatoActivity.FLAG_EDITA_EMAIL |
                            CadastroContatoActivity.FLAG_SALVA_CONTATO);
                    startActivityForResult(intent, RequestCode.ANY_ACTION);
                } else if(tabLayout.getSelectedTabPosition() == 1){
                    new FirebaseChatMessage().show(getSupportFragmentManager(), "msg");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.LISTA_BLUTOOTH) {
            if (resultCode == RESULT_OK) {
                String dispostivo = data.getStringExtra("dispositivo");
                final Snackbar snackbar = Snackbar.make(addNovoContato, String.format("Dispositivo selecionado: %s", dispostivo), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.acao_altera_cad:

                break;

            case R.id.lista_menu_search:
                break;

            case R.id.lista_menu_bluetooth:
                Intent intent = new Intent(this, ListaBluetoothActivity.class);
                startActivityForResult(intent, RequestCode.LISTA_BLUTOOTH);
                break;

            case R.id.ver_map:
                Intent intent2 = new Intent(this, MapsActivity.class);
                startActivityForResult(intent2, 0);
                break;

            case R.id.acao_sair:

                ContatosPos.logout();
                finish();
                System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }


}
