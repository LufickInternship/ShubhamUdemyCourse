package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shubhamkumarwinner.udemycourse.R;

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.TaskViewHolder> {
    private static final String TAG = "CursorRecyclerAdapter";
    private Cursor cursor;
    private final OnTaskClickListener listener;

    interface OnTaskClickListener{
        void onEditClick(@NonNull Task task);
        void onDeleteClick(@NonNull Task task);
        void onTaskLongClick(@NonNull Task task);
    }

    public CursorRecyclerViewAdapter(Cursor cursor, OnTaskClickListener listener) {
        Log.d(TAG, "CursorRecyclerViewAdapter: constructor called");
        this.cursor = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateViewHolder: new view requested");
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_timer_list_item,
                parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
//        Log.d(TAG, "onBindViewHolder: starts");
        if (cursor == null || cursor.getCount()==0){
            Log.d(TAG, "onBindViewHolder: providing instructions");
            holder.name.setText(R.string.task_timer_instructions_heading);
            holder.description.setText(R.string.task_timer_instructions);
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }else {
            if (!cursor.moveToPosition(position)){
                throw new IllegalStateException("Couldn't move cursor to position "+position);
            }

            Task task = new Task(
                    cursor.getLong(cursor.getColumnIndex(TasksContract.Columns._ID)),
                    cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASKS_NAME)),
                    cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASKS_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(TasksContract.Columns.TASKS_SORT_ORDER))
            );

            holder.name.setText(task.getName());
            holder.description.setText(task.getDescription());
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);

            View.OnClickListener buttonListener = view -> {
//                    Log.d(TAG, "onClick: starts");
                switch (view.getId()){
                    case R.id.tli_edit:
                        if (listener != null) {
                            listener.onEditClick(task);
                        }
                        break;
                    case R.id.tli_delete:
                        if (listener != null) {
                            listener.onDeleteClick(task);
                        }
                        break;
                    default:
                        Log.d(TAG, "onClick: found unexpected button id");
                }
            };

            View.OnLongClickListener buttonLongListener = view -> {
                Log.d(TAG, "onLongClick: starts");
                if (listener != null){
                    listener.onTaskLongClick(task);
                    return true;
                }
                return false;
            };

            holder.editButton.setOnClickListener(buttonListener);
            holder.deleteButton.setOnClickListener(buttonListener);
            holder.itemView.setOnLongClickListener(buttonLongListener);
        }

    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: starts");
        if (cursor == null || cursor.getCount()==0){
            return 1;
        }else {
            return cursor.getCount();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.
     * The returned old cursor is <em>not</em> closed
     * @param newCursor The new cursor to be used
     * @return Returns the previously set cursor, or null if there wasn't one.
     * If the given new Cursor is the same instance as the previously set
     * Cursor, null is also returned.
     */
    //TODO ask question about this method
    Cursor swapCursor(Cursor newCursor){
        if (newCursor == cursor){
            return null;
        }
        final Cursor oldCursor = cursor;
        cursor = newCursor;
        if (newCursor != null){
            // notify the observers about the new cursor
            notifyDataSetChanged();
        }else {
            //notify the observers about the lack of a data set
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView description;
        ImageButton editButton;
        ImageButton deleteButton;
        View itemView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
//            Log.d(TAG, "TaskViewHolder: starts");
            this.name = itemView.findViewById(R.id.tli_name);
            this.description = itemView.findViewById(R.id.tli_description);
            this.editButton = itemView.findViewById(R.id.tli_edit);
            this.deleteButton = itemView.findViewById(R.id.tli_delete);
            this.itemView = itemView;
        }
    }
}
