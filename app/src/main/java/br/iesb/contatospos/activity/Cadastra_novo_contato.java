package br.iesb.contatospos.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import br.iesb.contatospos.R;


public class Cadastra_novo_contato extends AppCompatActivity {

    private EditText addNome;
    private EditText addTelfone;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_novo_contato);



        addNome = (EditText) findViewById(R.id.addnome);
        addTelfone = (EditText) findViewById(R.id.addtelefone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_novo_contato, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.acao1:

                break;

            case R.id.acao2:

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}