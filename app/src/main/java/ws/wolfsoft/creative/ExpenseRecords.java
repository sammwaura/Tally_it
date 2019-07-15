package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import adapter.ExpenseRecordsAdapter;
import adapter.MySpinnerAdapter;
import adapter.SalesAdapter;
import iobserver.ExpenseRecordsIObserver;
import iobserver.SalesIObserver;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.get_expense_records;
import static ws.wolfsoft.creative.Constants.get_sales;

public class ExpenseRecords extends AppCompatActivity  implements ExpenseRecordsIObserver {

    private Spinner spinner;
    private SharedPreferences credentialsSharedPreferences;
    public List<beans.ExpenseRecords> expenseRecords;
    ExpenseRecordsAdapter expenseRecordsAdapter;
    private RecyclerView rv;
    TextView overall_number_head,highest,lowest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_records);

        rv =  findViewById(R.id.rv);

        overall_number_head =  findViewById(R.id.total_Sales);
        highest =  findViewById(R.id.highest_sales);
        lowest =  findViewById(R.id.lowest_sales);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        expenseRecords = new ArrayList<>();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpenseRecords.this.finish();
                Intent it = new Intent(ExpenseRecords.this, Home.class);
                startActivity(it);
            }
        });

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        spinner = findViewById(R.id.spinnerType);
        List<String> dataset = new ArrayList<>();
        dataset.add("Daily Statistics");
        dataset.add("Monthly");
        dataset.add("Yearly");
        dataset.add("Specify Date");
        dataset.add("Specify Date Range");
        dataset.add("Total");

        MySpinnerAdapter<String> adapter = new MySpinnerAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dataset);

        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ExpenseRecords.this, RecordExpense.class);
                startActivity(it);
            }
        });

        getAllExpenseRecords();
    }

    private void getAllExpenseRecords() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_expense_records,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("expense_records");
                                expenseRecords.clear();
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String expense_type_id = row.getString("expense_type_id");
                                        String to_be_paid = row.getString("to_be_paid");
                                        String balance = row.getString("balance");
                                        String name = row.getString("name")+"#"+to_be_paid+"#"+balance;
                                        String total = row.getString("total");


                                        String overall_total = row.getString("overall_total");
                                        String max_price = row.getString("max_expense");
                                        String min_price = row.getString("min_expense");

                                        overall_number_head.setText(overall_total);
                                        highest.setText(max_price);
                                        lowest.setText(min_price);

                                        //category_number_head.setText(row.getString("count")+" categories");
                                        //category.add(new Category(category_id, name, type));
                                        expenseRecords.add(new beans.ExpenseRecords(expense_type_id,name,total));
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));
                params.put("frequency",spinner.getSelectedItem().toString());
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ExpenseRecords.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void initializeData() {
        expenseRecordsAdapter = new ExpenseRecordsAdapter(expenseRecords);
        rv.setAdapter(expenseRecordsAdapter);
        expenseRecordsAdapter.setListener(this);
    }

    @Override
    public void onCardClicked(int pos, String name) {
        Toast.makeText(getApplicationContext(), "=> "+expenseRecords.get(pos).expense_type_id, Toast.LENGTH_LONG).show();

        /*Intent it = new Intent(ExpenseRecords.this, DetailExpense.class);
        it.putExtra("expense_type_id", expenseRecords.get(pos).expense_type_id);
        it.putExtra("expense_name", expenseRecords.get(pos).name);
        it.putExtra("frequency", spinner.getSelectedItem().toString());
        startActivity(it);*/

    }
}
