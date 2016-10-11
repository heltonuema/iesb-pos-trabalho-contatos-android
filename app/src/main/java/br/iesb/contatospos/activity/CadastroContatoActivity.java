package br.iesb.contatospos.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import br.iesb.contatospos.R;
import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.dao.ContatoDAO;
import br.iesb.contatospos.dao.UsuarioDAO;
import br.iesb.contatospos.exception.EntradaInvalidaException;
import br.iesb.contatospos.modelo.IContato;
import br.iesb.contatospos.modelo.Usuario;
import br.iesb.contatospos.modelo.UsuarioLogado;
import br.iesb.contatospos.util.InputUtils;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;


public class CadastroContatoActivity extends AppCompatActivity implements IContato {

    public static final int FLAG_FORMULARIO_USUARIO = 1;
    public static final int FLAG_EDITA_EMAIL = 2;
    public static final int FLAG_EDITA_CAMPOS = 4;
    public static final int FLAG_VOLTAR_SUPPORT_BAR = 8;
    public static final int FLAG_SALVA_CONTATO = 16;
    public static final int FLAG_HABILITA_EXCLUIR = 32;
    public static final int FLAG_NOVO_USUARIO = 64;

    private int flags = 0;
    private String idContato = null;
    private EditText edtNome;
    private EditText edtSobrenome;
    private EditText edtTelefone;
    private EditText edtEmail;
    private ImageView fotoContato;
    private Button btnFoto;
    private EditText edtSenha;
    private EditText edtConfirmaSenha;

    private String fotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flags = getIntent().getIntExtra("flags", 0);

        setContentView(R.layout.activity_cadastra_novo_contato);

