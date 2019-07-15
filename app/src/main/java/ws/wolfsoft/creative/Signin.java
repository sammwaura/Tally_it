package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.save_employee;
import static ws.wolfsoft.creative.Constants.validate_user;
import static ws.wolfsoft.creative.Constants.validate_user_two;

public class Signin extends AppCompatActivity {

    TextView create,forgot;
    TextView signin;
    Typeface fonts1;

    EditText emailMain,passwordMain;

    private SharedPreferences.Editor credentialsEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();

        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);

        emailMain= findViewById(R.id.email);
        passwordMain = findViewById(R.id.password);

        forgot = findViewById(R.id.forgotPassword);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Signin.this, ForgotPassword.class);
                startActivity(it);
            }
        });

        create = findViewById(R.id.create);
        signin=  findViewById(R.id.signin1);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Signin.this, Signup.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent it = new Intent(Signin.this, AddBusiness.class);
                //startActivity(it);


                    if (isValidEmail(email.getText().toString())) {
                        validate(email.getText().toString(), password.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter a valid Email", Toast.LENGTH_LONG).show();
                    }


            }
        });

        fonts1 =  Typeface.createFromAsset(Signin.this.getAssets(),
                "fonts/Lato-Regular.ttf");

        create.setTypeface(fonts1);

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void validate(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, validate_user_two,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        String status = null;
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);


                                JSONArray posts = jsonObj.getJSONArray("users");
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String user_id = row.getString("user_id");
                                        String user_name = row.getString("user_name");
                                        String email = row.getString("email");
                                        String phone = row.getString("phone");
                                         status = row.getString("status");
                                        String password = row.getString("password");
                                        String business_id = row.getString("business_id");
                                        String business_name = row.getString("business_name");
                                        String business_location = row.getString("business_location");
                                        String nature_of_business = row.getString("nature_of_business");

                                        credentialsEditor.putString(Constants.user_id, user_id);
                                        credentialsEditor.putString(Constants.name, user_name);
                                        credentialsEditor.putString(Constants.phone, phone);
                                        credentialsEditor.putString(Constants.email, email);

                                        credentialsEditor.putString(Constants.password, password);
                                        credentialsEditor.putString(Constants.business_no, business_id);
                                        credentialsEditor.putString(Constants.business_name, business_name);
                                        credentialsEditor.putString(Constants.location, business_location);
                                        credentialsEditor.putString(Constants.nature, nature_of_business);

                                        credentialsEditor.apply();

                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Server did not return any useful data",Toast.LENGTH_LONG).show();
                        }
                        System.out.println("MAIN response " + s);


                        if(status ==null || status.equals("fail"))
                        {
                            Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Intent it = new Intent(Signin.this, Home.class);
                            startActivity(it);
                        }


                        passwordMain.setText("");
                        emailMain.setText("");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("volleyError response " + volleyError.getMessage());
                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                params.put("email", email);
                params.put("password", password);


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Signin.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
