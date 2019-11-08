package com.android.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class CustomBackground extends RelativeLayout {
    private Paint mpaint;
    private AppCompatImageView ivCheck;
    private AppCompatTextView tvText;
    private boolean state;
    private RelativeLayout rlContainer;

    public CustomBackground(@NonNull Context context) {
        super(context);
    }

    public CustomBackground(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0,0);
    }

    public CustomBackground(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CustomBackground(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.layout_custom_image_include, this);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        ivCheck = findViewById(R.id.iv_check);
        tvText = findViewById(R.id.tv_label);
        rlContainer = findViewById(R.id.rl_container);
        if (set == null) {
            return;
        }
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomBackground);
        int mBackgroundColor = ta.getColor(R.styleable.CustomBackground_background_color, Color.GRAY);
        String text = ta.getString(R.styleable.CustomBackground_text);
        boolean stateSelected = ta.getBoolean(R.styleable.CustomBackground_state_selected, true);
        ta.recycle();
        handleStateSelection();
    }

    public void handleStateSelection() {
        tvText.setSelected(state);
        if (state) {
            showImage();
            rlContainer.setSelected(true);
            state = false;
        } else {
            hideImage();
            ivCheck.setVisibility(GONE);
            rlContainer.setSelected(false);
            state = true;
        }
    }

    public void setText(String text){
        tvText.setText(text);
    }

    private void showImage(){
        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = ivCheck.getWidth() / 2;
            int cy = ivCheck.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(ivCheck, cx, cy, 0f, finalRadius);

            // make the view visible and start the animation
            ivCheck.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            // set the view to invisible without a circular reveal animation below Lollipop
            ivCheck.setVisibility(View.INVISIBLE);
        }
    }

    private void hideImage() {
        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = ivCheck.getWidth() / 2;
            int cy = ivCheck.getHeight() / 2;

            // get the initial radius for the clipping circle
            float initialRadius = (float) Math.hypot(cx, cy);

            // create the animation (the final radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(ivCheck, cx, cy, initialRadius, 0f);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ivCheck.setVisibility(View.GONE);
                }
            });

            // start the animation
            anim.start();
        } else {
            // set the view to visible without a circular reveal animation below Lollipop
            ivCheck.setVisibility(View.VISIBLE);
        }

    }
}
