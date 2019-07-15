package ws.wolfsoft.creative;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import adapter.MySpinnerAdapter;
import adapter.ProfitsAdapter;
import adapter.SalesAdapter;
import iobserver.ProfitIObserver;
import iobserver.SalesIObserver;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.get_ratings;
import static ws.wolfsoft.creative.Constants.get_sales;

public class SalesRatings extends AppCompatActivity  implements ProfitIObserver {


    private SharedPreferences credentialsSharedPreferences;
    public List<beans.Profit> profits;
    ProfitsAdapter profitsAdapter;
    private RecyclerView rv;
    DatePickerDialog picker;
    String selDate,fromDate,toDate;
    TextView overall_number_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_ratings);

        rv =  findViewById(R.id.rv);



        overall_number_head =  findViewById(R.id.total_Sales);
        overall_number_head.setText(getIntent().getExtras().getString("title")+ " 10");


        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        profits = new ArrayList<>();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesRatings.this.finish();
                Intent it = new Intent(SalesRatings.this, Home.class);
                startActivity(it);
            }
        });

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);


        getAllSales();
    }

    private void getAllSales() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_ratings,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss1234 " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("profits");
                                profits.clear();
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String stock_id = row.getString("stock_id");
                                        String name = row.getString("name");
                                        String total = row.getString("total");

                                        //String overall_total = row.getString("overall_total");


                                        //overall_number_head.setText(overall_total);


                                        //category_number_head.setText(row.getString("count")+" categories");
                                        //category.add(new Category(category_id, name, type));
                                        profits.add(new beans.Profit(stock_id,name,total));
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
                params.put("frequency","Weekly");
                params.put("title",getIntent().getExtras().getString("title"));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(SalesRatings.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void initializeData() {
        profitsAdapter = new ProfitsAdapter(profits);
        rv.setAdapter(profitsAdapter);
        profitsAdapter.setListener(this);
    }

    @Override
    public void onCardClicked(int pos, String name) {
        Toast.makeText(getApplicationContext(), "=> "+profits.get(pos).stock_id, Toast.LENGTH_LONG).show();



    }

}
