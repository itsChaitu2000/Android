package com.example.rakshit.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.rakshit.pets.data.PetsDBHelper;
import com.example.rakshit.pets.data.PetsContract.tableCols;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo()
    {
        PetsDBHelper DbHelper = new PetsDBHelper(this);

        SQLiteDatabase db = DbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + tableCols.TABLE_NAME, null);
        try
        {
            TextView displayView = (TextView) findViewById(R.id.pet_tv);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
        }
        finally
        {
            cursor.close();
        }
    }

    private void insertDummyData()
    {
        PetsDBHelper dbHelper = new PetsDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(tableCols.COL_NAME, "toto");
        values.put(tableCols.COL_BREED, "terrier");
        values.put(tableCols.COLUMN_GENDER, tableCols.GENDER_MALE);
        values.put(tableCols.COLUMN_WEIGHT, 10);

        long rowId = db.insert(tableCols.TABLE_NAME, null, values);
        if(rowId!=-1)
            displayDatabaseInfo();
        else
            Log.e("MainActivity", "Cannot insert data");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.options_add_dummy: insertDummyData();return true;
            case R.id.options_del_all: return true;
        }
        return super.onOptionsItemSelected(item);
    }
}