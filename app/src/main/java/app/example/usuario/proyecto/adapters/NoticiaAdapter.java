package app.example.usuario.proyecto.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adaptador estandar, que permite, según el tipo de los objetos del ArrayList que reciba, introducir en listView noticias, telefonos, etc
 */
public abstract class NoticiaAdapter extends BaseAdapter {

    private ArrayList<?> todasNoticias;
    private int R_layout_IdView;
    private Context contexto;

    public NoticiaAdapter(Context contexto, int R_layout_IdView, ArrayList<?> todasNoticias){
        super();
        this.contexto=contexto;
        this.todasNoticias=todasNoticias;
        this.R_layout_IdView=R_layout_IdView;
    }

    //Metodo que coge cada item del arraylist
    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {

        //Comprobamos que nos ha llegado un valor en view, y a continuación rellenamos
        // la vista con nuestro layout personalizado
        if(view==null){
            LayoutInflater vi=(LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }

        onEntrada(todasNoticias.get(posicion), view);
        return view;
    }

    //Metodo que calcula el número de valores en el ArrayList, para iterar ese numero de veces
    @Override
    public int getCount() {
        return todasNoticias.size();
    }

    //Metodo que devuelve un objeto noticia que se encuentra en una posición concreta
    @Override
    public Object getItem(int i) {
        return todasNoticias.get(i);
    }

    //Metodo que devuelve el identificador de una noticia en una posición concreta
    @Override
    public long getItemId(int i) {
        return i;
    }

    public abstract void onEntrada(Object noticia, View view);

}
