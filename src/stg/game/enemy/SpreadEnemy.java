package stg.game.enemy;

import java.awt.*;
import stg.game.bullet.CircularBullet;
import stg.game.ui.GameCanvas;

/**
 * 扇形弹幕敌人 - 发射扇形散开的子弹
 * @Time 2026-01-23
 */
public class SpreadEnemy extends Enemy {
	private float shootTimer;
	private float shootInterval;
	private float moveSpeed;

	public SpreadEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 22, Color.ORANGE, 180, gameCanvas);
		this.shootTimer = 0;
		this.shootInterval = 90;
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
			shootSpread();
			shootTimer = 0;
		}
	}

	private void shootSpread() {
		if (!alive || gameCanvas == null) return;

		float bulletSpeed = -8.0f;
		int bulletCount = 7;
		float spreadAngle = 0.3f;

		for (int i = 0; i < bulletCount; i++) {
			float angleOffset = (i - bulletCount / 2) * spreadAngle;
			float vx = (float)Math.sin(angleOffset) * bulletSpeed;
			float vy = bulletSpeed;

			CircularBullet bullet = new CircularBullet(x, y, vx, vy);
			bullet.setGameCanvas(gameCanvas);
			gameCanvas.addEnemyBullet(bullet);
		}
	}

	@Override
	public void render(Graphics2D g) {
		int canvasWidth = gameCanvas != null ? gameCanvas.getWidth() : 548;
		int canvasHeight = gameCanvas != null ? gameCanvas.getHeight() : 921;
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;

		int[] xPoints = {
			(int)screenX,
			(int)(screenX - size * 1.2),
			(int)(screenX + size * 1.2)
		};
		int[] yPoints = {
			(int)(screenY - size),
			(int)(screenY + size),
			(int)(screenY + size)
		};

		g.setColor(color);
		g.fillPolygon(xPoints, yPoints, 3);

		g.setColor(Color.WHITE);
		g.drawPolygon(xPoints, yPoints, 3);

		renderHealthBar(g, screenX, screenY);
	}
}
