package app.example.usuario.proyecto.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.example.usuario.proyecto.R;
import app.example.usuario.proyecto.models.Noticia;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class info_noticia extends AppCompatActivity implements View.OnClickListener{

    TextView tvFecha;
    TextView tvTitulo;
    TextView tvTexto;
    ImageView img_view;

    FloatingActionButton fab_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.example.usuario.proyecto.R.layout.activity_info_noticia);

        //Cogo los identificadores de cada textView
        tvFecha=(TextView)findViewById(app.example.usuario.proyecto.R.id.textoFecha);
        tvTitulo=(TextView)findViewById(app.example.usuario.proyecto.R.id.tituloNoticia);
        img_view=(ImageView)findViewById(app.example.usuario.proyecto.R.id.img_noticia);
        tvTexto=(TextView)findViewById(app.example.usuario.proyecto.R.id.textoNoticia);

        //Cogo el identificador de boton flotante
        fab_home=(FloatingActionButton)findViewById(app.example.usuario.proyecto.R.id.fab_home_info_not);

        //Cogemos la información que nos llega de la otra activity y obtenmos un String
        Intent intent = getIntent();
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String fechaRecibido = b.getString("fecha");
        String tituloRecibido = b.getString("titulo");
        String img_recibida= b.getString("imagen");
        String descripcionObtenida = b.getString("texto");

        //Introduzco la información el el textView
        tvFecha.setText(fechaRecibido);
        tvTitulo.setText(tituloRecibido);
        Picasso.get().load(img_recibida).into(img_view);
        tvTexto.setText(descripcionObtenida);

        //Añadimos el listener al botón flotante
        fab_home.setOnClickListener(this);

    }

    //Creamos el listener para el botón flotante
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(info_noticia.this, MainActivity.class);
        startActivity(intent);
    }
}