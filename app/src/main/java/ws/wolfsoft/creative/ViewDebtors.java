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
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.CreditorAdapter;
import adapter.DebtorAdapter;
import adapter.MySpinnerAdapter;
import beans.Creditor;
import iobserver.CreditorIObserver;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.creditor_number;
import static ws.wolfsoft.creative.Constants.edit_creditor;
import static ws.wolfsoft.creative.Constants.edit_debtor;
import static ws.wolfsoft.creative.Constants.get_creditors;
import static ws.wolfsoft.creative.Constants.get_debtors;

public class ViewDebtors extends AppCompatActivity implements CreditorIObserver,SlyCalendarDialog.Callback {
    private SharedPreferences credentialsSharedPreferences;
    public List<Creditor> creditors;
    DebtorAdapter adapter;
    private RecyclerView rv;
    TextView creditor_number_head;
    private NiftyDialogBuilder dialogBuilder;
    private Spinner spinner;
    DatePickerDialog picker;
    String selDate,fromDate,toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_debtors);

        creditors = new ArrayList<>();

        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

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
                    picker = new DatePickerDialog(ViewDebtors.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    selDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    Toast.makeText(ViewDebtors.this,selDate,Toast.LENGTH_LONG).show();
                                    getAllDebtors();
                                }
                            }, year, month, day);
                    picker.show();
                }
                else if(selectedCategory.equals("Specify Date Range"))
                {
                    new SlyCalendarDialog()
                            .setSingle(false)
                            .setCallback(ViewDebtors.this)
                            .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
                }
                else
                {
                    getAllDebtors();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        creditor_number_head = findViewById(R.id.head2);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDebtors.this.finish();
                Intent it = new Intent(ViewDebtors.this, AddCreditors.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        if(getIntent().getExtras().getString("type").equals("is_settings"))
        {
            findViewById(R.id.head2).setVisibility(View.GONE);
            findViewById(R.id.back).setVisibility(View.GONE);
        }

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        creditor_number_head.setText(String.format("%s Debtors(s)", Objects.requireNonNull(credentialsSharedPreferences.getString(creditor_number, "0"))));

        getAllDebtors();

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ViewDebtors.this, AddCreditors.class);
                it.putExtra("type", "not_setUp");
                startActivity(it);
            }
        });

    }

    private void getAllDebtors() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_debtors,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("debtors");
                                creditors.clear();
                                if(posts.length() >0)
                                {
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
                }) {
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

                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewDebtors.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    protected void onResume() {
        super.onResume();
        getAllDebtors();
    }

    private void initializeData() {

        adapter = new DebtorAdapter(creditors);
        rv.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    public void onCardClicked(final int pos, String id, String name, String amount) {
        dialogBuilder = NiftyDialogBuilder.getInstance(ViewDebtors.this);
        dialogBuilder
                .withTitle("Edit Debtors")
                .withTitleColor("#303f9f")                                  //def
                .withDividerColor("#3f51b5")                              //def//.withMessage(null)  no Msg
                .withMessageColor("#00B873")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)
                .withDuration(700)
                .isCancelableOnTouchOutside(false)
                .isCancelable(false)
                .withEffect(Effectstype.Newspager)
                .withButton2Text("NOT SOLD")//def Effectstype.Slidetop
                .withButton1Text("SOLD")
                .setCustomView(R.layout.dialog_edit_debtor, ViewDebtors.this)//def gone//def gone
                .isCancelableOnTouchOutside(true)
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final EditText category_name_edit =  dialogBuilder.findViewById(R.id.category_name_edit);
                        final EditText category_name_amount =  dialogBuilder.findViewById(R.id.category_name_amount);
                        final EditText category_name_quantity =  dialogBuilder.findViewById(R.id.category_name_quantity);


                        System.out.println("RRRRRRRRRRRrRRRRRRRRRR "+category_name_edit.getText().toString());

                        editDebtor(creditors.get(pos).id,category_name_edit.getText().toString(),category_name_amount.getText().toString(),category_name_quantity.getText().toString(),"NOT SOLD");

                    }
                })
                .setButton1Click(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {

                                         final EditText category_name_edit =  dialogBuilder.findViewById(R.id.category_name_edit);
                                         final EditText category_name_amount =  dialogBuilder.findViewById(R.id.category_name_amount);
                                         final EditText category_name_quantity =  dialogBuilder.findViewById(R.id.category_name_quantity);


                                         System.out.println("RRRRRRRRRRRrRRRRRRRRRR "+category_name_edit.getText().toString());

                                         editDebtor(creditors.get(pos).id,category_name_edit.getText().toString(),category_name_amount.getText().toString(),category_name_quantity.getText().toString(),"SOLD");

                                     }
                                 }
                );

        final EditText category_name_edit =  dialogBuilder.findViewById(R.id.category_name_edit);
        final EditText category_name_amount =  dialogBuilder.findViewById(R.id.category_name_amount);
        final EditText category_name_quantity =  dialogBuilder.findViewById(R.id.category_name_quantity);

        final EditText date =  dialogBuilder.findViewById(R.id.debtor_date);
        final EditText stock_name =  dialogBuilder.findViewById(R.id.stock_name);


        category_name_edit.setText(creditors.get(pos).name.split("#")[0]);
        date.setText(creditors.get(pos).name.split("#")[2]);
        stock_name.setText(creditors.get(pos).name.split("#")[1]);

        category_name_amount.setText(creditors.get(pos).amount);


        dialogBuilder.show();
    }

    public void refreshActivity() {
        this.finish();
        Intent it = new Intent(ViewDebtors.this, ViewDebtors.class);
        it.putExtra("type", "is_settings");
        startActivity(it);
    }

    private void editDebtor(final String id, final String creditorName, final String creditorAmount,final String debtorQuantity,final String value) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, edit_debtor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss2 " + s);
                        Toast.makeText(getApplicationContext(), "Edited Debtors(s)", Toast.LENGTH_LONG).show();
                        refreshActivity();
                        initializeData();
                        dialogBuilder.dismiss();
                    }
                },
                new Response.ErrorListener(){
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
                params.put("creditorName", creditorName);
                params.put("creditorAmount", creditorAmount);
                params.put("debtorQuantity", debtorQuantity);
                params.put("is_sold", value);
                params.put("id", id);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewDebtors.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {

    }
}
