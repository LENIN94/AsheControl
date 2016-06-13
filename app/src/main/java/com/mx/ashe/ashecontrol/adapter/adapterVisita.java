package com.mx.ashe.ashecontrol.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mx.ashe.ashecontrol.R;
import com.mx.ashe.ashecontrol.model.Visita;

import java.util.List;

/**
 * Created by LENIN on 10/06/2016.
 */
public class adapterVisita extends RecyclerView.Adapter<adapterVisita.AnimeViewHolder> {
    private List<Visita> items;

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView visitas;

        public AnimeViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            visitas = (TextView) v.findViewById(R.id.visitas);
        }
    }

    public adapterVisita(List<Visita> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_visita, viewGroup, false);
        return new AnimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageResource(items.get(i).getPhotoId());
        viewHolder.nombre.setText(items.get(i).getData());
        viewHolder.visitas.setText(items.get(i).getData2());
    }
}
