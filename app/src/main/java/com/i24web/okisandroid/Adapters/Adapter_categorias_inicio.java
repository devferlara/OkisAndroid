package com.i24web.okisandroid.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.i24web.okisandroid.Modelos.Modelo_categorias_inicio;
import com.i24web.okisandroid.R;

import java.util.List;


/**
 * Created by diegofernandolaramontealegre on 2/01/18.
 */

public class Adapter_categorias_inicio extends RecyclerView.Adapter<Adapter_categorias_inicio.MyViewHolder> {

    private Context mContext;
    private List<Modelo_categorias_inicio> Array;

    @Override
    public int getItemCount() {
        return Array.size();
    }

    public Adapter_categorias_inicio(Context mContext, List<Modelo_categorias_inicio> contenido) {
        this.mContext = mContext;
        this.Array = contenido;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView categoria;
        public ImageView imagen, seleccionado;

        public MyViewHolder(View view) {
            super(view);
            imagen = view.findViewById(R.id.Imagen);
            seleccionado = view.findViewById(R.id.seleccionado);
            categoria = view.findViewById(R.id.Categoria);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_categorias, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Modelo_categorias_inicio model = Array.get(position);

        holder.categoria.setText(model.getCategoria());
        Glide.with(mContext).load(model.getImagen()).into(holder.imagen);
        if (model.getSelected()){
            holder.seleccionado.setVisibility(View.VISIBLE);
        } else {
            holder.seleccionado.setVisibility(View.GONE);
        }

        holder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getSelected()){
                    Array.get(position).setIsSelected(false);
                    holder.seleccionado.setVisibility(View.GONE);
                } else {
                    Array.get(position).setIsSelected(true);
                    holder.seleccionado.setVisibility(View.VISIBLE);
                }

            }
        });

    }

}
