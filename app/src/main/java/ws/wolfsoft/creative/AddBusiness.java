package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.MySpinnerAdapterOther;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.edit_business;
import static ws.wolfsoft.creative.Constants.sign_business;
import static ws.wolfsoft.creative.Constants.sign_up;
import static ws.wolfsoft.creative.Constants.user_id;

public class AddBusiness extends AppCompatActivity {

    TextView signinhere;
    TextView business_name;
    TextView location;
    TextView employees_no;
    TextView nature;

    TextView password;
    TextView username;
    TextView email;
    TextView phone;

    private NiftyDialogBuilder dialogBuilder;
    TextView next,reuse;
    Typeface fonts1;
    private SharedPreferences.Editor credentialsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_sign_up);

        next = findViewById(R.id.next);
        reuse = findViewById(R.id.reuse);

        business_name = findViewById(R.id.business_name);
        location = findViewById(R.id.location);

        nature = findViewById(R.id.nature);
        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();

        username= findViewById(R.id.username);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        phone= findViewById(R.id.phone);


        if(getIntent().getExtras().getString("type").equals("is_settings"))
        {
            findViewById(R.id.head2).setVisibility(View.GONE);
            next.setText("Edit");

            reuse.setVisibility(View.VISIBLE);
            reuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder = NiftyDialogBuilder.getInstance(AddBusiness.this);
                    dialogBuilder
                            .withTitle("Add Owner")
                            .withTitleColor("#303f9f")                                  //def
                            .withDividerColor("#3f51b5")                              //def//.withMessage(null)  no Msg
                            .withMessageColor("#00B873")                              //def  | withMessageColor(int resid)
                            .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)
                            .withDuration(700)
                            .isCancelableOnTouchOutside(false)
                            .isCancelable(false)
                            .withEffect(Effectstype.Newspager)
                            .withButton2Text("CANCEL")//def Effectstype.Slidetop
                            .withButton1Text("SAVE")
                            .setCustomView(R.layout.dialog_add_owner, AddBusiness.this)//def gone//def gone
                            .isCancelableOnTouchOutside(true)
                            .setButton2Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                }
                            })
                            .setButton1Click(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     final EditText email =  dialogBuilder.findViewById(R.id.email);
                                                     final EditText username =  dialogBuilder.findViewById(R.id.username);
                                                     final EditText password =  dialogBuilder.findViewById(R.id.password);
                                                     final EditText phone =  dialogBuilder.findViewById(R.id.phone);

                                                     StringRequest stringRequest = new StringRequest(Request.Method.POST, sign_up,
                                                             new Response.Listener<String>() {
                                                                 @Override
                                                                 public void onResponse(String s) {
                                                                     System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                                                                     dialogBuilder.dismiss();
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
                                                             params.put("new_owner", "yes");
                                                             params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));
                                                             //returning parameters
                                                             return params;
                                                         }
                                                     };

                                                     //Creating a Request Queue
                                                     RequestQueue requestQueue = Volley.newRequestQueue(AddBusiness.this);

                                                     //Adding request to the queue
                                                     requestQueue.add(stringRequest);

                                                 }
                                             }
                            );
                    dialogBuilder.show();
                }
            });

            business_name.setText(credentialsSharedPreferences.getString(Constants.business_name, "0"));
            location.setText(credentialsSharedPreferences.getString(Constants.location, "0"));
            nature.setText(credentialsSharedPreferences.getString(Constants.nature, "0"));

        }

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getExtras().getString("type").equals("is_settings"))
                {
                    editBusiness();
                }
                else
                {
                    System.out.println("Called Hereeee");
                    if(!nature.getText().toString().equals("") && !location.getText().toString().equals("") && !business_name.getText().toString().equals("") )
                    {
                        signUpBusiness();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();
                    }

                }


            }
        });
    }

    private void signUpUser() {


    }

    private void editBusiness() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, edit_business,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);


                        credentialsEditor.putString(Constants.nature, nature.getText().toString());
                        credentialsEditor.putString(Constants.location, location.getText().toString());
                        credentialsEditor.putString(Constants.business_name,business_name.getText().toString());
                        credentialsEditor.apply();

                        Intent it = new Intent(AddBusiness.this, Settings.class);
                        startActivity(it);
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

                params.put("business_name", business_name.getText().toString());
                params.put("location", location.getText().toString());
                params.put("nature", nature.getText().toString());
                params.put("is_original", "1");
                params.put("user_id", Objects.requireNonNull(credentialsSharedPreferences.getString(user_id, "0")));
                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));



                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(AddBusiness.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }


    private void signUpBusiness() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sign_business,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(!s.equals("0")) {
                            System.out.println("sssssssssssssssssssssssssssssssssssss " + s);

                            credentialsEditor.putString(Constants.business_no, s);
                            credentialsEditor.putString(Constants.nature, nature.getText().toString());
                            credentialsEditor.putString(Constants.location, location.getText().toString());
                            credentialsEditor.putString(Constants.business_name, business_name.getText().toString());
                            credentialsEditor.apply();

                            Intent it = new Intent(AddBusiness.this, Home.class);
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

                params.put("business_name", business_name.getText().toString());
                params.put("location", location.getText().toString());
                params.put("nature", nature.getText().toString());
                params.put("user_id", Objects.requireNonNull(credentialsSharedPreferences.getString(user_id, "0")));




                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(AddBusiness.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
