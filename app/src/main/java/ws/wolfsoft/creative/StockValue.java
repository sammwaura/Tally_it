package ws.wolfsoft.creative;

import android.content.Context;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.MySpinnerAdapter;
import beans.Creditor;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.get_debtors;
import static ws.wolfsoft.creative.Constants.get_stock_value;

public class StockValue extends AppCompatActivity {
    private SharedPreferences credentialsSharedPreferences;
    TextView totalStockValue;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_value);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);

        spinner = findViewById(R.id.spinnerType);
        List<String> dataset = new ArrayList<>();
        dataset.add("Daily Statistics");
        dataset.add("Monthly");
        dataset.add("Yearly");
        dataset.add("Total");

        totalStockValue = findViewById(R.id.totalStockValue);

        MySpinnerAdapter<String> adapter = new MySpinnerAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dataset);

        spinner.setAdapter(adapter);

        getStockValue();
    }

    private void getStockValue() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_stock_value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss__get_stock_value " + s);
                        if(s!=null)
                        {
                            totalStockValue.setText("Ksh. "+s);

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
                params.put("frequency",spinner.getSelectedItem().toString());


                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(StockValue.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
