package com.example.memoweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    public static final String SP_NAME = "settings";
    public static final String KEY_DARK = "dark_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LC", "Settings onCreate");
        setContentView(R.layout.activity_settings);

        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        boolean dark = sp.getBoolean(KEY_DARK, false);

        SwitchMaterial sw = findViewById(R.id.swDarkMode);
        sw.setChecked(dark);

        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sp.edit().putBoolean(KEY_DARK, isChecked).apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            // ✅ 立刻重建当前页面，避免“部分生效”
            recreate();
        });
    }
}
