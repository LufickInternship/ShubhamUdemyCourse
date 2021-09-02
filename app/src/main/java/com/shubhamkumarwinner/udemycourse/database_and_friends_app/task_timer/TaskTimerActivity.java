package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.databinding.ActivityTaskTimerBinding;

public class TaskTimerActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.OnTaskClickListener {
    private static final String TAG = "TaskTimerActivity";

    // Whether or not the activity is in 2-pane mode
    // i.e. running in landscape or a tablet
    private boolean twoPane = false;
    public static final String ADD_EDIT_FRAGMENT = "AddEditFragment";

    private ActivityTaskTimerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTaskTimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        if (binding.taskTimer.taskDetailsContainer != null){
            // The detail container view will be present only in the large-screen layouts (res/values-land and res/values-sw600dp).
            // If this view is present, then the activity should be in two-pane mode.
            twoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_timer_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.task_timer_add_task:
                taskEditRequest(null);
                break;
            case R.id.task_timer_show_durations:
                break;
            case R.id.task_timer_settings:
                break;
            case R.id.task_timer_show_about:
                break;
            case R.id.task_timer_generate:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void taskEditRequest(Task task){
        Log.d(TAG, "taskEditRequest: starts");
        if (twoPane){
            Log.d(TAG, "taskEditRequest: in two-pane mode (tablet)");
            AddEditFragment fragment = new AddEditFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable(Task.class.getSimpleName(), task);
            fragment.setArguments(arguments);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.task_details_container, fragment);
            fragmentTransaction.commit();
        }else {
            Log.d(TAG, "taskEditRequest: in single-pane mode (phone)");
            Intent detailIntent = new Intent(this, AddEditActivity.class);
            if (task != null){ // editing a task
                detailIntent.putExtra(Task.class.getSimpleName(), task);
                startActivity(detailIntent);
            }else { // adding a new task
                startActivity(detailIntent);
            }
        }
    }

    @Override
    public void onEditClick(Task task) {
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        getContentResolver().delete(TasksContract.buildTaskUri(task.getId()), null, null);
    }
}