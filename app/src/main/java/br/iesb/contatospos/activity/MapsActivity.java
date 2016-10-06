package br.iesb.contatospos.activity;


import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import br.iesb.contatospos.Manifest;
import br.iesb.contatospos.R;
import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.modelo.AlarmReceiver;
import br.iesb.contatospos.modelo.UsuarioLogado;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int MAP_PERMISSION_ACCESS_COURSE_LOCATION = 9999;
    public static final String ALARM_RECEIVED_EVENT = "br.iesb.contatospos.ALARM_MESSAGE";
    private Marker marker;
    private double latitudeUsuario;
    private double longitudeUsuario;
    private LatLng loc;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        UsuarioLogado usuarioLogado = ContatosPos.getUsuarioLogado(this);
        latitudeUsuario = usuarioLogado.getUltimaLatitude();
        longitudeUsuario = usuarioLogado.getUltimaLongitude();
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(ALARM_RECEIVED_EVENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loc = intent.getParcelableExtra("LOC");
            updateMap(loc);

            latitudeUsuario = loc.latitude;
            longitudeUsuario = loc.longitude;
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MAP_PERMISSION_ACCESS_COURSE_LOCATION);
        } else {
            createAlarm();
        }

        LatLng usuario = new LatLng(latitudeUsuario, longitudeUsuario);

        if(usuario == null){
            usuario = loc;
        }

        mMap.addMarker(new MarkerOptions().position(usuario).title("Minha ultima localizacao!"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(usuario, 16.0f));
    }

    private void createAlarm() {
        int seconds = 500;
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), seconds * 1000, pendingIntent);

        Toast.makeText(this, "Alarm set in " + seconds + " seconds", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MAP_PERMISSION_ACCESS_COURSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createAlarm();
                } else {
                    //Permissão negada
                }
                return;
            }
        }
    }

    private void updateMap(LatLng loc) {
        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(loc).title("Minha ultima localizacao!"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
    }


}


