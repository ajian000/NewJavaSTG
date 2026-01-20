package stg.game;

import stg.game.bullet.Bullet;
import java.awt.*;

/**
 * 萃香自机类
 * 鬼符-百万鬼夜令：大范围弹幕
 */
public class SuikaPlayer extends Player {
	private static final float SUIKA_SPEED = 4.0f;
	private static final float SUIKA_SPEED_SLOW = 1.8f;
	private static final float SUIKA_SIZE = 22f;
	private static final int SUIKA_SHOOT_INTERVAL = 9;
	private static final Color SUIKA_COLOR = new Color(200, 100, 100);
	private static final Color DENSITY_COLOR = new Color(255, 150, 150);

	public SuikaPlayer(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		setSpeed(SUIKA_SPEED);
		setSpeedSlow(SUIKA_SPEED_SLOW);
		setSize(SUIKA_SIZE);
		setShootInterval(SUIKA_SHOOT_INTERVAL);
	}

	@Override
	protected void shoot() {
		GameCanvas canvas = getGameCanvas();
		if (canvas == null) return;

		boolean slowMode = isSlowMode();
		float densitySpeed = 8.0f;
		float densitySize = slowMode ? 7.0f : 5.0f;

		// 萃香的特殊射击模式：密度弹
		int densityCount = slowMode ? 8 : 5;
		float spreadAngle = 0.3f;

		for (int i = 0; i < densityCount; i++) {
			float angle = (i - (densityCount - 1) / 2.0f) * spreadAngle;
			float vx = (float)Math.sin(angle) * densitySpeed;
			float vy = (float)Math.cos(angle) * densitySpeed;

			Bullet density = new Bullet(getX(), getY(), vx, vy, densitySize, DENSITY_COLOR);
			density.setGameCanvas(canvas);
			canvas.addBullet(density);
		}
	}

	@Override
	public void render(Graphics2D g) {
		float screenX = getX() + getGameCanvas().getWidth() / 2.0f;
		float screenY = getGameCanvas().getHeight() / 2.0f - getY();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (isSlowMode()) {
			g.setColor(Color.WHITE);
			g.fillOval((int)(screenX - getHitboxRadius()), (int)(screenY - getHitboxRadius()),
			          (int)(getHitboxRadius() * 2), (int)(getHitboxRadius() * 2));
		}

		// 萃香外观：深红色球体
		g.setColor(SUIKA_COLOR);
		g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
		          (int)(getSize() * 2), (int)(getSize() * 2));

		// 萃香的角
		g.setColor(new Color(100, 50, 50));
		int hornSize = 6;
		g.fillPolygon(
			new int[]{(int)(screenX - getSize() + 2), (int)(screenX - getSize() + 6), (int)(screenX - getSize() + 4)},
			new int[]{(int)(screenY - getSize() + 1), (int)(screenY - getSize() - hornSize + 1), (int)(screenY - getSize() + 1)},
			3
		);
	}
}
