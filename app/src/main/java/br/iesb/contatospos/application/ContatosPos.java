package br.iesb.contatospos.application;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import br.iesb.contatospos.modelo.Usuario;
import br.iesb.contatospos.modelo.UsuarioLogado;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by Helton on 05/09/16.
 */
public class ContatosPos extends Application {

    private static UsuarioLogado usuarioLogado;

    private static boolean isInTask = false;

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        FacebookSdk.sdkInitialize(this);
        if (usuarioLogado == null && AccessToken.getCurrentAccessToken() != null) {
            getCredentials();
        }
    }

    public static void getCredentials() {


        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.i("LoginActivity", response.toString());
                    try {

                        final Usuario usuario = new Usuario();
                        usuario.setEmail(response.getJSONObject().getString("email"));
                        usuario.setNome(response.getJSONObject().getString("first_name"));
                        usuario.setSobrenome(response.getJSONObject().getString("last_name"));
                        usuario.setFacebookId(response.getJSONObject().getString("id"));
                        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
                            if (usuario.getNome() != null && !usuario.getNome().isEmpty()) {
                                usuarioLogado = new UsuarioLogado(usuario);
                                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        try {
                                            realm.copyToRealmOrUpdate(usuario);
                                        }catch (RealmPrimaryKeyConstraintException e){
                                            Log.i("ja existente", e.getLocalizedMessage());
                                        }

                                    }
                                });
                            }
                        } else {
                            usuarioLogado = null;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        isInTask = false;
                    }

                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
            request.setParameters(parameters);
            request.executeAsync();
            isInTask = true;
        }


    }


    public static UsuarioLogado getUsuarioLogado() {


        if (usuarioLogado == null && AccessToken.getCurrentAccessToken() != null) {

            Realm realm = Realm.getDefaultInstance();
            RealmQuery<Usuario> query = realm.where(Usuario.class);
            query.equalTo("facebookId", AccessToken.getCurrentAccessToken().getUserId());
            RealmResults<Usuario> results = query.findAll();
            if (results.size() > 0) {
                usuarioLogado = new UsuarioLogado(results.first());
            }
            realm.close();

            if (usuarioLogado == null) {
                getCredentials();
            }
        }

        if (isInTask) {
            long timeout = 10000;
            long init = System.currentTimeMillis();
            while ((System.currentTimeMillis() - init) < timeout && usuarioLogado == null) {
            }
        }

        return usuarioLogado;
    }

    public static void setUsuarioLogado(final UsuarioLogado novoUsuarioLogado) {
        usuarioLogado = novoUsuarioLogado;
    }

    public static void logout() {
        usuarioLogado = null;
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
    }

}
