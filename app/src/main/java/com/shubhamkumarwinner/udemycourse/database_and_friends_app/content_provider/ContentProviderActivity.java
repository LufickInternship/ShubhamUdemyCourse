package com.shubhamkumarwinner.udemycourse.database_and_friends_app.content_provider;

import static android.Manifest.permission.READ_CONTACTS;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.shubhamkumarwinner.udemycourse.MainActivity;
import com.shubhamkumarwinner.udemycourse.R;
import com.shubhamkumarwinner.udemycourse.databinding.ActivityContentProviderBinding;

import java.util.ArrayList;
import java.util.List;

public class ContentProviderActivity extends AppCompatActivity {
    private static final String TAG = "ContentProviderActivity";

//    private static final int REQUEST_CODE_READ_CONTACTS = 1;

    private ActivityContentProviderBinding binding;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    readContacts();
                }
                else if (!ActivityCompat.shouldShowRequestPermissionRationale(ContentProviderActivity.this, READ_CONTACTS)) {
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

        /*int hasReadContactPermission = ContextCompat.checkSelfPermission(this, READ_CONTACTS);
        Log.d(TAG, "onCreate: checkSelfPermission = " + hasReadContactPermission);

        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: permission granted");
        } else {
            Log.d(TAG, "onCreate: requesting permission");
            ActivityCompat.requestPermissions(this, new String[]{READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }

        binding.fab.setOnClickListener(view -> {
            Log.d(TAG, "fab onClick: starts");
//                if (READ_CONTACTS_GRANTED) {
            if(ContextCompat.checkSelfPermission(this, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                String[] projection = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                        projection,
                        null,
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

                if (cursor != null) {
                    List<String> contacts = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    }
                    cursor.close();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.contact_detail, R.id.name, contacts);
                    binding.contentProviderDetail.contactsName.setAdapter(adapter);
                }

            }
            else {
                Snackbar.make(view, "This app can't display your Contacts records unless you...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Grant Access", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.d(TAG, "Snackbar onClick: starts");
                                        if(ActivityCompat.shouldShowRequestPermissionRationale(ContentProviderActivity.this, READ_CONTACTS)) {
                                            Log.d(TAG, "Snackbar onClick: calling requestPermissions");
                                            ActivityCompat.requestPermissions(ContentProviderActivity.this, new String[] {READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
                                        } else {
                                            // The user has permanently denied the permission, so take them to the settings
                                            Log.d(TAG, "Snackbar onClick: launching settings");
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            Log.d(TAG, "Snackbar onClick: Intent Uri is " + uri.toString());
                                            intent.setData(uri);
                                            ContentProviderActivity.this.startActivity(intent);

                                        }
                                        Log.d(TAG, "Snackbar onClick: ends");
                                    }
                                }

                        ).show();
            }
            Log.d(TAG, "fab onClick: ends");
        });*/

        binding.fab.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: starts");
            requestPermissionLauncher.launch(READ_CONTACTS);
            Log.d(TAG, "fab onClick: ends");
        });
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: starts");
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACTS: {
                // If request is cancelled, the rest arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "onRequestPermissionsResult: permission refused");
                }
            }
        }

        Log.d(TAG, "onRequestPermissionsResult: ends");
    }*/
    private void readContacts() {
        String[] projection = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+" COLLATE NOCASE"
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