package app.example.usuario.proyecto.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import app.example.usuario.proyecto.R;
import app.example.usuario.proyecto.activities.incidencia_package.formulario_incidencia;

/**
 * Clase que utiliza la librería Volley para hacer una petición http/get a un WebService
 */
public class contactar extends AppCompatActivity {

    //Defino los elementos de la interfaz
    EditText tv_nombre;
    EditText tv_email;
    EditText tv_comentario;
    TextView tv_confimacion;

    //Variable para comrprobar que se ha rellenado todos los campos
    int completados=0;

    //Patron para comprobar email
    String emailPatron = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactar);

        //Busco el identificador de los elementos
        tv_nombre=(EditText)findViewById(R.id.tv_nombre);
        tv_email=(EditText)findViewById(R.id.tv_email);
        tv_confimacion=(TextView)findViewById(R.id.tv_confimacion);
        tv_comentario=(EditText)findViewById(R.id.tv_comentario);

    }

    //Método para crear el menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_enviar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Método para actuar cuando se pinche en la opción del menu menú
    public boolean onOptionsItemSelected(MenuItem item) {

        //Comprobamos que se ha introducido información en el formulario en cada uno de los campos
        //Comprobamos el campo nombre
        if(!TextUtils.isEmpty(tv_nombre.getText().toString())){
            completados+=1;
        }else{
            tv_nombre.setError("Este campo es obligatorio");
        }

        //Comprobamos el campo email
        if((!TextUtils.isEmpty(tv_email.getText().toString()) && (tv_email.getText().toString()).matches(emailPatron))) {
            completados+=1;
        }else {
            tv_email.setError("Campo incorrecto");
        }

        //Comprobamos el campo comentario
        if(!TextUtils.isEmpty(tv_comentario.getText().toString())){
            completados+=1;
        }else {
            tv_comentario.setError("Este campo es obligatorio");
        }

        //Si todos los campos están rellenos correctamente
        if(completados==3){

            //Cogemos los valores que ha introducido el usuario, quitandoles los espacios y reemplazando por el %20
            String nombre = (tv_nombre.getText().toString().trim()).replaceAll(" ", "%20");
            String email = (tv_email.getText().toString()).trim();
            String comentario = (tv_comentario.getText().toString().trim()).replaceAll(" ", "%20");


            // Creamos la cola de peticiones, usando la librería Volley, que habremos importado (Infomación optenida de la documentación de Android Developers)
            RequestQueue cola = Volley.newRequestQueue(this);

            //Url con la información recogida del formulario
            String url = "https://hinojosapp.000webhostapp.com/hinojos/web_service/noticiaController.php?vista=insertar&nombre=" + nombre + "&email=" + email + "&comentario=" + comentario;
            //String url = "http://192.168.0.125/hinojos/web_service/noticiaController.php?vista=insertar&nombre=" + nombre + "&email=" + email + "&comentario=" + comentario;

            // Creamos un objeto StringRequest, y dentro capturamos la información que devuelve el WebService
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        //Metodo que coge lo que ha mostrado la web
                        @Override
                        public void onResponse(String response) {

                                //Verifico la respueta del servidor y se lo notifico al usuario
                            if (response.equals("Su consulta ha sido registrada correctamente")) {
                                Toast.makeText(contactar.this, "Su consulta ha sido registrada correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(contactar.this, "Su consulta NO ha sido registrada correctamente", Toast.LENGTH_SHORT).show();
                            }

                            //Limpiamos los campos y ponemos el foco en el primero
                            tv_nombre.setText("");
                            tv_comentario.setText("");
                            tv_email.setText("");
                            tv_nombre.requestFocus();

                        }
                    }, new Response.ErrorListener() {
                //Metodo en caso de error
                @Override
                public void onErrorResponse(VolleyError error) {
                    tv_confimacion.setText("No se ha podido registrar su consulta");
                    Toast.makeText(contactar.this,"No se ha podido registrar su consulta",Toast.LENGTH_LONG).show();
                }
            });

            //Añadimos la petición a la cola para que se ejecute
            cola.add(stringRequest);


            //Reseteamos la variable de comprobación
            completados=0;

        }else {
            //Reseteamos la variable de comprobación
            completados=0;
        }


        return super.onOptionsItemSelected(item);
    }


}

