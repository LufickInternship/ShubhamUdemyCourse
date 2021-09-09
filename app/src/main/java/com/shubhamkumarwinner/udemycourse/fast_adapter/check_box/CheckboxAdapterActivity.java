package com.shubhamkumarwinner.udemycourse.fast_adapter.check_box;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.shubhamkumarwinner.udemycourse.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheckboxAdapterActivity extends AppCompatActivity {
    private static final String[] ALPHABET = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    //save our FastAdapter
    private FastItemAdapter<CheckboxItem> fastItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_item_adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checkbox Adapter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //create our FastAdapter which will manage everything
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.withSelectable(true);

        //configure our fastAdapter
        //TODO ask what does method do
//        fastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
//            Toast.makeText(v.getContext(), (item).name.getText(v.getContext()), Toast.LENGTH_LONG).show();
//            return true;
//        });

        fastItemAdapter.withOnPreClickListener((v, adapter, item, position) -> {
            // consume otherwise radio/checkbox will be deselected
            Toast.makeText(v.getContext(), (item).name.getText(v.getContext()), Toast.LENGTH_SHORT).show();
            return true;
        });
        fastItemAdapter.withEventHook(new CheckboxItem.CheckBoxClickEvent());

        RecyclerView recyclerView = findViewById(R.id.simple_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fastItemAdapter);

        //fill with some sample data
        int x = 0;
        List<CheckboxItem> items = new ArrayList<>();
        for (String s : ALPHABET) {
            int count = new Random().nextInt(20);
            for (int i = 1; i <= count; i++) {
                items.add(new CheckboxItem().withName(s + " Test " + x).withDescription("Checkout "+s+" with "+x).withIdentifier(100 + x));
                x++;
            }
        }
        fastItemAdapter.add(items);

        //restore selections (this has to be done after the items were added
        if (savedInstanceState != null) {
            fastItemAdapter.withSavedInstanceState(savedInstanceState);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        fastItemAdapter.saveInstanceState(outState);
    }
}