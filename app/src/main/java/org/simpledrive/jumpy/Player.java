package org.simpledrive.jumpy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;

public class Player extends Entity {
	public float GRAVITY = 400f;
	public int score = 0;
	public float speedX = 200f;
	public float speedY = 0f;
	private boolean jumpAvailable = false;
	private boolean falling = false;
	public boolean movingRight, movingLeft, movingUp = false;
	private Paint paint;
	private boolean buttonPressed = false;
	private int jumpCount = 0;
	private float JUMPMAX = 300f;
	private boolean canJump = true;
	private boolean killjumping = false;
	private MediaPlayer jumpSound;

	public Player(Context ctx) {
		super(0, 0, 40, 40, "player", 0, true);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLACK);
		jumpSound = MediaPlayer.create(ctx, R.raw.jumpsound);
	}

	@Override
	public void update(float dt) {
		if (movingLeft && !collision.get("left")) {
			x -= speedX * dt;
		}
		if (movingRight && !collision.get("right")) {
			x += speedX * dt;
		}
		if (collision.get("bottom")) {
			this.falling = false;
			if (!this.buttonPressed) {
				this.jumpAvailable = true;
			}
		}

		if (collision.get("top")) {
			this.movingUp = false;
		}

		if ((this.movingUp || this.killjumping) && !this.collision.get("top")) {
			this.y -= this.speedY * dt;
			this.speedY -= this.GRAVITY* dt;
			if (this.speedY <= 0) {
				this.speedY = 0;
				this.movingUp = false;
				this.killjumping = false;
			}
			if (this.killjumping || this.jumpCount > 1) {
				this.jumpAvailable = false;
				this.jumpCount = 0;
			}
		}
		else if (!this.collision.get("bottom")) {
			this.y += this.speedY * dt;
			this.speedY += (this.buttonPressed) ? (this.GRAVITY/ 3) * dt * 2 : this.GRAVITY* dt * 2;
		}

		this.resetCollisions();
	}

	public void jump(boolean value) {
		this.buttonPressed = value;
		if(value && this.jumpAvailable && this.canJump) {
			this.jumpCount++;
			this.movingUp = true;
			this.speedY = this.JUMPMAX;
			jumpSound.seekTo(0);
			jumpSound.start();
			this.canJump = false;
		}
		else if (!value) {
			this.movingUp = false;
			this.falling = true;
			this.speedY = 0;
			this.canJump = true;
		}
	}

	public void addScore() {
		this.score += this.speedX;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, paint);
	}
}
