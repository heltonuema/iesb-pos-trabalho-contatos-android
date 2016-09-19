package br.iesb.contatospos.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import br.iesb.contatospos.R;
import br.iesb.contatospos.modelo.Contato;
import br.iesb.contatospos.modelo.Foto;
import br.iesb.contatospos.modelo.IContato;
import io.realm.Realm;


public class CadastroContatoActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtEmail;
    private ImageView fotoContato;
    private IContato contato;
    private String fotoPath;
    private Bitmap fotoBitmap;
    private String contatoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastra_novo_contato);

        edtNome = (EditText) findViewById(R.id.cadastroContatoNome);
        edtTelefone = (EditText) findViewById(R.id.cadastroContatoTelefone);
        edtEmail = (EditText) findViewById(R.id.cadastroContatoEmail);
        fotoContato = (ImageView) findViewById(R.id.fotoCadastroContato);

        fotoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherFoto();
            }
        });

        contato = new Contato();
        contato.setId(UUID.randomUUID().toString());


        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbarCadContato);
//        toolBar.inflateMenu(R.menu.menu_add_novo_contato);
//        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.menuContatoSalvar:
//                        salvaContato();
//                        break;
//                }
//
//                return true;
//            }
//        });
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private File criaArquivoParaImagem(final String idContato) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File fotosDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File foto = File.createTempFile(imageFileName, ".jpg", fotosDir);

        fotoPath = foto.getAbsolutePath(); //"removido file:"
        return foto;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_novo_contato, menu);
//
        if (contato == null) {
            MenuItem menuItem = menu.getItem(1);
            menuItem.setEnabled(false);
        }
//        if (contato != null)
//            menu.getItem(1).setVisible(true);

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

//            case R.id.acao2:
//
//                deletaContato();
//                finish();
//
//                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void preencheDados() {
        edtNome.setText(contato.getNome());
        edtTelefone.setText(contato.getTelefone());
        edtEmail.setText(contato.getEmail());

    }

    private void salvaContato() {

        if (fotoBitmap != null) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                if (fotoBitmap.compress(Bitmap.CompressFormat.JPEG, Bitmap.DENSITY_NONE, out)) {
                    Toast.makeText(this, "Imagem salva", Toast.LENGTH_SHORT).show();
                    final String base64 = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Foto foto = realm.createObject(Foto.class);
                            foto.setPath(fotoPath);
                            foto.setBase64(base64);
                            realm.copyToRealmOrUpdate(foto);
                        }
                    });
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Contato contato = realm.createObject(Contato.class);
                contato.setId(UUID.randomUUID().toString());
                contato.setNome(edtNome.getText().toString());
                contato.setUriFoto(fotoPath);
                contato.setEmail(edtEmail.getText().toString());
                contato.setTelefone(edtTelefone.getText().toString());
                realm.copyToRealm(contato);
            }
        });
        Toast.makeText(this, "Contato salvo", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, null);
        finish();
    }


    private File fotoContatoArquivo = null;

    protected void tirarFotoEGravar() {
        Toast.makeText(this, "Tirando fotos", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        try {
            fotoContatoArquivo = criaArquivoParaImagem(contato.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (fotoContato != null) {

            startActivityForResult(intent, RequestCode.CADASTRO_CONTATO_FOTO);
        }

    }

    private void escolherFoto() {
        Toast.makeText(this, "Tirando fotos", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                File fotoContato = null;

                try {
                    fotoContato = criaArquivoParaImagem(contato.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (fotoContato != null) {
                    try {
                        FileInputStream fis = new FileInputStream(fotoContato);
                        BufferedReader reader = new BufferedReader(new FileReader(fotoContato));
                        System.out.print(reader.readLine());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Uri uriParaFoto = FileProvider.getUriForFile(getApplicationContext(), "br.iesb.contatospos", fotoContato);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriParaFoto);
                    startActivityForResult(intent, RequestCode.CADASTRO_CONTATO_FOTO);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCode.REQUEST_WRITE_PERMISSION);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RequestCode.REQUEST_CAMERA_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestCode.REQUEST_CAMERA_PERMISSION || requestCode == RequestCode.REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                escolherFoto();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.CADASTRO_CONTATO_FOTO && resultCode == RESULT_OK) {


            adicionaFotoGaleria();
            setPic();

//            if (data != null) {
//                Bundle extras = data.getExtras();
//                fotoBitmap = (Bitmap) extras.get("data"); //JPEG
//                setPic();
//            }
        }
    }


    private void adicionaFotoGaleria() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(fotoPath);
        try {
            FileInputStream fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private void setPic() {

        // Get the dimensions of the View
        int targetW = fotoContato.getWidth();
        int targetH = fotoContato.getHeight();

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
        fotoContato.setImageBitmap(bitmap);
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
}