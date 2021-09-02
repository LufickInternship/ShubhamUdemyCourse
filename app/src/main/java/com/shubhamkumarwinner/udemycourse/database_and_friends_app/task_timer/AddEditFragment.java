package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.shubhamkumarwinner.udemycourse.databinding.FragmentAddEditBinding;

public class AddEditFragment extends Fragment {
    private static final String TAG = "AddEditFragment";

    private FragmentAddEditBinding binding;

    public enum FragmentEditMode{EDIT, ADD}
    private FragmentEditMode mode;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAddEditBinding.inflate(inflater, container, false);
//        Bundle arguments = getActivity().getIntent().getExtras(); // The line we'll change later
        Bundle arguments = getArguments(); // The line we'll change later
        final Task task;
        if (arguments != null){
            Log.d(TAG, "onCreateView: retrieving task details");

            task = (Task) arguments.getSerializable(Task.class.getSimpleName());
            if (task != null){
                Log.d(TAG, "onCreateView: Task details found, editing...");
                binding.addEditName.setText(task.getName());
                binding.addEditDescription.setText(task.getDescription());
                binding.addEditSortOrder.setText(Integer.toString(task.getSortOrder()));
                mode = FragmentEditMode.EDIT;
            }else {
                // No task, so we must be adding a new task, and not editing an existing one
                mode = FragmentEditMode.ADD;
            }
        }else {
            task = null;
            Log.d(TAG, "onCreateView: No arguments, adding new record");
            mode = FragmentEditMode.ADD;
        }

        binding.addEditSaveButton.setOnClickListener(view -> {
            // update the database if at least on field has changed.
            // - There's no need to hit the database unless this has happened
            int so;   // to save repeated conversions to int
            if (binding.addEditSortOrder.length()>0){
                so = Integer.parseInt(binding.addEditSortOrder.getText().toString());
            }else {
                so = 0;
            }

            ContentResolver contentResolver = getActivity().getContentResolver();
            ContentValues values = new ContentValues();

            switch (mode){
                case EDIT:
                    if (!binding.addEditName.getText().toString().equals(task.getName())){
                        values.put(TasksContract.Columns.TASKS_NAME, binding.addEditName.getText().toString());
                    }
                    if (!binding.addEditDescription.getText().toString().equals(task.getDescription())){
                        values.put(TasksContract.Columns.TASKS_DESCRIPTION, binding.addEditDescription.getText().toString());
                    }
                    if (so != task.getSortOrder()){
                        values.put(TasksContract.Columns.TASKS_SORT_ORDER, so);
                    }
                    if (values.size() != 0){
                        Log.d(TAG, "onCreateView: updating task");
                        contentResolver.update(TasksContract.buildTaskUri(task.getId()), values, null, null);
                    }
                    break;
                case ADD:
                    if (binding.addEditName.length()>0){
                        Log.d(TAG, "onCreateView: adding new task");
                        values.put(TasksContract.Columns.TASKS_NAME, binding.addEditName.getText().toString());
                        values.put(TasksContract.Columns.TASKS_DESCRIPTION, binding.addEditDescription.getText().toString());
                        values.put(TasksContract.Columns.TASKS_SORT_ORDER, so);
                        contentResolver.insert(TasksContract.CONTENT_URI, values);
                    }
                    break;
            }
            Log.d(TAG, "onCreateView: Done editing");
        });
        Log.d(TAG, "onCreateView: Exiting...");
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}