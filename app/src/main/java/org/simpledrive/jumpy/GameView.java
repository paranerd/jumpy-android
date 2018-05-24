package org.simpledrive.jumpy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GameView extends SurfaceView implements View.OnTouchListener {
    private GameThread gameThread;
    private Context ctx;
    private int canvasHeight;
    private int canvasWidth;

    private Bitmap background;
    private float bgPos = 0; // Position of Background
    private float bgPos2 = 0;
    public Player player;
    private MediaPlayer backgroundSound;

    public ArrayList<Entity> allEntities = new ArrayList<>();

    public GameView(Context context) {
        super(context);
        ctx = context;
        init();
    }

    public GameView(Context context, AttributeSet attribs){
        super(context, attribs);
        ctx = context;
        init();
    }

    public GameView(Context context, AttributeSet attribs, int defStyle) {
        super(context, attribs, defStyle);
        ctx = context;
        init();
    }

    public void init() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        canvasHeight = dm.heightPixels;
        canvasWidth = dm.widthPixels;

        player = new Player(ctx);
        allEntities.add(player);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.cloudbg);
        //backgroundSound = MediaPlayer.create(ctx, R.raw.erasurealways);
        //backgroundSound.start();
        bgPos2 = background.getWidth();

        // Init level
        int[][] map = Level.getMap();
        for (int[] aMap : map) {
            if (aMap[0] == 10) {
                // Create Enemy
            } else {
                Block block = new Block(aMap[0], aMap[1], aMap[2], aMap[3], aMap[4], aMap[5], aMap[6]);
                allEntities.add(block);
            }
        }

        setOnTouchListener(GameView.this);

        gameThread = new GameThread(this);
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameThread.setRunning(false);
                while (retry) {
                    try {
                        gameThread.join();
                        retry = false;
                        //backgroundSound.release();
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameThread.setRunning(true);
                gameThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
    }

    protected void doDraw(Canvas canvas) {
        // Draw Background
        canvas.drawBitmap(background, bgPos, 0, null);
        canvas.drawBitmap(background, bgPos2, 0, null);
        if(bgPos == -background.getWidth()) {
            bgPos = background.getWidth();
        }
        if(bgPos2 == -background.getWidth()) {
            bgPos2 = background.getWidth();
        }
        bgPos -= 1;
        bgPos2 -= 1;

        for (Entity e : allEntities) {
            e.draw(canvas);
        }
    }

    protected HashMap<String, Boolean> collision(Entity ent1, Entity ent2) {
        HashMap<String, Boolean> collision = new HashMap<>();

        if (    ent1.x + ent1.width >= ent2.x && ent1.x <= ent2.x + ent2.width &&
                ent1.y + ent1.height >= ent2.y && ent1.y <= ent2.y + ent2.height)
        {
            float bottom_diff = ent2.y + ent2.height - ent1.y;
            float top_diff = ent1.y + ent1.height - ent2.y;
            float left_diff = ent1.x + ent1.width - ent2.x;
            float right_diff = ent2.x + ent2.width - ent1.x;

            collision.put("bottom", top_diff < bottom_diff && top_diff < left_diff && top_diff < right_diff);
            collision.put("right", left_diff < right_diff && left_diff < top_diff && left_diff < bottom_diff);
            collision.put("left", right_diff < left_diff && right_diff < top_diff && right_diff < bottom_diff);
            collision.put("top", bottom_diff < top_diff && bottom_diff < left_diff && bottom_diff < right_diff);
        }
        return collision;
    }

    private void addRandomBlock() {
        int x = 1200;
        int width = 100;

        int[][] map =   {{4, x, 300, 10, 40, 40, 40},
                        {5, x + 40, 300, 10, width, 10, 40},
                        {6, x + 40 + width, 300, 10, 40, 40, 40}
        };

        for (int[] aMap : map) {
            if (aMap[0] == 10) {
                // Create Enemy
            } else {
                Block block = new Block(aMap[0], aMap[1], aMap[2], aMap[3], aMap[4], aMap[5], aMap[6]);
                allEntities.add(block);
            }
        }
    }

    protected void updatePhysics(float dt) {
        boolean add = false;
        Iterator<Entity> it = allEntities.iterator();

        while (it.hasNext()) {
            Entity e = it.next();
            if (e.is("block") && e.x <= -e.width) {
                if (e.art == 6) {
                    add = true;
                }
                it.remove();
            }
            else {
                e.update(dt);
            }
        }

        if (add) {
            addRandomBlock();
        }

        for (int i = 0; i < allEntities.size(); i++) {
            Entity entity1 = allEntities.get(i);

            // Collision against canvas
            if (entity1.is("player")) {
                if (entity1.y + entity1.height >= canvasHeight) {
                    entity1.collision.put("bottom", true);
                    entity1.y = canvasHeight - entity1.height;
                }
                if (entity1.x <= 0) {
                    entity1.collision.put("left", true);
                    entity1.x = 0;
                }
                if (entity1.x + entity1.width >= canvasWidth) {
                    entity1.collision.put("right", true);
                    entity1.x = canvasWidth - entity1.width;
                }
                if (entity1.y <= 0) {
                    entity1.collision.put("top", true);
                    entity1.y = 0;
                }
            }

            for (int j = 0; j < allEntities.size(); j++) {
                Entity entity2 = allEntities.get(j);
                HashMap<String, Boolean> coll = collision(entity1, entity2);

                if (coll.size() > 0 && entity1 != entity2) {
                    if (coll.get("bottom")) {
                        entity1.collision.put("bottom", true);
                        entity1.y = (!entity1.is("block")) ? entity2.y - entity1.height : entity1.y;
                    }
                    if(coll.get("top")) {
                        entity1.collision.put("top", !(entity1.is("player") && !entity2.visible));
                        entity1.y = (!entity1.is("block")) ? entity2.y + entity2.height : entity1.y;
                    }
                    if(coll.get("right")) {
                        entity1.collision.put("right", true);
                        entity1.x = (!entity1.is("block")) ? entity2.x - entity1.width : entity1.x;
                    }
                    if(coll.get("left")) {
                        entity1.collision.put("left", true);
                        entity1.x = (!entity1.is("block")) ? entity2.x + entity2.width : entity1.x;
                    }
                }
            }
        }

        player.addScore();
    }

    public void moveLeft(boolean value) {
        player.movingLeft = value;
    }

    public void moveRight(boolean value) {
        player.movingRight = value;
    }

    @Override
    public boolean onTouch(View view, MotionEvent me) {
        int action = me.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                player.jump(true);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                player.jump(false);
                break;
            }
        }

        return true;
    }
}