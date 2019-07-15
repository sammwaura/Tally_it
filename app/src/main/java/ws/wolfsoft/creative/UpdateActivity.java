package ws.wolfsoft.creative;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thebluealliance.spectrum.SpectrumPalette;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import beans.Note;

import static ws.wolfsoft.creative.Constants.delete_note;
import static ws.wolfsoft.creative.Constants.update_notes;

public class UpdateActivity extends AppCompatActivity {

    EditText et_title, et_note;
    ProgressDialog progressDialog;
    SpectrumPalette palette;

    private Context context;
//    private int color;
    private ArrayList<Note> noter;
    private SpectrumPalette mSpectrumPalette;
    private int selectedColor;
    private int note_id;
    private String title;
    private String note;
    private int color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Bundle extras = getIntent().getExtras();

        et_title = findViewById(R.id.title);
        et_note = findViewById(R.id.note);


        //getting values inputed
        note_id = extras.getInt("note_id");
        title = extras.getString("title");
        et_title.setText(title);
        System.out.println("titletitletitle"+title);
        note = extras.getString("note");
        et_note.setText(note);
        color = extras.getInt("color", 0);


        mSpectrumPalette = findViewById(R.id.palette);
        mSpectrumPalette.setHorizontalScrollBarEnabled(true);
        mSpectrumPalette.setFixedColumnCount(17);
        mSpectrumPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(@ColorInt int color) {
                selectedColor = color;
            }
        });

        noter = new ArrayList<>();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                //Update
                final String title = et_title.getText().toString().trim();
                final String note = et_note.getText().toString().trim();
                final int color = selectedColor;

                if (title.isEmpty()) {
                    et_title.setError("Please enter a title");
                } else if (note.isEmpty()) {
                    et_note.setError("Please enter a note");
                } else {

                    final StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            update_notes,
                            new Response.Listener <String>() {
                        @Override
                        public void onResponse(String response) {

                            Intent intent = new Intent(getApplicationContext(), MainNotesActivity.class);
                            startActivity(intent);
                            Log.d("Response", response);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("note_id", String.valueOf(note_id));
                            params.put("title", title);
                            params.put("note", note);
                            params.put("color", String.valueOf(color));
                            return params;
                        }


                    };
                    final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
                return true;

            case R.id.delete:
                //Delete

                //System.out.println("Deleting "+note_id);

                    final StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            delete_note,
                            new Response.Listener <String>() {
                                @Override
                                public void onResponse(String response) {

                                    System.out.println("REponse"+response);

                                    Intent intent = new Intent(getApplicationContext(), MainNotesActivity.class);
                                    startActivity(intent);
                                    Log.d("Response", response);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            System.out.println("note_idnote_id"+note_id);

                            params.put("note_id", String.valueOf(note_id));
                            return params;
                        }


                    };
                    final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



