package com.example.chris.sipho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by salv8 on 03/12/2017.
 */

public class AdaptadorUsr extends BaseAdapter {

    Context contexto;
    List<Usuario> ListaUsuarios;
    ImageView imagenUsuario;

    public AdaptadorUsr(Context contexto, List<Usuario> listaUsuarios) {
        this.contexto = contexto;
        ListaUsuarios = listaUsuarios;
    }
    @Override
    public int getCount() {
        return ListaUsuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return ListaUsuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        long i= Long.valueOf(ListaUsuarios.get(position).getId());
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista = convertView;
        LayoutInflater inflate = LayoutInflater.from(contexto);

        vista = inflate.inflate(R.layout.usr_adapter , null);
        imagenUsuario = (ImageView) vista.findViewById(R.id.imageViewUsrAdapter);
        TextView nombreCompleto= (TextView) vista.findViewById(R.id.textViewNombreUsrAdapter);
        TextView nombreCorto = (TextView) vista.findViewById(R.id.textViewUsrAdapter);

        nombreCompleto.setText("@"+ListaUsuarios.get(position).getNombreCompleto());
        nombreCorto.setText(ListaUsuarios.get(position).getNombreUsuario());

        Glide.with(getApplicationContext())
                .load(ListaUsuarios.get(position).getImagen())
                .signature((new StringSignature(String.valueOf(System.currentTimeMillis()))))
                .into(imagenUsuario);

        return vista;
    }
}
