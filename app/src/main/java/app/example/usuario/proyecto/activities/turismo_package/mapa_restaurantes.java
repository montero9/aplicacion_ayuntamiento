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

public class mapa_restaurantes extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

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
        setContentView(R.layout.activity_mapa_restaurantes);

        //Objetos para comprobar que hay acceso a internet
        ConnectivityManager conectivityMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoRed = conectivityMan.getActiveNetworkInfo();

        //Si NO tenemos acceso a internet mandamos a la activity principal
        if (infoRed != null) {

            mapView = (MapView) findViewById(R.id.mv_restaurantes);

            //Comprobamos que hay un mapa en la interfaz
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }

        }else{
            Intent iSiguiente = new Intent(mapa_restaurantes.this, MainActivity.class);
            iSiguiente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(iSiguiente);
            finish();
            Toast.makeText(mapa_restaurantes.this,"Es necesario acceso a internet para ver el mapa",Toast.LENGTH_LONG).show();
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
        setMarcador(37.285735,-6.386511,"Restaurante la Choza","Click para más información");
        setMarcador(37.288882,-6.379288,"Restaurante la Bodeguita ","Click para más información");
        setMarcador(37.291560,-6.377992,"Bar el Cucu","Click para más información");
        setMarcador(37.291724,-6.378311,"Bar Central","Click para más información");
        setMarcador(37.290863,-6.376295,"Pizzería los Ratones Coloraos","Click para más información");
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

            marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marcador_restaurantes));


            //Definimo parametros del mapa y del marcador
            gMap.addMarker(marcador);

            //Desplazamos la camara
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pueblo, 15));


            //Objeto que posee un método, al cual le pasamos longitud y latitud y nos devuelve la dirección
            geocoder = new Geocoder(this, Locale.getDefault());

            //Obtenmos la dirección de la longitud y latitud obtenida
            try {
                direciones = geocoder.getFromLocation(lat, longi, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Obtenemos la inforación de la direción que nos interesa
            calle = direciones.get(0).getAddressLine(0);

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
                navegador = new Intent(mapa_restaurantes.this,web_info_marcador.class);
                navegador.putExtra("url",getResources().getString(R.string.choza_hinojos));
                startActivity(navegador);
                break;
            case "m1":
                navegador = new Intent(mapa_restaurantes.this,web_info_marcador.class);
                navegador.putExtra("url",getResources().getString(R.string.bodeguita));
                startActivity(navegador);
                break;
            case "m2":
                navegador = new Intent(mapa_restaurantes.this,web_info_marcador.class);
                navegador.putExtra("url",getResources().getString(R.string.cucu));
                startActivity(navegador);
                break;
            case "m3":
                navegador = new Intent(mapa_restaurantes.this,web_info_marcador.class);
                navegador.putExtra("url",getResources().getString(R.string.bar_central));
                startActivity(navegador);
                break;
            case "m4":
                navegador = new Intent(mapa_restaurantes.this,web_info_marcador.class);
                navegador.putExtra("url",getResources().getString(R.string.ratones));
                startActivity(navegador);
                break;
        }
    }
}
