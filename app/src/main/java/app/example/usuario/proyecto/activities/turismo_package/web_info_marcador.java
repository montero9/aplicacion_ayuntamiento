package app.example.usuario.proyecto.activities.turismo_package;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Browser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.Toolbar;

import app.example.usuario.proyecto.R;
import app.example.usuario.proyecto.activities.MainActivity;

public class web_info_marcador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_info_marcador);

        //Objetos para comprobar que hay acceso a internet
        ConnectivityManager conectivityMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoRed = conectivityMan.getActiveNetworkInfo();

        //Si NO tenemos acceso a internet mandamos a la activity principal
        if (infoRed != null) {

            //Elemento de la interfza
            WebView wvInformacion = new WebView(this);
            wvInformacion.getSettings().setJavaScriptEnabled(true);
            wvInformacion.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            setContentView(wvInformacion);

            //Recogemos la url
            Intent i = getIntent();

            //Cargamos la url en el webView
            wvInformacion.loadUrl(i.getStringExtra("url"));
        }else{
            Intent iSiguiente = new Intent(web_info_marcador.this, MainActivity.class);
            iSiguiente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(iSiguiente);
            finish();
            Toast.makeText(web_info_marcador.this,"Es necesario acceso a internet para ver más información",Toast.LENGTH_LONG).show();
        }
    }



}
