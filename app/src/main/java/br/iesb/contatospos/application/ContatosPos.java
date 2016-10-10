package br.iesb.contatospos.application;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import br.iesb.contatospos.activity.CadastroContatoActivity;
import br.iesb.contatospos.activity.CadastroUsuarioFacebook;
import br.iesb.contatospos.dao.ContatoDAO;
import br.iesb.contatospos.dao.UsuarioDAO;
import br.iesb.contatospos.modelo.Contato;
import br.iesb.contatospos.modelo.IContato;
import br.iesb.contatospos.modelo.IUsuario;
import br.iesb.contatospos.modelo.Usuario;
import br.iesb.contatospos.modelo.UsuarioLogado;
import br.iesb.contatospos.util.InputUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Helton on 05/09/16.
 * Garante a incializacao
 */
public class ContatosPos extends Application implements GoogleApiClient.OnConnectionFailedListener{

    private static UsuarioLogado usuarioLogado;

    private static boolean isInTask = false;

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        FacebookSdk.sdkInitialize(this);
        if (usuarioLogado == null && AccessToken.getCurrentAccessToken() != null) {
            getCredentials(getApplicationContext());
        }

    }

    public static void getCredentials(final Context context) {


//        if (AccessToken.getCurrentAccessToken() != null) {
//            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//
//                @Override
//                public void onCompleted(JSONObject object, GraphResponse response) {
//                    Log.i("LoginActivity", response.toString());
//                    try {
//
//                        final Usuario usuarioCadastrado = new UsuarioDAO().findUsuarioOnRealm("facebookId", response.getJSONObject().getString("id"));
//
//                        final Usuario usuario = (usuarioCadastrado != null) ? copyFromUsuario(usuarioCadastrado) : new Usuario();
//
//                        Contato contatoCadastrado = null;
//                        if (InputUtils.notNullOrEmpty(usuario.getContatoUsuario())) {
//                            contatoCadastrado = new ContatoDAO().findContatoOnRealm("id", usuario.getContatoUsuario());
//                        }
//
//                        final Contato contatoUsuario = (contatoCadastrado != null) ? copyFromContato(contatoCadastrado) : new Contato();
//
//                        usuario.setEmailUsuario(response.getJSONObject().getString("email"));
//                        usuario.setFacebookId(response.getJSONObject().getString("id"));
//
//                        contatoUsuario.setNome(response.getJSONObject().getString("first_name"));
//                        contatoUsuario.setSobrenome(response.getJSONObject().getString("last_name"));
//                        contatoUsuario.setEmail(response.getJSONObject().getString("email"));
//
//                        cadastroUsuario(contatoUsuario, usuario, context);
//
//                        String urlFoto = response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
//                        new CadastroUsuarioFacebook().setContato(contatoUsuario)
//                                .setUsuario(usuario).execute(urlFoto);
//
//                    } catch (JSONException | IOException e) {
//                        e.printStackTrace();
//                    } finally {
//                        isInTask = false;
//                    }
//                }
//
//            });
//            Bundle parameters = new Bundle();
//            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location, picture");
//            request.setParameters(parameters);
//            request.executeAsync();
//            isInTask = true;
//        }

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            try {
                cadastrarUsuarioFirebase(FirebaseAuth.getInstance().getCurrentUser(), context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private static void cadastrarUsuarioFirebase(final FirebaseUser firebaseUser, final Context context) throws IOException {
        final Usuario usuarioCadastrado = new UsuarioDAO().findUsuarioOnRealm("facebookId", firebaseUser.getUid());

        final Usuario usuario = (usuarioCadastrado != null) ? copyFromUsuario(usuarioCadastrado) : new Usuario();

        Contato contatoCadastrado = null;
        if (InputUtils.notNullOrEmpty(usuario.getContatoUsuario())) {
            contatoCadastrado = new ContatoDAO().findContatoOnRealm("id", usuario.getContatoUsuario());
        }

        final Contato contatoUsuario = (contatoCadastrado != null) ? copyFromContato(contatoCadastrado) : new Contato();

        usuario.setEmailUsuario(firebaseUser.getEmail());
        usuario.setFacebookId(firebaseUser.getUid());
        String[] names = firebaseUser.getDisplayName().split(" ");
        int posLastName = names.length - 1;

        contatoUsuario.setNome(firebaseUser.getDisplayName().replace(names[posLastName], "").trim());
        contatoUsuario.setSobrenome(names[posLastName]);
        contatoUsuario.setEmail(firebaseUser.getEmail());

        cadastroUsuario(contatoUsuario, usuario, context);

        String urlFoto = firebaseUser.getPhotoUrl().toString();
        new CadastroUsuarioFacebook().setContato(contatoUsuario)
                .setUsuario(usuario).execute(urlFoto);
    }

    private static void cadastroUsuario(IContato contatoUsuario, IUsuario usuario, Context context) throws IOException {
        File file = null;
        if (contatoUsuario.getUriFoto() != null) {
            file = new File(contatoUsuario.getUriFoto());
        }
        if (file == null || !file.exists()) {
            file = CadastroContatoActivity.criaArquivoParaImagem(context);
            Log.i("File path", file.getAbsolutePath());
        }
        contatoUsuario.setUriFoto(file.getAbsolutePath());
        ContatoDAO contatoDAO = new ContatoDAO();
        UUID uuid = contatoDAO.incluiOuAltera(contatoUsuario);
        String retorno = uuid.toString();

        usuario.setContatoUsuario(retorno);
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.incluiOuAltera(usuario, null);
        ContatosPos.setUsuarioLogado(new UsuarioLogado(usuario));

    }

    public static Contato copyFromContato(IContato contatoCadastrado) {
        Contato retorno = new Contato();

        retorno.setUriFoto(contatoCadastrado.getUriFoto());
        retorno.setNome(contatoCadastrado.getNome());
        retorno.setSobrenome(contatoCadastrado.getSobrenome());
        retorno.setEmail(contatoCadastrado.getEmail());
        retorno.setId(contatoCadastrado.getId());
        retorno.setTelefone(contatoCadastrado.getTelefone());

        return retorno;
    }

    public static Usuario copyFromUsuario(final IUsuario usuario) {
        Usuario retorno = new Usuario();
        retorno.setEmailUsuario(usuario.getEmailUsuario());
        retorno.setFacebookId(usuario.getFacebookId());
        retorno.setContatoUsuario(usuario.getContatoUsuario());
        retorno.setSenha(usuario.getSenha());
        return retorno;
    }

    public static UsuarioLogado getUsuarioLogado(final Context context) {


        if (usuarioLogado == null && AccessToken.getCurrentAccessToken() != null) {

            Realm realm = Realm.getDefaultInstance();
            RealmQuery<Usuario> query = realm.where(Usuario.class);
            query.equalTo("facebookId", AccessToken.getCurrentAccessToken().getUserId());
            RealmResults<Usuario> results = query.findAll();
            if (results.size() > 0) {
                usuarioLogado = new UsuarioLogado(results.first());
            }
            realm.close();

            if (usuarioLogado == null && !isInTask) {
                getCredentials(context);
            }
        }else if(usuarioLogado == null && FirebaseAuth.getInstance().getCurrentUser() != null){
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<Usuario> query = realm.where(Usuario.class);
            query.equalTo("facebookId", FirebaseAuth.getInstance().getCurrentUser().getUid());
            RealmResults<Usuario> results = query.findAll();
            if (results.size() > 0) {
                usuarioLogado = new UsuarioLogado(results.first());
            }
            realm.close();

            if (usuarioLogado == null && !isInTask) {
                getCredentials(context);
            }
        }

        if (isInTask) {
            long timeout = 10000;
            long init = System.currentTimeMillis();
            int i = 0;
            while ((System.currentTimeMillis() - init) < timeout && usuarioLogado == null) {
                i++;
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
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}