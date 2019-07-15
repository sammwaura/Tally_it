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
import adapter.SalesAdapter;
import iobserver.SalesIObserver;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.get_category_sales;

public class GoodsPerformance extends AppCompatActivity  implements SalesIObserver,SlyCalendarDialog.Callback {

    private Spinner spinner;
    private SharedPreferences credentialsSharedPreferences;
    public List<beans.Sales> sales;
    SalesAdapter salesAdapter;
    private RecyclerView rv;
    DatePickerDialog picker;
    String selDate,fromDate,toDate;
    String employee_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_performance);

        rv =  findViewById(R.id.rv);
        employee_id = getIntent().getExtras().getString("employee_id");

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        sales = new ArrayList<>();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsPerformance.this.finish();
                Intent it = new Intent(GoodsPerformance.this, Home.class);
                startActivity(it);
            }
        });

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        spinner = findViewById(R.id.spinnerType);
        List<String> dataset = new ArrayList<>();
        dataset.add("Daily Statistics");
        dataset.add("Monthly");
        dataset.add("Weekly");
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = spinner.getSelectedItem().toString();
                if(selectedCategory.equals("Specify Date"))
                {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(GoodsPerformance.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    selDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    Toast.makeText(GoodsPerformance.this,selDate,Toast.LENGTH_LONG).show();
                                    getAllSales(employee_id);
                                }
                            }, year, month, day);
                    picker.show();
                }
                else if(selectedCategory.equals("Specify Date Range"))
                {
                    new SlyCalendarDialog()
                            .setSingle(false)
                            .setCallback(GoodsPerformance.this)
                            .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
                }
                else
                {
                    getAllSales(employee_id);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //getAllSales();
    }

    private void getAllSales(final String employee_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_category_sales,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss1234 " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("sales");
                                sales.clear();
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length()-1; i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String stock_id = row.getString("stock_id");
                                        String name = row.getString("name");
                                        String total = row.getString("total");

                                        //category_number_head.setText(row.getString("count")+" categories");
                                        //category.add(new Category(category_id, name, type));
                                        sales.add(new beans.Sales(stock_id,name,total));
                                    }

                                    String overall_total = posts.getJSONObject(posts.length()-1).getString("overall_total");
                                    String max_price = posts.getJSONObject(posts.length()-1).getString("cash_sales");
                                    String min_price = posts.getJSONObject(posts.length()-1).getString("debt_sales");



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
                params.put("theSelectedProduct","Select Product");
                params.put("employee_id", employee_id);
                if(spinner.getSelectedItem().toString().equals("Specify Date"))
                {
                    params.put("date_specified",selDate);
                }
                if(spinner.getSelectedItem().toString().equals("Specify Date Range"))
                {
                    params.put("from_date_specified",fromDate);
                    params.put("to_date_specified",toDate);
                }
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(GoodsPerformance.this);

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

        Intent it = new Intent(GoodsPerformance.this, DetailsGoodsPerformance.class);
        it.putExtra("stock_id", sales.get(pos).stock_id);
        it.putExtra("stock_name", sales.get(pos).name);
        it.putExtra("frequency", spinner.getSelectedItem().toString());
        startActivity(it);

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
        if (firstDate != null) {
            if (secondDate == null) {
                ////
                //Toast.makeText(getApplicationContext(), "First Date.", Toast.LENGTH_LONG).show();
                firstDate.set(Calendar.HOUR_OF_DAY, hours);
                firstDate.set(Calendar.MINUTE, minutes);

                Toast.makeText(this, new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(firstDate.getTime()), Toast.LENGTH_LONG).show();

            } else {
                ///
                //Toast.makeText(getApplicationContext(), "Second plus First Date.", Toast.LENGTH_LONG).show();
                Toast.makeText(
                        this,
                        getString(
                                R.string.period,
                                new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(firstDate.getTime()),
                                new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(secondDate.getTime())
                        ),
                        Toast.LENGTH_LONG

                ).show();

                fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(firstDate.getTime());
                toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(secondDate.getTime());
            }
        }
    }
}
