package stg.game;

import stg.game.bullet.Bullet;
import java.awt.*;

/**
 * 妖梦自机类
 * 人鬼剑-楼观剑：快速连斩
 */
public class YoumuPlayer extends Player {
	private static final float YOUMU_SPEED = 5.5f;
	private static final float YOUMU_SPEED_SLOW = 2.8f;
	private static final float YOUMU_SIZE = 19f;
	private static final int YOUMU_SHOOT_INTERVAL = 4;
	private static final Color YOUMU_COLOR = new Color(150, 150, 200);
	private static final Color SWORD_COLOR = new Color(200, 200, 255);

	public YoumuPlayer(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		setSpeed(YOUMU_SPEED);
		setSpeedSlow(YOUMU_SPEED_SLOW);
		setSize(YOUMU_SIZE);
		setShootInterval(YOUMU_SHOOT_INTERVAL);
	}

	@Override
	protected void shoot() {
		GameCanvas canvas = getGameCanvas();
		if (canvas == null) return;

		boolean slowMode = isSlowMode();
		float swordSpeed = 12.0f;
		float swordSize = slowMode ? 5.5f : 4.0f;

		// 妖梦的特殊射击模式：快速剑气
		Bullet[] swords = new Bullet[slowMode ? 4 : 2];
		float spread = 0.25f;

		for (int i = 0; i < swords.length; i++) {
			float angle = (i - (swords.length - 1) / 2.0f) * spread;
			float vx = (float)Math.sin(angle) * swordSpeed;
			float vy = (float)Math.cos(angle) * swordSpeed;
			swords[i] = new Bullet(getX(), getY(), vx, vy, swordSize, SWORD_COLOR);
		}

		for (Bullet sword : swords) {
			sword.setGameCanvas(canvas);
			canvas.addBullet(sword);
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

		// 妖梦外观：淡紫色球体
		g.setColor(YOUMU_COLOR);
		g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
		          (int)(getSize() * 2), (int)(getSize() * 2));

		// 妖梦的发饰
		g.setColor(new Color(255, 200, 200));
		g.fillRect((int)(screenX - getSize() + 3), (int)(screenY - getSize() - 1),
		          (int)(getSize() * 2 - 6), 3);
	}
}
