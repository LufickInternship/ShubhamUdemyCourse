package com.shubhamkumarwinner.udemycourse.fast_adapter.diffutil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.commons.utils.FastAdapterDiffUtil;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.itemanimators.AlphaInAnimator;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.fast_adapter.simple_item.SimpleItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DiffUtilAdapterActivity extends AppCompatActivity {
    //save our FastAdapter
    private FastItemAdapter<SimpleItem> fastItemAdapter;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_item_adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Diff util item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //create our FastAdapter which will manage everything
        fastItemAdapter = new FastItemAdapter<>();

        //get our recyclerView and do basic setup
        RecyclerView rv = findViewById(R.id.simple_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new AlphaInAnimator());
        rv.setAdapter(fastItemAdapter);

        //fill with some sample data
        setData();
        //restore selections (this has to be done after the items were added
        fastItemAdapter.withSavedInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the adapter to the bundel
        outState = fastItemAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        menu.findItem(R.id.item_refresh).setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_refresh).color(Color.BLACK).actionBar());
        menu.findItem(R.id.item_refresh_async).setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_refresh_sync).color(Color.BLACK).actionBar());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_refresh:
                setData();
                Toast.makeText(this, "Refresh synchronous", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_refresh_async:
                setDataAsync();
                Toast.makeText(this, "Refresh asynchronous", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setData() {
        List<SimpleItem> items = createData();
        FastAdapterDiffUtil.set(fastItemAdapter, items);
    }

    private void setDataAsync() {
        disposables.add(Single.fromCallable(new Callable<List<SimpleItem>>() {
            @Override
            public List<SimpleItem> call() throws Exception {
                return createData();
            }
        }).map(new Function<List<SimpleItem>, DiffUtil.DiffResult>() {
            @Override
            public DiffUtil.DiffResult apply(List<SimpleItem> simpleItems) throws Exception {
                return FastAdapterDiffUtil.calculateDiff(fastItemAdapter, simpleItems);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiffUtil.DiffResult>() {
                    @Override
                    public void accept(DiffUtil.DiffResult result) throws Exception {
                        FastAdapterDiffUtil.set(fastItemAdapter, result);
                    }
                }));
    }

    private List<SimpleItem> createData() {
        List<SimpleItem> items = Arrays.asList(
                new SimpleItem().withName("Item 1").withIdentifier(1),
                new SimpleItem().withName("Item 2").withIdentifier(2),
                new SimpleItem().withName("Item 3").withIdentifier(3),
                new SimpleItem().withName("Item 4").withIdentifier(4),
                new SimpleItem().withName("Item 5").withIdentifier(5),
                new SimpleItem().withName("Item 6").withIdentifier(6),
                new SimpleItem().withName("Item 7").withIdentifier(7),
                new SimpleItem().withName("Item 8").withIdentifier(8),
                new SimpleItem().withName("Item 9").withIdentifier(9),
                new SimpleItem().withName("Item 10").withIdentifier(10)
        );
        Collections.shuffle(items);
        return items;
    }

}