package app.example.usuario.proyecto.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import app.example.usuario.proyecto.activities.incidencia_package.incidencia_mapa;
import app.example.usuario.proyecto.activities.turismo_package.ocio;
import app.example.usuario.proyecto.connectors.HttpHandler;
import app.example.usuario.proyecto.models.Prediccion;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Elementos de la interfaz
    TextView tv_tempactual;
    TextView tv_tempmax;
    TextView tv_tempmin;
    ImageView iv_icono;
    ImageView iv_separador;
    ImageView iv_ver_noticia;
    ImageView iv_registrar_incidencia;

    //Ruta del API, para obtener las temperaturas
    String rutaApiApixu ="https://api.apixu.com/v1/forecast.json?key=f5918cfaa0a34a17a70130031180908&q=Almonte";

    //Almacen de temperaturas, como objetos
    ArrayList<Prediccion> todasPrediccion = new ArrayList<Prediccion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.example.usuario.proyecto.R.layout.activity_main);

        //Localizo los identificadores de los elementos
        tv_tempactual=findViewById(app.example.usuario.proyecto.R.id.tv_tempactual);
        tv_tempmax=findViewById(app.example.usuario.proyecto.R.id.tv_tempmax);
        tv_tempmin=findViewById(app.example.usuario.proyecto.R.id.tv_tempmin);
        iv_icono=findViewById(app.example.usuario.proyecto.R.id.iv_icono);
        iv_separador=findViewById(app.example.usuario.proyecto.R.id.iv_separador);
        iv_ver_noticia=findViewById(app.example.usuario.proyecto.R.id.iv_ver_noticia);
        iv_registrar_incidencia=findViewById(app.example.usuario.proyecto.R.id.iv_registrar_incidencia);

        //Ejecuto el hilo que obtiene los datos del tiempo
        new obtenerMeteorologia().execute();


       Toolbar toolbar = (Toolbar) findViewById(app.example.usuario.proyecto.R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(app.example.usuario.proyecto.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, app.example.usuario.proyecto.R.string.navigation_drawer_open, app.example.usuario.proyecto.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(app.example.usuario.proyecto.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Añado los listener a las imagenes de la intefaz
        iv_ver_noticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, listado_noticias.class);
                startActivity(i);
            }
        });

        iv_registrar_incidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, incidencia_mapa.class);
                startActivity(in);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(app.example.usuario.proyecto.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Controlo sobre la opción que se pincha y actuo en consecuencia
        if (id == app.example.usuario.proyecto.R.id.noticias) {
           Intent intent = new Intent(this, listado_noticias.class);
           startActivity(intent);
        }else if(id == app.example.usuario.proyecto.R.id.turismo){
            Intent intent = new Intent(this, ocio.class);
            startActivity(intent);
        } else if (id == app.example.usuario.proyecto.R.id.historia) {
            Intent intent = new Intent(this, historia.class);
            startActivity(intent);
        } else if (id == app.example.usuario.proyecto.R.id.telefonos) {
            Intent intent = new Intent(this, telefonos.class);
            startActivity(intent);
        }else if(id == app.example.usuario.proyecto.R.id.contactar) {
            Intent intent = new Intent(this, contactar.class);
            startActivity(intent);
        }else if(id == app.example.usuario.proyecto.R.id.incidencia){
            Intent intent = new Intent(this, incidencia_mapa.class);
            startActivity(intent);
        } else if (id == app.example.usuario.proyecto.R.id.salir) {
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(app.example.usuario.proyecto.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Hilo asincrono para obtener las temperaturas de una url
     */
    public class obtenerMeteorologia extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            String infoTempActual=null;

            //Utilizo el metodo comprobarInternet antes de  solicitar noticias,
            //para evitar errores en la aplicación
            if (comprobarInternet()){

                HttpHandler hh = new HttpHandler();
                //Solicito el listado de información al servidor
                infoTempActual = hh.SolicitarInformacion(rutaApiApixu);

            }

            //Si hemos obtenidos valores del servidor
            if (infoTempActual != null) {
                try {

                    //Cogemos el objeto json y vamos accediendo por el arbol a la información que nos interese
                    JSONObject objetoJSON = new JSONObject(infoTempActual);


                    JSONObject tempActual=new JSONObject(objetoJSON.getString("current"));

                    JSONObject condition=new JSONObject(tempActual.getString("condition"));


                    JSONObject forecast=new JSONObject(objetoJSON.getString("forecast"));
                    JSONArray forecastday= forecast.getJSONArray("forecastday");
                    JSONObject principal = forecastday.getJSONObject(0);
                    JSONObject datos = principal.getJSONObject("day");

                    //Recogemos los valores
                    String temp_actual = tempActual.getString("temp_c");
                    String temp_min = datos.getString("mintemp_c");
                    String temp_max = datos.getString("maxtemp_c");
                    String icono = condition.getString("icon");


                    //Añado cada temperatura a la coleccion
                    todasPrediccion.add(new Prediccion(temp_actual, temp_max, temp_min,icono));


                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        //Tareas que se ejecutan tras la ejecución del hilo en segundo plano, en este caso mostrar la información
        //obtenida del servidor
        @Override
        protected void onPostExecute(Void resultado){
            super.onPostExecute(resultado);

            if(todasPrediccion.size()!=0) {
                iv_separador.setBackgroundColor(Color.rgb(17, 178, 225));
                tv_tempactual.setText(todasPrediccion.get(0).getTemp().toString() + "º");
                tv_tempmax.setText("Máx: " + todasPrediccion.get(0).getTemp_max().toString() + "º");
                tv_tempmin.setText("Mín: " + todasPrediccion.get(0).getTemp_min().toString() + "º");
                Picasso.get().load("http:" + todasPrediccion.get(0).getIcono().toString()).into(iv_icono);
            }
        }
    }

    /**
     * Metodo que sirve para verificar si el dispositivo tiene acceso a internet, para ello
     * hace una conexion a los dns de google, y verifica si se conecta o no
     * @return true si la conexión ha sido correcta
     * @return false si la conexión no ha sido correcta
     */
    private boolean comprobarInternet(){
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1600);
            sock.close();
            return true;
        } catch (IOException e) {
            return false; }
    }
}
