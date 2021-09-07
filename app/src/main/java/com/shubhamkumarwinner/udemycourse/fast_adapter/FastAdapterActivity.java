package com.shubhamkumarwinner.udemycourse.fast_adapter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.fast_adapter.grid_item.IconGridAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.simple_item.SimpleItemAdapterActivity;

public class FastAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_adapter);
        Button simpleAdapter = findViewById(R.id.simple_adapter);
        Button iconGridAdapter = findViewById(R.id.icon_grid_adapter);
        simpleAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, SimpleItemAdapterActivity.class);
            startActivity(intent);
        });
        iconGridAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, IconGridAdapterActivity.class);
            startActivity(intent);
        });
    }
}