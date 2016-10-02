package br.iesb.contatospos.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.dao.ContatoDAO;
import br.iesb.contatospos.dao.UsuarioDAO;
import br.iesb.contatospos.exception.EntradaInvalidaException;
import br.iesb.contatospos.modelo.Contato;
import br.iesb.contatospos.modelo.Usuario;
import br.iesb.contatospos.modelo.UsuarioLogado;

/**
 * Created by Helton on 02/10/16.
 */

public class CadastroUsuarioFacebook extends AsyncTask<String, Void, String> {

    private File file;

    private Contato contato;
    private Usuario usuario;

    @Override
    protected String doInBackground(String... strings) {
        String retorno = null;
        Bitmap foto = null;
        try {
            file = new File(contato.getUriFoto());
            URL url = new URL(strings[0]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setDoInput(true);

            connection.connect();
            int resp = connection.getResponseCode();

            InputStream inputStream = connection.getInputStream();

            foto = BitmapFactory.decodeStream(inputStream);

            FileOutputStream out = new FileOutputStream(file);
            foto.compress(Bitmap.CompressFormat.JPEG, 100, out);


            out.flush();
            out.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public CadastroUsuarioFacebook setContato(final Contato contato) {
        this.contato = contato;
        return this;
    }

    public CadastroUsuarioFacebook setUsuario(final Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    @Override
    protected void onPostExecute(String uuid) {
        super.onPostExecute(uuid);
    }
}