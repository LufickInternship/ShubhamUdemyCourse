package com.shubhamkumarwinner.udemycourse.database_and_friends_app.sqlite_database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;


import com.shubhamkumarwinner.udemycourse.databinding.ActivitySqliteDatabaseBinding;

public class SqliteDatabaseActivity extends AppCompatActivity {
    private static final String TAG = "SqliteDatabaseActivity";

    private AppBarConfiguration appBarConfiguration;
    private ActivitySqliteDatabaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySqliteDatabaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        SQLiteDatabase sqLiteDatabase = getBaseContext().openOrCreateDatabase("sqlite-test-1.db", MODE_PRIVATE, null);
        String sql = "DROP TABLE IF EXISTS contacts;";
        Log.d(TAG, "onCreate: sql is "+sql);
        sqLiteDatabase.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS contacts(name TEXT, phobe INTEGER, email TEXT);";
        Log.d(TAG, "onCreate: sql is "+sql);
        sqLiteDatabase.execSQL(sql);
        sql = "INSERT INTO contacts VALUES('shubham', 7900148057, 'shubham@gmail.com');";
        Log.d(TAG, "onCreate: sql is "+sql);
        sqLiteDatabase.execSQL(sql);
        sql = "INSERT INTO contacts VALUES('rahul', 1234148057, 'rahul@gmail.com');";
        Log.d(TAG, "onCreate: sql is "+sql);
        sqLiteDatabase.execSQL(sql);

        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM contacts;", null);
        if (query.moveToFirst()){
            do {
                String name = query.getString(0);
                int phone = query.getInt(1);
                String email = query.getString(2);
                Toast.makeText(this, "Name: "+name+" phone: "+phone+" email: "+email, Toast.LENGTH_SHORT).show();
            }while (query.moveToNext());
        }
        query.close();
        sqLiteDatabase.close();

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

}