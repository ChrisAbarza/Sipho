package com.example.chris.sipho;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.util.List;

import static com.example.chris.sipho.R.id.photoImageView;
import static com.facebook.FacebookSdk.getApplicationContext;
import static java.lang.System.load;

/**
 * Created by salv8 on 02/10/2017.
 */

public class AdaptadorCateOferta extends BaseAdapter {

    Context contexto;
    List<Oferta> ListaOfertas;
    ImageView imagenCate;

    public AdaptadorCateOferta(Context contexto, List<Oferta> listaOfertas) {
        this.contexto = contexto;
        ListaOfertas = listaOfertas;
    }

    public int getCount() {
        return ListaOfertas.size();
    }

    @Override
    public Object getItem(int position) {
        return ListaOfertas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ListaOfertas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista = convertView;
        LayoutInflater inflate = LayoutInflater.from(contexto);

        vista = inflate.inflate(R.layout.ofertas_list_view , null);

        ImageView imagenOferta = (ImageView) vista.findViewById(R.id.imageViewOfertaList);
        imagenCate = (ImageView) vista.findViewById(R.id.imageViewCateList);
        TextView textFecha = (TextView) vista.findViewById(R.id.textViewFechaList);
        TextView textUsuario = (TextView) vista.findViewById(R.id.textViewUsuarioList);
        TextView textNombre = (TextView) vista.findViewById(R.id.textViewNombreList);
        TextView textPrecio = (TextView) vista.findViewById(R.id.textViewPrecioList);
        TextView textDescripcion = (TextView) vista.findViewById(R.id.textViewDescripcionList);

        textFecha.setText(ListaOfertas.get(position).getFecha().toString());
        textUsuario.setText("@"+ListaOfertas.get(position).getUsuario());
        textNombre.setText(ListaOfertas.get(position).getNomOferta());
        textPrecio.setText(String.valueOf("$"+ListaOfertas.get(position).getPrecioOferta()));
        textDescripcion.setText(ListaOfertas.get(position).getDescOferta());
        cargarImagenCate(ListaOfertas.get(position).getCateOferta());


        Glide.with(getApplicationContext())
               .load(ListaOfertas.get(position).getImagen())
                .signature((new StringSignature(String.valueOf(System.currentTimeMillis()/ (15 * 1000)))))
               .into(imagenOferta);

        return vista;
    }
    private void cargarImagenCate(String cate){
        switch (cate){
            case "Comida":
                imagenCate.setImageResource(R.drawable.comida);
                break;
            case "Fiesta":
                imagenCate.setImageResource(R.drawable.copete);
                break;
            case "Actividades y eventos":
                imagenCate.setImageResource(R.drawable.eventos);
                break;
            case "Servicios":
                imagenCate.setImageResource(R.drawable.servicio);
                break;
            case "Moda":
                imagenCate.setImageResource(R.drawable.moda);
                break;
            case "Electrodomésticos":
                imagenCate.setImageResource(R.drawable.electrodomesticos);
                break;
            case "Autos y motos":
                imagenCate.setImageResource(R.drawable.rueda);
                break;
            case "Hogar":
                imagenCate.setImageResource(R.drawable.casa);
                break;
            case "Otros":
                imagenCate.setImageResource(R.drawable.otros);
                break;
        }
    }
}
