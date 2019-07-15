package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import adapter.CreditorAdapter;
import beans.Creditor;
import iobserver.CreditorIObserver;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.creditor_number;
import static ws.wolfsoft.creative.Constants.delete_creditor;
import static ws.wolfsoft.creative.Constants.delete_stock;
import static ws.wolfsoft.creative.Constants.edit_creditor;
import static ws.wolfsoft.creative.Constants.edit_employee;
import static ws.wolfsoft.creative.Constants.get_creditors;

public class ViewCreditors extends AppCompatActivity implements CreditorIObserver {
    private SharedPreferences credentialsSharedPreferences;
    public List<Creditor> creditors;
    CreditorAdapter adapter;
    private RecyclerView rv;
    TextView creditor_number_head;
    private NiftyDialogBuilder dialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_creditors);

        creditors = new ArrayList<>();

        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        creditor_number_head = findViewById(R.id.head2);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCreditors.this.finish();
                Intent it = new Intent(ViewCreditors.this, AddCreditors.class);
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
        creditor_number_head.setText(String.format("%s Creditor(s)", Objects.requireNonNull(credentialsSharedPreferences.getString(creditor_number, "0"))));

        getAllCreditors();

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ViewCreditors.this, AddCreditors.class);
                it.putExtra("type", "not_setUp");
                startActivity(it);
            }
        });

    }

    protected void onResume() {
        super.onResume();
        getAllCreditors();
    }

    private void getAllCreditors() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_creditors,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("creditors");
                                creditors.clear();
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String id = row.getString("id");
                                        String name = row.getString("name");
                                        String amount = row.getString("amount");
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

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewCreditors.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void initializeData() {

        adapter = new CreditorAdapter(creditors);
        rv.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    public void onCardClicked(final int pos, String id, String name, String amount) {
        dialogBuilder = NiftyDialogBuilder.getInstance(ViewCreditors.this);
        dialogBuilder
                .withTitle("Edit Creditors")
                .withTitleColor("#303f9f")                                  //def
                .withDividerColor("#3f51b5")                              //def//.withMessage(null)  no Msg
                .withMessageColor("#00B873")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)
                .withDuration(700)
                .isCancelableOnTouchOutside(false)
                .isCancelable(false)
                .withEffect(Effectstype.Newspager)
                .withButton2Text("CANCEL")//def Effectstype.Slidetop
                .withButton1Text("EDIT")
                .setCustomView(R.layout.dialog_edit_credit, ViewCreditors.this)//def gone//def gone
                .isCancelableOnTouchOutside(true)
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .setButton1Click(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {

                                         final EditText category_name_edit =  dialogBuilder.findViewById(R.id.category_name_edit);
                                         final EditText description =  dialogBuilder.findViewById(R.id.description);
                                         final EditText date =  dialogBuilder.findViewById(R.id.date);
                                         final EditText category_name_amount =  dialogBuilder.findViewById(R.id.category_name_amount);
                                         final EditText category_name_id =  dialogBuilder.findViewById(R.id.category_name_id);


                                         System.out.println("RRRRRRRRRRRrRRRRRRRRRR "+category_name_edit.getText().toString());

                                         editCreditor(creditors.get(pos).id,category_name_edit.getText().toString(),category_name_amount.getText().toString());

                                     }
                                 }
                );

        final EditText category_name_edit =  dialogBuilder.findViewById(R.id.category_name_edit);
        final EditText category_name_amount =  dialogBuilder.findViewById(R.id.category_name_amount);
        final EditText category_name_id =  dialogBuilder.findViewById(R.id.category_name_id);
        final EditText description =  dialogBuilder.findViewById(R.id.description);
        final EditText date =  dialogBuilder.findViewById(R.id.date);
        final Button delete =  dialogBuilder.findViewById(R.id.button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Deleting",Toast.LENGTH_LONG).show();
                deleteCreditor(creditors.get(pos).id);
            }
        });


        category_name_edit.setText(creditors.get(pos).name.split("#")[0]);
        if(creditors.get(pos).name.split("#").length >=1)
        {
            description.setText(creditors.get(pos).name.split("#")[1]);
        }
        if(creditors.get(pos).name.split("#").length >=2)
        {
            date.setText(creditors.get(pos).name.split("#")[2]);
        }

        //category_name_edit.setText(creditors.get(pos).name);

        category_name_amount.setText(creditors.get(pos).amount);
        category_name_id.setText(creditors.get(pos).id);

        dialogBuilder.show();
    }

    private void deleteCreditor(final String id) {
        System.out.println("HEREHERE");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, delete_creditor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssssNewSub " + s);
                        Toast.makeText(getApplicationContext(), "Deleted Creditor", Toast.LENGTH_LONG).show();
                        refreshActivity();
                        initializeData();
                        dialogBuilder.dismiss();
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
                params.put("creditor_id", id);
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewCreditors.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void refreshActivity() {
        this.finish();
        Intent it = new Intent(ViewCreditors.this, ViewCreditors.class);
        it.putExtra("type", "");
        startActivity(it);
    }

    private void editCreditor(final String id, final String creditorName, final String creditorAmount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, edit_creditor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss2 " + s);
                        Toast.makeText(getApplicationContext(), "Edited Creditors(s)", Toast.LENGTH_LONG).show();
                        refreshActivity();
                        initializeData();
                        dialogBuilder.dismiss();
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
                params.put("creditorName", creditorName);
                params.put("creditorAmount", creditorAmount);
                params.put("id", id);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewCreditors.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
