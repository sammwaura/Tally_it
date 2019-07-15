package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
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

import adapter.CategoryAdapter;
import adapter.MySpinnerAdapterOther;
import beans.Category;
import iobserver.CategoryIObserver;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.edit_category;
import static ws.wolfsoft.creative.Constants.get_categories;

public class ViewCategories extends AppCompatActivity implements CategoryIObserver{

    private SharedPreferences credentialsSharedPreferences;
    public List<Category> category;
    CategoryAdapter categoryAdapter;
    private RecyclerView rv;
    TextView category_number_head;
    private NiftyDialogBuilder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_categories);

        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        category = new ArrayList<>();

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        category_number_head = findViewById(R.id.head2);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCategories.this.finish();
                Intent it = new Intent(ViewCategories.this, AddCategories.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });


        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ViewCategories.this, AddCategories.class);
                it.putExtra("type", "not_setUp");
                startActivity(it);
            }
        });

            getAllCategory();

        if(getIntent().getExtras().getString("type").equals("is_settings"))
        {
            findViewById(R.id.head2).setVisibility(View.GONE);
            findViewById(R.id.back).setVisibility(View.GONE);
        }


    }

    protected void onResume() {
        super.onResume();
        getAllCategory();
    }

    private void getAllCategory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_categories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("categories");
                                category.clear();
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String category_id = row.getString("category_id");
                                        String name = row.getString("name");
                                        String type = row.getString("type");
                                        category_number_head.setText(row.getString("count")+" categories");
                                        category.add(new Category(category_id, name, type));
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
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewCategories.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }





    private void initializeData() {
        categoryAdapter = new CategoryAdapter(this, (CategoryAdapter.AdapterListener) this, category);
        rv.setAdapter(categoryAdapter);
        categoryAdapter.setListener(this);
    }


    @Override
    public void onCategorySelected(Category category) {

    }

    @Override
    public void onCardClicked(final int pos, String name) {
        dialogBuilder = NiftyDialogBuilder.getInstance(ViewCategories.this);
        dialogBuilder
                .withTitle("Edit Categories")
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
                .setCustomView(R.layout.dialog_edit_category, ViewCategories.this)//def gone//def gone
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

                                         final EditText category_name =  dialogBuilder.findViewById(R.id.category_name_edit);
                                         final Spinner spinnerType =  dialogBuilder.findViewById(R.id.spinnerType);


                                         System.out.println("RRRRRRRRRRRrRRRRRRRRRR "+category_name.getText().toString());
                                         editCategory(category.get(pos).category_id,category_name.getText().toString(),spinnerType.getSelectedItem().toString());
                                     }
                                 }
                );

        final EditText category_name =  dialogBuilder.findViewById(R.id.category_name_edit);
        final Spinner spinnerType =  dialogBuilder.findViewById(R.id.spinnerType);


        List<String> datasetType = new ArrayList<>();
        datasetType.add("Select Type");
        datasetType.add("Product");
        datasetType.add("Service");

        MySpinnerAdapterOther<String> adapter4 = new MySpinnerAdapterOther<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                datasetType);
        spinnerType.setAdapter(adapter4);

        String type = category.get(pos).type;
        ArrayAdapter myAdap = (ArrayAdapter) spinnerType.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = myAdap.getPosition(type);

        //set the default according to value
        spinnerType.setSelection(spinnerPosition);

        category_name.setText(category.get(pos).name);

        dialogBuilder.show();
    }

    private void editCategory(final String category_id, final String name, final String type) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, edit_category,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss2 " + s);
                        Toast.makeText(getApplicationContext(), "Edited Category", Toast.LENGTH_LONG).show();
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
                params.put("category_id", category_id);
                params.put("name", name);
                params.put("type", type);


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewCategories.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void refreshActivity() {
        this.finish();
        Intent it = new Intent(ViewCategories.this, ViewCategories.class);
        it.putExtra("type", "");
        startActivity(it);
    }
}
