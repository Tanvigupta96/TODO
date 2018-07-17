package com.example.tanvigupta.todolist3;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class PermissionSettings extends AppCompatActivity {
    CheckBox c;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_settings);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        c = findViewById(R.id.checkbox);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
            c.setChecked(true);
            //save();
        } else {
            c.setChecked(false);
            //save();
        }

        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (ActivityCompat.checkSelfPermission(PermissionSettings.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                        String[] permissions = {Manifest.permission.RECEIVE_SMS};
                        ActivityCompat.requestPermissions(PermissionSettings.this, permissions, 1);
                    }
                } else {
                    save();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int callGrantResult = grantResults[0];
            if (callGrantResult == PackageManager.PERMISSION_GRANTED) {
                c.setChecked(true);
            } else {
                c.setChecked(false);
            }
            save();

        }
    }

    public void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.IS_SMS_READ_ENABLE, c.isChecked());
        editor.commit();
    }


}
