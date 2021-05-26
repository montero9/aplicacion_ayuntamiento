package app.example.usuario.proyecto.connectors;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Contiene los métodos que se encargan de realizar la solicitud de información al servidor
 */
public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    //Constructor por defecto
    public HttpHandler() {
    }

    //Metodo que hace la llamada al servidor para que le devuelva la informacion
    public String SolicitarInformacion(String ruta) {
        String respuesta = null;

        try {

            //Conectamos con el servidor
            URL url = new URL(ruta);
            HttpsURLConnection conexion = (HttpsURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");

            //Leemos la respuesta del servidor, y la almacenamos en formato texto en una variable
            InputStream is = new BufferedInputStream(conexion.getInputStream());
            respuesta=transformarFlujoACadena(is);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return respuesta;
    }


    //Metodo que almacena los Stream en una cadena de texto
    private String transformarFlujoACadena(InputStream iss){
        BufferedReader br = new BufferedReader(new InputStreamReader(iss));
        StringBuilder sb = new StringBuilder();

        String fila;

        try{
            while ((fila = br.readLine()) != null){
                sb.append(fila).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                iss.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}


