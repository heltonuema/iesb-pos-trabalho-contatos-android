package br.iesb.contatospos.application;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import br.iesb.contatospos.modelo.Usuario;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Helton on 05/09/16.
 */
public class ContatosPos extends Application{

    private static Usuario usuarioLogado;

    @Override
    public void onCreate(){
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        FacebookSdk.sdkInitialize(this);
        if(usuarioLogado == null && AccessToken.getCurrentAccessToken() != null){
            getCredentials();
        }
    }

    public static void getCredentials() {

        final Usuario usuario = new Usuario();

        if(AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.i("LoginActivity", response.toString());
                    try {
                        usuario.setEmail(response.getJSONObject().getString("email"));
                        usuario.setNome(response.getJSONObject().getString("first_name"));
                        usuario.setSobrenome(response.getJSONObject().getString("last_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
            request.setParameters(parameters);
            request.executeAsync();
        }

        if(usuario.getEmail()!=null && !usuario.getEmail().isEmpty()){
            if(usuario.getNome()!=null && !usuario.getNome().isEmpty()){
                usuarioLogado = usuario;
            }
        }
        else{
            usuarioLogado = null;
        }
    }

    public static Usuario getUsuarioLogado(){
        return usuarioLogado;
    }

    public static void setUsuarioLogado(final Usuario novoUsuarioLogado){
        usuarioLogado = novoUsuarioLogado;
    }

}
