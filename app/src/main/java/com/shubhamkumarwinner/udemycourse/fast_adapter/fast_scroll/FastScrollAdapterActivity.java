package com.shubhamkumarwinner.udemycourse.fast_adapter.fast_scroll;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ModelAdapter;
import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.typeface.ITypeface;
import com.mikepenz.itemanimators.SlideDownAlphaAnimator;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.fast_adapter.multi_type.IconModel;
import com.shubhamkumarwinner.udemycourse.fast_adapter.multi_type.ModelIconItem;
import com.turingtechnologies.materialscrollbar.CustomIndicator;
import com.turingtechnologies.materialscrollbar.DragScrollBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FastScrollAdapterActivity extends AppCompatActivity {
    //save our FastAdapter
    private FastAdapter fastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_item_adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fast scroll item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //adapters
        FastScrollIndicatorAdapter fastScrollIndicatorAdapter = new FastScrollIndicatorAdapter();
        ModelAdapter<IconModel, ModelIconItem> itemAdapter = new ModelAdapter<>(iconModel ->
                new ModelIconItem(iconModel)
        );

        //create our FastAdapter which will manage everything
        fastAdapter = FastAdapter.with(Collections.singletonList(itemAdapter));
        fastAdapter.withSelectable(true);

        RecyclerView recyclerView = findViewById(R.id.simple_recycler_view);
        //init our gridLayoutManager and configure RV
        recyclerView.setAdapter(fastScrollIndicatorAdapter.wrap(fastAdapter));

        DragScrollBar materialScrollBar = new DragScrollBar(this, recyclerView, true);
        materialScrollBar.setHandleColour(ContextCompat.getColor(this, R.color.colorAccent));
        materialScrollBar.setHandleOffColour(ContextCompat.getColor(this, R.color.design_default_color_secondary));
        materialScrollBar.addIndicator(new CustomIndicator(this), false);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new SlideDownAlphaAnimator());

        //order fonts by their name
        List<ITypeface> mFonts = new ArrayList<>(Iconics.getRegisteredFonts(this));
        Collections.sort(mFonts, new Comparator<ITypeface>() {
            @Override
            public int compare(final ITypeface object1, final ITypeface object2) {
                return object1.getFontName().compareTo(object2.getFontName());
            }
        });

        //add all icons of all registered Fonts to the list
        ArrayList<IconModel> models = new ArrayList<>();
        for (ITypeface font : mFonts) {
            for (String icon : font.getIcons()) {
                models.add(new IconModel(font.getIcon(icon)));
            }
        }

        //fill with some sample data
        itemAdapter.add(models);

        //restore selections (this has to be done after the items were added
        fastAdapter.withSavedInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
}