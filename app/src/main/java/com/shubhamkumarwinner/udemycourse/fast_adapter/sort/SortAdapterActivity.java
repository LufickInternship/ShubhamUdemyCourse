package com.shubhamkumarwinner.udemycourse.fast_adapter.sort;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.utils.ComparableItemListImpl;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.fast_adapter.simple_item.SimpleItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortAdapterActivity extends AppCompatActivity {
    private static final int SORT_ASCENDING = 0;
    private static final int SORT_DESCENDING = 1;
    private static final int SORT_NONE = -1;

    private static final String[] ALPHABET = new String[]{"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
            "Z"};


    //save our FastAdapter
    private FastAdapter<SimpleItem> fastAdapter;
    private ItemAdapter<SimpleItem> itemAdapter;
    private ComparableItemListImpl<SimpleItem> itemListImpl;

    private int sortingStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_item_adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sort");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //create our FastAdapter which will manage everything
        itemListImpl = new ComparableItemListImpl<>(getComparator());
        itemAdapter = new ItemAdapter<>(itemListImpl);
        fastAdapter = FastAdapter.with(itemAdapter);
        fastAdapter.withSelectable(true);

        //configure our fastAdapter
        fastAdapter.withOnClickListener(new OnClickListener<SimpleItem>() {
            @Override
            public boolean onClick(View v, IAdapter<SimpleItem> adapter,
                                   @NonNull SimpleItem item, int position) {
                Toast.makeText(v.getContext(), (item).name.getText(v.getContext()),
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });

        //get our recyclerView and do basic setup
        RecyclerView recyclerView = findViewById(R.id.simple_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fastAdapter);

        if (savedInstanceState != null) {
            //Retrieve the previous sorting strategy from the instance state
            sortingStrategy = toSortingStrategy(savedInstanceState.getInt("sorting_strategy"));
        } else {
            //Set the default so
            sortingStrategy = SORT_NONE;
        }

        //initial filling of the list
        itemAdapter.setNewList(generateUnsortedList());

        //restore selections (this has to be done after the items were added
        fastAdapter.withSavedInstanceState(savedInstanceState);
    }
    int toSortingStrategy(int val) {
        return val;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the adapter to the bundle
        outState = fastAdapter.saveInstanceState(outState);
        //We need to persist our sorting strategy between orientation changes
        outState.putInt("sorting_strategy", sortingStrategy);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);
        menu.findItem(R.id.item_sort_random).setIcon(new IconicsDrawable(this,
                MaterialDesignIconic.Icon.gmi_wrap_text).color(Color.BLACK).actionBar());
        menu.findItem(R.id.item_sort_asc).setIcon(new IconicsDrawable(this,
                MaterialDesignIconic.Icon.gmi_sort_asc).color(Color.BLACK).actionBar());
        menu.findItem(R.id.item_sort_desc).setIcon(new IconicsDrawable(this,
                MaterialDesignIconic.Icon.gmi_sort_desc).color(Color.BLACK).actionBar());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle the menu item click
        switch (item.getItemId()) {
            case R.id.item_sort_random:
                //Set the new sorting strategy
                sortingStrategy = SORT_NONE;
                //randomize the items
                Collections.shuffle(itemAdapter.getAdapterItems());
                fastAdapter.notifyDataSetChanged();
                return true;
            case R.id.item_sort_asc:
                //Set the new sorting strategy
                sortingStrategy = SORT_ASCENDING;
                //Set the new comparator to the list
                itemListImpl.withComparator(getComparator());
                return true;
            case R.id.item_sort_desc:
                //Set the new sorting strategy
                sortingStrategy = SORT_DESCENDING;
                //Set the new comparator to the list
                itemListImpl.withComparator(getComparator());
                return true;
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "selections = " +
                        fastAdapter.getSelections(), Toast.LENGTH_LONG).show();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<SimpleItem> generateUnsortedList() {
        ArrayList<SimpleItem> result = new ArrayList<>(26);

        for (int i = 0; i < 26; i++) {
            result.add(makeItem(i));
        }

        Collections.shuffle(result);

        return result;
    }

    private SimpleItem makeItem(@IntRange(from = 0, to = 25) int position) {
        SimpleItem result = new SimpleItem();

        result.withName(ALPHABET[position]);

        position++;

        String description = "The " + (position);

        if (position == 1 || position == 21) {
            description += "st";
        } else if (position == 2 || position == 22) {
            description += "nd";
        } else if (position == 3 || position == 23) {
            description += "rd";
        } else {
            description += "th";
        }

        return result.withDescription(description + " letter in the alphabet");
    }

    @Nullable
    private Comparator<SimpleItem> getComparator() {
        switch (sortingStrategy) {
            case SORT_ASCENDING:
                return new AlphabetComparatorAscending();
            case SORT_DESCENDING:
                return new AlphabetComparatorDescending();
            case SORT_NONE:
                return null;
        }

        throw new RuntimeException("This sortingStrategy is not supported.");
    }

    /**
     * A simple Comparator to sort the items ascending.
     */
    private class AlphabetComparatorAscending implements Comparator<SimpleItem>, Serializable {
        @Override
        public int compare(SimpleItem lhs, SimpleItem rhs) {
            return lhs.name.getText().toString().compareTo(rhs.name.getText().toString());
        }
    }

    /**
     * A simple Comparator to sort the items descending.
     */
    private class AlphabetComparatorDescending implements Comparator<SimpleItem>, Serializable {
        @Override
        public int compare(SimpleItem lhs, SimpleItem rhs) {
            return rhs.name.getText().toString().compareTo(lhs.name.getText().toString());
        }
    }
}