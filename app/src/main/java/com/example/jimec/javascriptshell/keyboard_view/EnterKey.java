package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

import com.example.jimec.javascriptshell.R;

public class EnterKey extends android.support.v7.widget.AppCompatImageView implements KeyboardKeyConnection {

    private Keyboard mKeyboard;

    public EnterKey(Context context) {
        super(context);
        init();
    }

    public EnterKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EnterKey(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(new EnterKey.TouchListener());
        setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_enter));
        DrawableCompat.setTint(DrawableCompat.wrap(getDrawable()), ContextCompat.getColor(getContext(), R.color.keyEnter));
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
        mKeyboard = keyboard;
    }

    private class TouchListener extends AbstractKeyTouchListener {

        @Override
        public void onTap() {
            mKeyboard.writeEnter();
        }

        @Override
        public void onDown() {
            //DrawableCompat.setTint(DrawableCompat.wrap(getDrawable()), ContextCompat.getColor(getContext(), R.color.keyActive));
        }

        @Override
        public void onUp() {
            //DrawableCompat.setTint(DrawableCompat.wrap(getDrawable()), ContextCompat.getColor(getContext(), R.color.keyInactive));
        }

    }
}