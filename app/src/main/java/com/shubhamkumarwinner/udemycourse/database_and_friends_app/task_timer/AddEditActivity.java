package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.databinding.ActivityAddEditBinding;

import java.util.Objects;

public class AddEditActivity extends AppCompatActivity implements AddEditFragment.OnSaveClicked, TaskTimerDialog.DialogEvents {
    private static final String TAG = "AddEditActivity";
    public static final int DIALOG_ID_CANCEL_EDIT = 1;

    private ActivityAddEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);

        binding = ActivityAddEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras(); // The line we'll change later
        AddEditFragment fragment = new AddEditFragment();
//        arguments.putSerializable(Task.class.getSimpleName(), task);
        fragment.setArguments(arguments);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveClicked() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: home button pressed");
                AddEditFragment fragment = (AddEditFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment);
                if(fragment.canClose()) {
                    return super.onOptionsItemSelected(item);
                } else {
                    showConfirmationDialog();
                    return true;  // indicate we are handling this
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditFragment fragment = (AddEditFragment) fragmentManager.findFragmentById(R.id.fragment);
        if(fragment.canClose()) {
            super.onBackPressed();
        } else {
            showConfirmationDialog();
        }
    }

    private void showConfirmationDialog() {
        // show dialogue to get confirmation to quit editing
        TaskTimerDialog dialog = new TaskTimerDialog();
        Bundle args = new Bundle();
        args.putInt(TaskTimerDialog.DIALOG_ID, DIALOG_ID_CANCEL_EDIT);
        args.putString(TaskTimerDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDialog_message));
        args.putInt(TaskTimerDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDialog_positive_caption);
        args.putInt(TaskTimerDialog.DIALOG_NEGATIVE_RID, R.string.cancelEditDialog_negative_caption);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);
    }


    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: called");
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");
        finish();
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: called");
    }
}