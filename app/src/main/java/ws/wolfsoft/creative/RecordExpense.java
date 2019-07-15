package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.MySpinnerAdapterOther;

import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.get_categories;
import static ws.wolfsoft.creative.Constants.get_expense_type;
import static ws.wolfsoft.creative.Constants.get_expenses_details;
import static ws.wolfsoft.creative.Constants.save_recorded_expenses;
import static ws.wolfsoft.creative.Constants.save_subcategories;

public class RecordExpense extends AppCompatActivity {

    private Spinner spinnerCat;
    List<String> dataset;
    TextView subcategories,subcategory_balance,subcategory_amount_ofExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_expense);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);


        subcategories = findViewById(R.id.subcategory_name);
        subcategory_balance = findViewById(R.id.subcategory_balance);
        subcategory_amount_ofExpense = findViewById(R.id.subcategory_amount_ofExpense);

        spinnerCat =  findViewById(R.id.spinnerTypeCat);
        dataset = new ArrayList<>();
        dataset.add("Select Expense#0");
        getAllExpense();

        subcategories.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String enteredAmount = subcategories.getText().toString();
                String owed = subcategory_amount_ofExpense.getText().toString();
                if(!enteredAmount.equals("") && !owed.equals("")){
                    int paying= Integer.parseInt(enteredAmount);
                    int owing= Integer.parseInt(owed);
                    int bal=owing-paying;
                    subcategory_balance.setText(bal);
                }
                return false;
            }
        });

        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = spinnerCat.getSelectedItem().toString();
                if(!selectedCategory.equals("Select Expense#0"))
                {
                    System.out.println("++++++++++++++++++++++ "+selectedCategory);

                    getExpenseDetails(selectedCategory.split("#")[0]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });




        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecordedExpense();
            }
        });

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(RecordExpense.this, AddCategories.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

    }

    private void getExpenseDetails(final String s) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_expenses_details,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        subcategory_amount_ofExpense.setText(s);
                        subcategory_balance.setText(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("volleyError response " + volleyError.getMessage());
                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                params.put("expense_type_id", s);


                System.out.println();

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(RecordExpense.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void saveRecordedExpense() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, save_recorded_expenses,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        Toast.makeText(getApplicationContext(), "Recorded Expenses", Toast.LENGTH_LONG).show();

                        Intent it = new Intent(RecordExpense.this, ExpenseRecords.class);

                        startActivity(it);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("volleyError response " + volleyError.getMessage());
                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                params.put("expense_type", spinnerCat.getSelectedItem().toString().split("#")[1]);
                params.put("amount", subcategories.getText().toString());
                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));

                System.out.println();

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(RecordExpense.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getAllExpense() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_expense_type,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("expenses");
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);


                                        String name = row.getString("name");

                                        dataset.add(name);
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            MySpinnerAdapterOther<String> adapter = new MySpinnerAdapterOther<>(
                                    RecordExpense.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    dataset);

                            spinnerCat.setAdapter(adapter);

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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(RecordExpense.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
