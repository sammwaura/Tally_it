package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class No_Internet extends AppCompatActivity {

    private Internet internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internet = new Internet(getApplicationContext(), view);
                internet.execute("");
            }
        });
    }

    private  class Internet extends AsyncTask<String, Integer, Boolean> {
        String data = null;
        private Context mContext;
        private View rootView;
        boolean status=false;

        public Internet (Context context, View rootView) {
            this.mContext = context;
            this.rootView = rootView;
        }

        @Override
        protected Boolean doInBackground(String... views) {
            status = CheckNetwork.isInternetAvailable(No_Internet.this);
            return status;
        }

        @Override
        protected void onPreExecute() {
            findViewById(R.id.button3).setVisibility(View.GONE);
            findViewById(R.id.progressBar4).setVisibility(View.VISIBLE);
            Toast.makeText(mContext, "Please Wait", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            findViewById(R.id.button3).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar4).setVisibility(View.GONE);
            if(result)
            {
                Intent intent = new Intent(No_Internet.this, Home.class);
                No_Internet.this.startActivity(intent);
            }
            else
            {
                Toast.makeText(mContext, "Now try again checking", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
