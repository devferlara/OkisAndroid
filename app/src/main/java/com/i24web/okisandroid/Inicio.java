package com.i24web.okisandroid;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Inicio extends AppCompatActivity {

    @BindView(R.id.crea)
    TextView crea;
    @BindView(R.id.comparte)
    TextView comparte;
    @BindView(R.id.descubre)
    TextView descubre;
    @BindView(R.id.ingresar)
    Button ingresar;
    @BindView(R.id.no_tengo)
    Button no_tengo;

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
                startActivity(new Intent(Inicio.this, ingreso.class));
                finish();
            }
        });

        no_tengo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inicio.this, registro.class));
                finish();
            }
        });

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

    }
}
