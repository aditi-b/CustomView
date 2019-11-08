package com.android.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class CustomBackgroundActivity extends AppCompatActivity  implements View.OnClickListener {

    private CustomBackground customBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_background);
        customBackground = findViewById(R.id.custom_text_view);
        customBackground.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.custom_text_view) {
            customBackground.handleStateSelection();
        }
    }
}
