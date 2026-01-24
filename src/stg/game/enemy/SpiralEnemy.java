package stg.game.enemy;

import java.awt.*;
import stg.game.bullet.SpiralBullet;
import stg.game.ui.GameCanvas;

/**
 * 螺旋弹幕敌人 - 发射螺旋前进的子弹
 * @Time 2026-01-23
 */
public class SpiralEnemy extends Enemy {
	private float shootTimer;
	private float shootInterval;
	private float moveSpeed;
	private float spiralAngle;

	public SpiralEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 25, Color.CYAN, 150, gameCanvas);
		this.shootTimer = 0;
		this.shootInterval = 120;
		this.moveSpeed = moveSpeed;
		this.spiralAngle = 0;
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
			shootSpiral();
			shootTimer = 0;
		}
	}

	private void shootSpiral() {
		if (!alive || gameCanvas == null) return;

		float bulletSpeed = 6.0f;
		float baseAngle = (float)(Math.PI / 2);
		float radius = 30.0f;
		float angleSpeed = 0.15f;

		for (int i = 0; i < 3; i++) {
			float angleOffset = (float)(i * 2 * Math.PI / 3);
			SpiralBullet bullet = new SpiralBullet(
				x, y, bulletSpeed, baseAngle,
				radius, angleSpeed, 5.0f, Color.CYAN
			);
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

		g.setColor(color);
		g.fillRect((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		g.setColor(Color.WHITE);
		g.drawRect((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		renderHealthBar(g, screenX, screenY);
	}
}
