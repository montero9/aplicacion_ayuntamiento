package app.example.usuario.proyecto.activities.incidencia_package;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.example.usuario.proyecto.R;
import app.example.usuario.proyecto.activities.MainActivity;

public class incidencia_foto extends AppCompatActivity{

    //Elementos de la interfaz
    Button bt_hacer_foto;
    Button bt_sin_foto;
    ImageView iv_foto;

    //Cogo el identificador de boton flotante
    FloatingActionButton fab_home;

    //Almacen de la fotografía
    Bitmap foto;

    //Codigos de resultados
    private static final int CAMARA_RESULTADO_FOTO_CODIGO=1;
    private static final int CAMARA_PERMISO_SOLICITUD_CODIGO=2;


    //Variable para activar el pasar a la siguiente activity
    boolean comprobadorDeSeleccion=false;

    //Ruta de la imagen en el almacenamiento interno
    String imagenRuta;
    //Intent de la camara
    Intent iCamara;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia_foto);

        iv_foto=(ImageView)findViewById(R.id.iv_foto);
        bt_hacer_foto=(Button)findViewById(R.id.bt_hacer_foto);
        bt_sin_foto=(Button)findViewById(R.id.bt_sin_foto);
        fab_home=(FloatingActionButton)findViewById(R.id.fab_home);

        //Solicitamos los permisos
        comprobarPermisos();

        //Listener para el boton hacer foto
        bt_hacer_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent para abrir la camara
                iCamara= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //Instancia de file, en la que se almacenará el fichero que contendrá la imagen
                File archivo_foto=null;

                try {
                    //Creamos un archivo temporal
                    archivo_foto=crearArchivoImagenTemporal();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Si el archivo ha sido creado, creamos una uri del archivo, se la pasamos al intent y lo ejecutamos
                if(archivo_foto!=null){
                    Uri photoURI = FileProvider.getUriForFile(incidencia_foto.this,"app.example.usuario.proyecto.provider", archivo_foto);
                    iCamara.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    iCamara.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(iCamara,CAMARA_RESULTADO_FOTO_CODIGO);
                }


            }
        });


        //Listener para no seleccionar ninguna foto
        bt_sin_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Recogo la dirección
                Intent iReceptor=getIntent();

                Intent intent= new Intent(incidencia_foto.this,formulario_incidencia.class);
                intent.putExtra("direccion",iReceptor.getStringExtra("direccion"));
                startActivity(intent);
            }
        });


        //Evento para el botón flotante
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(incidencia_foto.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    /**
     * Permite obtener el resultado de solicitar una fotografia al usuario
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);

        if ( resultCode == Activity.RESULT_OK){

            switch (requestCode) {
                case CAMARA_RESULTADO_FOTO_CODIGO:

                    //Creamos un objeto File al cual le pasamos la ruta del archivo de la foto y creamos un uri con ese File
                    File file =new File(imagenRuta);
                    Uri uri = Uri.fromFile(file);

                    //Obtenemos el contenido del archivo y lo convertimos en un bitmap para introducirlo en el imageView
                    try{
                        foto = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        iv_foto.setImageBitmap(foto);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    comprobadorDeSeleccion=true;
                    break;

                 default:
                    break;
            }

        }


    }


    /**
     * Meotodo para comprobar y solicitar los permisos de la camara
     */
    public void comprobarPermisos(){
        //Compruebo si la aplicación tiene los permisos requeridos y los solicito
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(incidencia_foto.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED  ) {
                //Solicito los permisos
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMARA_PERMISO_SOLICITUD_CODIGO);
                return;
            }

        }
    }


    /**
     * Permite obtener el resultado de la solicitud de permisos al usuario
     * @param codigoSolictud
     * @param permisos
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int codigoSolictud, @NonNull String[] permisos, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(codigoSolictud, permisos, grantResults);

        if (codigoSolictud == CAMARA_PERMISO_SOLICITUD_CODIGO) {

            //Si el permiso ha sido denegado mando al usuario a la siguiente activity
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Intent i = new Intent(incidencia_foto.this, formulario_incidencia.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                //Para evitar que usuario vuelva a esta activity si ha denegado el permiso, finalizo la activity
                finish();
            }else {
                //Si uno de los permisos está aceptado, verifico el otro
                comprobarPermisos();
            }

        }


    }


    /**
     * Metodo que crea un archivo temporal, con un nombre formado por la fecha y una serie de números diferentes
     * @return File
     * @throws IOException
     */
    private File crearArchivoImagenTemporal() throws IOException {

        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nombreImagen = "incidencia_" + fecha + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen,".jpg",storageDir);

        imagenRuta = imagen.getAbsolutePath();

        return imagen;
    }




    //---------------------------------------------------------------------------------------------
    // Métodos referentes al menú de opciones
    //---------------------------------------------------------------------------------------------

    //Metodo que crea el menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_siguiente, menu);;
        return super.onCreateOptionsMenu(menu);
    }

    //Método que añade la opción al menu
    public boolean onOptionsItemSelected(MenuItem item) {

        //Compruebo que se ha seleccionado alguna de las opciones
        if (comprobadorDeSeleccion && item.getItemId()==R.id.siguiente) {

            //Recogo el intent anterior, para posteriormente obtener la dirección
            Intent iReceptor = getIntent();

            //Creo el nuevo intent hacia la siguiente activity
            Intent intent = new Intent(incidencia_foto.this, formulario_incidencia.class);


            //Envio el bitmap y la direccion a la otra activity
            intent.putExtra("imagen", imagenRuta);
            intent.putExtra("direccion", iReceptor.getStringExtra("direccion"));
            startActivity(intent);

        }

        //Si no se ha seleccionado ninguna opción, muestro un mensaje
        if (!comprobadorDeSeleccion && item.getItemId()==R.id.siguiente) {
            Toast.makeText(incidencia_foto.this,"Primero ha de seleccionar una opción",Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);

    }


}
