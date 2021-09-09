package com.shubhamkumarwinner.udemycourse.fast_adapter.multi_select;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.ISelectionListener;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.listeners.OnLongClickListener;
import com.mikepenz.fastadapter_extensions.ActionModeHelper;
import com.mikepenz.fastadapter_extensions.UndoHelper;
import com.mikepenz.itemanimators.SlideDownAlphaAnimator;
import com.mikepenz.materialize.util.UIUtils;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.fast_adapter.simple_item.SimpleItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MultiSelectAdapterActivity extends AppCompatActivity {
    //save our FastAdapter
    private FastAdapter<SimpleItem> fastAdapter;

    private UndoHelper undoHelper;

    private ActionModeHelper<SimpleItem> actionModeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_item_adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Multiple select");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //create our adapters
        final ItemAdapter<SimpleItem> headerAdapter = new ItemAdapter<>();
        ItemAdapter<SimpleItem> itemAdapter = new ItemAdapter<>();

        //create our FastAdapter
        fastAdapter = FastAdapter.with(Arrays.asList(headerAdapter, itemAdapter));

        //configure our mFastAdapter
        //as we provide id's for the items we want the hasStableIds enabled to speed up things
        fastAdapter.setHasStableIds(true);
        fastAdapter.withSelectable(true);
        fastAdapter.withMultiSelect(true);
        fastAdapter.withSelectOnLongClick(true);
        fastAdapter.withSelectionListener(new ISelectionListener<SimpleItem>() {
            @Override
            public void onSelectionChanged(SimpleItem item, boolean selected) {
                Log.i("FastAdapter", "SelectedCount: " + fastAdapter.getSelections().size() + " ItemsCount: " + fastAdapter.getSelectedItems().size());
            }
        });
        fastAdapter.withOnPreClickListener(new OnClickListener<SimpleItem>() {
            @Override
            public boolean onClick(View v, @NonNull IAdapter<SimpleItem> adapter, @NonNull SimpleItem item, int position) {
                //we handle the default onClick behavior for the actionMode. This will return null if it didn't do anything and you can handle a normal onClick
                Boolean res = actionModeHelper.onClick(item);
                return res != null ? res : false;
            }
        });
        fastAdapter.withOnClickListener(new OnClickListener<SimpleItem>() {
            @Override
            public boolean onClick(View v, @NonNull IAdapter<SimpleItem> adapter, @NonNull SimpleItem item, int position) {
                Toast.makeText(v.getContext(), "SelectedCount: " + fastAdapter.getSelections().size() + " ItemsCount: " + fastAdapter.getSelectedItems().size(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        fastAdapter.withOnPreLongClickListener(new OnLongClickListener<SimpleItem>() {
            @Override
            public boolean onLongClick(@NonNull View v, @NonNull IAdapter<SimpleItem> adapter,
                                       @NonNull SimpleItem item, int position) {
                ActionMode actionMode = actionModeHelper.onLongClick(MultiSelectAdapterActivity.this, position);

                if (actionMode != null) {
                    //we want color our CAB
                    findViewById(R.id.toolbar).setBackgroundColor(UIUtils
                            .getThemeColorFromAttrOrRes(MultiSelectAdapterActivity.this,
                                    R.attr.colorPrimary, R.color.material_drawer_primary));
                }

                //if we have no actionMode we do not consume the event
                return actionMode != null;
            }
        });

        //
        undoHelper = new UndoHelper<>(fastAdapter, new UndoHelper.UndoListener<SimpleItem>() {
            @Override
            public void commitRemove(Set<Integer> positions, ArrayList<FastAdapter.RelativeInfo<SimpleItem>> removed) {
                Log.e("UndoHelper", "Positions: " + positions.toString() + " Removed: " + removed.size());
            }
        });

        //we init our ActionModeHelper
        actionModeHelper = new ActionModeHelper<>(fastAdapter, R.menu.cab, new ActionBarCallBack());

        //get our recyclerView and do basic setup
        RecyclerView recyclerView =  findViewById(R.id.simple_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new SlideDownAlphaAnimator());
        recyclerView.setAdapter(fastAdapter);

        //fill with some sample data
        SimpleItem SimpleItem = new SimpleItem();
        SimpleItem
                .withName("Header")
                .withIdentifier(1)
                .withSelectable(false);
        headerAdapter.add(SimpleItem);
        List<SimpleItem> items = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            SimpleItem item = new SimpleItem();
            item.withName("Test " + i).withIdentifier(100 + i);
            items.add(item);
        }
        itemAdapter.add(items);

        //restore selections (this has to be done after the items were added
        fastAdapter.withSavedInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //add the values which need to be saved from the adapter to the bundle
        outState = fastAdapter.saveInstanceState(outState);
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

    /**
     * Our ActionBarCallBack to showcase the CAB
     */
    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            undoHelper.remove(findViewById(android.R.id.content), "Item removed", "Undo", 2000, fastAdapter.getSelections());
            //as we no longer have a selection so the actionMode can be finished
            mode.finish();
            //we consume the event
            return true;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }
}