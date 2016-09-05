package br.iesb.contatospos.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import br.iesb.contatospos.R;
import br.iesb.contatospos.modelo.Contato;
import br.iesb.contatospos.util.InputUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Tela de Cadastro
 */
public class CadastroActivity extends AppCompatActivity{

    private AutoCompleteTextView vEmail;
    private AutoCompleteTextView vNome;
    private AutoCompleteTextView vSenha;
    private AutoCompleteTextView vConfirmacao;
    private AutoCompleteTextView vTelefone;
    private Button bEntrar;
    private Contato contatoNovo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        contatoNovo = new Contato();
        vEmail = (AutoCompleteTextView) findViewById(R.id.cadastroEmail);
        vNome = (AutoCompleteTextView) findViewById(R.id.cadastroNome);
        vSenha = (AutoCompleteTextView) findViewById(R.id.cadastroSenha);
        vConfirmacao = (AutoCompleteTextView) findViewById(R.id.cadastroConfirmaSenha);
        vTelefone = (AutoCompleteTextView) findViewById(R.id.cadastroTelefone);
        bEntrar = (Button) findViewById(R.id.cadastroBtnEntrar);

        vEmail.requestFocus();

        String emailRecebido = getIntent().getStringExtra("email");
        if(emailRecebido != null && !emailRecebido.isEmpty()){
            vEmail.setText(emailRecebido);
            vNome.requestFocus();
        }

        bEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contatoNovo.setEmail(vEmail.getText().toString());
                contatoNovo.setNome(vNome.getText().toString());
                contatoNovo.setSenha(vSenha.getText().toString());
                cadastrar();
            }
        });

    }

    private void cadastrar(){
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm realm = Realm.getInstance(realmConfig);

        realm.beginTransaction();

        Contato contatoPersistido = realm.createObject(Contato.class);
        contatoPersistido.setEmail(contatoNovo.getEmail());
        contatoPersistido.setNome(contatoNovo.getNome());
        contatoPersistido.setSenha(InputUtils.geraMD5(contatoNovo.getSenha()));

        realm.commitTransaction();

        Snackbar.make(bEntrar, "Usuario cadastrado com sucesso", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Toast.makeText(getApplicationContext(), "teste", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

