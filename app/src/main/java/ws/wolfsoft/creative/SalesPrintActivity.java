package ws.wolfsoft.creative;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SalesPrintActivity extends AppCompatActivity {

    LinearLayout all,individual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_print);

        all=findViewById(R.id.allCard);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SalesPrintActivity.this, PrintAll.class);
                startActivity(it);
            }
        });

    }
}
