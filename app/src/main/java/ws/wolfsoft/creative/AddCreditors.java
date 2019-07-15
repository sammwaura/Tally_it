package ws.wolfsoft.creative;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.MySpinnerAdapterOther;
import customfonts.MyTextView;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.save_creditor;
import static ws.wolfsoft.creative.Constants.save_employee;

public class AddCreditors extends AppCompatActivity {

    private SharedPreferences credentialsSharedPreferences;
    private SharedPreferences.Editor credentialsEditor;
    EditText name,amount,creditDesc;

    MyTextView creditor_date;
    DatePickerDialog picker;
    String selDate,fromDate,toDate;

    private Spinner frequency,spinnerTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_creditors);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();

        frequency = findViewById(R.id.spinnerFrequency);
        spinnerTime = findViewById(R.id.spinnerTime);
        creditDesc = findViewById(R.id.creditDesc);
        creditor_date = findViewById(R.id.creditor_date);

        List<String> dataset3 = new ArrayList<>();
        dataset3.add("Select Frequency");
        dataset3.add("Weekly");
        dataset3.add("Monthly");
        dataset3.add("Yearly");
        dataset3.add("Specific Date");


        MySpinnerAdapterOther<String> adapter3 = new MySpinnerAdapterOther<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dataset3);
        frequency.setAdapter(adapter3);
        frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = frequency.getSelectedItem().toString();
                if(!selectedCategory.equals("Select Category"))
                {
                    if(selectedCategory.equals("Specific Date"))
                    {

                        creditor_date.setVisibility(View.VISIBLE);
                        creditor_date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int day = cldr.get(Calendar.DAY_OF_MONTH);
                                int month = cldr.get(Calendar.MONTH);
                                int year = cldr.get(Calendar.YEAR);
                                // date picker dialog
                                picker = new DatePickerDialog(AddCreditors.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                selDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                                Toast.makeText(AddCreditors.this,selDate,Toast.LENGTH_LONG).show();
                                                creditor_date.setText(selDate);
                                            }
                                        }, year, month, day);
                                picker.show();
                            }
                        });

                    }
                    else if(selectedCategory.equals("Weekly"))
                    {
                        creditor_date.setVisibility(View.GONE);
                        List<String> dataset3 = new ArrayList<>();
                        dataset3.add("Sunday");
                        dataset3.add("Monday");
                        dataset3.add("Tuesday");
                        dataset3.add("Wednesday");
                        dataset3.add("Thursday");
                        dataset3.add("Friday");
                        dataset3.add("Saturday");

                        MySpinnerAdapterOther<String> adapter3 = new MySpinnerAdapterOther<>(
                                AddCreditors.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                dataset3);
                        spinnerTime.setAdapter(adapter3);


                    }
                    else if(selectedCategory.equals("Monthly"))
                    {
                        creditor_date.setVisibility(View.GONE);
                        List<String> dataset3 = new ArrayList<>();
                        dataset3.add("1");
                        dataset3.add("2");
                        dataset3.add("3");
                        dataset3.add("4");
                        dataset3.add("5");
                        dataset3.add("6");
                        dataset3.add("7");
                        dataset3.add("8");
                        dataset3.add("9");
                        dataset3.add("10");
                        dataset3.add("11");
                        dataset3.add("12");
                        dataset3.add("13");
                        dataset3.add("14");
                        dataset3.add("15");
                        dataset3.add("16");
                        dataset3.add("17");
                        dataset3.add("18");
                        dataset3.add("19");
                        dataset3.add("20");
                        dataset3.add("21");
                        dataset3.add("22");
                        dataset3.add("23");
                        dataset3.add("24");
                        dataset3.add("25");
                        dataset3.add("26");
                        dataset3.add("27");
                        dataset3.add("28");
                        dataset3.add("29");
                        dataset3.add("30");
                        dataset3.add("31");


                        MySpinnerAdapterOther<String> adapter3 = new MySpinnerAdapterOther<>(
                                AddCreditors.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                dataset3);
                        spinnerTime.setAdapter(adapter3);
                    }
                    else if(selectedCategory.equals("Yearly"))
                    {
                        creditor_date.setVisibility(View.GONE);
                        List<String> dataset3 = new ArrayList<>();
                        dataset3.add("January");
                        dataset3.add("February");
                        dataset3.add("March");
                        dataset3.add("April");
                        dataset3.add("May");
                        dataset3.add("June");
                        dataset3.add("July");
                        dataset3.add("August");
                        dataset3.add("September");
                        dataset3.add("October");
                        dataset3.add("November");
                        dataset3.add("December");


                        MySpinnerAdapterOther<String> adapter3 = new MySpinnerAdapterOther<>(
                                AddCreditors.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                dataset3);
                        spinnerTime.setAdapter(adapter3);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCreditor();
            }
        });

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(AddCreditors.this, ViewCreditors.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });

        if(getIntent().getExtras().getString("type").equals("not_setUp"))
        {
            findViewById(R.id.head2).setVisibility(View.GONE);
            findViewById(R.id.previous).setVisibility(View.GONE);
            findViewById(R.id.next).setVisibility(View.GONE);
        }

        name = findViewById(R.id.creditor_name);
        amount = findViewById(R.id.creditor_amount);

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddCreditors.this, Home.class);
                startActivity(it);
            }
        });

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddCreditors.this, AddExpenses.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });
    }

    private void addCreditor() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, save_creditor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss22 " + s);
                        Toast.makeText(getApplicationContext(), "Saved "+s+" Creditor(s)", Toast.LENGTH_LONG).show();

                        credentialsEditor.putString(Constants.creditor_number, s);
                        credentialsEditor.apply();

                        name.setText("");
                        amount.setText("");

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
                params.put("name", name.getText().toString());
                params.put("amount", amount.getText().toString());
                params.put("frequency", frequency.getSelectedItem().toString());
                params.put("creditor_date", creditor_date.getText().toString());
                params.put("description", creditDesc.getText().toString());
                if(frequency.getSelectedItem().toString().equals("Specific Date"))
                {
                    params.put("time", "");
                }
                else
                {
                    params.put("time", spinnerTime.getSelectedItem().toString());
                }

                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(AddCreditors.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
