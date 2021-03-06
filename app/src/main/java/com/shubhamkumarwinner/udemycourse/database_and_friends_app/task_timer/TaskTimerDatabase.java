package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.LongDef;

/**
 * Basic database class for the application
 *
 * The only class that should use this is {@link TaskTimerProvider}.
 * */
public class TaskTimerDatabase extends SQLiteOpenHelper {
    private static final String TAG = "TaskTimerDatabase";

    public static final String DATABASE_NAME = "TaskTimer.db";
    public static final int DATABASE_VERSION = 3;

    // Implement AppDatabase as a Singleton
    private static TaskTimerDatabase instance = null;

    private TaskTimerDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "TaskTimerDatabase: constructor");
    }

    /**
     * Get an instance of the app's singleton database helper object
     * @param context the content provider context.
     * @return a SQLite database helper object
     */
    static TaskTimerDatabase getInstance(Context context){
        if (instance == null){
            Log.d(TAG, "getInstance: creating new instance");
            instance = new TaskTimerDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");
        String sSQL;   // Use a String variable to facilitate logging
//        sSQL = "CREATE TABLE Tasks (_id INTEGER PRIMARY KEY NOT NULL, Name TEXT NOT NULL, Description TEXT, SortOrder INTEGER);";
        sSQL = "CREATE TABLE "+TasksContract.TABLE_NAME + " ("
                + TasksContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + TasksContract.Columns.TASKS_NAME + " TEXT NOT NULL, "
                + TasksContract.Columns.TASKS_DESCRIPTION + " TEXT, "
                + TasksContract.Columns.TASKS_SORT_ORDER+" INTEGER);";
        Log.d(TAG, sSQL);
        db.execSQL(sSQL);

        addTimingsTable(db);
        addDurationView(db);

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch (oldVersion){
            case 1:
                // upgrade logic from version 1
                addTimingsTable(db);
                // fall through, to include version 2 upgrade logic as well
            case 2:
                // upgrade logic from version 2
                addDurationView(db);
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: "+ newVersion);
        }
        Log.d(TAG, "onUpgrade: ends");
    }
    private void addTimingsTable(SQLiteDatabase db){
        String sSQL;
        sSQL = "CREATE TABLE "+TimingsContract.TABLE_NAME + " ("
                + TimingsContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + TimingsContract.Columns.TIMINGS_TASK_ID + " INTEGER NOT NULL, "
                + TimingsContract.Columns.TIMINGS_START_TIME + " INTEGER, "
                + TimingsContract.Columns.TIMINGS_DURATION + " INTEGER);";
        Log.d(TAG, sSQL);
        db.execSQL(sSQL);

        //TODO ask question about following sql command
        sSQL = "CREATE TRIGGER Remove_Task"
                + " AFTER DELETE ON "+ TasksContract.TABLE_NAME
                + " FOR EACH ROW BEGIN DELETE FROM "+ TimingsContract.TABLE_NAME
                + " WHERE " + TimingsContract.Columns.TIMINGS_TASK_ID + " = OLD."+TasksContract.Columns._ID+";"
                + " END;";
        db.execSQL(sSQL);
    }

    private void addDurationView(SQLiteDatabase db){
        String sSQL;
        sSQL = "CREATE VIEW "+DurationsContract.TABLE_NAME
                + " AS SELECT "+ TimingsContract.TABLE_NAME + "."+TimingsContract.Columns._ID+","
                + TasksContract.TABLE_NAME + "."+TasksContract.Columns.TASKS_NAME + ", "
                + TasksContract.TABLE_NAME + "."+TasksContract.Columns.TASKS_DESCRIPTION + ", "
                + TimingsContract.TABLE_NAME + "."+TimingsContract.Columns.TIMINGS_START_TIME + ","
                + " DATE("+TimingsContract.TABLE_NAME+"."+TimingsContract.Columns.TIMINGS_START_TIME+", 'unixepoch')"
                + " AS " + DurationsContract.Columns.DURATIONS_START_DATE+","
                + " SUM("+ TimingsContract.TABLE_NAME + "."+TimingsContract.Columns.TIMINGS_DURATION+")"
                + " AS "+DurationsContract.Columns.DURATIONS_DURATION
                + " FROM "+ TasksContract.TABLE_NAME + " JOIN "+ TimingsContract.TABLE_NAME
                + " ON "+ TasksContract.TABLE_NAME+"."+TasksContract.Columns._ID+ " = "
                + TimingsContract.TABLE_NAME+"."+TimingsContract.Columns.TIMINGS_TASK_ID
                + " GROUP BY "+ DurationsContract.Columns.DURATIONS_START_DATE+ ", "
                + DurationsContract.Columns.DURATIONS_NAME+";";
        Log.d(TAG, sSQL);
        db.execSQL(sSQL);
    }
}
