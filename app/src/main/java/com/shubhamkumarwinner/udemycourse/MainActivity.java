package com.shubhamkumarwinner.udemycourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.shubhamkumarwinner.udemycourse.database_and_friends_app.DatabaseActivity;

public class MainActivity extends AppCompatActivity {
    Button databaseAndFriends, contentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseAndFriends = findViewById(R.id.database);
        contentProvider = findViewById(R.id.content_provider);
        databaseAndFriends.setOnClickListener(view -> {
            Intent intent = new Intent(this, DatabaseActivity.class);
            startActivity(intent);
        });
    }
}