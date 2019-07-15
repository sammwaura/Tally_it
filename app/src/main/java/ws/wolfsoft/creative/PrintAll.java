package ws.wolfsoft.creative;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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
import beans.Sales;
import iobserver.SalesIObserver;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.email;
import static ws.wolfsoft.creative.Constants.employee_id;
import static ws.wolfsoft.creative.Constants.get_all_products;
import static ws.wolfsoft.creative.Constants.get_category_sales;
import static ws.wolfsoft.creative.Constants.print_sales;

public class PrintAll extends AppCompatActivity implements SlyCalendarDialog.Callback, SalesIObserver {

    private Spinner spinner;
    private SharedPreferences credentialsSharedPreferences;
    public List<beans.Sales> sales;
    SalesAdapter salesAdapter;
    private RecyclerView rv;
    List<String> dataset;
    DatePickerDialog picker;
    String selDate,fromDate,toDate;

    SearchableSpinner spinnerTypeProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_all);

        rv =  findViewById(R.id.rv);
        findViewById(R.id.fab_print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Check your email");
                printSales();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        sales = new ArrayList<>();

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);



        spinnerTypeProduct = findViewById(R.id.spinnerTypeProduct);

        spinnerTypeProduct.setFocusable(true);
        spinnerTypeProduct.setFocusableInTouchMode(true);

        dataset = new ArrayList<>();
        dataset.add("Select Product");
        getAllProducts(spinnerTypeProduct);

        spinnerTypeProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getAllSales();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    picker = new DatePickerDialog(PrintAll.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    selDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    Toast.makeText(PrintAll.this,selDate,Toast.LENGTH_LONG).show();
                                    getAllSales();
                                }
                            }, year, month, day);
                    picker.show();
                }
                else if(selectedCategory.equals("Specify Date Range"))
                {
                    new SlyCalendarDialog()
                            .setSingle(false)
                            .setCallback(PrintAll.this)
                            .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
                }
                else
                {
                    getAllSales();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getAllProducts(final SearchableSpinner spinnerTypeCat) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_all_products,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssssgetAllCategory " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);
                                JSONArray posts = jsonObj.getJSONArray("stock");
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);
                                        String name = row.getString("name");
                                        dataset.add(name);
                                    }

                                }

                                MySpinnerAdapter<String> adapter3 = new MySpinnerAdapter<>(
                                        PrintAll.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        dataset);
                                spinnerTypeCat.setAdapter(adapter3);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Toast.makeText(PrintAll.this,"Server did not return any useful data",Toast.LENGTH_LONG).show();
                        }
                        System.out.println("MAIN response " + s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        System.out.println("volleyError response " + volleyError.getMessage());
                        Toast.makeText(PrintAll.this, "Error. Try Reopening App", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PrintAll.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void printSales(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, print_sales,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        System.out.println("Check your email"+s);
                        Toast.makeText(getApplicationContext(), "Check your email within 5 minutes.", Toast.LENGTH_LONG).show();
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
                params.put("email", Objects.requireNonNull(credentialsSharedPreferences.getString(email, "0")));
                params.put("frequency",spinner.getSelectedItem().toString());
                params.put("theSelectedProduct",spinnerTypeProduct.getSelectedItem().toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(PrintAll.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getAllSales(){
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
        RequestQueue requestQueue = Volley.newRequestQueue(PrintAll.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void initializeData() {
        salesAdapter = new SalesAdapter(sales);
        rv.setAdapter(salesAdapter);
        salesAdapter.setListener(this);
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
                getAllSales();

            }
        }
    }

    @Override
    public void onCardClicked(int pos, String name) {

    }
}
