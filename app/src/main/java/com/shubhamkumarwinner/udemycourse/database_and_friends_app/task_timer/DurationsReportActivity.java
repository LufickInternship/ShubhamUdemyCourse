package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.databinding.ActivityDurationsReportBinding;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class DurationsReportActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        DatePickerDialog.OnDateSetListener {
    private static final String TAG = "DurationsReport";
    private ActivityDurationsReportBinding binding;

    private static final int LOADER_ID = 1;

    public static final int DIALOG_FILTER = 1;
    public static final int DIALOG_DELETE = 2;

    private static final String  SELECTION_PARAM = "SELECTION";
    private static final String  SELECTION_ARGS_PARAM = "SELECTION_ARGS";
    private static final String  SORT_ORDER_PARAM = "SORT_ORDER";

    private static final String  CURRENT_DATE = "CURRENT_DATE";
    private static final String  DISPLAY_WEEK = "DISPLAY_WEEK";

    //module level arguments - so when we change sort order, for example, the selection
    // is retained (and vice-versa).
    private Bundle args = new Bundle();
    private boolean displayWeek = true;

    private DurationsRVAdapter adapter;
    private final GregorianCalendar calendar = new GregorianCalendar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDurationsReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        applyFilter();

        RecyclerView recyclerView = findViewById(R.id.td_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Create an empty adapter we will use, to display the loaded data.
        if (adapter == null){
            adapter = new DurationsRVAdapter(this, null);
        }
        recyclerView.setAdapter(adapter);
        LoaderManager.getInstance(this).initLoader(LOADER_ID, args, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.rm_filter_period:
                displayWeek = !displayWeek;
                applyFilter();
                invalidateOptionsMenu();
                LoaderManager.getInstance(this).restartLoader(LOADER_ID, args, this);
                return true;
            case R.id.rm_filter_date:
                showDatePickerDialog("Select date for report", DIALOG_FILTER);
                return true;
            case R.id.rm_delete:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.rm_filter_period);
        if (item != null){
            if (displayWeek){
                item.setIcon(R.drawable.ic_filter_1);
                item.setTitle(R.string.rm_title_filter_day);
            }else {
                item.setIcon(R.drawable.ic_filter_7);
                item.setTitle(R.string.rm_title_filter_week);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void showDatePickerDialog(String title, int dialogId){
        Log.d(TAG, "showDatePickerDialog: entering");
        DialogFragment dialogFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(DatePickerFragment.DATE_PICKER_ID, dialogId);
        args.putString(DatePickerFragment.DATE_PICKER_TITLE, title);
        args.putSerializable(DatePickerFragment.DATE_PICKER_DATE, calendar.getTime());
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "datePicker");
        Log.d(TAG, "showDatePickerDialog: exiting");
    }

    private void applyFilter(){
        Log.d(TAG, "applyFilter: entering");
        if (displayWeek){
            Date currentCalenderDate = calendar.getTime();
            int dayOfWeek = calendar.get(GregorianCalendar.DAY_OF_WEEK);
            int weekStart = calendar.getFirstDayOfWeek();
            Log.d(TAG, "applyFilter: first day of calender week is "+ weekStart);
            Log.d(TAG, "applyFilter: day of week is "+ dayOfWeek);
            Log.d(TAG, "applyFilter: date is "+ calendar.getTime());

            calendar.set(GregorianCalendar.DAY_OF_WEEK, weekStart);
            String startDate = String.format(Locale.US, "%04d-%02d-%02d",
                    calendar.get(GregorianCalendar.YEAR),
                    calendar.get(GregorianCalendar.MONTH) + 1,
                    calendar.get(GregorianCalendar.DAY_OF_MONTH));
            calendar.add(GregorianCalendar.DATE, 6);
            String endDate = String.format(Locale.US, "%04d-%02d-%02d",
                    calendar.get(GregorianCalendar.YEAR),
                    calendar.get(GregorianCalendar.MONTH) + 1,
                    calendar.get(GregorianCalendar.DAY_OF_MONTH));
            String[] selectionArgs = new String[]{startDate, endDate};
            calendar.setTime(currentCalenderDate);
            Log.d(TAG, "applyFilter(7), Start date is "+ startDate+", End date is "+endDate);
            args.putString(SELECTION_PARAM, "StartDate Between ? AND ?");
            args.putStringArray(SELECTION_ARGS_PARAM, selectionArgs);
        }else {
            String startDate = String.format(Locale.US, "%04d-%02d-%02d",
                    calendar.get(GregorianCalendar.YEAR),
                    calendar.get(GregorianCalendar.MONTH) + 1,
                    calendar.get(GregorianCalendar.DAY_OF_MONTH));
            String[] selectionArgs = new String[]{startDate};
            Log.d(TAG, "applyFilter: Start date "+ startDate);
            args.putString(SELECTION_PARAM, "StartDate = ?");
            args.putStringArray(SELECTION_ARGS_PARAM, selectionArgs);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case LOADER_ID:
                String[] projection = {BaseColumns._ID,
                        DurationsContract.Columns.DURATIONS_NAME,
                        DurationsContract.Columns.DURATIONS_DESCRIPTION,
                        DurationsContract.Columns.DURATIONS_START_TIME,
                        DurationsContract.Columns.DURATIONS_START_DATE,
                        DurationsContract.Columns.DURATIONS_DURATION };
                String selection = null;
                String[] selectionArgs = null;
                String sortOrder = null;

                if(args != null) {
                    selection = args.getString(SELECTION_PARAM);
                    selectionArgs = args.getStringArray(SELECTION_ARGS_PARAM);
                    sortOrder = args.getString(SORT_ORDER_PARAM);
                }

                return new CursorLoader(this,
                        DurationsContract.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);

            default:
                throw new InvalidParameterException(TAG+".onCreateLoader called with invalid loader id "+id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: starts");
        adapter.swapCursor(data);
        int count = adapter.getItemCount();
        Log.d(TAG, "onLoadFinished: count is " + count);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: starts");
        adapter.swapCursor(null);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Log.d(TAG, "onDateSet: entering");
        int dialogId = (int)datePicker.getTag();
        switch (dialogId){
            case DIALOG_FILTER:
                calendar.set(year, month, dayOfMonth, 0, 0, 0);
                applyFilter();
                LoaderManager.getInstance(this).restartLoader(LOADER_ID, args, this);
                break;
            case DIALOG_DELETE:
                break;
            default:
                throw new IllegalArgumentException("Invalid mode when receiving DatePickerDialog result");
        }
    }
}