package com.android.customview;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class MoveViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_view);
        AppCompatImageView ivCircle = findViewById(R.id.iv_cloud);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float width = 400f;
            float height = 400f;
            float radius;
            radius = height / 4;
            Path path = new Path();
            path.addCircle(width / 2,
                    height / 2, radius,
                    Path.Direction.CW);
            ObjectAnimator animator = ObjectAnimator.ofFloat(ivCircle, View.X, View.Y, path);
            animator.setDuration(1500);
            animator.start();
        } else {
            // Create animator without using curved path
        }
    }
}
