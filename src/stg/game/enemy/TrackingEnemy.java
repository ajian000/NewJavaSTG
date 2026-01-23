package stg.game.enemy;

import stg.game.ui.GameCanvas;
import stg.game.bullet.TrackingBullet;
import java.awt.*;

/**
 * 追踪弹幕敌人 - 发射追踪玩家的子弹
 * @Time 2026-01-23
 */
public class TrackingEnemy extends Enemy {
	private float shootTimer;
	private float shootInterval;
	private float moveSpeed;

	public TrackingEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 20, Color.PINK, 200, gameCanvas);
		this.shootTimer = 0;
		this.shootInterval = 150;
		this.moveSpeed = moveSpeed;
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

		if (shootTimer >= shootInterval) {
			shootTracking();
			shootTimer = 0;
		}
	}

	private void shootTracking() {
		if (!alive || gameCanvas == null) return;

		float bulletSpeed = 5.0f;
		float initialAngle = (float)(Math.PI / 2);
		float turnSpeed = 0.03f;

		TrackingBullet bullet = new TrackingBullet(
			x, y, bulletSpeed, initialAngle, turnSpeed, 6.0f, Color.PINK
		);
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
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		g.setColor(Color.WHITE);
		g.drawOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		renderHealthBar(g, screenX, screenY);
	}
}
