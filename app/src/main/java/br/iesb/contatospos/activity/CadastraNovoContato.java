package br.iesb.contatospos.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import br.iesb.contatospos.R;
import br.iesb.contatospos.dao.ContatoDAO;
import br.iesb.contatospos.modelo.Contato;


public class CadastraNovoContato extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtEmail;
    private ImageView fotoContato;
    private Contato contato;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastra_novo_contato);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        fotoContato = (ImageView) findViewById(R.id.fotocontato);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_novo_contato, menu);

        if (contato.getId() != null)
            menu.getItem(1).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.acao1:

                salvaContato();
                finish();

                break;

            case R.id.acao2:

                deletaContato();
                finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void preencheDados()
    {
        edtNome.setText( contato.getNome() );
        edtTelefone.setText( contato.getTelefone() );
        edtEmail.setText( contato.getEmail());

    }

    private void salvaContato() {

        try
        {

            contato.setNome(edtNome.getText().toString());
            contato.setTelefone(edtTelefone.getText().toString());
            contato.setEmail(edtEmail.getText().toString());

            if (contato.getId() == null)
                ContatoDAO.inserir(contato);
            else
                ContatoDAO.alterar(contato);

        }catch(Exception ex)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao salvar o Contato! " + ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }

    }


    private void deletaContato () {
        try
        {
            ContatoDAO.excluir( contato.getId() );

        }catch(Exception ex)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao excluir o Contato! " + ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }

    }
}