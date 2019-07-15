package ws.wolfsoft.creative;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Statistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        LinearLayout stock_value,debtor_value;

        debtor_value =  findViewById(R.id.debtor_value);
        stock_value = findViewById(R.id.stock_value);

        stock_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Statistics.this, StockValue.class);
                startActivity(it);
            }
        });

        debtor_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Statistics.this, DebtorValue.class);
                startActivity(it);
            }
        });

        android.support.v7.widget.Toolbar mToolbar =  findViewById(R.id.my_toolbar);
        if(mToolbar!=null) {
            mToolbar.setTitle("Statistics");
            setSupportActionBar(mToolbar);
        }
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        findViewById(R.id.businessCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Statistics.this, GoodsPerformance.class);
                startActivity(it);
            }
        });

        findViewById(R.id.employeeCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Statistics.this, BusinessHealth.class);
                startActivity(it);
            }
        });

        findViewById(R.id.bankHealth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Statistics.this, BankingHealth.class);
                startActivity(it);
            }
        });
    }
}
