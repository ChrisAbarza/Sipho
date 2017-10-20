package com.example.chris.sipho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by salv8 on 19/10/2017.
 */

public class AdaptadorComentarios extends BaseAdapter {

    Context contexto;
    List<Comentario> ListaComentario;

    public AdaptadorComentarios(Context contexto, List<Comentario> listaComentario) {
        this.contexto = contexto;
        ListaComentario = listaComentario;
    }

    @Override
    public int getCount() {
        return ListaComentario.size();
    }

    @Override
    public Object getItem(int position) {
        return ListaComentario.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ListaComentario.get(position).getUsuario();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista = convertView;
        LayoutInflater inflate = LayoutInflater.from(contexto);

        vista = inflate.inflate(R.layout.comentarios_list_view , null);

        CircleImageView imgusr = (CircleImageView) vista.findViewById(R.id.imageViewUsuarioComent);
        TextView nomusr = (TextView) vista.findViewById(R.id.textUsrNmeComent);
        TextView fecha = (TextView) vista.findViewById(R.id.textFechaComent);
        TextView coment = (TextView) vista.findViewById(R.id.textComentComent);
        ImageView reco = (ImageView) vista.findViewById(R.id.imageViewComent);

        nomusr.setText("@"+ListaComentario.get(position).getNomusr());
        fecha.setText(ListaComentario.get(position).getFecha());
        coment.setText(ListaComentario.get(position).getComent());

        Glide.with(getApplicationContext())
                .load(ListaComentario.get(position).getImg())
                .into(imgusr);

        if(ListaComentario.get(position).getValoracion().equals("Si")){
            reco.setImageResource(R.drawable.icon_thumbs_up);
        }else{
            reco.setImageResource(R.drawable.icon_thumbs_down);
        }

        return vista;
    }
}
