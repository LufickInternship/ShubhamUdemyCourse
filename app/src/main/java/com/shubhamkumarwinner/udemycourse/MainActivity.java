package com.shubhamkumarwinner.udemycourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.shubhamkumarwinner.udemycourse.database_and_friends_app.DatabaseActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.FastAdapterActivity;

public class MainActivity extends AppCompatActivity {
    Button databaseAndFriends, fastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseAndFriends = findViewById(R.id.database);
        fastAdapter = findViewById(R.id.fast_adapter);
        databaseAndFriends.setOnClickListener(view -> {
            Intent intent = new Intent(this, DatabaseActivity.class);
            startActivity(intent);
        });
        fastAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, FastAdapterActivity.class);
            startActivity(intent);
        });
    }
}