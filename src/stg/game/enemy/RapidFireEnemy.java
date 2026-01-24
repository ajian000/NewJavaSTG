package stg.game.enemy;

import java.awt.*;
import stg.game.bullet.CircularBullet;
import stg.game.ui.GameCanvas;

/**
 * 快速射击敌人 - 高频率发射子弹
 * @Time 2026-01-23
 */
public class RapidFireEnemy extends Enemy {
	private float shootTimer;
	private float shootInterval;
	private float moveSpeed;
	private int burstCount;
	private int currentBurst;
	private float burstDelay;

	public RapidFireEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 18, Color.YELLOW, 100, gameCanvas);
		this.shootTimer = 0;
		this.shootInterval = 180;
		this.moveSpeed = moveSpeed;
		this.burstCount = 5;
		this.currentBurst = 0;
		this.burstDelay = 8;
	}

	@Override
	public void update() {
		super.update();

		if (gameCanvas == null || !alive) return;

		int canvasWidth = gameCanvas.getWidth();
		float leftBound = -canvasWidth / 2.0f + size;
		float rightBound = canvasWidth / 2.0f - size;

		if (x <= leftBound) {
			vx = Math.abs(moveSpeed);
		} else if (x >= rightBound) {
			vx = -Math.abs(moveSpeed);
		}

		shootTimer++;

		if (currentBurst > 0) {
			if (shootTimer >= burstDelay) {
				shootSingle();
				currentBurst--;
				shootTimer = 0;
			}
		} else if (shootTimer >= shootInterval) {
			currentBurst = burstCount;
			shootTimer = 0;
		}
	}

	private void shootSingle() {
		if (!alive || gameCanvas == null) return;

		float bulletSpeed = -10.0f;
		CircularBullet bullet = new CircularBullet(x, y, 0, bulletSpeed);
		bullet.setGameCanvas(gameCanvas);
		gameCanvas.addEnemyBullet(bullet);
	}

	@Override
	public void render(Graphics2D g) {
		int canvasWidth = gameCanvas != null ? gameCanvas.getWidth() : 548;
		int canvasHeight = gameCanvas != null ? gameCanvas.getHeight() : 921;
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;

		g.setColor(color);
		g.fillRect((int)(screenX - size * 0.8f), (int)(screenY - size), (int)(size * 1.6f), (int)(size * 2));

		g.setColor(Color.WHITE);
		g.drawRect((int)(screenX - size * 0.8f), (int)(screenY - size), (int)(size * 1.6f), (int)(size * 2));

		renderHealthBar(g, screenX, screenY);
	}
}
