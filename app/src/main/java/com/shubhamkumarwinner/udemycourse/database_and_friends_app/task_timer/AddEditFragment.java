package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.shubhamkumarwinner.udemycourse.databinding.FragmentAddEditBinding;

public class AddEditFragment extends Fragment {
    private static final String TAG = "AddEditFragment";

    private FragmentAddEditBinding binding;

    public boolean canClose() {
        return false;
    }

    public enum FragmentEditMode{EDIT, ADD}
    private FragmentEditMode mode;

    private OnSaveClicked saveListener = null;

    interface OnSaveClicked{
        void onSaveClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);
        Activity activity = getActivity();
        if (!(activity instanceof OnSaveClicked) && activity !=null){
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement AddEditFragment.OnSaveClicked interface");
        }
        saveListener = (OnSaveClicked) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        saveListener = null;
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
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

            ContentResolver contentResolver = requireActivity().getContentResolver();
            ContentValues values = new ContentValues();

            switch (mode){
                case EDIT:
                    if (task ==null){
                        break;
                    }
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
            if (saveListener != null) {
                saveListener.onSaveClicked();
            }
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