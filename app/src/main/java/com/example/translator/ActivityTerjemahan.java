package com.example.translator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityTerjemahan extends AppCompatActivity {
    TextView t1, t2, t3, t4;
    Intent i;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terjemahan);

        t1 = (TextView) findViewById(R.id.textView5);
        t2 = (TextView) findViewById(R.id.textView6);
        t3 = (TextView) findViewById(R.id.textView2);
        t4 = (TextView) findViewById(R.id.textView3);
        bt = (Button) findViewById(R.id.button2);

        i = getIntent();

        DBControl dbControl = new DBControl(getBaseContext());

        t1.setText(i.getStringExtra("lang1"));
        t2.setText(i.getStringExtra("lang2"));
        t3.setText(i.getStringExtra("textin"));
        t4.setText(i.getStringExtra("textout"));

        SQLiteDatabase ReadData = dbControl.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM "+ DBControl.MyColumns.NamaTabel,null);
        cursor.moveToFirst();

        for(int count=0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);
            cursor.getString(1);
            cursor.getString(2);
            cursor.getString(3);
            cursor.getString(4);
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityTerjemahan.this, MainActivity.class));
            }
        });
    }
}