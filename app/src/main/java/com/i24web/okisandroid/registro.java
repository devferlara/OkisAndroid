package com.i24web.okisandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.i24web.okisandroid.Utils.showAlerter;
import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class registro extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.no_tengo)
    Button ya_tengo;
    @BindView(R.id.ingresar)
    Button ingresar;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.gmail)
    SignInButton gmail;

    @BindView(R.id.nombres)
    EditText nombres;
    @BindView(R.id.apellidos)
    EditText apellidos;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.cd)
    TextView cd;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.n1)
    EditText n1;
    @BindView(R.id.n2)
    EditText n2;
    @BindView(R.id.n3)
    EditText n3;
    @BindView(R.id.n4)
    EditText n4;
    @BindView(R.id.n5)
    EditText n5;
    @BindView(R.id.n6)
    EditText n6;
    @BindView(R.id.reenviar)
    Button reenviar;

    CallbackManager mCallbackManager;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    CognitoUser usuario_registrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);

        setTitle("Registrarme");

        ya_tengo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registro.this, ingreso.class));
                finish();
            }
        });

        /* Consulta de datos para facebook */
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {
                                    putData(object.getString("name"), "", object.getString("email"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.d("RESULT", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("RESULT", "facebook:onError", error);
                // ...
            }
        });
        /* Consulta de datos para facebook */

        /* Consulta de datos para gmail */
        configureSignIn();
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        /* Consulta de datos para gmail */


        /* Registrar usuario */
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nombres.getText().toString().length() > 2) {
                    if (apellidos.getText().toString().length() > 2) {
                        if (checkEmail(email.getText().toString())) {
                            if (password.getText().toString().length() > 7) {

                                closeKeyboard();

                                final ProgressDialog dialog = ProgressDialog.show(registro.this, "",
                                        "Registrando usuario", true);

                                CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                                final CognitoUserPool userPool = new CognitoUserPool(registro.this, getString(R.string.cognito_pool_id), getString(R.string.cognito_client_id), getString(R.string.cognito_secret));

                                userAttributes.addAttribute("given_name", nombres.getText().toString());
                                userAttributes.addAttribute("email", email.getText().toString());
                                userAttributes.addAttribute("family_name", apellidos.getText().toString());

                                userPool.signUpInBackground(email.getText().toString(), password.getText().toString(), userAttributes, null, new SignUpHandler() {
                                    @Override
                                    public void onSuccess(final CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                                        showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.cuenta_creada_correctamente), R.color.colorPrimary);
                                        dialog.dismiss();

                                        ya_tengo.setVisibility(View.GONE);
                                        nombres.setVisibility(View.GONE);
                                        apellidos.setVisibility(View.GONE);
                                        email.setVisibility(View.GONE);
                                        password.setVisibility(View.GONE);
                                        ingresar.setVisibility(View.GONE);
                                        loginButton.setVisibility(View.GONE);
                                        gmail.setVisibility(View.GONE);

                                        cd.setVisibility(View.VISIBLE);
                                        linearLayout.setVisibility(View.VISIBLE);
                                        reenviar.setVisibility(View.VISIBLE);
                                        usuario_registrado = user;

                                    }

                                    @Override
                                    public void onFailure(Exception exception) {
                                        showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.no_se_pudo_crear_cuenta), R.color.colorRojo);
                                        dialog.dismiss();
                                    }
                                });

                            } else {
                                password.requestFocus();
                                showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.completa_password), R.color.colorRojo);
                            }
                        } else {
                            email.requestFocus();
                            showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.validar_email), R.color.colorRojo);
                        }
                    } else {
                        apellidos.requestFocus();
                        showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.completa_apellido), R.color.colorRojo);
                    }
                } else {
                    nombres.requestFocus();
                    showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.completa_nombre), R.color.colorRojo);
                }

            }
        });
        /* Registrar usuario */

        // Sección de confirmación
        n1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    n1.setText("");
                }
            }
        });

        n2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    n2.setText("");
                }
            }
        });

        n3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    n3.setText("");
                }
            }
        });

        n4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    n4.setText("");
                }
            }
        });

        n5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    n5.setText("");
                }
            }
        });

        n6.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    n6.setText("");
                }
            }
        });

        n1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    n2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        n2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    n3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        n3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    n4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        n4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    n5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        n5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    n6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        n6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (n1.getText().toString().length() > 0) {
                    if (n2.getText().toString().length() > 0) {
                        if (n3.getText().toString().length() > 0) {
                            if (n4.getText().toString().length() > 0) {
                                if (n5.getText().toString().length() > 0) {
                                    if (n6.getText().toString().length() > 0) {
                                        final ProgressDialog dialog = ProgressDialog.show(registro.this, "",
                                                "Validando código", true);
                                        closeKeyboard();
                                        String codigo_confirmacion = n1.getText().toString() + n2.getText().toString() + n3.getText().toString() + n4.getText().toString() + n5.getText().toString() + n6.getText().toString();
                                        usuario_registrado.confirmSignUpInBackground(codigo_confirmacion, false, new GenericHandler() {
                                            @Override
                                            public void onSuccess() {
                                                showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.usuario_confirmado), R.color.colorPrimary);
                                                dialog.dismiss();
                                                final ProgressDialog dialog = ProgressDialog.show(registro.this, "",
                                                        "Autenticando usuario", true);

                                                // Autenticacion
                                                AuthenticationHandler authenticationHandler = new AuthenticationHandler() {

                                                    @Override
                                                    public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                                                        Log.d("Estado", "Success");
                                                        dialog.dismiss();
                                                        startActivity(new Intent(registro.this, seleccionar_categorias.class));
                                                    }

                                                    @Override
                                                    public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                                                        AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, password.getText().toString(), null);
                                                        authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                                                        authenticationContinuation.continueTask();
                                                    }

                                                    @Override
                                                    public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                                                        Log.d("Estado", "getMFACode");
                                                    }

                                                    @Override
                                                    public void authenticationChallenge(ChallengeContinuation continuation) {
                                                        Log.d("Estado", "authenticationChallenge");
                                                    }

                                                    @Override
                                                    public void onFailure(Exception exception) {
                                                        Log.d("Estado", exception.toString());
                                                    }
                                                };

                                                usuario_registrado.getSessionInBackground(authenticationHandler);
                                                // Autenticacion

                                            }

                                            @Override
                                            public void onFailure(Exception exception) {
                                                Log.d("Fallo", exception.toString());
                                                showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.usuario_no_confirmado), R.color.colorRojo);
                                                dialog.dismiss();
                                            }
                                        });

                                    } else {
                                        n6.requestFocus();
                                    }
                                } else {
                                    n5.requestFocus();
                                }
                            } else {
                                n4.requestFocus();
                            }
                        } else {
                            n3.requestFocus();
                        }
                    } else {
                        n2.requestFocus();
                    }
                } else {
                    n1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // Sección de confirmación

        //Seccion reenviar código de confirmación
        reenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(registro.this)
                        .title(R.string.informacion)
                        .content(R.string.reenviar_codigo_pregunta)
                        .positiveText(R.string.reenviar_boton)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                usuario_registrado.resendConfirmationCodeInBackground(new VerificationHandler() {
                                    @Override
                                    public void onSuccess(CognitoUserCodeDeliveryDetails verificationCodeDeliveryMedium) {
                                        showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.codigo_reenviado), R.color.colorPrimary);
                                    }

                                    @Override
                                    public void onFailure(Exception exception) {
                                        showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.problema_al_reenviar_codigo), R.color.colorRojo);
                                    }
                                });
                            }
                        })
                        .negativeText(R.string.cancelar)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();

            }
        });
        //Seccion reenviar código de confirmación


    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(registro.this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            putData(account.getDisplayName(), "", account.getEmail());
        } catch (ApiException e) {
            showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.no_hemos_obtenido_tus_datos), R.color.colorRojo);
        }
    }

    public void putData(String nombres_, String apellidos_, String email_) {
        nombres.setText(nombres_);
        apellidos.setText(apellidos_);
        email.setText(email_);
        showAlerter.show(registro.this, getString(R.string.informacion), getString(R.string.hemos_obtenido_tus_datos), R.color.colorPrimary);
        loginButton.setVisibility(View.GONE);
        gmail.setVisibility(View.GONE);
    }

    public void configureSignIn() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(registro.this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();
        mGoogleApiClient.connect();
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
