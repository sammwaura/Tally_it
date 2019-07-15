package ws.wolfsoft.creative;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

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

import adapter.EmployeeSalesAdapter;
import adapter.MySpinnerAdapter;
import iobserver.SalesIObserver;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.get_employee_sales;

public class EmployeeSales extends AppCompatActivity implements SlyCalendarDialog.Callback {

    private Spinner spinner;
    private SharedPreferences credentialsSharedPreferences;
    List <beans.EmployeeSales> employeeSales;
    EmployeeSalesAdapter employeeSalesAdapter;
    RecyclerView recyclerView;
    private NiftyDialogBuilder dialogBuilder;
    String selDate,fromDate,toDate;
    DatePickerDialog picker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_sales);

        employeeSales = new ArrayList <>();
        recyclerView = findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);

        getAllEmployeeSales();

        spinner = findViewById(R.id.spinnerType);
        List <String> dataset = new ArrayList <>();
        dataset.add("Daily Statistics");
        dataset.add("Monthly");
        dataset.add("Yearly");
        dataset.add("Specify Date");
        dataset.add("Specify Date Range");
        dataset.add("Total");

        MySpinnerAdapter <String> adapter = new MySpinnerAdapter <>(this,
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
                    picker = new DatePickerDialog(EmployeeSales.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    selDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    Toast.makeText(EmployeeSales.this,selDate,Toast.LENGTH_LONG).show();
                                    getAllEmployeeSales();
                                }
                            }, year, month, day);
                    picker.show();
                }
                else if(selectedCategory.equals("Specify Date Range"))
                {
                    new SlyCalendarDialog()
                            .setSingle(false)
                            .setCallback(EmployeeSales.this)
                            .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
                }
                else
                {
                    getAllEmployeeSales();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllEmployeeSales() {
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, get_employee_sales,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("######@@@@@@##@#@#@#@#@" + s);
                        if (s != null) {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObject = new JSONObject(s);

                                JSONArray post = jsonObject.getJSONArray("employees");
                                employeeSales.clear();
                                if (post.length() > 0) {
                                    for (int i = 0; i < post.length(); i++) {
                                        JSONObject row = post.getJSONObject(i);

                                        String name = row.getString("name");
                                        String id = row.getString("id");
//                                        String total_amount = row.getString("total_amount");


                                        employeeSales.add(new beans.EmployeeSales(id, name));
                                    }

                                    initializeData();
                                    findViewById(R.id.no_internet).setVisibility(View.GONE);
                                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                                    findViewById(R.id.empty_view).setVisibility(View.GONE);
                                    findViewById(R.id.rv).setVisibility(View.VISIBLE);
                                } else {
                                    findViewById(R.id.no_internet).setVisibility(View.GONE);
                                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                    findViewById(R.id.rv).setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Server did not return any useful data", Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new Hashtable <>();

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

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeSales.this);
        requestQueue.add(stringRequest);

    }

    private void initializeData() {
        employeeSalesAdapter = new EmployeeSalesAdapter(employeeSales);
        recyclerView.setAdapter(employeeSalesAdapter);
//        employeeSalesAdapter.setListener(this);
    }

    public void refreshActivity() {
        this.finish();
        Intent it = new Intent(EmployeeSales.this, EmployeeSales.class);
        it.putExtra("type", "");
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
                getAllEmployeeSales();

            }
        }

    }
}
