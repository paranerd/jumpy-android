package org.simpledrive.jumpy;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Game extends Activity {
    // Game
    static int                  score = 0;

    // Interface
    static TextView             tScore;
    private Handler             mHandler;
    private GameView            gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        mHandler = new Handler();
        gameView = new GameView(this);

        // Interface
        tScore = (TextView) findViewById(R.id.score);

        RelativeLayout surface = (RelativeLayout) findViewById(R.id.canvas);
        surface.addView(gameView);

        Button bLeft = (Button) findViewById(R.id.bLeft);
        Button bRight = (Button) findViewById(R.id.bRight);

        bLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent me) {
                int action = me.getAction() & MotionEvent.ACTION_MASK;

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN: {
                        gameView.moveLeft(true);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP: {
                        gameView.moveLeft(false);
                        break;
                    }
                }
                return true;
            }
        });

        bRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent me) {
                int action = me.getAction() & MotionEvent.ACTION_MASK;

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN: {
                        Log.i("right", "down");
                        gameView.moveRight(true);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP: {
                        Log.i("right", "up");
                        gameView.moveRight(false);
                        break;
                    }
                }
                return true;
            }
        });

        startGameLoop();
    }

    Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            try {
                updateScore(gameView.player.score);
            } finally {
                mHandler.postDelayed(gameLoop, 1000/60);
            }
        }
    };

    void startGameLoop() {
        gameLoop.run();
    }

    public static void updateScore(int score) {
        tScore.setText(Integer.toString(score));
    }
}
