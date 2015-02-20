package com.example.sadarik.gestoractividadeszvergeles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends ArrayAdapter<Actividad> {

    private Context contexto;
    private ArrayList<Actividad> lista;
    private int recurso;
    private static LayoutInflater i;


    public static class ViewHolder{
        public TextView tvFecha,tvDescripcion,tvTipo;

        public int posicion;
    }

    public Adaptador(Context context, int resource, ArrayList<Actividad> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.lista=objects;
        this.recurso=resource;
        this.i=(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvFecha = (TextView) convertView.findViewById(R.id.tvFecha);
            vh.tvDescripcion = (TextView) convertView.findViewById(R.id.tvLugar);
            vh.tvTipo = (TextView) convertView.findViewById(R.id.tvTipo);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.posicion = position;
        vh.tvFecha.setText(lista.get(position).getFechaInicio().substring(0,10));
        vh.tvDescripcion.setText(lista.get(position).getDescripcion() + "");
        vh.tvTipo.setText(lista.get(position).getTipo() + "");
        return convertView;
    }
}
