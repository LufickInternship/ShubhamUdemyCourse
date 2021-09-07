package com.shubhamkumarwinner.udemycourse.fast_adapter.simple_item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.fast_adapter.simple_item.SimpleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleItemAdapterActivity extends AppCompatActivity {
    private static final String[] ALPHABET = new String[]{"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private ItemAdapter<SimpleItem> itemAdapter;
    FastAdapter<SimpleItem> fastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_item_adapter);
        RecyclerView recyclerView = findViewById(R.id.simple_recycler_view);
        //create the ItemAdapter holding your Items
        // both below methods works same
//        itemAdapter = ItemAdapter.items();
        itemAdapter = new ItemAdapter<>();
        //create the managing FastAdapter, by passing in the itemAdapter
        fastAdapter = FastAdapter.with(itemAdapter);

        //set the items to your ItemAdapter
        int x = 0;
        List<SimpleItem> items = new ArrayList<>();
        for (String s : ALPHABET) {
            int count = new Random().nextInt(20);
            for (int i = 1; i <= count; i++) {
                SimpleItem item = new SimpleItem().withName(s + " Test " + x).withIdentifier(100 + x);
                items.add(item);
                x++;
            }
        }
        itemAdapter.add(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set our adapters to the RecyclerView
        recyclerView.setAdapter(fastAdapter);

    }
}