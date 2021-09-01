package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.databinding.ActivityTaskTimerBinding;

public class TaskTimerActivity extends AppCompatActivity {
    private static final String TAG = "TaskTimerActivity";

    private ActivityTaskTimerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTaskTimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        String[] projection = {TasksContract.Columns._ID,
                               TasksContract.Columns.TASKS_NAME,
                               TasksContract.Columns.TASKS_DESCRIPTION,
                               TasksContract.Columns.TASKS_SORT_ORDER};
        ContentResolver contentResolver = getContentResolver();

        ContentValues values = new ContentValues();

        //for deleting item using selection args
        String selection = TasksContract.Columns.TASKS_DESCRIPTION + " = ?";
        String[] args = {"For deletion"};
        int count = contentResolver.delete(TasksContract.CONTENT_URI, selection, args);
        Log.d(TAG, "onCreate: " + count + " record deleted");

        //for deleting item
//        int count = contentResolver.delete(TasksContract.buildTaskUri(1), null, null);
//        Log.d(TAG, "onCreate: " + count + " record deleted");

        //for updating multiple row using selectionArgs for prevention from sql injection
//        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "For deletion");
//        String selection = TasksContract.Columns.TASKS_SORT_ORDER +" = ?";
//        String[] args = {"99"};
//        int count = contentResolver.update(TasksContract.CONTENT_URI, values, selection, args);
//        Log.d(TAG, "onCreate: " + count + " record updated");

        //for updating multiple row
//        values.put(TasksContract.Columns.TASKS_SORT_ORDER, "99");
//        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Completed");
//        String selection = TasksContract.Columns.TASKS_SORT_ORDER +" = "+2;
//        int count = contentResolver.update(TasksContract.CONTENT_URI, values, selection, null);
//        Log.d(TAG, "onCreate: " + count + " record updated");

        // for updating value of given id
//        values.put(TasksContract.Columns.TASKS_NAME, "Content provider");
//        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Record content provider video");
//        int count = contentResolver.update(TasksContract.buildTaskUri(1), values, null, null);
//        Log.d(TAG, "onCreate: "+count+" record updated");

        //for inserting item
//        values.put(TasksContract.Columns.TASKS_NAME, "New Task 1");
//        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Description 1");
//        values.put(TasksContract.Columns.TASKS_SORT_ORDER, 2);
//        Uri uri = contentResolver.insert(TasksContract.CONTENT_URI, values);

        Cursor cursor = contentResolver.query(
                TasksContract.CONTENT_URI,
                projection,
                null,
                null,
                TasksContract.Columns.TASKS_NAME
        );
        if (cursor != null){
            Log.d(TAG, "onCreate: number of rows: "+cursor.getCount());
            while (cursor.moveToNext()){
                for (int i=0; i<cursor.getColumnCount(); i++){
                    Log.d(TAG, "onCreate: "+cursor.getColumnName(i) + " : " + cursor.getString(i));
                }
                Log.d(TAG, "onCreate: ================================================");
            }
            cursor.close();
        }
//        TaskTimerDatabase taskTimerDatabase = TaskTimerDatabase.getInstance(this);
//        final SQLiteDatabase db = taskTimerDatabase.getReadableDatabase();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_timer_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.task_timer_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}