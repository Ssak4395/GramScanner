package com.example.bar_code_scanner;

import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bar_code_scanner.database.CoilMasterRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * Main entry point of the Application, user(administrator) will sign in to the application
 */
public class StartScreen extends AppCompatActivity {

    private Context context = this;
    private Button button;
    private Button skin;


    /**
     * @Author: Sadman Sakib
     * starts the UI Thread, and sets the layout.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        verifyPermissions();
        initView();
        explodeNewActivity();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * @Author: Sadman Sakib
     * Initialises the user interface objects, button , etc.
     */
    public void initView()
    {
        button = findViewById(R.id.login);
        skin = findViewById(R.id.skin);
    }

    /**
     * Verifies permission pretty self explanatory.
     */
    private void verifyPermissions()
    {
        Log.d("Permission", "Permissions are requested");
        String[] permissions = {Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this,permissions[0]) == PackageManager.PERMISSION_GRANTED)
        {

        }else
        {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    public void explodeNewActivity()
    {

        skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainView.class);
                intent.putExtra("choice","Skin");
                context.startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainView.class);
                intent.putExtra("choice","Transfer");
                context.startActivity(intent);
                finish();

            }
        });
    }



}