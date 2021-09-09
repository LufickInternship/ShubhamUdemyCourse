package com.shubhamkumarwinner.udemycourse.fast_adapter.image_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.shubhamkumarwinner.udemycourse.R;

public class ImageListAdapterActivity extends AppCompatActivity {
    //save our FastAdapter
    private FastItemAdapter<ImageItem> fastItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_item_adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Image list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //create our FastAdapter which will manage everything
        fastItemAdapter = new FastItemAdapter<>();

        //configure our fastAdapter
        fastItemAdapter.withOnClickListener(new OnClickListener<ImageItem>() {
            @Override
            public boolean onClick(View v, @NonNull IAdapter<ImageItem> adapter, @NonNull ImageItem item, int position) {
                Toast.makeText(v.getContext(), item.mName, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        RecyclerView recyclerView = findViewById(R.id.simple_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fastItemAdapter);

        //fill with some sample data
        fastItemAdapter.add(ImageDummyData.getImageItems());

        //restore selections (this has to be done after the items were added
        if (savedInstanceState != null) {
            fastItemAdapter.withSavedInstanceState(savedInstanceState);
        }

        //a custom OnCreateViewHolder listener class which is used to create the viewHolders
        //we define the listener for the imageLovedContainer here for better performance
        //you can also define the listener within the items bindView method but performance is better if you do it like this
        fastItemAdapter.withEventHook(new ImageItem.ImageItemHeartClickEvent());

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