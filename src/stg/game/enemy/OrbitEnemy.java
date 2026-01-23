package stg.game.enemy;

import stg.game.ui.GameCanvas;
import stg.game.bullet.CircularBullet;
import java.awt.*;

/**
 * 轨道敌人 - 围绕中心点旋转移动
 * @Time 2026-01-23
 */
public class OrbitEnemy extends Enemy {
	private float centerX;
	private float centerY;
	private float orbitRadius;
	private float orbitAngle;
	private float orbitSpeed;
	private float shootTimer;
	private float shootInterval;

	public OrbitEnemy(float centerX, float centerY, float orbitRadius, float orbitSpeed, GameCanvas gameCanvas) {
		super(0, 0, 0, 0, 24, Color.GREEN, 160, gameCanvas);
		this.centerX = centerX;
		this.centerY = centerY;
		this.orbitRadius = orbitRadius;
		this.orbitAngle = 0;
		this.orbitSpeed = orbitSpeed;
		this.shootTimer = 0;
		this.shootInterval = 100;

		updatePosition();
	}

	@Override
	public void update() {
		super.update();

		if (!alive) return;

		orbitAngle += orbitSpeed;
		updatePosition();

		shootTimer++;

		if (shootTimer >= shootInterval) {
			shootOrbit();
			shootTimer = 0;
		}
	}

	private void updatePosition() {
		x = centerX + (float)Math.cos(orbitAngle) * orbitRadius;
		y = centerY + (float)Math.sin(orbitAngle) * orbitRadius;
	}

	private void shootOrbit() {
		if (!alive || gameCanvas == null) return;

		float bulletSpeed = -7.0f;
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
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		g.setColor(Color.WHITE);
		g.drawOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		renderHealthBar(g, screenX, screenY);
	}
}
