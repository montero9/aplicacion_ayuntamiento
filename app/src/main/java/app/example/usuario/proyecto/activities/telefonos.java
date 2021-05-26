package app.example.usuario.proyecto.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import app.example.usuario.proyecto.R;
import app.example.usuario.proyecto.adapters.NoticiaAdapter;
import app.example.usuario.proyecto.models.telefono;

import java.util.ArrayList;

/**
 * Clase encargada de añadir los telefonos al listView
 */
public class telefonos extends AppCompatActivity {

    ListView lv_telefono;

    ArrayList<telefono> todosTelefonos = new ArrayList<telefono>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefonos);

        lv_telefono = (ListView) findViewById(R.id.lista_telefonos);

        //Añado los telefonos a la colección
        todosTelefonos.add(new telefono("959459453", "Ayuntamiento de Hinojos"));
        todosTelefonos.add(new telefono("959459325", "Centro médico"));
        todosTelefonos.add(new telefono("654342998","Polideportivo Municipal"));
        todosTelefonos.add(new telefono("637913965","Casa de la Cultura"));
        todosTelefonos.add(new telefono("647041622","Juzgado de Paz"));
        todosTelefonos.add(new telefono("959439510","Instituto IES El Valle"));
        todosTelefonos.add(new telefono("654343032","Policia Local"));
        todosTelefonos.add(new telefono("959439577","Centro de Adultos"));


        //Utilizamos el adaptador universal para meter la información de la forma adecuada en el imageView
        lv_telefono.setAdapter(new NoticiaAdapter(telefonos.this, R.layout.telefono_lista_layout, todosTelefonos) {
            @Override
            public void onEntrada(Object Telefono, View view) {
                if (Telefono != null) {

                    ImageView img_view_telefono = (ImageView) view.findViewById(R.id.imagen_telefono);

                    //Comprobamos que está el imagenView y añadimos la ruta de la imagen a mostrar
                    if (img_view_telefono != null) {
                        img_view_telefono.setImageResource(R.drawable.icon_telefono);
                    }

                    //Comprobamos que está el textView y añadimos el texto
                    TextView organismo = (TextView) view.findViewById(R.id.organismo);
                    if (organismo != null) {
                        organismo.setText(((telefono) Telefono).getNombre_organismo());
                    }

                }
            }
        });

        //Listener para el click en cada noticia
        lv_telefono.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {
                telefono tel_selecionado = (telefono) adapterView.getItemAtPosition(posicion);
                String numero_telf = tel_selecionado.getNum_telf();
                //Comprobamos que tenemos un número al que llamar
                if(numero_telf!=null){
                    Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+numero_telf));
                    startActivity(intent);
                }

            }
        });

    }

}
