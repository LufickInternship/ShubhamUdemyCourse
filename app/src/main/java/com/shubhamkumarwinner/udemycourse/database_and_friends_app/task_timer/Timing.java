package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

class Timing implements Serializable {
    private static final long serialVersionUID = 20210902L;
    private static final String TAG = Timing.class.getSimpleName();

    private long id;
    private Task task;
    private long startTime;
    private long duration;

    public Timing(Task task) {
        this.task = task;
        // Initialize the start time to now and the duration to zero for a new object.
        Date currentTime = new Date();
        startTime = currentTime.getTime()/1000; // we are only tracking whole seconds
        duration = 0;
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    Task getTask() {
        return task;
    }

    void setTask(Task task) {
        this.task = task;
    }

    long getStartTime() {
        return startTime;
    }

    void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    long getDuration() {
        return duration;
    }

    void setDuration() {
        Date currentTime = new Date();
        this.duration = currentTime.getTime()/1000-startTime;
        Log.d(TAG, task.getId() + " - start time: "+startTime+" | Duration: "+duration);
    }
}
