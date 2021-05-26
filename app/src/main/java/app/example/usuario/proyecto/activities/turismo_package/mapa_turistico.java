package app.example.usuario.proyecto.activities.turismo_package;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import app.example.usuario.proyecto.R;
import app.example.usuario.proyecto.activities.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.example.usuario.proyecto.activities.MainActivity;

public class mapa_turistico extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private MapView mapView;
    private GoogleMap gMap;
    private Geocoder geocoder;

    MarkerOptions marcador;

    LatLng place;

    LatLng pueblo=new LatLng(37.290844,  -6.380537);

    private List<Address> direciones;
    private String calle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(app.example.usuario.proyecto.R.layout.activity_mapa_turistico);

        //Objetos para comprobar que hay acceso a internet
        ConnectivityManager conectivityMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoRed = conectivityMan.getActiveNetworkInfo();

        //Si NO tenemos acceso a internet mandamos a la activity principal
        if (infoRed != null) {

            mapView=(MapView) findViewById(app.example.usuario.proyecto.R.id.mv_turistico);

            //Comprobamos que hay un mapa en la interfaz
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }

        }else{
            Intent iSiguiente = new Intent(mapa_turistico.this, MainActivity.class);
            iSiguiente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(iSiguiente);
            finish();
            Toast.makeText(mapa_turistico.this,"Es necesario acceso a internet para ver el mapa",Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Método que se ejecuta cuando el mapa está listo
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnInfoWindowClickListener(this);
        setMarcador(37.292701,-6.376176,"Iglesia Santiago el Mayor","Click para más información");
        setMarcador(37.292304,-6.380101,"Ermita Ntra. Sra. del Valle","Click para más información");
        setMarcador(37.286712,-6.385364,"Parque Los Centenales","Click para más información");
        setMarcador(37.292717,-6.376547,"Ayuntamiento de Hinojos","Click para más información");
        setMarcador(37.293394,-6.377678,"Cerrillo","Click para más información");
    }


    /**
     * Método que sirve para establecer un marcador en el mapa
     * @param lat
     * @param longi
     * @param nombre
     * @param informacion
     */
    private void setMarcador(Double lat, Double longi, String nombre, String informacion) {

        //Comprobamos que se ha proporcinado una latitud y longitud
        if (lat != 0 && longi != 0) {

            //Definimos la longitud y latitud, pasandole las variable que hemos obtenido anteriormente
            place = new LatLng(lat, longi);

            //Definimos las caracteristicas del marcador
            marcador = new MarkerOptions();
            marcador.position(place);
            marcador.title(nombre);
            marcador.snippet(informacion);

            marcador.icon(BitmapDescriptorFactory.fromResource(app.example.usuario.proyecto.R.drawable.ic_marcador_turistico));


            //Definimo parametros del mapa y del marcador
            gMap.addMarker(marcador);

            //Desplazamos la camara
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pueblo, 15));
        }

    }


    /**
     * Metodo que controla la pulsación sobre el globo del marcador y lleva a una activity con un webView
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {

        Intent navegador;

        switch (marker.getId()){
            case "m0":
                navegador = new Intent(mapa_turistico.this,web_info_marcador.class);
                navegador.putExtra("url",getResources().getString(app.example.usuario.proyecto.R.string.igle_santiago_apostol));
                startActivity(navegador);
                break;
            case "m1":
                navegador = new Intent(mapa_turistico.this,web_info_marcador.class);
                navegador.putExtra("url",getResources().getString(app.example.usuario.proyecto.R.string.ermi_senora_valle));
                startActivity(navegador);
                break;
            case "m2":
                navegador = new Intent(mapa_turistico.this,web_info_marcador.class);
                navegador.putExtra("url",getResources().getString(app.example.usuario.proyecto.R.string.parque_centenales));
                startActivity(navegador);
                break;
            case "m3":
                navegador = new Intent(mapa_turistico.this,web_info_marcador.class);
                navegador.putExtra("url",getResources().getString(app.example.usuario.proyecto.R.string.ayto_hinojos));
                startActivity(navegador);
                break;
            case "m4":
                navegador = new Intent(mapa_turistico.this,web_info_marcador.class);
                navegador.putExtra("url",getResources().getString(app.example.usuario.proyecto.R.string.cerrillo));
                startActivity(navegador);
                break;
        }
    }
}
