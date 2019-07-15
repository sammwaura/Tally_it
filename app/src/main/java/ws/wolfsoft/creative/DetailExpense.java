package ws.wolfsoft.creative;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import adapter.DetailsExpenseAdapter;
import adapter.DetailsSalesAdapter;
import beans.DetailsExpenses;
import beans.DetailsSales;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.get_details_expense;
import static ws.wolfsoft.creative.Constants.get_details_sales;

public class DetailExpense extends AppCompatActivity {

    String expense_type_id,expense_name,frequency;

    private SharedPreferences credentialsSharedPreferences;
    public List<DetailsExpenses> detailsExpenses;
    DetailsExpenseAdapter detailsExpenseAdapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_expense);

        expense_type_id = getIntent().getExtras().getString("expense_type_id");
        expense_name = getIntent().getExtras().getString("expense_name");
        frequency = getIntent().getExtras().getString("frequency");

        android.support.v7.widget.Toolbar mToolbar =  findViewById(R.id.my_toolbar);
        if(mToolbar!=null) {
            mToolbar.setTitle(expense_name);
            setSupportActionBar(mToolbar);
        }
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        detailsExpenses = new ArrayList<>();

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        getAllDetailExpense(expense_type_id,expense_name);
    }

    private void getAllDetailExpense(final String expense_type_id,final String expense_name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_details_expense,
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
                                detailsExpenses.clear();
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String expense_records_id = row.getString("expense_records_id");
                                        String amount = row.getString("amount");
                                        String expenses_type = row.getString("expenses_type");
                                        String employee_name = row.getString("employee_name");
                                        String stock_id = row.getString("stock_id");
                                        String date = row.getString("date");



                                        detailsExpenses.add(new DetailsExpenses(expense_records_id,expense_type_id,expenses_type,amount,employee_name,stock_id,date));
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
                params.put("expense_type_id", expense_type_id);
                params.put("frequency",frequency);
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(DetailExpense.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void initializeData() {
        detailsExpenseAdapter = new DetailsExpenseAdapter(detailsExpenses);
        rv.setAdapter(detailsExpenseAdapter);

    }
}
