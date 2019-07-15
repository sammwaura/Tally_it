package ws.wolfsoft.creative;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.edit_user;
import static ws.wolfsoft.creative.Constants.sign_up;
import static ws.wolfsoft.creative.Constants.user_id;

public class Signup extends AppCompatActivity {

    TextView signinhere;
    TextView signup;

    TextView password;
    TextView username;
    TextView email;
    TextView phone;

    ProgressBar progressBar;

    Typeface fonts1;
    private SharedPreferences.Editor credentialsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signinhere = findViewById(R.id.signinhere);
        signup= findViewById(R.id.signup1);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();

        username= findViewById(R.id.username);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        phone= findViewById(R.id.phone);

        progressBar = findViewById(R.id.progressBar2);

        signinhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Signup.this, Signin.class);
                startActivity(it);


            }
        });

        if(getIntent().getExtras().getString("type").equals("is_settings"))
        {
            findViewById(R.id.acc).setVisibility(View.GONE);
            findViewById(R.id.signinhere).setVisibility(View.GONE);
            signup.setText("Edit");

            username.setText(credentialsSharedPreferences.getString(Constants.name, "0"));
            email.setText(credentialsSharedPreferences.getString(Constants.email, "0"));
            phone.setText(credentialsSharedPreferences.getString(Constants.phone, "0"));
            password.setText(credentialsSharedPreferences.getString(Constants.password, "0"));

        }


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent it = new Intent(Signup.this, AddBusiness.class);
                //startActivity(it);


                    signup.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                if(getIntent().getExtras().getString("type").equals("is_settings"))
                {
                    editUser();
                }
                else
                {
                    if(!username.getText().toString().equals("") && !password.getText().toString().equals("") && !phone.getText().toString().equals("") && !email.getText().toString().equals(""))
                    {
                        signUpUser();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();
                    }

                }


            }
        });

        fonts1 =  Typeface.createFromAsset(Signup.this.getAssets(),
                "fonts/Lato-Regular.ttf");

        signinhere.setTypeface(fonts1);

    }


    private void editUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, edit_user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        signup.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);


                        credentialsEditor.putString(Constants.name, username.getText().toString());
                        credentialsEditor.putString(Constants.phone, phone.getText().toString());
                        credentialsEditor.putString(Constants.email, email.getText().toString());
                        credentialsEditor.putString(Constants.password, password.getText().toString());

                        credentialsEditor.apply();


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {


                        //Dismissing the progress dialog
                        //loading.dismiss();
                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();





                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();

                params.put("email", email.getText().toString());
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("user_id", Objects.requireNonNull(credentialsSharedPreferences.getString(user_id, "0")));


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Signup.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void signUpUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sign_up,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(!s.equals("0"))
                        {
                            System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                            signup.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                            credentialsEditor.putString(Constants.user_id, s);
                            credentialsEditor.putString(Constants.name, username.getText().toString());
                            credentialsEditor.putString(Constants.phone, phone.getText().toString());
                            credentialsEditor.putString(Constants.email, email.getText().toString());
                            credentialsEditor.putString(Constants.password, password.getText().toString());

                            credentialsEditor.apply();

                            Intent it = new Intent(Signup.this, AddBusiness.class);
                            it.putExtra("type", "");
                            startActivity(it);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Failed. Try again.", Toast.LENGTH_LONG).show();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {


                        //Dismissing the progress dialog
                        //loading.dismiss();
                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();





                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();

                params.put("email", email.getText().toString());
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("new_owner", "no");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Signup.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }


}
