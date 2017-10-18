package io.jimeckerlein.jsshell.keyboard;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TapListenerRepeatable implements View.OnTouchListener {
    
    private static final long INITIAL_DELAY_MILLIS = 450;
    private static final long REPEAT_DELAY_MILLIS = 70;
    
    private Timer mRepeatTimer;
    
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean processed = false;
        int action = event.getAction();
        
        if (MotionEvent.ACTION_DOWN == action) {
            mRepeatTimer = new Timer();
            mRepeatTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(() -> onTap());
                }
            }, INITIAL_DELAY_MILLIS, REPEAT_DELAY_MILLIS);
            onDown();
            onTap();
            processed = true;
        }

        else if (MotionEvent.ACTION_UP == action || MotionEvent.ACTION_CANCEL == action) {
            mRepeatTimer.cancel();
            onUp();
            processed = true;
        }
        
        return processed;
    }
    
    public void onTap() {
    }
    
    public void onDown() {
    }
    
    public void onUp() {
    }
    
}