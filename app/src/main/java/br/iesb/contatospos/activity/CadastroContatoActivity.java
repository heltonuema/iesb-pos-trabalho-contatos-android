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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.iesb.contatospos.R;
import br.iesb.contatospos.dao.ContatoDAO;
import br.iesb.contatospos.modelo.IContato;


public class CadastroContatoActivity extends AppCompatActivity implements IContato{

    private EditText edtNome;
    private EditText edtSobrenome;
    private EditText edtTelefone;
    private EditText edtEmail;
    private ImageView fotoContato;
    private Button btnFoto;

    private String fotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastra_novo_contato);

        edtNome = (EditText) findViewById(R.id.cadastroContatoNome);
        edtSobrenome = (EditText) findViewById(R.id.cadastroContatoSobrenome);
        edtTelefone = (EditText) findViewById(R.id.cadastroContatoTelefone);
        edtEmail = (EditText) findViewById(R.id.cadastroContatoEmail);
        fotoContato = (ImageView) findViewById(R.id.fotoCadastroContato);
        btnFoto = (Button) findViewById(R.id.buttonAlterarFoto);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherFoto(v.getContext());
            }
        });

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbarCadContato);
        toolBar.setTitle("Contato");
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    protected File criaArquivoParaImagem() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File fotosDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File foto = File.createTempFile(imageFileName, ".jpg", fotosDir);
        fotoPath = foto.getAbsolutePath();
        return foto;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_novo_contato, menu);

        if (! "Contato - Editar".equals(getSupportActionBar().getTitle())) {
            MenuItem menuItem = menu.getItem(1);
            menuItem.setEnabled(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuContatoSalvar:

                salvaContato();
                Toast.makeText(this, "Contato salvo", Toast.LENGTH_SHORT).show();
                finish();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void salvaContato() {
        try (ContatoDAO contatoDAO = new ContatoDAO()) {
            contatoDAO.novoContato(this);
            Toast.makeText(this, "Contato salvo", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }

    private void escolherFoto(final Context context) {
        Toast.makeText(context, "Tirando fotos", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {

                File fotoContato = null;

                try {
                    fotoContato = criaArquivoParaImagem();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (fotoContato != null) {

                    Uri uriParaFoto = FileProvider.getUriForFile(context,
                            "br.iesb.contatospos", fotoContato);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriParaFoto);
                    startActivityForResult(intent, RequestCode.CADASTRO_CONTATO_FOTO);
                }
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

    }

    @Override
    public String getId() {
        return null;
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