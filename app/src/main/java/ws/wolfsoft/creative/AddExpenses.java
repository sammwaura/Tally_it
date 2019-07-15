package ws.wolfsoft.creative;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.MySpinnerAdapterOther;

import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.save_expense;

public class AddExpenses extends AppCompatActivity {

    TextView name,amount;
    private Spinner spinnerExpenses,frequency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        name = findViewById(R.id.expenses_name);
        spinnerExpenses = findViewById(R.id.spinnerExpenses);
        frequency = findViewById(R.id.spinnerFrequency);
        amount=findViewById(R.id.expenses_amount);

        List<String> dataset2 = new ArrayList<>();
        dataset2.add("Select Type");
        dataset2.add("General Expense");
        dataset2.add("Product Specific Expense");


        MySpinnerAdapterOther<String> adapter2 = new MySpinnerAdapterOther<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dataset2);
        spinnerExpenses.setAdapter(adapter2);

        List<String> dataset3 = new ArrayList<>();
        dataset3.add("Select Frequency");
        dataset3.add("Daily");
        dataset3.add("Weekly");
        dataset3.add("Monthly");
        dataset3.add("Yearly");


        MySpinnerAdapterOther<String> adapter3 = new MySpinnerAdapterOther<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dataset3);
        frequency.setAdapter(adapter3);


        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().equals(""))
                {
                    saveExpense();
                }

            }
        });


        if(getIntent().getExtras().getString("type").equals("not_setUp"))
        {
            findViewById(R.id.head2).setVisibility(View.GONE);
            findViewById(R.id.previous).setVisibility(View.GONE);
            findViewById(R.id.next).setVisibility(View.GONE);

        }

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddExpenses.this, AddStock.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(AddExpenses.this, ViewExpenses.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddExpenses.this, AddCreditors.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });
    }

    private void saveExpense() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, save_expense,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        Toast.makeText(getApplicationContext(), "Saved  Expense", Toast.LENGTH_LONG).show();
                        name.setText("");
                        frequency.setSelection(0);
                        spinnerExpenses.setSelection(0);
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
                params.put("expenses_type", spinnerExpenses.getSelectedItem().toString());
                params.put("amount", amount.getText().toString());
                params.put("frequency", frequency.getSelectedItem().toString());
                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(AddExpenses.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
