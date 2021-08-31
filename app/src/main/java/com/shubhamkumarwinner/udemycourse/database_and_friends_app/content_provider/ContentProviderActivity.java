package com.shubhamkumarwinner.udemycourse.database_and_friends_app.content_provider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.databinding.ActivityContentProviderBinding;

import java.util.ArrayList;
import java.util.List;

public class ContentProviderActivity extends AppCompatActivity {
    private static final String TAG = "ContentProviderActivity";

    private ActivityContentProviderBinding binding;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    readContacts();
                }
                else if (!ActivityCompat.shouldShowRequestPermissionRationale(ContentProviderActivity.this, Manifest.permission.READ_CONTACTS)) {
                    //requires api 23 and above
//                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("You have permanently denied contact permission");
                    alertDialog.setMessage("Please allow it in your settings");
                    alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(
                                    new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            .setData(Uri.fromParts("package", getPackageName(), null)));
                        }
                    });
                    alertDialog.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alert = alertDialog.create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.show();
                }
                else {
                    Toast.makeText(this, "Permission is denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContentProviderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: starts");
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
            Log.d(TAG, "fab onClick: ends");
        });
    }

    private void readContacts() {
        String[] projection = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        );

        if (cursor != null) {
            List<String> contacts = new ArrayList<>();
            while (cursor.moveToNext()) {
                contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
            }
            cursor.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ContentProviderActivity.this, R.layout.contact_detail, R.id.name, contacts);
            binding.contentProviderDetail.contactsName.setAdapter(adapter);
        }
    }
}