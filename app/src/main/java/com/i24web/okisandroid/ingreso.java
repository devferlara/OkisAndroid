package com.i24web.okisandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ingreso extends AppCompatActivity {

    @BindView(R.id.no_tengo) Button no_tengo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);

        ButterKnife.bind(this);

        setTitle("Ingresar");

        no_tengo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ingreso.this, registro.class));
                finish();
            }
        });

    }
}
