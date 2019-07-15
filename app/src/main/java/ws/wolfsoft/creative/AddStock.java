package ws.wolfsoft.creative;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.MySpinnerAdapterOther;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.get_categories;
import static ws.wolfsoft.creative.Constants.get_sub_categories;
import static ws.wolfsoft.creative.Constants.get_sub_sub_categories;
import static ws.wolfsoft.creative.Constants.save_stock;

public class AddStock extends AppCompatActivity {

    private Spinner spinnerCat,spinnerSubCat,spinnerSubSubCat;
    private Spinner spinnerQuantity;
    List<String> dataset;
    List<String> datasetType;
    List<String> dataset1,dataset2,dataset3;
    private SharedPreferences.Editor credentialsEditor;

    EditText name,quantity;

    String selDate,fromDate,toDate;

    String selectedTypeOfStock;

    View lineQuantity;
    EditText buyingPrice;
    TextView pickDate;
    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stock);

        name = findViewById(R.id.stock_name);

        spinnerSubCat = findViewById(R.id.spinnerTypeSubCat);

        pickDate= findViewById(R.id.pickDate);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddStock.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                selDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                Toast.makeText(AddStock.this,selDate,Toast.LENGTH_LONG).show();
                                pickDate.setText(selDate);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        spinnerSubSubCat = findViewById(R.id.spinnerTypeSubSubCat);

        buyingPrice = findViewById(R.id.buying_price);
        quantity = findViewById(R.id.quantity);
        lineQuantity = findViewById(R.id.quantity_view);


        if(getIntent().getExtras().getString("type").equals("not_setUp"))
        {
            findViewById(R.id.head2).setVisibility(View.GONE);
            findViewById(R.id.previous).setVisibility(View.GONE);
            findViewById(R.id.next).setVisibility(View.GONE);

        }


        spinnerCat =  findViewById(R.id.spinnerTypeCat);
        dataset = new ArrayList<>();
        dataset.add("Select Category");
        getAllCategory();

        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = spinnerCat.getSelectedItem().toString();
                if(!selectedCategory.equals("Select Category"))
                {
                    System.out.println("++++++++++++++++++++++ "+selectedCategory);
                    dataset1 = new ArrayList<>();
                    getSubCategory(selectedCategory);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();


        MySpinnerAdapterOther<String> adapter = new MySpinnerAdapterOther<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dataset);

        spinnerCat.setAdapter(adapter);


        spinnerQuantity = findViewById(R.id.spinnerTypeUnit);
        List<String> dataset2 = new ArrayList<>();
        dataset2.add("Select Unit");
        dataset2.add("pieces");
        dataset2.add("kilogrammes");
        dataset2.add("tonnes");
        dataset2.add("grammes");
        dataset2.add("liters");
        dataset2.add("meter square");
        dataset2.add("meters");
        dataset2.add("inches");
        dataset2.add("centimeters");
        dataset2.add("sets");
        dataset2.add("cartons");
        dataset2.add("trays");


        MySpinnerAdapterOther<String> adapter2 = new MySpinnerAdapterOther<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dataset2);
        spinnerQuantity.setAdapter(adapter2);

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(AddStock.this, AddExpenses.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(AddStock.this, ViewStock2.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddStock.this, AddCategories.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStock();
            }
        });

    }

    private void saveStock() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, save_stock,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        name.setText("");
                        quantity.setText("");
                        buyingPrice.setText("");
                        spinnerCat.setSelection(0);

                        spinnerQuantity.setSelection(0);
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
                params.put("quantity", quantity.getText().toString());
                params.put("category", spinnerCat.getSelectedItem().toString());
                params.put("subCategory", spinnerSubCat.getSelectedItem().toString());
                params.put("sub_subCategory", spinnerSubSubCat.getSelectedItem().toString());
                params.put("buying_price", buyingPrice.getText().toString());
                params.put("metric", spinnerQuantity.getSelectedItem().toString());
                params.put("date", pickDate.getText().toString());

                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(AddStock.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getSubCategory(final String selectedCategory) {
        System.out.println("HEREHERE");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_sub_categories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("sub_category");
                                dataset1.clear();
                                dataset1.add("Select Sub-Category");
                                spinnerSubCat.setSelection(0);


                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String sub_category_id = row.getString("sub_category_id");
                                        String name = row.getString("name");

                                        dataset1.add(name);
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            MySpinnerAdapterOther<String> adapter = new MySpinnerAdapterOther<>(
                                    AddStock.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    dataset1);

                            spinnerSubCat.setAdapter(adapter);

                            spinnerSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                    String selectedSubCategory = spinnerSubCat.getSelectedItem().toString();
                                    if(!selectedSubCategory.equals("Select Category"))
                                    {
                                        System.out.println("++++++++++++++++++++++ "+selectedSubCategory);
                                        dataset3 = new ArrayList<>();
                                        getSubSubCategory(selectedSubCategory,selectedCategory);
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    // your code here
                                }

                            });
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
                params.put("selected_category", selectedCategory);
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(AddStock.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getSubSubCategory(final String selectedSubCategory,final String selectedCategory) {
        System.out.println("HEREHERE");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_sub_sub_categories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("sub_subcategory");
                                dataset3.clear();
                                dataset3.add("Select Sub-subCategory");
                                spinnerSubSubCat.setSelection(0);


                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);
                                        String sub_category_id = row.getString("sub_category_id");
                                        String name = row.getString("name");
                                        dataset3.add(name);
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            MySpinnerAdapterOther<String> adapter = new MySpinnerAdapterOther<>(
                                    AddStock.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    dataset3);

                            spinnerSubSubCat.setAdapter(adapter);


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
                params.put("selected_sub_category", selectedSubCategory);
                params.put("selected_category", selectedCategory);
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(AddStock.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
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
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);
                                        String name = row.getString("name");
                                        dataset.add(name);
                                    }

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
        RequestQueue requestQueue = Volley.newRequestQueue(AddStock.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
