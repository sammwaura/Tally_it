package ws.wolfsoft.creative;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class PrintingOptions extends AppCompatActivity {

    LinearLayout sale,debtors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing_options);

        sale=findViewById(R.id.salesCard);
        debtors=findViewById(R.id.debtorCard);
        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(PrintingOptions.this, PrintAll.class);
                startActivity(it);
            }
        });

        debtors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(PrintingOptions.this, PrintDebtors.class);
                startActivity(it);
            }
        });
    }
}
