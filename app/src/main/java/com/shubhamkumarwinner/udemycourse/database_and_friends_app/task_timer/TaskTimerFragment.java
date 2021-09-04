package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.app.Activity;
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

public class TaskTimerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerViewAdapter.OnTaskClickListener {
    private static final String TAG = "TaskTimerFragment";

    public static final int LOADER_ID = 0;

    private CursorRecyclerViewAdapter adapter;

    private FragmentTaskTimerBinding binding;

    //TODO ask question about what is the replacement of deprecated methods
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: starts");
        super.onActivityCreated(savedInstanceState);

        // Activities containing this fragment must implement its callback
        Activity activity = getActivity();
        if (!(activity instanceof CursorRecyclerViewAdapter.OnTaskClickListener) && activity !=null){
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement CursorRecyclerViewAdapter.OnTaskClickListener interface");
        }
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onEditClick(Task task) {
        Log.d(TAG, "onEditClick: called");
        CursorRecyclerViewAdapter.OnTaskClickListener listener = (CursorRecyclerViewAdapter.OnTaskClickListener) getActivity();
        if (listener != null){
            listener.onEditClick(task);
        }
    }

    @Override
    public void onDeleteClick(Task task) {
        Log.d(TAG, "onDeleteClick: called");
        CursorRecyclerViewAdapter.OnTaskClickListener listener = (CursorRecyclerViewAdapter.OnTaskClickListener) getActivity();
        if (listener != null){
            listener.onDeleteClick(task);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d(TAG, "onCreateView: starts");
        binding = FragmentTaskTimerBinding.inflate(inflater, container, false);
        binding.taskList.setLayoutManager(new LinearLayoutManager(getContext()));
        if (adapter == null){
            //TODO ask question about why to use this in case of (CursorRecyclerViewAdapter.OnTaskClickListener) getActivity() and ask question about video 280 and 281
//            adapter = new CursorRecyclerViewAdapter(null, (CursorRecyclerViewAdapter.OnTaskClickListener) getActivity());
            adapter = new CursorRecyclerViewAdapter(null, this);
        }
//        else {
//            adapter.setListener((CursorRecyclerViewAdapter.OnTaskClickListener) getActivity());
//        }
        binding.taskList.setAdapter(adapter);

        Log.d(TAG, "onCreateView: returning");
        return binding.getRoot();

    }

    //TODO ask question about mainActivity starts after rotation of device why to use setRetainInstance(true)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //TODO ask question about these method (onCreateLoader, onLoadFinished, onLoaderReset)
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