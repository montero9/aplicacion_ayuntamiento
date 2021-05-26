package app.example.usuario.proyecto.activities.incidencia_package;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.example.usuario.proyecto.activities.MainActivity;

public class incidencia_mapa extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    //Creo los objetos para los mapas
    private MapView mapView;
    private GoogleMap gMap;
    private Geocoder geocoder;

    //Almacen de las opciones del marcador
    MarkerOptions marcador;

    //Objetos y variables para el gps y la dirección que se obtenga
    private LocationManager locationManager;
    private List<Address> direciones;
    private String calle;
    double lat = 0;
    double longi = 0;
    LatLng place;

    //Componentes de la interfaz
    TextView tv_direccion;

    //Almacen para respuesta de solicitud de permisos del gps
    private final int RESPUESTA_ACCESS_FINE_LOCATION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.example.usuario.proyecto.R.layout.activity_incidencia_mapa);

        //Objetos para comprobar que hay acceso a internet
        ConnectivityManager conectivityMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoRed = conectivityMan.getActiveNetworkInfo();

        //Si NO tenemos acceso a internet mandamos a la siguiente activity
        if (infoRed == null) {
            Intent iSiguiente = new Intent(incidencia_mapa.this, MainActivity.class);
            iSiguiente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(iSiguiente);
            finish();
            Toast.makeText(incidencia_mapa.this,"Es necesario acceso a internet para registrar una incidencia",Toast.LENGTH_LONG).show();

        } else {
            //Busco el identificador de los elementos del ui
            mapView = (MapView) findViewById(app.example.usuario.proyecto.R.id.map_incidencia);
            tv_direccion = (TextView) findViewById(app.example.usuario.proyecto.R.id.tv_direccion);


            //Compruebo que existe el mapView y  llamo a los diferentes metodos
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }

            final LocationManager gestorPermisos = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            //Si el GPS no está disponible creo una ventana para que usuario active el GPS
            if (!gestorPermisos.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Su dispositivo tiene el GPS desactivado, ¿Desea activarlo?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                Intent i = new Intent(incidencia_mapa.this, incidencia_foto.class);
                                startActivity(i);
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }

            //Compruebo si la aplicación tiene los permisos requeridos y los solicito
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    //Solicito los permisos
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RESPUESTA_ACCESS_FINE_LOCATION);

                    return;

                } else {

                    //Objeto para acceder al gps y los tiempos de actulización de la localización del gps
                    locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 50, this);
                }
            }

        }
    }

    //Metodo que se encarga de controlar las repuesta del usuario a la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int codigoSolictud, @NonNull String[] permisos, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(codigoSolictud, permisos, grantResults);

        if (codigoSolictud == RESPUESTA_ACCESS_FINE_LOCATION) {

            //Si el permiso ha sido denegado mando al usuario a la siguiente activity
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Intent i = new Intent(incidencia_mapa.this, incidencia_foto.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                //Para evitar que usuario vuelva a esta activity si ha denegado el permiso, finalizo la activity
                finish();
            } else {

                //Objeto para acceder al gps y los tiempos de actulización de la localización del gps
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                //Comprobamos el permiso de nuevo
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RESPUESTA_ACCESS_FINE_LOCATION);

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 50, this);
            }

        }

    }


    /**
     * Metodo que se ejecuta cuando el mapa esté listo
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

            //Instanciamos el objeto
            gMap = googleMap;

    }


    //---------------------------------------------------------------------------------------------
    // Métodos obligatorios al implementar la interfaz LocationListener, son listerners que están
    // a la espera de un suceso
    //---------------------------------------------------------------------------------------------

    @Override
    public void onLocationChanged(Location location) {
        //Cada vez que la posición cambie en los metros que pusimos
        // anteriormente llamará al metodo que dispone el marcador en el mapa
        setMarcador();
        //Liberamos el servidio del gps
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        setMarcador();
    }

    @Override
    public void onProviderDisabled(String s) {
        //Limpiamos el mapa
        gMap.clear();
        calle=null;
        tv_direccion.setText("Esperando señal GPS...");
        tv_direccion.setTextColor(getResources().getColor(app.example.usuario.proyecto.R.color.gpsError));
    }


    //-------------------------------------------------------------------------------------------
    //Metodo para definir el marcador en el mapa
    //-------------------------------------------------------------------------------------------

    private void setMarcador() {

        //Limpiamos viejos marcadores
        gMap.clear();

        //Comprobamos los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Solicitamos la localización del GPS
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);


        //Si no se ha podido obtener la localización del GPS, solicitamos la de la red
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        //Y si se ha obtenido la localización del GPS o de la red, obtengo la latitud y la longitud
        if (location != null) {
            lat = location.getLatitude();
            longi = location.getLongitude();

        }

        //Comprobamos que se ha obtenido una latitud y longitud
        if (lat != 0 && longi != 0) {

            //Definimos la longitud y latitud, pasandole las variable que hemos obtenido anteriormente
            place = new LatLng(lat, longi);

            //Definimos las caracteristicas del marcador
            marcador = new MarkerOptions();
            marcador.position(place);
            marcador.title("Zona del incidencia");
            marcador.icon(BitmapDescriptorFactory.fromResource(app.example.usuario.proyecto.R.drawable.ic_incidencia_mapa));


            //Definimo parametros del mapa y del marcador
            gMap.setMinZoomPreference(5);
            gMap.addMarker(marcador);

            //Desplazamos la camara
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place, 17));


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

            //Mostramos por pantalla la dirección
            tv_direccion.setText(direciones.get(0).getAddressLine(0));
            tv_direccion.setTextColor(getResources().getColor(app.example.usuario.proyecto.R.color.gpsCorrecto));

        }

    }


    //---------------------------------------------------------------------------------------------
    // Métodos referentes al menú de opciones
    //---------------------------------------------------------------------------------------------

    /**
     * Método para crear el menu
     * @param menu
     * @return
     */

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(app.example.usuario.proyecto.R.menu.menu_siguiente, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Métodod para actuar cuando se pinche el menú
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item){

        if(calle != null && calle != ""){

            Intent intent = new Intent(this,incidencia_foto.class);
            intent.putExtra("direccion",calle);
            locationManager=null;
            startActivity(intent);


        }

        return super.onOptionsItemSelected(item);
    }


}
