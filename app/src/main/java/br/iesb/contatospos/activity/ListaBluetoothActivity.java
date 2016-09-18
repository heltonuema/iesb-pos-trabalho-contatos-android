package br.iesb.contatospos.activity;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import br.iesb.contatospos.R;

public class ListaBluetoothActivity extends AppCompatActivity {

    ListView listView;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_bluetooth);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) ((Integer) displayMetrics.heightPixels * 0.8);
        int width = (int)((Integer) displayMetrics.widthPixels * 0.7);

        getWindow().setLayout(width, height);

        getWindow().setElevation(10.0f);

        listView = (ListView) findViewById(R.id.lista_bluetooth);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Dispositivo não possui bluetooth", Toast.LENGTH_LONG);
            finish();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                exibeDispositivos();
            }else{
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                intent.putExtra(BluetoothAdapter.EXTRA_LOCAL_NAME, "IESB Social");
                startActivityForResult(intent, RequestCode.LIGAR_BLUETOOTH);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RequestCode.LIGAR_BLUETOOTH){
            if(resultCode == RESULT_OK){
                exibeDispositivos();
            }else{
                Toast.makeText(this, "Sem bluetooth, sem dispositivos.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void exibeDispositivos(){

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.text_view_bluetooth);


        for(BluetoothDevice device : devices){
            arrayAdapter.add(device.getName());
        }

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent returnIntent = new Intent();

                String dispositivo = (String)adapterView.getAdapter().getItem(i);;
                returnIntent.putExtra("dispositivo", dispositivo);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
