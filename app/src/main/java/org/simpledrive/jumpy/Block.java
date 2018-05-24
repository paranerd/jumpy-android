package org.simpledrive.jumpy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Block extends Entity {
	public boolean visible;
	public float speed = 200f;
	private int spriteHeight;
	private int spriteWidth;
	private Paint paint;
	
	public Block(int a, int x, int y, int h, int w, int sH, int sW) {
		super(x, y, w, h, "block", a, true);
		this.visible = true;
		this.spriteHeight = sH;
		this.spriteWidth = sW;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLUE);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, paint);
		/* For a spritesheet:
		for (int i = 0; i < this.height; i += this.spriteHeight) {
			for (int j = 0; j < this.width; j += this.spriteWidth) {
				canvas.drawRect(...)
			}
		}
		 */
	}

	@Override
	public void update(float dt) {
		this.x -= this.speed * dt;
	}
}
