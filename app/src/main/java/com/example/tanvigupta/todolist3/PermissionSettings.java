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

public class PermissionSettings extends AppCompatActivity {
    CheckBox c;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_settings);

        c=findViewById(R.id.checkbox);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED)
        {     c.setChecked(true);
            c.setEnabled(false);
            //save();
        }
        else{
            c.setChecked(false);
            // save();
        }


    }

    public void onCheckBoxClicked(View view){
        String[] permissions = {Manifest.permission.RECEIVE_SMS};
        ActivityCompat.requestPermissions(this, permissions, 1);


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

    public void save(){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("Check",c.isChecked());
        editor.commit();
    }


}
