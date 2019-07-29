package ws.wolfsoft.creative;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import adapter.CreditorAdapter;
import adapter.MySpinnerAdapter;
import beans.BeanClass;
import beans.Category;
import beans.Creditor;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static ws.wolfsoft.creative.Constants.CREDENTIALSPREFERENCES;
import static ws.wolfsoft.creative.Constants.business_name;
import static ws.wolfsoft.creative.Constants.business_no;
import static ws.wolfsoft.creative.Constants.credentialsSharedPreferences;
import static ws.wolfsoft.creative.Constants.employee_number;
import static ws.wolfsoft.creative.Constants.get_categories;
import static ws.wolfsoft.creative.Constants.get_creditors;
import static ws.wolfsoft.creative.Constants.get_due_creditors;
import static ws.wolfsoft.creative.Constants.get_home_data;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,SlyCalendarDialog.Callback {

    private ListView listview;

    int number = 1;


    private int image;
    private String title;
    private String description;
    private String price;


    public int[] IMAGE = {R.drawable.supermarket, R.drawable.shop, R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.shop};
    public String[] TITLE = {"Super Product", "Classic Finishing", "Standerd Quality",
            "Eassy to Search", "List by List", "Made By India"};
    public String[] DESCRIPTION = {"", "", "", "", "", ""};


    private ArrayList<BeanClass> beanClassArrayList;
    private adapter.listViewAdapter listViewAdapter;
    private Spinner spinner;

    String allCreditors="";

    Typeface font_bold,font_Semibold;
    TextView sign,titlez,home,messages,services,notes,circle_count,mpesa,bankings,stock,shop,expenses,employeesSales,pettycash,favourites,stats,creditorsMenu,debtors,loss,replacement,logout,debtor_value,stock_value
            ,total_profits,total_sales,total_expenses,employee_count,profits2,income,sales2,best,worst,worst1,nameStock1,worst2,nameStock2,worst3,nameStock3;
    private SharedPreferences.Editor credentialsEditor;
    DatePickerDialog picker;
    String selDate,fromDate,toDate;
    CardView lowstock;

    public List<Creditor> creditors;
    CreditorAdapter adapter;
    private RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        creditors = new ArrayList<>();

        font_bold =  Typeface.createFromAsset(Home.this.getAssets(),"fonts/OpenSans-Bold.ttf");
        font_Semibold =  Typeface.createFromAsset(Home.this.getAssets(),"fonts/OpenSans-Semibold.ttf");

        lowstock = findViewById(R.id.low_stock);

        lowstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, LowstockActivity.class);
                startActivity(it);
            }
        });

        findViewById(R.id.settIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, Settings.class);
                startActivity(it);
            }
        });

        rv =  findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();

         employee_count = findViewById(R.id.employee_number);
        total_expenses = findViewById(R.id.home_expenses);
        total_sales = findViewById(R.id.home_sales);
        total_profits = findViewById(R.id.home_profits);
        profits2 = findViewById(R.id.home_profits2);
        income = findViewById(R.id.home_income);
        sales2= findViewById(R.id.home_sales2);
        best= findViewById(R.id.best);
        worst= findViewById(R.id.worst);

        loss=findViewById(R.id.loss);
        replacement=findViewById(R.id.replacement);

        bankings = findViewById(R.id.banking);

        worst1= findViewById(R.id.worst1);
        nameStock1= findViewById(R.id.nameStock1);

        worst2= findViewById(R.id.worst2);
        nameStock2= findViewById(R.id.nameStock2);

        worst3= findViewById(R.id.worst3);
        nameStock3= findViewById(R.id.nameStock3);

        TextView title = findViewById(R.id.titleShop);
        TextView sign = findViewById(R.id.sign);
        title.setText(credentialsSharedPreferences.getString(business_name, "Shop"));

        sign.setText(credentialsSharedPreferences.getString(business_name, "Shop"));

        titlez = findViewById(R.id.title);
        titlez.setTypeface(font_bold);

        services = findViewById(R.id.services);
        services.setTypeface(font_Semibold);

        notes = findViewById(R.id.notes);
        notes.setTypeface(font_Semibold);



        logout = findViewById(R.id.logout);
        logout.setTypeface(font_Semibold);

        mpesa = findViewById(R.id.mpesa);
        mpesa.setTypeface(font_Semibold);

        bankings.setTypeface(font_Semibold);

        sign = findViewById(R.id.sign);
        sign.setTypeface(font_Semibold);

        home = findViewById(R.id.home);
        home.setTypeface(font_Semibold);

        loss.setTypeface(font_Semibold);
        replacement.setTypeface(font_Semibold);

        messages = findViewById(R.id.sales);
        messages.setTypeface(font_Semibold);

        creditorsMenu = findViewById(R.id.creditors);
        creditorsMenu.setTypeface(font_Semibold);

        debtors = findViewById(R.id.debtors);
        debtors.setTypeface(font_Semibold);

        pettycash = findViewById(R.id.pettyCash);
        pettycash.setTypeface(font_Semibold);

        stock = findViewById(R.id.stock);
        stock.setTypeface(font_Semibold);

        expenses = findViewById(R.id.expenses);
        expenses.setTypeface(font_Semibold);

        employeesSales = findViewById(R.id.employees);
        employeesSales.setTypeface(font_Semibold);

        favourites = findViewById(R.id.profits);
        favourites.setTypeface(font_Semibold);

        stats = findViewById(R.id.stats);
        stats.setTypeface(font_Semibold);


        spinner = findViewById(R.id.spinnerType);
        List<String> dataset = new ArrayList<>();
        dataset.add("Daily Statistics");
        dataset.add("Monthly");
        dataset.add("Yearly");
        dataset.add("Specify Date");
        dataset.add("Specify Date Range");
        dataset.add("Total");

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,dataset);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(arrayAdapter);

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
                    picker = new DatePickerDialog(Home.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    selDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    Toast.makeText(Home.this,selDate,Toast.LENGTH_LONG).show();
                                    getHomeData();
                                }
                            }, year, month, day);
                    picker.show();
                }
                else if(selectedCategory.equals("Specify Date Range"))
                {
                    new SlyCalendarDialog()
                            .setSingle(false)
                            .setCallback(Home.this)
                            .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
                }
                else
                {
                    getHomeData();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.imageProfits).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, Profits.class);
                startActivity(it);
            }
        });

        loss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, Loss.class);
                startActivity(it);
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, MainNotesActivity.class);
                startActivity(it);
            }
        });

        replacement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, ViewReplacement.class);
                startActivity(it);
            }
        });

        findViewById(R.id.imageSales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, Sales.class);
                startActivity(it);
            }
        });

        mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, Mpesa.class);
                startActivity(it);
            }
        });

        findViewById(R.id.imageEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, EmployeeSales.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });

        findViewById(R.id.imageExpenses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, ExpenseRecords.class);
                startActivity(it);
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, Statistics.class);
                startActivity(it);
            }
        });

        bankings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home.this, ViewBanking.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Home",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Home.this, Home.class);
                startActivity(it);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        findViewById(R.id.sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Sales",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Home.this, Sales.class);
                startActivity(it);
            }
        });

        findViewById(R.id.employees).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"EmployeesSales",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Home.this, EmployeeSales.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });

        findViewById(R.id.stock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Stock",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Home.this, ViewStockCategories.class);
                it.putExtra("type", "is_drawer");
                startActivity(it);
            }
        });

        findViewById(R.id.expenses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Expenses",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Home.this, ExpenseRecords.class);
                startActivity(it);
            }
        });

        findViewById(R.id.pettyCash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Petty Cash",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Home.this, PettyCash.class);
                startActivity(it);
            }
        });

        findViewById(R.id.creditors).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Creditors",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Home.this, ViewCreditors.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });

        findViewById(R.id.profits).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Profits",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Home.this, Profits.class);
                startActivity(it);
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                credentialsEditor.putString(Constants.business_no, "0");
                credentialsEditor.apply();
                Toast.makeText(Home.this,"Logging Out",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Home.this, MainActivity.class);
                startActivity(it);
                Home.this.finish();

            }
        });

        findViewById(R.id.debtors).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Home.this, ViewDebtors.class);
                it.putExtra("type", "is_settings");
                startActivity(it);

            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DrawerLayout mDrawerLayout;
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();
            }
        });
        getHomeData();
    }

    private void initializeData() {

        adapter = new CreditorAdapter(creditors);
        rv.setAdapter(adapter);
        //adapter.setListener(this);
    }

    protected void onResume() {
        super.onResume();
        getHomeData();
    }

    private void getHomeData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_home_data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("c " + s);
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("home");
                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String total_profitss = row.getString("total_profits");
                                        String total_saless = row.getString("total_sales");
                                        String total_expensess = row.getString("total_expenses");
                                        String total_employees = row.getString("total_employees");
                                        String best_stock = row.getString("best_stock");
                                        String worst_stock = row.getString("worst_stock");
                                        String creditors = row.getString("creditors");

                                        if(creditors.length() >0)
                                        {
                                            System.out.println(")))))))))))) "+creditors);
                                            allCreditors = creditors;
                                        }

                                        worst1.setText(row.getString("low_stock1_amount"));
                                        nameStock1.setText(row.getString("low_stock1"));

                                        worst2.setText(row.getString("low_stock2_amount"));
                                        nameStock2.setText(row.getString("low_stock2"));

                                        worst3.setText(row.getString("low_stock3_amount"));
                                        nameStock3.setText(row.getString("low_stock3"));

                                        if(best_stock == "null")
                                        {
                                            best_stock="Not available";
                                        }

                                        if(worst_stock == "null")
                                        {
                                            worst_stock="Not available";
                                        }

                                        if(total_expensess.equals("null"))
                                        {
                                            total_expensess="0";
                                        }

                                        if(total_saless.equals("null"))
                                        {
                                            total_saless="0";
                                        }

                                        employee_count.setText(total_employees);
                                        total_expenses.setText(total_expensess);

                                        if(total_expensess == "null")
                                        {
                                            total_expenses.setText("0");
                                        }
                                        else
                                        {
                                            total_expenses.setText(total_expensess);
                                        }

                                        if(total_saless == "null")
                                        {
                                            total_sales.setText("0");
                                        }
                                        else
                                        {
                                            total_sales.setText(total_saless);
                                        }

                                        total_profits.setText(total_profitss);

                                        best.setText(best_stock);
                                        worst.setText(worst_stock);

                                        findViewById(R.id.best_card).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent it = new Intent(Home.this, SalesRatings.class);
                                                it.putExtra("title", "BEST");
                                                startActivity(it);
                                            }
                                        });

                                        findViewById(R.id.worst_card).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent it = new Intent(Home.this, SalesRatings.class);
                                                it.putExtra("title", "WORST");
                                                startActivity(it);
                                            }
                                        });

                                        profits2.setText("Ksh "+total_profitss);
                                        income.setText("Ksh "+total_saless);
                                        sales2.setText("Ksh "+total_expensess);

                                    }

                                    if(allCreditors.length() >0)
                                    {
                                        setUpCreditors(allCreditors);
                                    }
                                    else
                                    {
                                        findViewById(R.id.no_internet).setVisibility(View.GONE);
                                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                                        findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                        findViewById(R.id.rv).setVisibility(View.GONE);
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
                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Home.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void setUpCreditors(final String allCreditors) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_due_creditors,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("ssssssssssssssssssssssssssssssssssssssetUpCreditors " + s);
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

                params.put("all_creditors", allCreditors);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Home.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            this.finish();
            System.exit(0);
            super.onBackPressed();

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Toast.makeText(this,"Home",Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.sales) {
            Toast.makeText(this,"Sales",Toast.LENGTH_LONG).show();
        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
        if (firstDate != null) {
            if (secondDate == null) {
                ////
                //Toast.makeText(getApplicationContext(), "First Date.", Toast.LENGTH_LONG).show();
                firstDate.set(Calendar.HOUR_OF_DAY, hours);
                firstDate.set(Calendar.MINUTE, minutes);

                Toast.makeText(this, new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(firstDate.getTime()), Toast.LENGTH_LONG).show();

            } else {
                ///
                //Toast.makeText(getApplicationContext(), "Second plus First Date.", Toast.LENGTH_LONG).show();
                Toast.makeText(
                        this,
                        getString(
                                R.string.period,
                                new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(firstDate.getTime()),
                                new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(secondDate.getTime())
                        ),
                        Toast.LENGTH_LONG

                ).show();

                fromDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(firstDate.getTime());
                toDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(secondDate.getTime());
                getHomeData();

            }
        }

    }
}



