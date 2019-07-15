package ws.wolfsoft.creative;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
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

import adapter.MySpinnerAdapterOther;
import adapter.SubCategoryAdapter;
import beans.Category;
import beans.Stock;
import iobserver.CategoryIObserver;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.edit_sub_category_view;
import static ws.wolfsoft.creative.Constants.edit_sub_sub_category_view;
import static ws.wolfsoft.creative.Constants.get_selected_category;
import static ws.wolfsoft.creative.Constants.get_selected_category_sub;
import static ws.wolfsoft.creative.Constants.get_sub_categories;
import static ws.wolfsoft.creative.Constants.get_sub_categories_sub;
import static ws.wolfsoft.creative.Constants.view_sub_categories;
import static ws.wolfsoft.creative.Constants.view_sub_sub_categories;

public class ViewSubSubCategories extends AppCompatActivity implements SubCategoryAdapter.AdapterListener, CategoryIObserver {

    private SharedPreferences credentialsSharedPreferences;
    public List<Category> category;
    SubCategoryAdapter subcategoryAdapter;
    private RecyclerView rv;
    TextView category_number_head;
    private NiftyDialogBuilder dialogBuilder;
    List<String> dataset;
    List<String> dataset1;
    String subcategoryId;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sub_sub_categories);

        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        category = new ArrayList<>();

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        category_number_head = findViewById(R.id.head2);
        subcategoryId = getIntent().getExtras().getString("sub_category_id");
        System.out.println("subsubcategoryIdsubsubcategoryIdsubsubcategoryIdsubsubcategoryId"+subcategoryId);




        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewSubSubCategories.this.finish();
                Intent it = new Intent(ViewSubSubCategories.this, AddCategories.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });


        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ViewSubSubCategories.this, AddSubSubCategory.class);
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

    String oldCategory="";
    private void getAllCategorySpinner(final Spinner spinnerTypeCat, final List<Category> categorySpinner, final int pos,final Spinner spinnerTypeSubCat) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_selected_category_sub,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssssgetAllCategory " + s);
                        String selCategory="";
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
                                        selCategory = row.getString("type");
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

                        MySpinnerAdapterOther<String> adapter3 = new MySpinnerAdapterOther<>(
                                ViewSubSubCategories.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                dataset);
                        spinnerTypeCat.setAdapter(adapter3);

                        int spinnerPosition1 = adapter3.getPosition(selCategory);
                        System.out.println("HAHAHAHAH "+oldCategory);

                        //set the default according to value
                        spinnerTypeCat.setSelection(spinnerPosition1);

                        spinnerTypeCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                String selectedType = spinnerTypeCat.getSelectedItem().toString();

                                if(!selectedType.equals("Select Category"))
                                {
                                    System.out.println("++++++++++++++++++++++ "+selectedType);
                                    getSubCategory(selectedType,spinnerTypeSubCat,category,pos);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // your code here
                            }

                        });


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        System.out.println("volleyError response " + volleyError.getMessage());
                        Toast.makeText(getApplicationContext(), "Poor network connection. In Sub Category", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));
                params.put("selected_sub_category", categorySpinner.get(pos).name);
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewSubSubCategories.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getSubCategory(final String selectedCategory, final Spinner spinnerTypeSubCat, final List<Category> category, final int pos) {
        System.out.println("HEREHERE");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_sub_categories_sub,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssssSubCategory " + selectedCategory);
                        String selectedSub = null;
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("sub_category");
                                dataset1 = new ArrayList<>();
                                dataset1.clear();
                                dataset1.add("Select Sub-Category");

                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String sub_category_id = row.getString("sub_category_id");
                                        String name = row.getString("name");
                                         selectedSub = row.getString("selectedSub");


                                        dataset1.add(name);
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

                        MySpinnerAdapterOther<String> adapter3 = new MySpinnerAdapterOther<>(
                                ViewSubSubCategories.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                dataset1);
                        spinnerTypeSubCat.setAdapter(adapter3);
                        //String subcategory = stock.get(pos).subcategory;
                        // System.out.println("HAHAHAHAH "+subcategory);

                        int spinnerPosition1 = adapter3.getPosition(selectedSub);

                        //set the default according to value
                        spinnerTypeSubCat.setSelection(spinnerPosition1);


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
                params.put("selected_category", category.get(pos).name);
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewSubSubCategories.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getAllCategory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, view_sub_sub_categories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("sub_sub_category");
                                category.clear();
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String category_id = row.getString("sub_sub_category_id");
                                        String name = row.getString("name");
                                        String type = row.getString("type");
                                        category_number_head.setText(row.getString("count")+" sub-categories");
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
                params.put("sub_category_id", subcategoryId);

                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ViewSubSubCategories.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }





    private void initializeData() {
        subcategoryAdapter = new SubCategoryAdapter(this, category, (SubCategoryAdapter.AdapterListener) this);
        rv.setAdapter(subcategoryAdapter);
        subcategoryAdapter.setListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ((SubCategoryAdapter)subcategoryAdapter).getFilter().filter(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ((SubCategoryAdapter)subcategoryAdapter).getFilter().filter(s);

                return false;
            }
        });

        return true;
    }

    @Override
    public void onCategorySelected(Category category) {

    }

    @Override
    public void onCardClicked(int pos, String name, List <Category> postFiltered) {
        Intent it = new Intent(ViewSubSubCategories.this, ViewStock.class);
        it.putExtra("category_id", category.get(pos).category_id);
        it.putExtra("category_name", category.get(pos).name);
        it.putExtra("type", "is_drawer");
        startActivity(it);
    }

    @Override
    public void onCardClicked(final int pos, String name) {

    }

    public void refreshActivity() {
        this.finish();
        Intent it = new Intent(ViewSubSubCategories.this, ViewSubSubCategories.class);
        it.putExtra("type", "");
        startActivity(it);
    }
}