        edtNome = (EditText) habilitaEdicao(findViewById(R.id.cadastroContatoNome),
                FLAG_EDITA_CAMPOS);
        edtSobrenome = (EditText) habilitaEdicao(findViewById(R.id.cadastroContatoSobrenome),
                FLAG_EDITA_CAMPOS);
        edtTelefone = (EditText) habilitaEdicao(findViewById(R.id.cadastroContatoTelefone),
                FLAG_EDITA_CAMPOS);
        edtEmail = (EditText) habilitaEdicao(findViewById(R.id.cadastroContatoEmail),
                FLAG_EDITA_EMAIL);
        fotoContato = (ImageView) findViewById(R.id.fotoCadastroContato);
        btnFoto = (Button) findViewById(R.id.buttonAlterarFoto);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherFoto(v.getContext());
            }
        });

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbarCadContato);
        if (isOn(FLAG_FORMULARIO_USUARIO)) {
            edtTelefone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            LinearLayout formulario = (LinearLayout) findViewById(R.id.formularioContato);
            getLayoutInflater().inflate(R.layout.fragment_senha, formulario);
            edtSenha = (EditText) habilitaEdicao(findViewById(R.id.cadastroUsuarioSenha), FLAG_EDITA_CAMPOS);
            edtConfirmaSenha = (EditText) habilitaEdicao(findViewById(
                    R.id.cadastroUsuarioConfirmaSenha), FLAG_EDITA_CAMPOS);
            toolBar.setTitle("Contato");
        }

        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(isOn(FLAG_VOLTAR_SUPPORT_BAR));
        }
    }

    private boolean isOn(final int flag) {
        return (flag & flags) == flag;
    }

    private View habilitaEdicao(final View view, int flagsView) {
        view.setEnabled((flagsView & flags) == flagsView);
        return view;
    }

    public static File criaArquivoParaImagem(final Context context) throws IOException {
        String timeStamp = SimpleDateFormat.getDateTimeInstance().format(new Date()).replaceAll("/", "-");
        String imageFileName = "JPEG_" + timeStamp + "_";
        File fotosDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", fotosDir);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void getRoundedBitmap(final Context context, Bitmap bitmap, Integer id, ImageView imageView){
        Glide.with(context)
                .load(bitmap)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .placeholder(id)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        if (isOn(FLAG_NOVO_USUARIO)) {
            inflater.inflate(R.menu.menu_tb_cadastro_usuario, menu);
        } else {
            inflater.inflate(R.menu.menu_add_novo_contato, menu);
        }

        if (isOn(FLAG_HABILITA_EXCLUIR)) {
            MenuItem menuItem = menu.getItem(1);
            menuItem.setEnabled(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuContatoSalvar:
                if (isOn(FLAG_SALVA_CONTATO)) {
                    salvaContato();
                    Toast.makeText(this, "Contato salvo", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, null);
                }
                finish();

                break;
            case R.id.cadastrarUsuarioTB:
                if(isOn(FLAG_NOVO_USUARIO)){
                    cadastraUsuario();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    private void salvaContato() {

        ContatoDAO contatoDAO = new ContatoDAO();
        UUID uuid = contatoDAO.incluiOuAltera(this);
        Toast.makeText(this, "Contato salvo", Toast.LENGTH_SHORT).show();
        this.idContato = uuid.toString();
    }

    private void escolherFoto(final Context context) {
        Toast.makeText(context, "Tirando fotos", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {

                File fotoContato;

                try {
                    fotoContato = criaArquivoParaImagem(this);
                    fotoPath = fotoContato.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }


                Uri uriParaFoto = FileProvider.getUriForFile(context,
                        "br.iesb.contatospos", fotoContato);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriParaFoto);
                startActivityForResult(intent, RequestCode.CADASTRO_CONTATO_FOTO);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        RequestCode.REQUEST_WRITE_PERMISSION);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    RequestCode.REQUEST_CAMERA_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestCode.REQUEST_CAMERA_PERMISSION || requestCode == RequestCode.REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                escolherFoto(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.CADASTRO_CONTATO_FOTO && resultCode == RESULT_OK) {
            adicionaFotoGaleria();
            setPic(fotoContato, fotoPath);
        }
    }


    private void adicionaFotoGaleria() {
        File f = new File(fotoPath);
        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private void cadastraUsuario() {

//        Realm realm = Realm.getDefaultInstance();
        try {
            if (edtNome.getText().toString().isEmpty()){
                throw new EntradaInvalidaException("Informe o nome", edtNome);
            }
            if (edtEmail.getText().toString().isEmpty()) {
                throw new EntradaInvalidaException("E-mail é obrigatório", edtEmail);
            }
            if (edtSenha.getText().toString().isEmpty()) {
                throw new EntradaInvalidaException("Preencha a senha", edtSenha);
            }
            if (edtConfirmaSenha.getText().toString().isEmpty()) {
                throw new EntradaInvalidaException("Confirme a senha", edtConfirmaSenha);
            }
            if (!(edtSenha.getText().toString().equals(edtConfirmaSenha.getText().toString()))) {
                throw new EntradaInvalidaException("As senhas nao conferem", edtSenha);
            }
            if (InputUtils.isSenhaValida(edtSenha.getText().toString(), edtEmail.getText().toString(), edtSenha)) {

                UsuarioDAO usuarioDAO = new UsuarioDAO();

                if(usuarioDAO.findUsuarioOnRealm("emailUsuario", edtEmail.getText().toString()) != null){
                    throw new RealmPrimaryKeyConstraintException("Valuel already exists: " + edtEmail.getText().toString());
                }
                final Usuario usuarioPersistido = new Usuario();
//                new UsuarioDAO().incluiOuAltera(usuarioPersistido, this);
//                usuarioPersistido.setEmailUsuario(edtEmail.getText().toString());
//                usuarioPersistido.setSenha(InputUtils.geraMD5(edtSenha.getText().toString()));
//                usuarioPersistido.setContatoUsuario(idContato);
//
//                ContatosPos.setUsuarioLogado(new UsuarioLogado(usuarioPersistido));
//                final Snackbar snackbar = Snackbar.make(edtEmail, "Usuario cadastrado com sucesso", Snackbar.LENGTH_INDEFINITE);
//                snackbar.setAction("Continuar", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        snackbar.dismiss();
//                        irParaActivityPrincipal();
//                    }
//                });
//                snackbar.show();

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    final Usuario usuarioPersistido = new Usuario();
                                    usuarioPersistido.setEmailUsuario(edtEmail.getText().toString());
                                    usuarioPersistido.setSenha(InputUtils.geraMD5(edtSenha.getText().toString()));
                                    usuarioPersistido.setContatoUsuario(idContato);
                                    new UsuarioDAO().setUidFirebase(task.getResult().getUser().getUid())
                                            .incluiOuAltera(usuarioPersistido, CadastroContatoActivity.this);

                                    ContatosPos.setUsuarioLogado(new UsuarioLogado(usuarioPersistido));
                                    final Snackbar snackbar = Snackbar.make(edtEmail, "Usuario cadastrado com sucesso", Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction("Continuar", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                            irParaActivityPrincipal();
                                        }
                                    });
                                    snackbar.show();
                                }else{
                                    Toast.makeText(CadastroContatoActivity.this, "Cadastro no Firebase falhou", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        });


            }

        } catch (EntradaInvalidaException e) {
            if (e.getAutoCompleteTextView() != null) {
                e.getAutoCompleteTextView().setError(e.getLocalizedMessage());
            } else {
                Snackbar.make(edtEmail, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            }
        } catch (RealmPrimaryKeyConstraintException e) {
            e.printStackTrace();
            Snackbar.make(edtEmail, e.getLocalizedMessage().replace("Value already exists:", "Usuário já existente:"), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void irParaActivityPrincipal() {
        Intent intent = new Intent(this, ListaContatosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    public static void setPic(final ImageView fotoContato, final String fotoPath) {

        // Get the dimensions of the View
        int targetW = fotoContato.getWidth();
        int targetH = fotoContato.getHeight();


        if (targetH == 0) {
            targetH = 100;
        }
        if (targetW == 0) {
            targetW = 100;
        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fotoPath, bmOptions);
        if (bitmap != null) {
            fotoContato.setImageBitmap(getRoundedCornerBitmap(bitmap));
        }
    }

    private void deletaContato() {
        try {
            //       ContatoDAO.excluir( contato );
        } catch (Exception ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao excluir o Contato! " + ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }

    }

    @Override
    public void setId(String id) {
        this.idContato = id;
    }

    @Override
    public String getId() {
        return this.idContato;
    }

    @Override
    public String getUriFoto() {
        return fotoPath;
    }

    @Override
    public void setUriFoto(String uri) {
        fotoPath = uri;
    }

    @Override
    public String getNome() {
        return edtNome.getText().toString();
    }

    @Override
    public void setNome(final String nome) {
        edtNome.setText(nome);
    }

    @Override
    public String getSobrenome() {
        return edtSobrenome.getText().toString();
    }

    @Override
    public void setSobrenome(String sobrenome) {
        edtSobrenome.setText(sobrenome);
    }

    @Override
    public String getEmail() {
        return edtEmail.getText().toString();
    }

    @Override
    public void setEmail(final String email) {
        edtEmail.setText(email);
    }

    @Override
    public String getTelefone() {
        return edtTelefone.getText().toString();
    }

    @Override
    public void setTelefone(String telefone) {
        edtTelefone.setText(telefone);
    }
}