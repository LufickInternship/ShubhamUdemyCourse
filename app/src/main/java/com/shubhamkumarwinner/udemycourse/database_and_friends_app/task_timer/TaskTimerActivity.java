package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.shubhamkumarwinner.udemycourse.BuildConfig;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.databinding.ActivityTaskTimerBinding;

public class TaskTimerActivity extends AppCompatActivity
        implements CursorRecyclerViewAdapter.OnTaskClickListener,
        AddEditFragment.OnSaveClicked,
        TaskTimerDialog.DialogEvents{
    private static final String TAG = "TaskTimerActivity";
    public static final int DIALOG_ID_CANCEL_EDIT = 2;

    private AlertDialog dialog= null;       //module scope because we need to dismiss it in onStop

    // Whether or not the activity is in 2-pane mode
    // i.e. running in landscape or a tablet
    private boolean twoPane = false;
    public static final String ADD_EDIT_FRAGMENT = "AddEditFragment";

    public static final int DIALOG_ID_DELETE = 1;

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
                showAboutDialog();
                break;
            case R.id.task_timer_generate:
                break;
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
        tv.setText("v"+BuildConfig.VERSION_NAME);
        dialog.show();
    }

    private void taskEditRequest(Task task){
        Log.d(TAG, "taskEditRequest: starts");
        if (twoPane){
            Log.d(TAG, "taskEditRequest: in two-pane mode (tablet)");
            AddEditFragment fragment = new AddEditFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable(Task.class.getSimpleName(), task);
            fragment.setArguments(arguments);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.task_details_container, fragment);
//            fragmentTransaction.commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.task_details_container, fragment)
                    .commit();
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
                // no action required
                break;
        }
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");
        switch (dialogId){
            case DIALOG_ID_DELETE:
                // no action required
                break;
            case DIALOG_ID_CANCEL_EDIT:
                finish();
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
            TaskTimerDialog dialog = new TaskTimerDialog();
            Bundle args = new Bundle();
            args.putInt(TaskTimerDialog.DIALOG_ID, DIALOG_ID_CANCEL_EDIT);
            args.putString(TaskTimerDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDialog_message));
            args.putInt(TaskTimerDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDialog_positive_caption);
            args.putInt(TaskTimerDialog.DIALOG_NEGATIVE_RID, R.string.cancelEditDialog_negative_caption);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), null);
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