package com.example.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp.Config.ServerConfig;
import com.example.chatapp.Util.GlobalApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText txtUsername, txtPassword;

    Button buttonLogin;

    String URL = ServerConfig.SERVER_URI + ServerConfig.LOGIN_API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setActionBar();
        init();
    }
    private void init(){
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startLogin();
            }
        });
    }
    private void setActionBar(){
        Toolbar toolbar = findViewById(R.id.toolBarLogin);
        setSupportActionBar(toolbar);
    }
    private void startLogin() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(response != null && response.length() != 2){
                            Log.d("Response", response);
                            try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    Boolean login = jsonObject.getBoolean("login");

                                    if(login){

                                        String username = jsonObject.getJSONObject("user").getString("username");
                                        String id = jsonObject.getJSONObject("user").getString("_id");
                                        JSONArray arr = jsonObject.getJSONObject("user").getJSONArray("friends");

                                        List<String> lstFriend = new ArrayList<>();
                                        for(int i = 0; i<arr.length(); i++){
                                            lstFriend.add(arr.getString(i));
                                        }

                                        GlobalApplication application = (GlobalApplication) getApplicationContext();
                                        application.setUsername(username);
                                        application.setUserId(id);
                                        application.setLstFriend(lstFriend);

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "Không tồn tại tài khoản hoặc sai", Toast.LENGTH_SHORT).show();
                        }
                    }},new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error", error.toString());
                            progressDialog.dismiss();
                            // volley finished and returned network error, update and unlock  itShouldLoadMore
                            //itShouldLoadMore = true;
                            Toast.makeText(LoginActivity.this, "Failed to login, network error", Toast.LENGTH_SHORT).show();
                        }
                })  {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("logemail", txtUsername.getText().toString());
                        params.put("logpassword", txtPassword.getText().toString());

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        }, 1000);
    }
}
