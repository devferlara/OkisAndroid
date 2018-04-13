package com.i24web.okisandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChooseMfaContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.i24web.okisandroid.Utils.AppHelper;
import com.i24web.okisandroid.Utils.Helpers;
import com.i24web.okisandroid.Utils.showAlerter;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ingreso extends AppCompatActivity {

    @BindView(R.id.no_tengo)
    Button no_tengo;
    @BindView(R.id.ingresar)
    Button ingresar;

    @BindView(R.id.email)
    EditText email_;
    @BindView(R.id.password)
    EditText password_;

    private String username;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);

        ButterKnife.bind(this);

        AppHelper.init(getApplicationContext());
        findCurrent();

        setTitle("Ingresar");

        no_tengo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ingreso.this, registro.class));
                finish();
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Helpers.checkEmail(email_.getText().toString())) {

                    if (password_.getText().length() > 7) {

                        closeKeyboard();
                        dialog = ProgressDialog.show(ingreso.this, "",
                                "Autenticando usuario", true);
                        AppHelper.setUser(email_.getText().toString());
                        AppHelper.getPool().getUser(email_.getText().toString()).getSessionInBackground(authenticationHandler);

                    } else {
                        password_.requestFocus();
                        showAlerter.show(ingreso.this, getString(R.string.informacion), getString(R.string.completa_password), R.color.colorRojo);
                    }

                } else {
                    email_.requestFocus();
                    showAlerter.show(ingreso.this, getString(R.string.informacion), getString(R.string.validar_email), R.color.colorRojo);
                }

            }
        });
    }

    private void findCurrent() {
        CognitoUser user = AppHelper.getPool().getCurrentUser();
        username = user.getUserId();
        if(username != null) {
            AppHelper.setUser(username);
            email_.setText(user.getUserId());
            user.getSessionInBackground(authenticationHandler);
        }
    }

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if(username != null) {
            this.username = username;
            AppHelper.setUser(username);
        }
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(email_.getText().toString(), password_.getText().toString(), null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(ingreso.this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            if(dialog != null){
                dialog.dismiss();
            }
            AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.newDevice(device);
            startActivity(new Intent(ingreso.this, Dentro.class));
            finish();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }

        @Override
        public void onFailure(Exception e) {
            Log.d("Sign-in failed", AppHelper.formatException(e));
            if(dialog != null){
                dialog.dismiss();
            }
            if(password_.getText().toString().length() > 7){
                showAlerter.show(ingreso.this, getString(R.string.informacion), getString(R.string.password_no_coincide), R.color.colorRojo);
            }

        }

    };

}
