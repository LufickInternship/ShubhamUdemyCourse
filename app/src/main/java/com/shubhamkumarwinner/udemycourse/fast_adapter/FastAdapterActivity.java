package com.shubhamkumarwinner.udemycourse.fast_adapter;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.shubhamkumarwinner.udemycourse.databinding.ActivityFastAdapterBinding;
import com.shubhamkumarwinner.udemycourse.fast_adapter.check_box.CheckboxAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.diffutil.DiffUtilAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.drag_drop.DragDropAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.endless_scroll.EndlessScrollListActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.expand_list.ExpandableAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.grid_item.IconGridAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.image_list.ImageListAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.fast_scroll.FastScrollAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.multi_select.MultiSelectAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.multi_type.MultipleTypeItemActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.radio_button.RadioButtonAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.simple_item.SimpleItemAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.sort.SortAdapterActivity;
import com.shubhamkumarwinner.udemycourse.fast_adapter.swipe_list.SwipeListAdapterActivity;

public class FastAdapterActivity extends AppCompatActivity {
    ActivityFastAdapterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFastAdapterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Fast adapter");
        binding.simpleAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, SimpleItemAdapterActivity.class);
            startActivity(intent);
        });
        binding.iconGridAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, IconGridAdapterActivity.class);
            startActivity(intent);
        });
        binding.imageListAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, ImageListAdapterActivity.class);
            startActivity(intent);
        });
        binding.checkboxAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, CheckboxAdapterActivity.class);
            startActivity(intent);
        });
        binding.endlessScrollAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, EndlessScrollListActivity.class);
            startActivity(intent);
        });
        binding.sortAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, SortAdapterActivity.class);
            startActivity(intent);
        });
        binding.radioAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, RadioButtonAdapterActivity.class);
            startActivity(intent);
        });
        binding.multiSelectAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, MultiSelectAdapterActivity.class);
            startActivity(intent);
        });
        binding.swipeableAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, SwipeListAdapterActivity.class);
            startActivity(intent);
        });
        binding.multiTypeAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, MultipleTypeItemActivity.class);
            startActivity(intent);
        });
        binding.expandableAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, ExpandableAdapterActivity.class);
            startActivity(intent);
        });
        binding.modelItemAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, FastScrollAdapterActivity.class);
            startActivity(intent);
        });

        binding.diffUtilAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, DiffUtilAdapterActivity.class);
            startActivity(intent);
        });
        binding.dragDropAdapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, DragDropAdapterActivity.class);
            startActivity(intent);
        });
    }
}