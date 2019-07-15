package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import adapter.MySpinnerAdapterOther;
import adapter.StockAdapter;
import beans.Category;
import beans.Stock;
import iobserver.StockIObserver;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.delete_stock;
import static ws.wolfsoft.creative.Constants.edit_category;
import static ws.wolfsoft.creative.Constants.edit_stock;
import static ws.wolfsoft.creative.Constants.get_categories;
import static ws.wolfsoft.creative.Constants.get_low_stock;
import static ws.wolfsoft.creative.Constants.get_sub_categories;

public  class LowstockActivity extends AppCompatActivity  implements StockIObserver, StockAdapter.AdapterListener {

    private SharedPreferences credentialsSharedPreferences;
    public List<Stock> stock;
    StockAdapter stockAdapter;
    private RecyclerView rv;
    TextView stock_number_head;
    List<String> dataset;
    private NiftyDialogBuilder dialogBuilder;
    private List<String> dataset1;
    String categoryName,categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lowstock);

        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        stock = new ArrayList<>();

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        stock_number_head = findViewById(R.id.head2);
        getAllLowStock();
    }

    protected void onResume() {
        super.onResume();
        getAllLowStock();
    }


    private void getAllLowStock() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_low_stock,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssssStock " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("stock");
                                stock.clear();
                                if(posts.length() >0)
                                {
                                    String total_stock = null;
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String id = row.getString("id");
                                        String name = row.getString("name");
                                        String quantity = row.getString("quantity");
                                        String metric = row.getString("metric");
                                        String category = row.getString("category");
                                        String buying_price = row.getString("buying_price");
                                        total_stock = row.getString("total_stock");
                                        stock.add(new Stock(id, name,quantity,metric,category, buying_price));
                                    }

                                    //TextView head  = findViewById(R.id.head2);
                                    //head.setText(String.format("%s Stock", total_stock));

                                    //TextView head1  = findViewById(R.id.head);
                                    //head1.setText("Stock("+categoryName+")");

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
        RequestQueue requestQueue = Volley.newRequestQueue(LowstockActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }





    private void initializeData() {

        stockAdapter = new StockAdapter(this, stock, this);
        rv.setAdapter(stockAdapter);
        stockAdapter.setListener(this);
    }



    @Override
    public void onCardClicked(final int pos, String name) {

        dialogBuilder = NiftyDialogBuilder.getInstance(LowstockActivity.this);
        dialogBuilder
                .withTitle("Edit Stock")
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
                .setCustomView(R.layout.dialog_edit_stock, LowstockActivity.this)//def gone//def gone
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

                                         final EditText stock_name =  dialogBuilder.findViewById(R.id.stock_name);
                                         final EditText quantity =  dialogBuilder.findViewById(R.id.quantity);
                                         final Spinner spinnerTypeCat =  dialogBuilder.findViewById(R.id.spinnerTypeCat);
                                         final EditText buyingPrice =  dialogBuilder.findViewById(R.id.buying_price);
                                         final Spinner spinnerTypeUnit =  dialogBuilder.findViewById(R.id.spinnerTypeUnit);
                                         editStock(stock_name.getText().toString(),quantity.getText().toString(),spinnerTypeCat.getSelectedItem().toString(),buyingPrice.getText().toString(),spinnerTypeUnit.getSelectedItem().toString(),stock.get(pos).id);
                                     }
                                 }
                );

        final EditText stock_name =  dialogBuilder.findViewById(R.id.stock_name);
        final Button delete =  dialogBuilder.findViewById(R.id.button);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Deleting",Toast.LENGTH_LONG).show();
                deleteStock(stock.get(pos).id);
            }
        });

        final EditText quantity =  dialogBuilder.findViewById(R.id.quantity);
        final Spinner spinnerTypeCat =  dialogBuilder.findViewById(R.id.spinnerTypeCat);
        final EditText buyingPrice =  dialogBuilder.findViewById(R.id.buying_price);
        dataset = new ArrayList<>();
        dataset.add("Select Category");
        getAllCategory(spinnerTypeCat,stock,pos);


        final Spinner spinnerTypeUnit =  dialogBuilder.findViewById(R.id.spinnerTypeUnit);

        List<String> dataset2 = new ArrayList<>();
        dataset2.add("Select Unit");
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
        dataset2.add("pieces");

        MySpinnerAdapterOther<String> adapter2 = new MySpinnerAdapterOther<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dataset2);
        spinnerTypeUnit.setAdapter(adapter2);
        String metric = stock.get(pos).metric;
        ArrayAdapter myAdap = (ArrayAdapter) spinnerTypeUnit.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = myAdap.getPosition(metric);

        //set the default according to value
        spinnerTypeUnit.setSelection(spinnerPosition);

        stock_name.setText(stock.get(pos).name.split("-")[0]);
        quantity.setText(stock.get(pos).quantity);
        buyingPrice.setText(stock.get(pos).buying_price);
        dialogBuilder.show();
    }

    private void deleteStock(final String id) {
        System.out.println("HEREHERE");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, delete_stock,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssssNewSub " + s);
                        Toast.makeText(getApplicationContext(), "Deleted Stock", Toast.LENGTH_LONG).show();
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
                params.put("stock_id", id);
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(LowstockActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getNewSubCategory(final String selectedCategory, final Spinner spinnerSubCat) {
        System.out.println("HEREHERE");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_sub_categories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssssNewSub " + s);
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
        RequestQueue requestQueue = Volley.newRequestQueue(LowstockActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void editStock(final String stock_name,final String quantity,final String category,final String buying_price,final String metric,final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, edit_stock,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssss2 " + s);
                        Toast.makeText(getApplicationContext(), "Edited Employee(s)", Toast.LENGTH_LONG).show();
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
                params.put("stock_name", stock_name);
                params.put("quantity", quantity);
                params.put("category", category);
                params.put("buying_price", buying_price);
                params.put("metric", metric);
                params.put("id", id);
                params.put("business_id", Objects.requireNonNull(credentialsSharedPreferences.getString(business_no, "0")));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(LowstockActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    String oldCategory="";
    private void getAllCategory(final Spinner spinnerTypeCat, final List<Stock> stock, final int pos) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_categories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssssgetAllCategory " + s);
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

                        MySpinnerAdapterOther<String> adapter3 = new MySpinnerAdapterOther<>(
                                LowstockActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                dataset);
                        spinnerTypeCat.setAdapter(adapter3);
                        final String category = stock.get(pos).category;
                        oldCategory =category;

                        int spinnerPosition1 = adapter3.getPosition(category);
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
                                    //getSubCategory(selectedCategory,spinnerTypeSubCat,stock,pos);
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
        RequestQueue requestQueue = Volley.newRequestQueue(LowstockActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getSubCategory(final String selectedCategory,final Spinner spinnerTypeSubCat, final List<Stock> stock, final int pos) {
        System.out.println("HEREHERE");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_sub_categories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("sssssssssssssssssssssssssssssssssssssSubCategory " + selectedCategory);
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
                                LowstockActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                dataset1);
                        spinnerTypeSubCat.setAdapter(adapter3);
                        //String subcategory = stock.get(pos).subcategory;
                        // System.out.println("HAHAHAHAH "+subcategory);

                        //int spinnerPosition1 = adapter3.getPosition(subcategory);

                        //set the default according to value
                        //spinnerTypeSubCat.setSelection(spinnerPosition1);

                        System.out.println("oldCategory === "+oldCategory+" new category "+selectedCategory);
                        if(!oldCategory.equals(selectedCategory))
                        {
                            spinnerTypeSubCat.setSelection(0);
                        }
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
        RequestQueue requestQueue = Volley.newRequestQueue(LowstockActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void editCategory(final String category_id, final String name, final String sub_category) {
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
                params.put("sub_category", sub_category);
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(LowstockActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void refreshActivity() {
        this.finish();
        Intent it = new Intent(LowstockActivity.this, LowstockActivity.class);
        it.putExtra("category_id", categoryId);
        it.putExtra("category_name", categoryName);
        it.putExtra("type", "is_drawer");
        startActivity(it);
    }

    @Override
    public void onCategorySelected(Category category) {

    }

    @Override
    public void onCardClicked(int posi, String name, List <Stock> stockFiltered) {

    }
}
