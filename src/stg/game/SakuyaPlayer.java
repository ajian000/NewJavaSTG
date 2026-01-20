package stg.game;

import stg.game.bullet.Bullet;
import java.awt.*;

/**
 * 咲夜自机类
 * 幻符-飞散：精准投掷多把小刀
 */
public class SakuyaPlayer extends Player {
	private static final float SAKUYA_SPEED = 5.0f;
	private static final float SAKUYA_SPEED_SLOW = 2.2f;
	private static final float SAKUYA_SIZE = 17f;
	private static final int SAKUYA_SHOOT_INTERVAL = 6;
	private static final Color SAKUYA_COLOR = new Color(200, 200, 255);
	private static final Color KNIFE_COLOR = new Color(255, 255, 255);

	public SakuyaPlayer(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		setSpeed(SAKUYA_SPEED);
		setSpeedSlow(SAKUYA_SPEED_SLOW);
		setSize(SAKUYA_SIZE);
		setShootInterval(SAKUYA_SHOOT_INTERVAL);
	}

	@Override
	protected void shoot() {
		GameCanvas canvas = getGameCanvas();
		if (canvas == null) return;

		boolean slowMode = isSlowMode();
		float knifeSpeed = 10.0f;
		float knifeSize = slowMode ? 4.5f : 3.0f;

		// 咲夜的特殊射击模式：飞刀
		int knifeCount = slowMode ? 5 : 3;
		float spreadAngle = 0.2f;

		for (int i = 0; i < knifeCount; i++) {
			float angleOffset = (i - (knifeCount - 1) / 2.0f) * spreadAngle;
			float vx = (float)Math.sin(angleOffset) * knifeSpeed;
			float vy = (float)Math.cos(angleOffset) * knifeSpeed;

			Bullet knife = new Bullet(getX(), getY(), vx, vy, knifeSize, KNIFE_COLOR);
			knife.setGameCanvas(canvas);
			canvas.addBullet(knife);
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

		// 咲夜外观：淡蓝色球体
		g.setColor(SAKUYA_COLOR);
		g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
		          (int)(getSize() * 2), (int)(getSize() * 2));

		// 咲夜的蝴蝶结装饰
		g.setColor(new Color(255, 255, 255));
		int bowSize = 5;
		g.fillOval((int)(screenX - getSize() - 1), (int)(screenY - getSize() + 2),
		          bowSize, bowSize);
		g.fillOval((int)(screenX + getSize() - bowSize + 1), (int)(screenY - getSize() + 2),
		          bowSize, bowSize);
	}
}
