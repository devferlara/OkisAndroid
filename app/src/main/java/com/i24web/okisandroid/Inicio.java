package com.i24web.okisandroid;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Inicio extends AppCompatActivity {

    @BindView(R.id.crea) TextView crea;
    @BindView(R.id.comparte) TextView comparte;
    @BindView(R.id.descubre) TextView descubre;
    @BindView(R.id.ingresar) Button ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        ButterKnife.bind(this);

        setTitle("Bienvenido");

        Typeface FuturaBold = Typeface.createFromAsset(getAssets(), "fonts/FuturaStd-Bold.ttf");

        crea.setTypeface(FuturaBold);
        comparte.setTypeface(FuturaBold);
        descubre.setTypeface(FuturaBold);

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inicio.this, registro.class));
                finish();
            }
        });

    }
}
