package com.mx.ashe.ashecontrol;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mx.ashe.ashecontrol.app.EndPoints;
import com.mx.ashe.ashecontrol.app.MyApplication;
import com.mx.ashe.ashecontrol.helper.PrefConstants;
import com.mx.ashe.ashecontrol.helper.SAppUtil;
import com.mx.ashe.ashecontrol.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ActivityLogin extends AppCompatActivity {
    Button btn;
    Toolbar toolbar;
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION

    };
    private static final int REQUEST_SIGNUP = 0;
    @InjectView(R.id.edtUsuario)
    EditText edtUsuario;
    @InjectView(R.id.edtPass)
    EditText edtPass;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    ProgressDialog progressDialog;
    private String MensajeError = "La Conexion ha Fallado";

    SharedPreferences SharedPrefs;
    CheckBox chGuardaS;
    private static final int INITIAL_REQUEST = 1337;
    private String TAG = ActivityLogin.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MyApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, ActivityMenu.class));
            finish();
        }
        checkShowTutorial();

        setContentView(R.layout.activity_login);
        btn = (Button) findViewById(R.id.btn_login);



        TypedValue typedValueColorPrimaryDark = new TypedValue();
        ActivityLogin.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().setStatusBarColor(colorPrimaryDark);


            try {
                if (!canAccessLocation()) {
                    requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                }
            } catch (Exception e) {
                Log.e("eerr", e.toString());
            }
        }


        ButterKnife.inject(this);
        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtPass = (EditText) findViewById(R.id.edtPass);





        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               login();

            }
        });


    }

   @TargetApi(Build.VERSION_CODES.M)
   private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }else
        {
            progressDialog = new ProgressDialog(ActivityLogin.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Conectando...");
            progressDialog.show();


            final String name = edtUsuario.getText().toString();
            final String pass = edtPass.getText().toString();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    EndPoints.LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getBoolean("error") == false) {
                            // user successfully logged in
                            JSONObject userObj = obj.getJSONObject("user");
                            User user = new User(userObj.getString("ID"),userObj.getString("NOMBRE"),userObj.getString("USUARIO"),
                                    userObj.getString("PASS"),userObj.getString("IMAGEN"));

                            // storing user in shared preferences
                            MyApplication.getInstance().getPrefManager().storeUser(user);
                            progressDialog.dismiss();//ocultamos progess dialog.
                            // start main activity

                            startActivity(new Intent(getApplicationContext(), ActivityMenu.class));

                            finish();

                        } else {
                            // login error - simply toast the message
                            Toast.makeText(getApplicationContext(), "Ocurrio un error"+ obj.getString("message") , Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("usuario", name);
                    params.put("pass", pass);

                    Log.e(TAG, "params: " + params.toString());
                    return params;
                }
            };

            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
        }

    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                // this.finish();
            }
        }
    }


    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        btnLogin.setEnabled(true);


    }

    public void onLoginFailed() {
        View parentLayout = findViewById(R.id.btn_login);
        // Toast.makeText(getBaseContext(), "Acceso Fallido", Toast.LENGTH_LONG).show();
        Snackbar.make(parentLayout, "Acceso Fallido", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = edtUsuario.getText().toString();
        String password = edtPass.getText().toString();

        if (email.isEmpty()) {
            edtUsuario.setError("Ingresa un usuario valido");
            valid = false;
        } else {
            edtUsuario.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtPass.setError("Entre 4 y 10 caracteres");
            valid = false;
        } else {
            edtPass.setError(null);
        }

        return valid;
    }


    private void checkShowTutorial() {
        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
        int currentVersionCode = SAppUtil.getAppVersionCode(this);
        if (currentVersionCode > oldVersionCode) {
            startActivity(new Intent(ActivityLogin.this, ProductTourActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            PrefConstants.putAppPrefInt(this, "version_code", currentVersionCode);
        }
    }



}
