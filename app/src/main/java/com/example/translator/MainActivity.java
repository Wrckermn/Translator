package com.example.translator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private Spinner sp1, sp2;
    private EditText ed1;
    private Button b1;
    private ListView lv1;
    private Intent i;

    private ArrayAdapter<CharSequence> adapter1, adapter2;
    private ArrayList<String> ListData;

    private String inputSpinnerlang1, inputSpinnerlang2, inputText, lg;

    private DBControl dbControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp1 = (Spinner) findViewById(R.id.spinner);
        sp2 = (Spinner) findViewById(R.id.spinner2);
        ed1 = (EditText) findViewById(R.id.edit);
        b1 = (Button) findViewById(R.id.button);
        lv1 = (ListView) findViewById(R.id.listview);

        adapter1 = ArrayAdapter.createFromResource(this, R.array.language, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter1);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.language, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);

        dbControl = new DBControl(getBaseContext());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getHttpResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                riwayat();
            }
        });
    }

    public void getHttpResponse() throws IOException {
        inputSpinnerlang1 = sp1.getSelectedItem().toString();
        inputSpinnerlang2 = sp2.getSelectedItem().toString();
        inputText = ed1.getText().toString();

        if (sp1.getSelectedItemPosition() == 0 || sp2.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Mohon pilih bahasa yang akan digunakan",Toast.LENGTH_LONG).show();
        }

        if (ed1.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Mohon masukkan kata",Toast.LENGTH_LONG).show();
        }

        String urli = "https://translated-mymemory---translation-memory.p.rapidapi.com/api/get?langpair="+inputSpinnerlang1+"%7C"+inputSpinnerlang2+"&q="+inputText;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urli.toString())
                .get()
                .addHeader("x-rapidapi-key", "35a3a401b4msh27b007f53540d43p1b965cjsnc4cadb690d4b")
                .addHeader("x-rapidapi-host", "translated-mymemory---translation-memory.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.e(TAG, mMessage);

                try {
                    JSONObject getdata = new JSONObject(mMessage);
                    lg = getdata.getJSONObject("responseData").getString("translatedText");

                    SQLiteDatabase create = dbControl.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DBControl.MyColumns.spn1, inputSpinnerlang1);
                    values.put(DBControl.MyColumns.spn2, inputSpinnerlang2);
                    values.put(DBControl.MyColumns.kata1, inputText);
                    values.put(DBControl.MyColumns.kata2, lg);
                    create.insert(DBControl.MyColumns.NamaTabel, null, values);

                    i = new Intent(MainActivity.this, ActivityTerjemahan.class);
                    i.putExtra("lang1",inputSpinnerlang1);
                    i.putExtra("lang2",inputSpinnerlang2);
                    i.putExtra("textin",inputText);
                    i.putExtra("textout",lg);

                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void riwayat() {
        ListData = new ArrayList<>();

        lv1.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListData));

        SQLiteDatabase ReadData = dbControl.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM "+ DBControl.MyColumns.NamaTabel,null);

        cursor.moveToFirst();

        for(int count=0; count < cursor.getCount(); count++){
            cursor.moveToPosition(count);

            ListData.add(cursor.getString(3));
            ListData.add(cursor.getString(4));
        }
    }

}