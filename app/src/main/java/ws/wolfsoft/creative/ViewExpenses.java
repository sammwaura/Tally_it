package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import adapter.ExpensesAdapter;
import beans.Expense;
import iobserver.ExpensesIObserver;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.get_expenses;

public class ViewExpenses extends AppCompatActivity implements ExpensesIObserver {
    private SharedPreferences credentialsSharedPreferences;
    public List<Expense> expenses;
    ExpensesAdapter adapter;
    private RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);

        expenses = new ArrayList<>();

        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewExpenses.this.finish();
                Intent it = new Intent(ViewExpenses.this, AddExpenses.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        if(getIntent().getExtras().getString("type").equals("is_settings"))
        {

            findViewById(R.id.back).setVisibility(View.GONE);
        }

        getAllExpenses();

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ViewExpenses.this, AddExpenses.class);
                it.putExtra("type", "not_setUp");
                startActivity(it);
            }
        });
    }

    private void getAllExpenses() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_expenses,
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
                                expenses.clear();
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String id = row.getString("id");
                                        String name = row.getString("name");
                                        String expense_type = row.getString("expenses_type");
                                        String frequency = row.getString("frequency");
                                        String business_id = row.getString("business_id");
                                        expenses.add(new Expense(id, name, expense_type, frequency, business_id));

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
        RequestQueue requestQueue = Volley.newRequestQueue(ViewExpenses.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void initializeData() {

        adapter = new ExpensesAdapter(expenses);
        rv.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    public void onCardClicked(int pos, String id, String name, String expense_type, String frequency) {

    }
}
