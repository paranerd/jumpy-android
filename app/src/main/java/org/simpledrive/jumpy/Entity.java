package org.simpledrive.jumpy;

import android.graphics.Canvas;

import java.util.HashMap;

public abstract class Entity {
    public float x;
    public float y;
    public int width;
    public int height;
    public String type;
    public HashMap<String, Boolean> collision = new HashMap<>();
    public boolean visible;
    public int art;

    public Entity(float x, float y, int width, int height, String type, int art, boolean visible) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.art = art;
        this.width = width;
        this.height = height;
        this.visible = visible;

        resetCollisions();
    }

    public abstract void draw(Canvas canvas);

    public abstract void update(float dt);

    public boolean is(String compare) {
        return this.type.equals(compare);
    }

    public void resetCollisions() {
        this.collision.put("left", false);
        this.collision.put("right", false);
        this.collision.put("top", false);
        this.collision.put("bottom", false);
    }
}
