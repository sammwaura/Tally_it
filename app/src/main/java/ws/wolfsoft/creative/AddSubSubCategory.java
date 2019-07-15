package ws.wolfsoft.creative;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

import adapter.MySpinnerAdapterOther;

import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.get_categories;
import static ws.wolfsoft.creative.Constants.get_sub_categories;
import static ws.wolfsoft.creative.Constants.save_sub_subcategories;
import static ws.wolfsoft.creative.Constants.save_subcategories;

public class AddSubSubCategory extends AppCompatActivity {

    private Spinner spinnerCat,spinnerSubCategory;
    List<String> dataset,dataset1;
    TextView sub_subcategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_sub_category);

        spinnerSubCategory =findViewById(R.id.spinnerTypeSubCat);
        dataset1 = new ArrayList<>();
        dataset1.add("Select Sub-Category");


        sub_subcategories = findViewById(R.id.subcategory_name);
        spinnerCat =  findViewById(R.id.spinnerTypeCat);
        dataset = new ArrayList<>();
        dataset.add("Select Category");
        getAllCategory();

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddSubSubCategory.this, ViewSubSubCategories.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(AddSubSubCategory.this, AddStock.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

        if(getIntent().getExtras().getString("type").equals("not_setUp"))
        {
            findViewById(R.id.head2).setVisibility(View.GONE);
            findViewById(R.id.previous).setVisibility(View.GONE);
            findViewById(R.id.next).setVisibility(View.GONE);

            findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(AddSubSubCategory.this, ViewSubSubCategories.class);
                    it.putExtra("type", "is_settings");
                    startActivity(it);
                }
            });

        }

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategory();
            }
        });

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddSubSubCategory.this, AddSubCategory.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });

    }

    private void saveCategory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, save_sub_subcategories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        Toast.makeText(getApplicationContext(), "Saved Sub subCategories", Toast.LENGTH_LONG).show();

                        spinnerCat.setSelection(0);
                        spinnerSubCategory.setSelection(0);
                        sub_subcategories.setText("");
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
                params.put("category", spinnerCat.getSelectedItem().toString());
                params.put("subcategory", spinnerSubCategory.getSelectedItem().toString());
                params.put("sub_subcategories", sub_subcategories.getText().toString());
                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));

                System.out.println();

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(AddSubSubCategory.this);

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
                                spinnerSubCategory.setSelection(0);


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
                                    AddSubSubCategory.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    dataset1);

                            spinnerSubCategory.setAdapter(adapter);
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
        RequestQueue requestQueue = Volley.newRequestQueue(AddSubSubCategory.this);

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

                            MySpinnerAdapterOther<String> adapter = new MySpinnerAdapterOther<>(
                                    AddSubSubCategory.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    dataset);

                            spinnerCat.setAdapter(adapter);

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
        RequestQueue requestQueue = Volley.newRequestQueue(AddSubSubCategory.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
