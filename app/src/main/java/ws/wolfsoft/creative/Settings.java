package ws.wolfsoft.creative;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Settings extends AppCompatActivity {

    LinearLayout employee,business,stock,print,categoriesCard,expense,profile,subcategoriesCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        employee = findViewById(R.id.employeeCard);
        business = findViewById(R.id.businessCard);
        expense = findViewById(R.id.expensesCard);
        print=findViewById(R.id.print);



        //profile = findViewById(R.id.profileCard);
        //subcategoriesCard = findViewById(R.id.subcategoriesCard);

        /*subcategoriesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Settings.this, ViewSubCategories.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });*/

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Settings.this, PrintingOptions.class);
                startActivity(it);
            }
        });

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Settings.this, BusinessSettings.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Settings.this, ViewEmployees.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });

        stock = findViewById(R.id.stockCard);
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Settings.this, StockSettings.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });
        /*categoriesCard = findViewById(R.id.categoriesCard);
        categoriesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Settings.this, ViewCategories.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });*/

        /*profile = findViewById(R.id.profileCard);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Settings.this, Signup.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });*/

        expense = findViewById(R.id.expensesCard);
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Settings.this, OthersSettings.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });

        /*findViewById(R.id.subsubcategoriesCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Settings.this, ViewSubSubCategories.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });*/

        android.support.v7.widget.Toolbar mToolbar =  findViewById(R.id.my_toolbar);
        if(mToolbar!=null) {
            mToolbar.setTitle("Settings");
            setSupportActionBar(mToolbar);
        }
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }


}
