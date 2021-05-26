package app.example.usuario.proyecto.activities.turismo_package;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import app.example.usuario.proyecto.R;
import app.example.usuario.proyecto.activities.MainActivity;
import app.example.usuario.proyecto.activities.incidencia_package.incidencia_mapa;

public class ocio extends AppCompatActivity {

    //Elmentos de la interfaz
    ImageView iv_visitar;
    ImageView iv_comer;
    ImageView iv_dormir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocio);

        //Objetos para comprobar que hay acceso a internet
        ConnectivityManager conectivityMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoRed = conectivityMan.getActiveNetworkInfo();

        //Si NO tenemos acceso a internet mandamos a la activity principal
        if (infoRed != null) {

            //Identificadores de los elementos
            iv_visitar = findViewById(R.id.iv_visitar);
            iv_comer = findViewById(R.id.iv_comer);
            iv_dormir = findViewById(R.id.iv_dormir);

            //Eventos sobres los botones de la interfaz
            iv_visitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ocio.this, mapa_turistico.class);
                    startActivity(i);
                }
            });

            iv_comer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ocio.this, mapa_restaurantes.class);
                    startActivity(i);
                }
            });

            iv_dormir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ocio.this, mapa_alojamiento.class);
                    startActivity(i);
                }
            });

        }else {
            Intent iSiguiente = new Intent(ocio.this, MainActivity.class);
            iSiguiente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(iSiguiente);
            finish();
            Toast.makeText(ocio.this,"Es necesario acceso a internet para el apartado ocio",Toast.LENGTH_LONG).show();
        }
    }
}
