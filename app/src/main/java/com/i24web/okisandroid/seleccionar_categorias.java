package com.i24web.okisandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.i24web.okisandroid.Adapters.Adapter_categorias_inicio;
import com.i24web.okisandroid.Modelos.Modelo_categorias_inicio;
import com.i24web.okisandroid.Utils.GridSpacingItemDecoration;
import com.i24web.okisandroid.Utils.Helpers;

import java.util.ArrayList;
import java.util.List;

public class seleccionar_categorias extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter_categorias_inicio adapter;
    private List<Modelo_categorias_inicio> categoriasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_categorias);

        recyclerView = findViewById(R.id.recycler_view);

        categoriasList = new ArrayList<>();
        adapter = new Adapter_categorias_inicio(this, categoriasList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Helpers.dpToPx(10, this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepararCategorias();

        Button siguiente = findViewById(R.id.crear_album);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(seleccionar_categorias.this, A_quien_seguir.class));
            }
        });

    }

    /**
     * Adding few albums for testing
     */
    private void prepararCategorias() {

        categoriasList.add(new Modelo_categorias_inicio(R.drawable.moda, "Moda"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.estilodevida, "Estilo de vida"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.viajes, "Viajes"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.receta, "Recetas"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.belleza, "Belleza"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.manualidades, "Manualidades"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.navidad, "Navidad"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.fotografia, "Fotografía"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.familia, "Familia"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.decoracion, "Decoración"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.inspiracion, "Inspiración"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.invierno, "Invierno"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.maquillaje, "Maquillaje"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.mascotas, "Mascotas"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.musica, "Música"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.naturaleza, "Naturaleza"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.negocios, "Negocios"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.otono, "Otoño"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.playa, "Playa"));
        categoriasList.add(new Modelo_categorias_inicio(R.drawable.tecnologia, "Tecnología"));

        adapter.notifyDataSetChanged();

    }
}
