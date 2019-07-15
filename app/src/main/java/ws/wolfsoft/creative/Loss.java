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

import adapter.MySpinnerAdapter;
import adapter.SalesAdapter;
import iobserver.SalesIObserver;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.get_loss;

public class Loss extends AppCompatActivity  implements SalesIObserver {

    private Spinner spinner;
    private SharedPreferences credentialsSharedPreferences;
    public List<beans.Sales> sales;
    SalesAdapter salesAdapter;
    private RecyclerView rv;
    TextView overall_number_head,highest,lowest;
     String employee_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss);

        rv =  findViewById(R.id.rv);
        employee_id = getIntent().getExtras().getString("employee_id");


        overall_number_head =  findViewById(R.id.total_Sales);
        highest =  findViewById(R.id.highest_sales);
        lowest =  findViewById(R.id.lowest_sales);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        sales = new ArrayList<>();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loss.this.finish();
                Intent it = new Intent(Loss.this, Home.class);
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

        getAllSales(employee_id);
    }

    private void getAllSales(final String employee_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_loss,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("sales");
                                sales.clear();
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String stock_id = row.getString("stock_id");
                                        String name = row.getString("name");
                                        String total = row.getString("total");

                                        String overall_total = row.getString("overall_total");
                                        String max_price = row.getString("max_price");
                                        String min_price = row.getString("min_price");

                                        overall_number_head.setText(overall_total);
                                        highest.setText(max_price);
                                        lowest.setText(min_price);

                                        //category_number_head.setText(row.getString("count")+" categories");
                                        //category.add(new Category(category_id, name, type));
                                        sales.add(new beans.Sales(stock_id,name,total));
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
                params.put("employee_id", employee_id);
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Loss.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void initializeData() {
        salesAdapter = new SalesAdapter(sales);
        rv.setAdapter(salesAdapter);
        salesAdapter.setListener(this);
    }

    @Override
    public void onCardClicked(int pos, String name) {
        Toast.makeText(getApplicationContext(), "=> "+sales.get(pos).stock_id, Toast.LENGTH_LONG).show();

        Intent it = new Intent(Loss.this, DetailSales.class);
        it.putExtra("stock_id", sales.get(pos).stock_id);
        it.putExtra("stock_name", sales.get(pos).name);
        it.putExtra("frequency", spinner.getSelectedItem().toString());
        startActivity(it);

    }
}
