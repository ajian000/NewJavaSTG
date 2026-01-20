package stg.game.player;

import stg.game.bullet.Bullet;
import stg.game.GameCanvas;
import java.awt.*;

/**
 * 魔理沙自机类
 * 恋符-极限火花：集中火力高伤害
 */
public class MarisaPlayer extends Player {
	private static final float MARISA_SPEED = 5.0f;
	private static final float MARISA_SPEED_SLOW = 2.5f;
	private static final float MARISA_SIZE = 16f;
	private static final int MARISA_SHOOT_INTERVAL = 5;
	private static final Color MARISA_COLOR = new Color(255, 220, 100);
	private static final Color BULLET_COLOR = new Color(255, 255, 150);

	public MarisaPlayer(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		setSpeed(MARISA_SPEED);
		setSpeedSlow(MARISA_SPEED_SLOW);
		setSize(MARISA_SIZE);
		setShootInterval(MARISA_SHOOT_INTERVAL);
	}

	@Override
	protected void shoot() {
		GameCanvas canvas = getGameCanvas();
		if (canvas == null) return;

		boolean slowMode = isSlowMode();
		float bulletSpeed = 28.0f; // 进一步提高弹速
		float bulletSize = slowMode ? 5.0f : 3.5f;

		// 魔理沙的特殊射击模式：集中火力
		if (slowMode) {
			// 低速模式：单发高伤害大弹
			Bullet bullet = new Bullet(getX(), getY(), 0, bulletSpeed, bulletSize + 2, BULLET_COLOR);
			bullet.setGameCanvas(canvas);
			canvas.addBullet(bullet);
		} else {
			// 普通模式：3发集中弹
			for (int i = -1; i <= 1; i++) {
				float vx = i * 1.0f;
				Bullet bullet = new Bullet(getX(), getY(), vx, bulletSpeed, bulletSize, BULLET_COLOR);
				bullet.setGameCanvas(canvas);
				canvas.addBullet(bullet);
			}
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

		// 魔理沙外观：金黄色球体
		g.setColor(MARISA_COLOR);
		g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
		          (int)(getSize() * 2), (int)(getSize() * 2));

		// 添加魔理沙的帽子装饰
		g.setColor(new Color(50, 50, 50));
		g.fillRect((int)(screenX - getSize() + 4), (int)(screenY - getSize() - 3),
		          (int)(getSize() * 2 - 8), 5);
	}
}
