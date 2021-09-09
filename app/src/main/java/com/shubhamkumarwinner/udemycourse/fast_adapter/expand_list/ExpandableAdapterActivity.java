package com.shubhamkumarwinner.udemycourse.fast_adapter.expand_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;
import com.mikepenz.itemanimators.SlideDownAlphaAnimator;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.fast_adapter.grid_item.SimpleSubExpandableItem;
import com.shubhamkumarwinner.udemycourse.fast_adapter.fast_scroll.SimpleSubItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExpandableAdapterActivity extends AppCompatActivity {
    //save our FastAdapter
    private FastItemAdapter<IItem> fastItemAdapter;
    private ExpandableExtension<IItem> expandableExtension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_item_adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Expandable list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //create our FastAdapter
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.withSelectable(true);
        expandableExtension = new ExpandableExtension<>();
        //expandableExtension.withOnlyOneExpandedItem(true);
        fastItemAdapter.addExtension(expandableExtension);

        //get our recyclerView and do basic setup
        RecyclerView recyclerView = findViewById(R.id.simple_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new SlideDownAlphaAnimator());
        recyclerView.setAdapter(fastItemAdapter);

        //fill with some sample data
        List<IItem> items = new ArrayList<>();
        AtomicInteger identifier = new AtomicInteger(1);
        for (int i = 1; i <= 100; i++) {
            if (i % 3 != 0) {
                items.add(new SimpleSubItem().withName("Test " + i).withIdentifier(identifier.getAndIncrement()));
                continue;
            }

            SimpleSubExpandableItem parent = new SimpleSubExpandableItem();
            parent.withName("Test " + i).withIdentifier(identifier.getAndIncrement());

            List<IItem> subItems = new LinkedList<>();
            for (int ii = 1; ii <= 5; ii++) {
                SimpleSubExpandableItem subItem = new SimpleSubExpandableItem();
                subItem.withName("-- SubTest " + ii).withIdentifier(identifier.getAndIncrement());

                if (ii % 2 == 0) {
                    continue;
                }

                List<IItem> subSubItems = new LinkedList<>();
                for (int iii = 1; iii <= 3; iii++) {
                    SimpleSubExpandableItem subSubItem = new SimpleSubExpandableItem();
                    subSubItem.withName("---- SubSubTest " + iii).withIdentifier(identifier.getAndIncrement());

                    List<IItem> subSubSubItems = new LinkedList<>();
                    for (int iiii = 1; iiii <= 4; iiii++) {
                        SimpleSubExpandableItem subSubSubItem = new SimpleSubExpandableItem();
                        subSubSubItem.withName("---- SubSubSubTest " + iiii).withIdentifier(identifier.getAndIncrement());
                        subSubSubItems.add(subSubSubItem);
                    }
                    subSubItem.withSubItems(subSubSubItems);
                    subSubItems.add(subSubItem);
                }
                subItem.withSubItems(subSubItems);
                subItems.add(subItem);
            }
            parent.withSubItems(subItems);
            items.add(parent);
        }
        fastItemAdapter.add(items);

        //restore selections (this has to be done after the items were added
        fastItemAdapter.withSavedInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the adapter to the bundle
        outState = fastItemAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
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
}