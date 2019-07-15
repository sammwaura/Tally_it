package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.EmployeeAdapter;
import adapter.ReplacementAdapter;
import beans.Employee;
import iobserver.CardIObserver;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.edit_employee;
import static ws.wolfsoft.creative.Constants.employee_number;
import static ws.wolfsoft.creative.Constants.get_employee;
import static ws.wolfsoft.creative.Constants.get_replacement;

public class ViewReplacement extends AppCompatActivity implements CardIObserver {


    private SharedPreferences credentialsSharedPreferences;
    public List<Employee> employee;
    ReplacementAdapter adapter;
    private RecyclerView rv;
    TextView employee_number_head;
    private NiftyDialogBuilder dialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_replacement);

        employee = new ArrayList<>();

        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        employee_number_head = findViewById(R.id.head2);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewReplacement.this.finish();
                Intent it = new Intent(ViewReplacement.this, AddEmployees.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });


            findViewById(R.id.head2).setVisibility(View.GONE);
            findViewById(R.id.back).setVisibility(View.GONE);


        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        employee_number_head.setText(String.format("%s Employee(s)", Objects.requireNonNull(credentialsSharedPreferences.getString(employee_number, "0"))));


            getAllEmployees();

            findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(ViewReplacement.this, AddEmployees.class);
                    it.putExtra("type", "not_setUp");
                    startActivity(it);
                }
            });


    }

    protected void onResume() {
        super.onResume();
        getAllEmployees();
    }

    private void getAllEmployees() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_replacement,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("employees");
                                employee.clear();
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String id = row.getString("id");
                                        String name = row.getString("replacing_name");
                                        String phone = row.getString("replaced_name");
                                        String password = row.getString("price");
                                        String business_id = row.getString("business_id");


                                        employee.add(new Employee(id, name, phone, password, business_id));


                                    }

                                    initializeData();
                                    //refreshFragment();
                                    findViewById(R.id.no_internet).setVisibility(View.GONE);
                                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                                    findViewById(R.id.empty_view).setVisibility(View.GONE);
                                    findViewById(R.id.rv).setVisibility(View.VISIBLE);
                                }
                                else {
                                    findViewById(R.id.no_internet).setVisibility(View.GONE);
                                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                    findViewById(R.id.rv).setVisibility(View.GONE);
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

                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewReplacement.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void initializeData() {

        adapter = new ReplacementAdapter(employee);
        rv.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    public void onCardClicked(final int pos, String id, String name, String phone, String password) {
        //Toast.makeText(getApplicationContext(), "Clicked "+employee.get(pos).name, Toast.LENGTH_LONG).show();


        /*
        dialogBuilder = NiftyDialogBuilder.getInstance(ViewReplacement.this);
        dialogBuilder
                .withTitle("Edit Employee")
                .withTitleColor("#303f9f")                                  //def
                .withDividerColor("#3f51b5")                              //def//.withMessage(null)  no Msg
                .withMessageColor("#00B873")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)
                .withDuration(700)
                .isCancelableOnTouchOutside(false)
                .isCancelable(false)
                .withEffect(Effectstype.Newspager)
                .withButton2Text("CANCEL")//def Effectstype.Slidetop
                .withButton1Text("EDIT")
                .setCustomView(R.layout.dialog_edit_employee, ViewReplacement.this)//def gone//def gone
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

                        final EditText employeeName =  dialogBuilder.findViewById(R.id.employee_name);
                        final EditText employeePhone =  dialogBuilder.findViewById(R.id.employee_phone);
                        final EditText employeePassword =  dialogBuilder.findViewById(R.id.employee_password);

                        System.out.println("RRRRRRRRRRRrRRRRRRRRRR "+employeeName.getText().toString());

                        editEmployee(employee.get(pos).id,employeeName.getText().toString(),employeePhone.getText().toString(),employeePassword.getText().toString());

                    }
                    }
                    );

        final EditText employeeName =  dialogBuilder.findViewById(R.id.employee_name);
        final EditText employeePhone =  dialogBuilder.findViewById(R.id.employee_phone);
        final EditText employeePassword =  dialogBuilder.findViewById(R.id.employee_password);

        employeeName.setText(employee.get(pos).name);
        employeePhone.setText(employee.get(pos).phone);
        employeePassword.setText(employee.get(pos).password);

        dialogBuilder.show(); */
    }





    public void refreshActivity() {
        this.finish();
        Intent it = new Intent(ViewReplacement.this, ViewReplacement.class);
        it.putExtra("type", "");
        startActivity(it);
    }

    private void editEmployee(final String id, final String employeeName, final String employeePhone, final String employeePassword) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, edit_employee,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss2 " + s);
                        Toast.makeText(getApplicationContext(), "Edited Employee(s)", Toast.LENGTH_LONG).show();
                        refreshActivity();
                        initializeData();
                        dialogBuilder.dismiss();
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
                params.put("employee_name", employeeName);
                params.put("phone", employeePhone);
                params.put("password", employeePassword);
                params.put("id", id);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewReplacement.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
