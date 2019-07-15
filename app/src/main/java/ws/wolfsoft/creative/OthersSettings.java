package ws.wolfsoft.creative;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class OthersSettings extends AppCompatActivity {

    LinearLayout employee,business,stock,subsubCategory,expense,profile,subcategoriesCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_settings);

        employee = findViewById(R.id.employeeCard);
        business = findViewById(R.id.businessCard);
        expense = findViewById(R.id.expensesCard);
        //subsubCategory= findViewById(R.id.subsubCategory);
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

        /*subsubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(OthersSettings.this, ViewSubSubCategories.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });*/

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(OthersSettings.this, ViewCreditors.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(OthersSettings.this, ViewDebtors.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });

        /*stock = findViewById(R.id.stockCard);
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(OthersSettings.this, ViewSubCategories.class);
                it.putExtra("type", "is_settings");
                startActivity(it);
            }
        });*/
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
                Intent it = new Intent(OthersSettings.this, ViewExpenses.class);
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
            mToolbar.setTitle("Stock Settings");
            setSupportActionBar(mToolbar);
        }
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }


}
