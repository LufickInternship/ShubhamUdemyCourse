package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.shubhamkumarwinner.udemycourse.BuildConfig;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.databinding.ActivityTaskTimerBinding;
import com.shubhamkumarwinner.udemycourse.debug.TestData;

import org.w3c.dom.Text;

public class TaskTimerActivity extends AppCompatActivity
        implements CursorRecyclerViewAdapter.OnTaskClickListener,
        AddEditFragment.OnSaveClicked,
        TaskTimerDialog.DialogEvents{
    private static final String TAG = "TaskTimerActivity";

    // Whether or not the activity is in 2-pane mode
    // i.e. running in landscape or a tablet
    private boolean twoPane = false;

    public static final int DIALOG_ID_DELETE = 1;
    public static final int DIALOG_ID_CANCEL_EDIT = 2;
    public static final int DIALOG_ID_CANCEL_EDIT_UP = 3;

    private AlertDialog dialog= null;       //module scope because we need to dismiss it in onStop


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTaskTimerBinding binding = ActivityTaskTimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

//        if (binding.taskTimer.taskDetailsContainer != null){
//            // The detail container view will be present only in the large-screen layouts (res/values-land and res/values-sw600dp).
//            // If this view is present, then the activity should be in two-pane mode.
//            twoPane = true;
//        }
        twoPane = getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE;
        Log.d(TAG, "onCreate: twoPane is "+twoPane);
        FragmentManager fragmentManager = getSupportFragmentManager();

        // If the AddEditFragment exists, we're editing
        boolean editing = fragmentManager.findFragmentById(R.id.task_details_container) != null;
        Log.d(TAG, "onCreate: editing is "+editing);

        // We need references to the containers, so we show or hide them as necessary
        // No need to cast them, as we're only calling a method that's  available for all views.
        View addEditLayout = findViewById(R.id.task_details_container);
        View taskTimerFragment = findViewById(R.id.fragment);

        if(twoPane){
            Log.d(TAG, "onCreate: two pane mode");
            taskTimerFragment.setVisibility(View.VISIBLE);
            addEditLayout.setVisibility(View.VISIBLE);
        }else if(editing){

            Log.d(TAG, "onCreate: single pane, editing");
            // hide the left hand fragment, to make room for editing
            taskTimerFragment.setVisibility(View.GONE);
        }else {
            Log.d(TAG, "onCreate: single pane not editing");
            // Show the left hand fragment
            taskTimerFragment.setVisibility(View.VISIBLE);
            // hide the editing frame
            addEditLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_timer_menu,menu);
        if (BuildConfig.DEBUG){
            MenuItem generate = menu.findItem(R.id.task_timer_generate);
            generate.setVisible(true);
        }
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
                showAboutDialog();
                break;
            case R.id.task_timer_generate:
                TestData.generateTestData(getContentResolver());
                break;
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: home button pressed");
                AddEditFragment fragment =
                        (AddEditFragment) getSupportFragmentManager().findFragmentById(R.id.task_details_container);
                if(fragment != null && fragment.canClose()) {
                    return super.onOptionsItemSelected(item);
                } else {
                    showConfirmationDialog(DIALOG_ID_CANCEL_EDIT_UP);
                    return true;  // indicate we are handling this
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog(){
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(messageView);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            Log.d(TAG, "onClick: Entering messageView.onClick, showing = "+dialog.isShowing());
            if (dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
        });
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher1);
//        messageView.setOnClickListener(view -> {
//            Log.d(TAG, "showAboutDialog: Entering messageView.onClick, showing = "+dialog.isShowing());
//            if (dialog !=null && dialog.isShowing()){
//                dialog.dismiss();
//            }
//        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        TextView tv = messageView.findViewById(R.id.about_version);
        tv.setText("v"+ BuildConfig.VERSION_NAME);
        dialog.show();
    }

    private void taskEditRequest(Task task){
        Log.d(TAG, "taskEditRequest: starts");
        Log.d(TAG, "taskEditRequest: in two-pane mode (tablet)");
        AddEditFragment fragment = new AddEditFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(Task.class.getSimpleName(), task);
        fragment.setArguments(arguments);
//           FragmentManager fragmentManager = getSupportFragmentManager();
//           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//           fragmentTransaction.replace(R.id.task_details_container, fragment);
//           fragmentTransaction.commit();

        Log.d(TAG, "taskEditRequest: twoPane mode");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.task_details_container, fragment)
                .commit();
        if (!twoPane){
            Log.d(TAG, "taskEditRequest: in single-pane mode (phone)");

            // Hide the left hand fragment and show the right hand fragment
            View taskTimerFragment = findViewById(R.id.fragment);
            View addEditLayout = findViewById(R.id.task_details_container);
            taskTimerFragment.setVisibility(View.GONE);
            addEditLayout.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "taskEditRequest: Exiting...");
    }

    @Override
    public void onEditClick(@NonNull Task task) {
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(@NonNull Task task) {
        Log.d(TAG, "onDeleteClick: starts");
        TaskTimerDialog dialog = new TaskTimerDialog();
        Bundle args = new Bundle();
        args.putInt(TaskTimerDialog.DIALOG_ID, DIALOG_ID_DELETE);
        args.putString(TaskTimerDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message,
                task.getId(), task.getName()));
        args.putInt(TaskTimerDialog.DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption);
        args.putLong("TaskId", task.getId());

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onTaskLongClick(@NonNull Task task) {
        //Require to satisfy the interface
        /*Log.d(TAG, "onTaskLongClick: called");
        Toast.makeText(this, "Task "+task.getId() + " clicked", Toast.LENGTH_SHORT).show();

        TextView taskName = findViewById(R.id.current_task);
        if (currentTiming != null){
            if (task.getId() == currentTiming.getTask().getId()){
                // the current was tapped a second time, so stop timing
                currentTiming = null;
                taskName.setText(R.string.task_timer_task_message);
            }else {
                // a new task is being timed, so stop the old one first
                currentTiming = new Timing(task);
                taskName.setText("Timing " + currentTiming.getTask().getName());
            }
        }else {
            // no task being timed, so start timing the new task
            currentTiming = new Timing(task);
            taskName.setText("Timing "+ currentTiming.getTask().getName());
        }*/
    }

    @Override
    public void onSaveClicked() {
        Log.d(TAG, "onSaveClicked: starts");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        View addEditLayout = findViewById(R.id.task_details_container);
        View taskTimerFragment = findViewById(R.id.fragment);
        if (!twoPane){
            // We've just removed the editing fragment, hide the frame
            addEditLayout.setVisibility(View.GONE);
            // and make sure the TaskTimerFragment is visible
            taskTimerFragment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: called");
        switch (dialogId){
            case DIALOG_ID_DELETE:
                long taskId = args.getLong("TaskId");
                if (BuildConfig.DEBUG && taskId == 0) throw new AssertionError("Task ID is zero");
                getContentResolver().delete(TasksContract.buildTaskUri(taskId), null, null);
                break;
            case DIALOG_ID_CANCEL_EDIT:
            case DIALOG_ID_CANCEL_EDIT_UP:
                // no action required
                break;
        }
    }


    private void showConfirmationDialog(int dialogId) {
        // show dialogue to get confirmation to quit editing
        TaskTimerDialog dialog = new TaskTimerDialog();
        Bundle args = new Bundle();
        args.putInt(TaskTimerDialog.DIALOG_ID, dialogId);
        args.putString(TaskTimerDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDialog_message));
        args.putInt(TaskTimerDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDialog_positive_caption);
        args.putInt(TaskTimerDialog.DIALOG_NEGATIVE_RID, R.string.cancelEditDialog_negative_caption);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");
        switch (dialogId){
            case DIALOG_ID_DELETE:
                // no action required
                break;
            case DIALOG_ID_CANCEL_EDIT:
            case DIALOG_ID_CANCEL_EDIT_UP:
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
                if (fragment != null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    if (twoPane){
                        // in Landscape, so quit only if the back button was pressed
                        if (dialogId == DIALOG_ID_CANCEL_EDIT) {
                            finish();
                        }
                    }else {
                        View addEditLayout = findViewById(R.id.task_details_container);
                        View taskTimerFragment = findViewById(R.id.fragment);
                        addEditLayout.setVisibility(View.GONE);
                        taskTimerFragment.setVisibility(View.VISIBLE);
                    }
                }else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: called");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditFragment fragment = (AddEditFragment) fragmentManager.findFragmentById(R.id.task_details_container);
        if (fragment == null || fragment.canClose()) {
            super.onBackPressed();
        }else {
            // show dialog to get confirmation to quit editing
            showConfirmationDialog(DIALOG_ID_CANCEL_EDIT);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}