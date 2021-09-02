package com.shubhamkumarwinner.udemycourse.database_and_friends_app.task_timer;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.databinding.ActivityAddEditBinding;

import java.util.Objects;

public class AddEditActivity extends AppCompatActivity {
    private static final String TAG = "AddEditActivity";

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

}