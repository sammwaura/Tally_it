package ws.wolfsoft.creative;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import beans.Category;
import iobserver.CategoryIObserver;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.get_categories;

public class ViewStockCategories extends AppCompatActivity  implements CategoryAdapter.AdapterListener, CategoryIObserver {

    private SharedPreferences credentialsSharedPreferences;
    public List<Category> category;
    CategoryAdapter categoryAdapter;
    private RecyclerView rv;
    TextView category_number_head;
    private NiftyDialogBuilder dialogBuilder;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stock_categories);

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
                ViewStockCategories.this.finish();
                Intent it = new Intent(ViewStockCategories.this, AddCategories.class);
                it.putExtra("type", "");
                startActivity(it);
            }
        });


        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ViewStockCategories.this, AddCategories.class);
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

    protected void onResume(){
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
        RequestQueue requestQueue = Volley.newRequestQueue(ViewStockCategories.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }





    private void initializeData() {
        categoryAdapter = new CategoryAdapter(this, (CategoryAdapter.AdapterListener) this, category);
        rv.setAdapter(categoryAdapter);
        categoryAdapter.setListener(this);
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
                ((CategoryAdapter)categoryAdapter).getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ((CategoryAdapter)categoryAdapter).getFilter().filter(s);
                return false;
            }
        });


        return true;
    }

    @Override
    public void onCategorySelected(Category category) {
        Toast.makeText(getApplicationContext(), "Selected: " + category.name, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCardClicked(final int pos, String name) {

        Intent it = new Intent(ViewStockCategories.this, ViewSubCategories.class);
        it.putExtra("category_id", category.get(pos).category_id);
        it.putExtra("category_name", category.get(pos).name);
        it.putExtra("type", "is_drawer");
        startActivity(it);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
