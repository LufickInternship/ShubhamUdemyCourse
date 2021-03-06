package com.shubhamkumarwinner.udemycourse.debug;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer.TimingsContract;

import java.util.GregorianCalendar;

public class TestData {
    public static void generateTestData(@NonNull ContentResolver contentResolver){
        final int SECS_IN_DAY = 86400;
        final int LOWER_BOUND = 100;
        final int UPPER_BOUND = 500;
        final int MAX_DURATION = SECS_IN_DAY/6;

        //get a list of task ID's from the database.
        String[] projection = {TimingsContract.Columns._ID};
        Uri uri = TimingsContract.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, projection, null, null,null);

        if (cursor != null && cursor.moveToNext()){
            do {
                long taskId = cursor.getLong(cursor.getColumnIndex(TimingsContract.Columns._ID));
                //generate between LOWER_BOUND and UPPER_BOUND random timings for this task
                int longCount = LOWER_BOUND + getRandomInt(UPPER_BOUND - LOWER_BOUND);

                for (int i = 0; i<longCount; i++) {

                    long randomDate = randomDateTime();

                    // generate a random duration between 0 and 4 hours
                    long duration = (long) getRandomInt(MAX_DURATION);

                    // create new TestTiming object
                    TestTiming testTiming = new TestTiming(taskId, randomDate, duration);

                    // add it to database
                    saveCurrentTiming(contentResolver, testTiming);
                }
            }while (cursor.moveToNext());
            cursor.close();
        }

    }

    private static int getRandomInt(int max){
        return (int) Math.round(Math.random() * max);
    }

    private static long randomDateTime(){
        // Set the range of years - change as necessary
        final int startYear = 2020;
        final int endYear = 2022;

        int sec = getRandomInt(59);
        int min = getRandomInt(59);
        int hour = getRandomInt(23);
        int month = getRandomInt(11);
        int year = startYear +  getRandomInt(endYear - startYear);

        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int day = 1 + getRandomInt(gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) - 1);

        gc.set(year, month, day, hour, min, sec);
        return gc.getTimeInMillis();
    }

    private static void saveCurrentTiming(ContentResolver contentResolver, TestTiming currentTiming){
        // save the timing record
        ContentValues values = new ContentValues();
        values.put(TimingsContract.Columns.TIMINGS_TASK_ID, currentTiming.taskId);
        values.put(TimingsContract.Columns.TIMINGS_START_TIME, currentTiming.startTime);
        values.put(TimingsContract.Columns.TIMINGS_DURATION, currentTiming.duration);

        //update the database
        contentResolver.insert(TimingsContract.CONTENT_URI, values);
    }
}
