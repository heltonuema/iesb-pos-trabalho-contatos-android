package br.iesb.contatospos.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import br.iesb.contatospos.R;
import br.iesb.contatospos.exception.EntradaInvalidaException;
import br.iesb.contatospos.modelo.Usuario;
import br.iesb.contatospos.util.InputUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Tela de Cadastro
 */
public class CadastroUsuarioActivity extends AppCompatActivity {

    private AutoCompleteTextView vEmail;
    private AutoCompleteTextView vNome;
    private AutoCompleteTextView vSenha;
    private AutoCompleteTextView vConfirmacao;
    private AutoCompleteTextView vTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        vEmail = (AutoCompleteTextView) findViewById(R.id.cadastroEmail);
        vNome = (AutoCompleteTextView) findViewById(R.id.cadastroNome);
        vSenha = (AutoCompleteTextView) findViewById(R.id.cadastroSenha);
        vConfirmacao = (AutoCompleteTextView) findViewById(R.id.cadastroConfirmaSenha);
        vTelefone = (AutoCompleteTextView) findViewById(R.id.cadastroTelefone);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCadUsuario);
        toolbar.inflateMenu(R.menu.menu_tb_cadastro_usuario);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch(item.getItemId()){
                    case R.id.cadastrarUsuarioTB:
                        cadastrar();
                }

                return true;
            }
        });

        vEmail.requestFocus();

        String emailRecebido = getIntent().getStringExtra("email");
        if (emailRecebido != null && !emailRecebido.isEmpty()) {
            vEmail.setText(emailRecebido);
            vNome.requestFocus();
        }

    }

    private void cadastrar() {

        Realm realm = Realm.getDefaultInstance();
        try {
            if (vSenha.getText().toString() == null || vSenha.getText().toString().isEmpty()) {
                throw new EntradaInvalidaException("Preencha a senha", vSenha);
            }
            if (vConfirmacao.getText().toString() == null || vConfirmacao.getText().toString().isEmpty()) {
                throw new EntradaInvalidaException("Confirme a senha", vConfirmacao);
            }
            if (!(vSenha.getText().toString().equals(vConfirmacao.getText().toString()))) {
                throw new EntradaInvalidaException("As senhas nao conferem", vConfirmacao);
            }
            if (vEmail.getText().toString() == null || vEmail.getText().toString().isEmpty()){
                throw new EntradaInvalidaException("E-mail é obrigatório", vEmail);
            }
            if (InputUtils.isSenhaValida(vSenha.getText().toString(), vEmail.getText().toString(), vSenha)) {

                realm.beginTransaction();
                Usuario usuarioPersistido = realm.createObject(Usuario.class);
                usuarioPersistido.setEmail(vEmail.getText().toString());
                usuarioPersistido.setNome(vNome.getText().toString());
                usuarioPersistido.setSenha(InputUtils.geraMD5(vSenha.getText().toString()));
                realm.commitTransaction();
                Snackbar.make(vEmail, "Usuario cadastrado com sucesso", Snackbar.LENGTH_SHORT).show();
            }

        } catch (EntradaInvalidaException e) {
            if(e.getAutoCompleteTextView() != null) {
                e.getAutoCompleteTextView().setError(e.getLocalizedMessage());
            }else{
                Snackbar.make(vEmail, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            }
        } catch (RealmPrimaryKeyConstraintException e) {
            e.printStackTrace();
            Snackbar.make(vEmail, e.getLocalizedMessage().replace("Value already exists:", "Usuário já existente:"), Snackbar.LENGTH_SHORT).show();
        } finally{
            if(realm.isInTransaction()) {
                Toast.makeText(this, "Cancelando transação", Toast.LENGTH_SHORT).show();
                realm.cancelTransaction();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "teste", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

