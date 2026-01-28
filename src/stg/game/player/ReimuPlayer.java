package stg.game.player;

import java.awt.*;
import stg.game.bullet.SimpleBullet;
import stg.game.ui.GameCanvas;

/**
 * 灵梦自机类
 * 灵符-梦想封印：高火力广域攻击
 */
public class ReimuPlayer extends Player {
	private static final float REIMU_SPEED = 4.5f;
	private static final float REIMU_SPEED_SLOW = 2.0f;
	private static final float REIMU_SIZE = 18f;
	private static final int REIMU_SHOOT_INTERVAL = 1;
	private static final Color REIMU_COLOR = new Color(255, 200, 220);
	private static final Color BULLET_COLOR = new Color(255, 150, 200);
	private static final int REIMU_BULLET_DAMAGE = 1; // @Time 2026-01-23 灵梦子弹伤害，普通DPS = (5 × 1 × 60) / 3 = 100

	public ReimuPlayer(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		setSpeed(REIMU_SPEED);
		setSpeedSlow(REIMU_SPEED_SLOW);
		setSize(REIMU_SIZE);
		setShootInterval(REIMU_SHOOT_INTERVAL);
		this.bulletDamage = REIMU_BULLET_DAMAGE; // @Time 2026-01-23 设置灵梦子弹伤害
	}

	/**
	 * 初始化灵梦的子机
	 * 参考东方正作，灵梦通常有2-4个子机
	 */
	public void initializeOptions(GameCanvas canvas) {
		setGameCanvas(canvas);

		// 添加2个子机，位于玩家左右两侧
		ReimuOption option1 = new ReimuOption(this, -25, 10, canvas);
		ReimuOption option2 = new ReimuOption(this, 25, 10, canvas);

		addOption(option1);
		addOption(option2);

		System.out.println("灵梦子机初始化完成，共2个子机");
	}

	@Override
	protected void shoot() {
		GameCanvas canvas = getGameCanvas();
		if (canvas == null) return;

		boolean slowMode = isSlowMode();
		float bulletSpeed = 46.0f;
		float bulletSize = slowMode ? 5.0f : 3.0f;

		// 灵梦的特殊射击模式：主弹 + 追踪弹
		// 主弹：5发散弹
		float spreadAngle = 0.15f;
		SimpleBullet[] bullets = new SimpleBullet[5];
		for (int i = -2; i <= 2; i++) {
			float angle = i * spreadAngle;
			float vx = (float)Math.sin(angle) * bulletSpeed;
			float vy = bulletSpeed;
			bullets[i + 2] = new SimpleBullet(getX(), getY(), vx, vy, bulletSize, BULLET_COLOR);
			// @Time 2026-01-23 设置灵梦子弹伤害
			bullets[i + 2].setDamage(bulletDamage);
		}

		for (SimpleBullet bullet : bullets) {
			bullet.setGameCanvas(canvas);
			canvas.addBullet(bullet);
		}

		// 低速模式下额外发射追踪弹
		if (slowMode) {
			float trackingBulletSpeed = 46.0f;
			SimpleBullet trackingBullet = new SimpleBullet(getX(), getY(), 0, trackingBulletSpeed, 4.0f, new Color(255, 255, 200));
			trackingBullet.setDamage(bulletDamage); // @Time 2026-01-23 设置追踪弹伤害
			trackingBullet.setGameCanvas(canvas);
			canvas.addBullet(trackingBullet);
		}
	}

	@Override
	public void render(Graphics2D g) {
		float screenX = getX() + getGameCanvas().getWidth() / 2.0f;
		float screenY = getGameCanvas().getHeight() / 2.0f - getY();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// @Time 2026-01-23 无敌闪烁效果：每5帧闪烁一次
		boolean shouldRender = true;
		if (isInvincible()) {
			// 通过反射获取父类的invincibleTimer（需要Player提供getter）
			int invincibleTimer = getInvincibleTimer();
			int flashPhase = invincibleTimer % 10;
			if (flashPhase < 5) {
				shouldRender = false;
			}
		}

		// 低速模式显示判定点
		if (isSlowMode() && shouldRender) {
			g.setColor(Color.WHITE);
			g.fillOval((int)(screenX - getHitboxRadius()), (int)(screenY - getHitboxRadius()),
			          (int)(getHitboxRadius() * 2), (int)(getHitboxRadius() * 2));
		}

		// 灵梦外观：粉红色球体
		if (shouldRender) {
			g.setColor(REIMU_COLOR);
			g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
			          (int)(getSize() * 2), (int)(getSize() * 2));

			// 添加灵梦标志性的红色头带
			g.setColor(new Color(220, 50, 50));
			g.fillRect((int)(screenX - getSize() + 2), (int)(screenY - getSize() - 2),
			          (int)(getSize() * 2 - 4), 4);
		}
	}
}
