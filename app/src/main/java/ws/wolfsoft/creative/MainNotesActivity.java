package ws.wolfsoft.creative;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import beans.Note;

import static ws.wolfsoft.creative.Constants.get_notes;
import static ws.wolfsoft.creative.Constants.select_all_notes;

public class MainNotesActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;


    RecyclerView.Adapter adapter;
    AdapterView.OnItemClickListener itemClickListener;
    ArrayList<Note> noter;
    private int INTENT_ADD = 100;

//    private int note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_note);

        noter = new ArrayList<>();

//      swipeRefresh = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(MainNotesActivity.this, noter);
        recyclerView.setAdapter(adapter);

        retrieveData();
//        deleteNote();


        floatingActionButton = findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add:
                        Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
                        startActivityForResult(intent, INTENT_ADD);
                }
            }
        });
    }

    private void retrieveData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving data....");
        progressDialog.show();


        System.out.println("!!!!!!saving!!!!!!");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                get_notes,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();

                        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSsssssSSSSSSSSSS "+s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("notes");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                Note note = new Note(
                                        row.getInt("note_id"),
                                        row.getString("title"),
                                        row.getString("note"),
                                        row.getInt("color")
                                );
                                noter.add(note);

                            }

                            adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volleyError error" + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
                    }
                }) {
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainNotesActivity.this);
        requestQueue.add(stringRequest);
    }

    private void retrieveUpdatedData() {

        System.out.println("!!!!!!!!updating!!!!!!!");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                select_all_notes,
                new Response.Listener <String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("notes");


                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);

                                Note note = new Note(
                                        object.getInt("note_id"),
                                        object.getString("title"),
                                        object.getString("note"),
                                        object.getInt("color")
                                );
                                noter.add(note);
                            }


                            adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volleyError error" + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MainNotesActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
//        retrieveData();
//        retrieveUpdatedData();
    }
}
