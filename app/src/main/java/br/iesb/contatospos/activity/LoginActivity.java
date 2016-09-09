package br.iesb.contatospos.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.iesb.contatospos.R;
import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.exception.EntradaInvalidaException;
import br.iesb.contatospos.modelo.Usuario;
import br.iesb.contatospos.modelo.UsuarioLogado;
import br.iesb.contatospos.util.InputUtils;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity { //implements LoaderCallbacks<Cursor> {

    Realm realm;
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LoginButton loginButtonFacebook;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
//        realmConfig = new RealmConfiguration.Builder(this).build();
        if(ContatosPos.getUsuarioLogado() != null){
            goToActivity(ListaContatosActivity.class, null);
        }
        realm = Realm.getDefaultInstance();
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        mEmailView.setNextFocusForwardId(R.id.password);
        mEmailView.setNextFocusDownId(R.id.password);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        if (mEmailSignInButton == null) throw new AssertionError();
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        loginButtonFacebook = (LoginButton) findViewById(R.id.login_button_facebook);

        loginButtonFacebook.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (!loginResult.getAccessToken().getPermissions().contains("email")) {
                    LoginManager.getInstance().logOut();
                    Snackbar.make(mEmailView, "Permissão para ler endereço de e-mail é necessária", Snackbar.LENGTH_SHORT).show();
                } else {
                    ContatosPos.getCredentials();
                    goToActivity(ListaContatosActivity.class, null);
                }

            }

            @Override
            public void onCancel() {
                Snackbar.make(loginButtonFacebook, getString(R.string.login_facebook_cancel), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Facebook", error.getLocalizedMessage());
                Snackbar.make(loginButtonFacebook, getString(R.string.facebook_login_fail), Snackbar.LENGTH_SHORT).show();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void goToActivity(Class<?> activity, String...extras) {

        Intent intent = new Intent(this, activity);

        if(extras != null){
            for(int i = 0; i < extras.length; i++){
                if(extras[i].split(",").length < 2){
                    throw new RuntimeException("Esperado chave e valor separado por virgula");
                }
                String key = extras[i].split(",")[0].trim();
                String value = extras[i].split(",")[1].trim();
                intent.putExtra(key,value);
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void cadastrar(final String email) {

        goToActivity(CadastroUsuarioActivity.class, "email,".concat(email));
//
    }

    private void populateAutoComplete() {

        if (realm != null) {
            if (realm.isClosed()) {
                realm = Realm.getDefaultInstance();
            }

            RealmQuery<Usuario> query = realm.where(Usuario.class);
            RealmResults<Usuario> results = query.findAll();
            List<String> emails = new ArrayList<>();
            for (Usuario usuario : results) {
                emails.add(usuario.getEmail());
            }

            addEmailsToAutoComplete(emails);

        }

    }

    private void attemptLogin() {


        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        try {
            InputUtils.isSenhaValida(password, email, mPasswordView);
        } catch (EntradaInvalidaException e) {
            e.getAutoCompleteTextView().setError(e.getLocalizedMessage());
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null) {
                focusView.requestFocus();
            }
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, this);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final Context context;
        private boolean usuarioInexistente = false;
        private UsuarioLogado usuarioLogado;

        UserLoginTask(String email, String password, Context context) {
            mEmail = email;
            mPassword = password;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try (Realm realm = Realm.getDefaultInstance()) {
                RealmQuery<Usuario> query = realm.where(Usuario.class);

                query.equalTo("email", mEmail);
                RealmResults<Usuario> results = query.findAll();

                if (results.size() == 0) {
                    usuarioInexistente = true;
                    return false;
                }

                if (results.size() != 1) {
                    throw new RuntimeException(getString(R.string.email_duplicado));
                }

                String pPassword = results.get(0).getSenha();
                if (pPassword != null) {
                    if(pPassword.equals(InputUtils.geraMD5(mPassword))){
                        usuarioLogado = new UsuarioLogado(results.get(0));
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                ContatosPos.setUsuarioLogado(usuarioLogado);
                Snackbar.make(mPasswordView, String.format("Fez login como %s", usuarioLogado.getEmail()), Snackbar.LENGTH_SHORT).show();
                goToActivity(ListaContatosActivity.class);
                finish();
            } else if (usuarioInexistente) {
                mEmailView.setError(getString(R.string.usuario_inexistente));
                cadastrar(mEmail);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

