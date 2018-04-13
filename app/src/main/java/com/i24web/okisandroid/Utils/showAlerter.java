package com.i24web.okisandroid.Utils;

import android.app.Activity;
import android.content.Context;

import com.tapadoo.alerter.Alerter;

public final class showAlerter {

    public static void show(Activity contexto, String titulo, String texto, int color) {
        Alerter.create(contexto)
                .setTitle(titulo)
                .setText(texto)
                .setBackgroundColorRes(color)
                .show();
    }

}
