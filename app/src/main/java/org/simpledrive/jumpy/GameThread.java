package org.simpledrive.jumpy;

import android.graphics.Canvas;
import android.util.Log;

public class GameThread extends Thread {
    private GameView view;
    private boolean running = false;
    private long lastRender = System.currentTimeMillis();

    public GameThread(GameView view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        while (running) {
            float delta = (System.currentTimeMillis() - lastRender) / 1000.0f;

            Canvas c = view.getHolder().lockCanvas();

            if (c != null) {
                synchronized (view.getHolder()) {
                    view.updatePhysics(delta);
                    view.doDraw(c);
                }
                view.getHolder().unlockCanvasAndPost(c);
            }

            lastRender = System.currentTimeMillis();

            try {
                sleep(1000/60);
            } catch (Exception e) {}
        }
    }
}