package com.shubhamkumarwinner.udemycourse.database_and_friends_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.database_and_friends_app.content_provider.ContentProviderActivity;
import com.shubhamkumarwinner.udemycourse.database_and_friends_app.sqlite_database.SqliteDatabaseActivity;
import com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer.TaskTimerActivity;
import com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer.TaskTimerDatabase;

public class DatabaseActivity extends AppCompatActivity {
    Button contentProvider, sqliteDatabase, taskTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        contentProvider = findViewById(R.id.content_provider);
        sqliteDatabase = findViewById(R.id.sqlite_database);
        taskTimer = findViewById(R.id.task_timer);
        sqliteDatabase.setOnClickListener(view -> {
            Intent intent = new Intent(this, SqliteDatabaseActivity.class);
            startActivity(intent);
        });
        contentProvider.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContentProviderActivity.class);
            startActivity(intent);
        });
        taskTimer.setOnClickListener(view -> {
            Intent intent = new Intent(this, TaskTimerActivity.class);
            startActivity(intent);
        });
    }
}