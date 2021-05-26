package app.example.usuario.proyecto.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import app.example.usuario.proyecto.R;
import app.example.usuario.proyecto.adapters.NoticiaAdapter;
import app.example.usuario.proyecto.barra_carga.DelayedProgressDialog;
import app.example.usuario.proyecto.connectors.HttpHandler;
import app.example.usuario.proyecto.models.Noticia;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import app.example.usuario.proyecto.barra_carga.DelayedProgressDialog;
import app.example.usuario.proyecto.models.Noticia;

public class listado_noticias extends AppCompatActivity {

    //Declaro el listView en donde se mostrarán los datos
    ListView lv;

    //Dialogo para mostrar información sobre la carga de las noticias
    DelayedProgressDialog progressDialog;

    //Almacen de noticias, como objetos
    ArrayList<Noticia> todasNoticias = new ArrayList<Noticia>();

    //Ruta a donde se solicita la informacion de todas las noticias que hay
    private static String ruta = "https://hinojosapp.000webhostapp.com/hinojos/web_service/noticiaController.php?vista=todas_noticias";
    //private static String ruta = "http://192.168.0.125/hinojos/web_service/noticiaController.php?vista=todas_noticias";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(app.example.usuario.proyecto.R.layout.activity_listado_noticias);

        //Localizo el listview en donde se mostrarán los datos
        lv=(ListView)findViewById(app.example.usuario.proyecto.R.id.todasNoticias);

        //Llamo a la clase que crea el hilo en segundo plano
        new obtenerNoticias().execute();

        //Listener para el click en cada noticia
        lv.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {
                Noticia notSelecionada = (Noticia)adapterView.getItemAtPosition(posicion);
                String fecha = notSelecionada.getFecha();
                String texto = notSelecionada.getTexto();
                String imagen = notSelecionada.getImagen();
                String titulo = notSelecionada.getTitulo();

                Intent intent = new Intent(listado_noticias.this, info_noticia.class);
                intent.putExtra("fecha",fecha);
                intent.putExtra("titulo",titulo);
                intent.putExtra("imagen", imagen);
                intent.putExtra("texto",texto);
                startActivity(intent);
            }
        });

    }


    //Permite crear tarea en segundo plano, puesto que la conexión con un servidor no se puede hacer en primer plano,
    //puesto que se bloqueraría la aplicación el tiempo que el servidor tarde en responder
    private class obtenerNoticias extends AsyncTask<Void, Void, Void>{

        //Tareas que se ejecutan antes del hilo en segundo plano
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Permite mostrar un barra de carga mientras se obtine la información del servidor
            progressDialog = new DelayedProgressDialog();
            progressDialog.show(getSupportFragmentManager(), "tag");
        }

        //Tareas en segundo plano
        @Override
        protected Void doInBackground(Void... voids) {

            String infoObtenida=null;

       //Utilizo el metodo comprobarInternet antes de ejecutar solicitar noticias,
       //para evitar errores en la aplicación
        if (comprobarInternet()){
            HttpHandler hh = new HttpHandler();
            //Solicito el listado de información al servidor
            infoObtenida = hh.SolicitarInformacion(ruta);
        }else {
            System.out.println("ERROR: NO SE HA PODIDO CONECTAR A INTERNET");
        }

        //Si hemos obtenidos valores del servidor
      if (infoObtenida != null) {
                try {
                    //Cogemos el objeto json
                    JSONObject objetoJSON = new JSONObject(infoObtenida);

                    //Cogemos del Objeto objetoJSON cada elemento que se llama noticia
                    JSONArray noticias = objetoJSON.getJSONArray("noticia");

                    //Recorremos cada una de las noticias y la añadimos a la coleccion
                    for (int i = 0; i < noticias.length(); i++) {

                        JSONObject n = noticias.getJSONObject(i);

                        int id = n.getInt("id");
                        String fecha= n.getString("fecha");
                        String titulo = n.getString("titulo");
                        String imagen = n.getString("imagen");
                        String texto = n.getString("texto");


                        //Añado cada noticia a la coleccion
                        todasNoticias.add(new Noticia(id,fecha,titulo,imagen,texto));

                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Mediante runOnUiThread, podemos mandar a ejecutar, lo que contiene el run ,desde el
                // hilo principal de la aplicación, sin necesidad de esperar que se tenga que hacer lo que hay en
                // este hilo secundario
                runOnUiThread(new Runnable() {
                    @Override
                    //Le mandamos al hilo principal que muestre un texto, para que el usuario no se quede a la espera
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "No se puede obtener la información del servidor, compruebo su conexión a internet",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        //Tareas que se ejecutan tras la ejecución del hilo en segundo plano, en este caso mostrar la información
        //obtenida del servidor
        @Override
        protected void onPostExecute(Void resultado){
            super.onPostExecute(resultado);

            //Utilizamos el adaptador para meter la información obtenida de la forma adecuada en el imageView
            lv.setAdapter(new NoticiaAdapter(listado_noticias.this, app.example.usuario.proyecto.R.layout.noticia_lista_layout,todasNoticias) {
                @Override
                public void onEntrada(Object noticia, View view) {
                    if(noticia != null){

                        //Comprobamos que está el textView y añadimos el texto
                        TextView titulo=(TextView)view.findViewById(app.example.usuario.proyecto.R.id.titulo);
                        TextView fecha=(TextView)view.findViewById(app.example.usuario.proyecto.R.id.tv_fecha);
                        if(titulo!=null && fecha!=null)
                            fecha.setText(((Noticia)noticia).getFecha());
                            titulo.setText(((Noticia)noticia).getTitulo());

                        //Comprobamos que está el imagenView y añadimos la ruta de la imagen a mostrar
                        ImageView imagenVista=(ImageView) view.findViewById(app.example.usuario.proyecto.R.id.imagen);
                        if(imagenVista!=null) {
                            //Utilizo la librería Picasso para introducir la imagen en el imagenview
                            Picasso.get().load(((Noticia)noticia).getImagen()).into(imagenVista);
                        }

                    }
                }
            });

            //Cierro la barra de carga
            progressDialog.cancel();

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
