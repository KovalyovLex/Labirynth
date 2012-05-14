package com.flexymind.labirynth.screens;


import com.flexymind.labirynth.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartScreen extends Activity implements OnClickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        
        // Кнопка "Start"
        Button startButton = (Button)findViewById(R.id.StartButton);
        startButton.setOnClickListener(this);

        // Кнопка "Exit"
        Button exitButton = (Button)findViewById(R.id.ExitButton);
        exitButton.setOnClickListener(this);

        // Кнопка "Settings"
        Button settingsButton = (Button)findViewById(R.id.SettingsButton);
        settingsButton.setOnClickListener(this);
    }

    /** Обработка нажатия кнопок */
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.StartButton:
            {
                Intent intent = new Intent();
                intent.setClass(this, GameScreen.class);
                startActivity(intent);
                break;
            }

            case R.id.SettingsButton:
            {
                break;
            }

            case R.id.ExitButton:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) { 
    	super.onConfigurationChanged(newConfig);
    }
}
