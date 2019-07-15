package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.DebtorAdapter;
import adapter.MySpinnerAdapterOther;
import adapter.StockAdapter;
import beans.Creditor;
import beans.Stock;
import iobserver.CreditorIObserver;
import iobserver.StockIObserver;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.delete_stock;
import static ws.wolfsoft.creative.Constants.edit_category;
import static ws.wolfsoft.creative.Constants.edit_stock;
import static ws.wolfsoft.creative.Constants.get_categories;
import static ws.wolfsoft.creative.Constants.get_debtor_details;
import static ws.wolfsoft.creative.Constants.get_stock;
import static ws.wolfsoft.creative.Constants.get_sub_categories;

public class ViewDebtorBreakdown extends AppCompatActivity implements CreditorIObserver {

    private SharedPreferences credentialsSharedPreferences;
    public List<Creditor> creditors;
    DebtorAdapter adapter;
    private RecyclerView rv;
    TextView stock_number_head;
    List<String> dataset;
    private List<String> dataset1;
    String categoryName,categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_debtor_breakdown);

        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        creditors = new ArrayList<>();

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        //stock_number_head = findViewById(R.id.head2);
        categoryId =getIntent().getExtras().getString("category_id");
        System.out.println("????????????????????????? "+categoryId);
        categoryName =getIntent().getExtras().getString("category_name");

        getAllDebtorDetails(categoryId);

    }
    protected void onResume() {
        super.onResume();
        getAllDebtorDetails(categoryId);
    }

    private void getAllDebtorDetails(final String categoryId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_debtor_details,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssssStock " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("debtors");
                                creditors.clear();
                                if(posts.length() >0)
                                {
                                    String total_stock = null;
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);
                                        String id = row.getString("id");
                                        String name = row.getString("name")+"#"+row.getString("stock_name")+"#"+row.getString("date");

                                        String amount = row.getString("amount");
                                        if(amount.equals("0"))
                                        {
                                            amount="PAID";
                                        }

                                        String stock_name = row.getString("stock_name");
                                        String business_id = row.getString("business_id");
                                        creditors.add(new Creditor(id, name, amount, business_id));
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
                params.put("categoryId", categoryId);
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewDebtorBreakdown.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }





    private void initializeData() {

        adapter = new DebtorAdapter(creditors);
        rv.setAdapter(adapter);
        adapter.setListener(this);
    }



    public void refreshActivity() {
        this.finish();
        Intent it = new Intent(ViewDebtorBreakdown.this, ViewDebtorBreakdown.class);
        it.putExtra("category_id", categoryId);
        it.putExtra("category_name", categoryName);
        it.putExtra("type", "is_drawer");
        startActivity(it);
    }

    @Override
    public void onCardClicked(int pos, String id, String name, String amount) {

    }
}
