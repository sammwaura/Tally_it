package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.save_employee;

public class AddEmployees extends AppCompatActivity {

    TextView employee_name,phone,password;
    private SharedPreferences.Editor credentialsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_employees);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();

        employee_name = findViewById(R.id.employee_name);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);

        if(getIntent().getExtras().getString("type").equals("not_setUp"))
        {
            findViewById(R.id.head2).setVisibility(View.GONE);
            findViewById(R.id.previous).setVisibility(View.GONE);
            findViewById(R.id.next).setVisibility(View.GONE);

        }

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(AddEmployees.this, ViewEmployees.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpEmployee();
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent it = new Intent(AddEmployees.this, AddCategories.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddEmployees.this, AddBusiness.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

    }

    private void signUpEmployee() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, save_employee,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        Toast.makeText(getApplicationContext(), "Saved "+s+" Employee(s)", Toast.LENGTH_LONG).show();

                        credentialsEditor.putString(Constants.employee_number, s);
                        credentialsEditor.apply();

                        employee_name.setText("");
                        phone.setText("");
                        password.setText("");

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
                params.put("employee_name", employee_name.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("password", password.getText().toString());
                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(AddEmployees.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
