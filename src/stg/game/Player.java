package stg.game;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Player {
	private float x;
	private float y;
	private float vx;
	private float vy;
	private float speed;
	private float speedSlow;
	private float size;
	private GameCanvas gameCanvas;

	private boolean slowMode;
	private boolean shooting;
	private boolean bombing;

	public Player(float spawnX, float spawnY) {
		this.x = spawnX;
		this.y = spawnY;
		this.vx = 0;
		this.vy = 0;
		this.speed = 5.0f;
		this.speedSlow = 2.0f;
		this.size = 20;
		this.slowMode = false;
		this.shooting = false;
		this.bombing = false;
	}

	public void update() {
		x += vx;
		y += vy;

		int canvasWidth = gameCanvas.getWidth();
		int canvasHeight = gameCanvas.getHeight();

		if (x < size) x = size;
		if (x > canvasWidth - size) x = canvasWidth - size;
		if (y < size) y = size;
		if (y > canvasHeight - size) y = canvasHeight - size;
	}

	public void render(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillOval((int)(x - size), (int)(y - size), (int)(size * 2), (int)(size * 2));

		g.setColor(Color.WHITE);
		g.fillRect((int)(x - size), (int)(y - size), (int)(size * 2), 4);

		if (slowMode) {
			g.setColor(Color.YELLOW);
			g.fillOval((int)(x - 3), (int)(y - 3), 6, 6);
		}
	}

	public void moveUp() {
		vy = slowMode ? -speedSlow : -speed;
	}

	public void moveDown() {
		vy = slowMode ? speedSlow : speed;
	}

	public void moveLeft() {
		vx = slowMode ? -speedSlow : -speed;
	}

	public void moveRight() {
		vx = slowMode ? speedSlow : speed;
	}

	public void stopVertical() {
		vy = 0;
	}

	public void stopHorizontal() {
		vx = 0;
	}

	public void setSlowMode(boolean slow) {
		this.slowMode = slow;
	}

	public void setGameCanvas(GameCanvas canvas) {
		this.gameCanvas = canvas;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getSize() {
		return size;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getSpeed() {
		return speed;
	}

	public float getSpeedSlow() {
		return speedSlow;
	}

	public boolean isSlowMode() {
		return slowMode;
	}
}
