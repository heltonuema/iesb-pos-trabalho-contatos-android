package br.iesb.contatospos.activity;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.iesb.contatospos.R;
import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.modelo.IUsuario;
import br.iesb.contatospos.modelo.Usuario;
import br.iesb.contatospos.modelo.UsuarioLogado;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private double latitudeUsuario;
    private double longitudeUsuario;

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
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng usuario = new LatLng(latitudeUsuario, longitudeUsuario);
        mMap.addMarker(new MarkerOptions().position(usuario).title("Minha ultima localizacao"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(usuario, 16.0f));
    }
}