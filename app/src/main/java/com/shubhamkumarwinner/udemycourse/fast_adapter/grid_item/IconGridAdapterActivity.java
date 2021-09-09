package com.shubhamkumarwinner.udemycourse.fast_adapter.grid_item;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;
import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mikepenz.iconics.typeface.ITypeface;
import com.mikepenz.materialize.MaterializeBuilder;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.fast_adapter.simple_item.SimpleItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IconGridAdapterActivity extends AppCompatActivity {
    //save our FastAdapter
    private FastItemAdapter fastItemAdapter;
    /*private FastAdapter fastItemAdapter;
    private ItemAdapter itemAdapter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        findViewById(android.R.id.content).setSystemUiVisibility(findViewById(android.R.id.content).getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

//        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
//        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_item_adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Icon grid adapter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //style our ui
//        new MaterializeBuilder().withActivity(this).build();

        //create our FastAdapter which will manage everything
        fastItemAdapter = new FastItemAdapter();
        /*itemAdapter = new ItemAdapter<>();
        //create the managing FastAdapter, by passing in the itemAdapter
        fastItemAdapter = FastAdapter.with(itemAdapter);*/

        ExpandableExtension expandableExtension = new ExpandableExtension();
        fastItemAdapter.addExtension(expandableExtension);

        RecyclerView recyclerView = findViewById(R.id.simple_recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (fastItemAdapter.getItemViewType(position)) {
                    case R.id.expand_parent_view:
                        return 3;
                    case R.id.icon_parent:
                        return 1;
                    default:
                        return -1;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(fastItemAdapter);

        List<ITypeface> mFonts = new ArrayList<>(Iconics.getRegisteredFonts(this));
        Collections.sort(mFonts, new Comparator<ITypeface>() {
            @Override
            public int compare(final ITypeface object1, final ITypeface object2) {
                return object1.getFontName().compareTo(object2.getFontName());
            }
        });

        //add all icons of all registered Fonts to the list
        int count = 0;
        ArrayList<SimpleSubExpandableItem> items = new ArrayList<>(Iconics.getRegisteredFonts(this).size());

        for (ITypeface font : mFonts) {
//        ITypeface font = mFonts.get(0);
            SimpleSubExpandableItem expandableItem = new SimpleSubExpandableItem();
            expandableItem
                    .withName(font.getFontName())
                    .withIdentifier(count);
            Log.d("TAG", "onCreate: "+font.getFontName());

            ArrayList<IItem> icons = new ArrayList<>();
            for (String icon : font.getIcons()) {
                IconItem iconItem = new IconItem();
                iconItem.withIcon(font.getIcon(icon));
                icons.add(iconItem);
            }
            expandableItem.withSubItems(icons);

            items.add(expandableItem);
            count++;
        }

        fastItemAdapter.add(items);
//        itemAdapter.add(items);
        if (savedInstanceState != null) {
            fastItemAdapter.withSavedInstanceState(savedInstanceState);
        } else {
            expandableExtension.expand(2);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
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