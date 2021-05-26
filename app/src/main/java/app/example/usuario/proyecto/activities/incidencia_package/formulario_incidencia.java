package app.example.usuario.proyecto.activities.incidencia_package;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import app.example.usuario.proyecto.R;
import app.example.usuario.proyecto.activities.MainActivity;

public class formulario_incidencia extends AppCompatActivity {

    //Elementos de la interfaz
    ImageView iv_foto_recibida;
    EditText et_direccion;
    EditText et_nombre;
    EditText et_telefono;
    EditText et_detalles;

    //Variable para comprobar que se ha rellenado los dos campos
    int completados=0;

    //Url del servidor donde haremos la peticion
    String url="https://hinojosapp.000webhostapp.com/hinojos/web_service/noticiaController.php?vista=insertar_incidencia";
    //String url="http://192.168.0.125/hinojos/web_service/noticiaController.php?vista=insertar_incidencia";

    //Almacen del bitmap
    Bitmap bmp;

    //Bitmap convertido a string, para enviarlo
    String imagenString;

    //Intent con los datos recibidos de la otra activity
    Intent iReceptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_incidencia);

        //Busco los elementos de la interfaz
        iv_foto_recibida=(ImageView)findViewById(R.id.iv_foto_recibida);
        et_direccion=(EditText) findViewById(R.id.et_direccion);
        et_nombre=(EditText) findViewById(R.id.et_nombre);
        et_telefono=(EditText) findViewById(R.id.et_telefono);
        et_detalles=(EditText) findViewById(R.id.et_detalles);

        //Recogo los datos de la otra activity
        iReceptor=getIntent();

        //Compruebo si viene la foto para introducirla en el imagenView
        if(iReceptor.hasExtra("imagen")){

            //Creamos un objeto file al cual le pasamos la ruta del archivo de la foto y creamos un uri con ese File
            File file =new File(iReceptor.getStringExtra("imagen"));

            //Metemos la imagen en el imagenView
            Picasso.get().load(file).into(iv_foto_recibida);

            //Creo un Bitmap del archivo JPEG, para luego enviarlo al servidor
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmp = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
            bmp = Bitmap.createScaledBitmap(bmp,600,400,true);


        }

        //Compruebo si he recibido una dirección para almacenarla en el editText
        if(iReceptor.hasExtra("direccion")){
               et_direccion.setText(iReceptor.getStringExtra("direccion"));
        }

    }

    //Método para redirigir al usuario a la activity principal pasados un segundo
    public void enviarUsuarioAprincipal(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent2 = new Intent(formulario_incidencia.this, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
                finish();
            }

        }, 1000);
    }


    //---------------------------------------------------------------------------------------------
    // Métodos referentes al menú de opciones
    //---------------------------------------------------------------------------------------------

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_enviar_cancelar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.enviar:

                //Compruebo que los campos obligatorios estén completados
                if(!TextUtils.isEmpty(et_direccion.getText().toString())){
                    completados+=1;
                }else{
                    et_direccion.setError("Este campo es obligatorio");
                }

                if(!TextUtils.isEmpty(et_nombre.getText().toString())){
                    completados+=1;
                }else{
                    et_nombre.setError("Este campo es obligatorio");
                }

                if(!TextUtils.isEmpty(et_telefono.getText().toString())){
                    completados+=1;
                }else{
                    et_telefono.setError("Este campo es obligatorio");
                }

                if(!TextUtils.isEmpty(et_detalles.getText().toString())){
                    completados+=1;
                }else{
                    et_detalles.setError("Este campo es obligatorio");
                }


                //Si los 4 campos están rellenos envío la consulta
                if(completados==4) {

                    //Compruebo si la consulta trae imagen
                    if(iReceptor.hasExtra("imagen")) {
                        //Convertimos la imagen a un string de base64 para poder enviar por post
                        ByteArrayOutputStream bArrayStream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, bArrayStream);
                        byte[] imagenBytes = bArrayStream.toByteArray();
                        imagenString = Base64.encodeToString(imagenBytes, Base64.DEFAULT);
                    }

                    //Creamos la llamada a la url
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String respuesta) {

                                        //Obtenemos lo que devuelve la web a la que hemos hecho la petición y lo mostramos
                                        if (respuesta.equals("Su incidencia ha sido registrada correctamente")) {
                                            Toast.makeText(formulario_incidencia.this, "Su incidencia ha sido registrada correctamente", Toast.LENGTH_SHORT).show();

                                            //Si la incidencia ha sido registrada correctamente, mando al usuario a la activity principal pasado un segundo
                                            enviarUsuarioAprincipal();

                                        } else {
                                            Toast.makeText(formulario_incidencia.this, "Su incidencia NO ha sido registrada correctamente", Toast.LENGTH_SHORT).show();
                                        }

                                }
                            },
                            //Si ocurre algún error
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(formulario_incidencia.this, "Compruebe su conexión a internet", Toast.LENGTH_LONG).show();;
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            //Almacenamos en una colección, lo que queremos enviar al servidor
                            Map<String,String> params = new HashMap<String, String>();

                            if (iReceptor.hasExtra("imagen")){
                                params.put("foto", imagenString);
                            }
                            params.put("direccion",et_direccion.getText().toString());
                            params.put("nombre",et_nombre.getText().toString());
                            params.put("telefono",et_telefono.getText().toString());
                            params.put("detalles",et_detalles.getText().toString());

                            return params;
                        }
                    };

                    //Creamos una cola y añadimos la petición a la cola para que se ejecute
                    RequestQueue rQueue = Volley.newRequestQueue(formulario_incidencia.this);
                    rQueue.add(stringRequest);
                }

                //Pongo el contador a cero para la próxima consulta
                completados=0;
                break;

            case R.id.cancelar:
                Intent intentCancelar = new Intent(formulario_incidencia.this, MainActivity.class);
                //Creo un flag para que no se pueda volver a esta incidendia, y evitar errores
                intentCancelar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentCancelar);
                finish();
                break;
        }



        return super.onOptionsItemSelected(item);
    }
}
