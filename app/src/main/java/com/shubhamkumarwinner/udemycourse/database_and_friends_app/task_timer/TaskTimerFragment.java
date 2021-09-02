package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shubhamkumarwinner.udemycourse.databinding.FragmentTaskTimerBinding;
import java.security.InvalidParameterException;

public class TaskTimerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "TaskTimerFragment";

    public static final int LOADER_ID = 0;

    private CursorRecyclerViewAdapter adapter;

    private FragmentTaskTimerBinding binding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: starts");
        super.onActivityCreated(savedInstanceState);
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d(TAG, "onCreateView: starts");
        binding = FragmentTaskTimerBinding.inflate(inflater, container, false);
        binding.taskList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CursorRecyclerViewAdapter(null, (CursorRecyclerViewAdapter.OnTaskClickListener) getActivity());
        binding.taskList.setAdapter(adapter);

        Log.d(TAG, "onCreateView: returning");
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: starts with id "+id);
        String[] projection = {TasksContract.Columns._ID, TasksContract.Columns.TASKS_NAME,
                                TasksContract.Columns.TASKS_DESCRIPTION, TasksContract.Columns.TASKS_SORT_ORDER};
        // <order by> Tasks.SortOrder, Tasks.Name COLLATE NOCASE
        String sortOrder = TasksContract.Columns.TASKS_SORT_ORDER + ","+TasksContract.Columns.TASKS_NAME+" COLLATE NOCASE";
        switch (id){
            case LOADER_ID:
                return new CursorLoader(getActivity(),
                        TasksContract.CONTENT_URI,
                        projection,
                        null,
                        null,
                        sortOrder);
            default:
                throw new InvalidParameterException(TAG+".onCreateLoader called with invalid loader id"+id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: starts");
        adapter.swapCursor(data);
        int count = adapter.getItemCount();
        Log.d(TAG, "onLoadFinished: count is "+count);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: starts");
        adapter.swapCursor(null);
    }
}