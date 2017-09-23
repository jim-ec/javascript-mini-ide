package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

import com.example.jimec.javascriptshell.R;

public class SpaceKey extends android.support.v7.widget.AppCompatImageView implements KeyboardKeyConnection {

    private Keyboard mKeyboard;

    public SpaceKey(Context context) {
        super(context);
        init();
    }

    public SpaceKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpaceKey(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(new SpaceKey.TouchListener());
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_rect));
        DrawableCompat.setTint(DrawableCompat.wrap(getBackground()), ContextCompat.getColor(getContext(), R.color.keyInactive));
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
        mKeyboard = keyboard;
    }

    private class TouchListener extends AbstractKeyTouchListener {

        @Override
        public void onTap() {
            mKeyboard.write(" ");
        }

        @Override
        public void onDown() {
            DrawableCompat.setTint(DrawableCompat.wrap(getBackground()), ContextCompat.getColor(getContext(), R.color.keyActive));
        }

        @Override
        public void onUp() {
            DrawableCompat.setTint(DrawableCompat.wrap(getBackground()), ContextCompat.getColor(getContext(), R.color.keyInactive));
        }
    }
}