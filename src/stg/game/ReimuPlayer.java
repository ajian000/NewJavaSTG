package stg.game;

import stg.game.bullet.Bullet;
import java.awt.*;

/**
 * 灵梦自机类
 * 灵符-梦想封印：高火力广域攻击
 */
public class ReimuPlayer extends Player {
	private static final float REIMU_SPEED = 4.5f;
	private static final float REIMU_SPEED_SLOW = 2.0f;
	private static final float REIMU_SIZE = 18f;
	private static final int REIMU_SHOOT_INTERVAL = 7;
	private static final Color REIMU_COLOR = new Color(255, 200, 220);
	private static final Color BULLET_COLOR = new Color(255, 150, 200);

	public ReimuPlayer(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		setSpeed(REIMU_SPEED);
		setSpeedSlow(REIMU_SPEED_SLOW);
		setSize(REIMU_SIZE);
		setShootInterval(REIMU_SHOOT_INTERVAL);
	}

	@Override
	protected void shoot() {
		GameCanvas canvas = getGameCanvas();
		if (canvas == null) return;

		boolean slowMode = isSlowMode();
		float bulletSpeed = 9.0f;
		float bulletSize = slowMode ? 5.0f : 3.0f;

		// 灵梦的特殊射击模式：主弹 + 追踪弹
		// 主弹：5发散弹
		float spreadAngle = 0.15f;
		Bullet[] bullets = new Bullet[5];
		for (int i = -2; i <= 2; i++) {
			float angle = i * spreadAngle;
			float vx = (float)Math.sin(angle) * bulletSpeed;
			float vy = bulletSpeed;
			bullets[i + 2] = new Bullet(getX(), getY(), vx, vy, bulletSize, BULLET_COLOR);
		}

		for (Bullet bullet : bullets) {
			bullet.setGameCanvas(canvas);
			canvas.addBullet(bullet);
		}

		// 低速模式下额外发射追踪弹
		if (slowMode) {
			float trackingBulletSpeed = 7.0f;
			Bullet trackingBullet = new Bullet(getX(), getY(), 0, trackingBulletSpeed, 4.0f, new Color(255, 255, 200));
			trackingBullet.setGameCanvas(canvas);
			canvas.addBullet(trackingBullet);
		}
	}

	@Override
	public void render(Graphics2D g) {
		float screenX = getX() + getGameCanvas().getWidth() / 2.0f;
		float screenY = getGameCanvas().getHeight() / 2.0f - getY();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 低速模式显示判定点
		if (isSlowMode()) {
			g.setColor(Color.WHITE);
			g.fillOval((int)(screenX - getHitboxRadius()), (int)(screenY - getHitboxRadius()),
			          (int)(getHitboxRadius() * 2), (int)(getHitboxRadius() * 2));
		}

		// 灵梦外观：粉红色球体
		g.setColor(REIMU_COLOR);
		g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
		          (int)(getSize() * 2), (int)(getSize() * 2));

		// 添加灵梦标志性的红色头带
		g.setColor(new Color(220, 50, 50));
		g.fillRect((int)(screenX - getSize() + 2), (int)(screenY - getSize() - 2),
		          (int)(getSize() * 2 - 4), 4);
	}

}
