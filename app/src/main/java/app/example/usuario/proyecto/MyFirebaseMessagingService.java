package app.example.usuario.proyecto;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import app.example.usuario.proyecto.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    //Creamos un handler para poder ejecutar una tarea dentro del hilo
    Handler handler = new Handler(Looper.getMainLooper());

    //Metodo que se llamará automaticamente cada vez que la apliación reciba un mensaje y este en PRIMER PLANO
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //Comprobamos que el método ha recibido un mensaje
        if (remoteMessage.getNotification() != null) {

            //Cogemos del mensaje tanto el titulo como el texto
            String titulo = remoteMessage.getNotification().getTitle();
            final String texto = remoteMessage.getNotification().getBody();

            //Utilizamos el handler para poder llamar al toast dentro de un hilo
            handler.post(new Runnable() {
                public void run() {
                    //Creamos un mediaplayer para generar un sonido cuando se reciba la notificación
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.notificacion);
                    mediaPlayer.start();
                    //Metemos un toast dentro de un bucle para que se repita varias veces y de tiempo a leerlo
                    for (int i=0; i < 4; i++) {
                        Toast.makeText(getApplicationContext(), "Aviso: "+texto, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

}
