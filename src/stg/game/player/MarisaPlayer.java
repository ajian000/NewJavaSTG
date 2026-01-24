package stg.game.player;

import java.awt.*;
import stg.game.bullet.SimpleBullet;
import stg.game.ui.GameCanvas;

/**
 * 魔理沙自机类
 * 恋符-极限火花：集中火力高伤害
 */
public class MarisaPlayer extends Player {
	private static final float MARISA_SPEED = 5.0f;
	private static final float MARISA_SPEED_SLOW = 2.5f;
	private static final float MARISA_SIZE = 16f;
	private static final int MARISA_SHOOT_INTERVAL = 1;
	private static final Color MARISA_COLOR = new Color(255, 220, 100);
	private static final Color BULLET_COLOR = new Color(255, 255, 150);
	private static final int MARISA_BULLET_DAMAGE = 1; // @Time 2026-01-23 魔理沙子弹伤害，普通DPS = (3 × 1 × 60) / 2 = 90

	public MarisaPlayer(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		setSpeed(MARISA_SPEED);
		setSpeedSlow(MARISA_SPEED_SLOW);
		setSize(MARISA_SIZE);
		setShootInterval(MARISA_SHOOT_INTERVAL);
		this.bulletDamage = MARISA_BULLET_DAMAGE; // @Time 2026-01-23 设置魔理沙子弹伤害
	}

	@Override
	protected void shoot() {
		GameCanvas canvas = getGameCanvas();
		if (canvas == null) return;

		boolean slowMode = isSlowMode();
		float bulletSpeed = 46.0f;
		float bulletSize = slowMode ? 5.0f : 3.5f;

		// 魔理沙的特殊射击模式：集中火力
		if (slowMode) {
			// 低速模式：单发高伤害大弹
			SimpleBullet bullet = new SimpleBullet(getX(), getY(), 0, bulletSpeed, bulletSize + 2, BULLET_COLOR);
			bullet.setDamage(bulletDamage * 4); // @Time 2026-01-23 低速模式伤害×4，DPS = (1 × 4 × 60) / 2 = 120
			bullet.setGameCanvas(canvas);
			canvas.addBullet(bullet);
		} else {
			// 普通模式：3发集中弹
			for (int i = -1; i <= 1; i++) {
				float vx = i * 1.0f;
				SimpleBullet bullet = new SimpleBullet(getX(), getY(), vx, bulletSpeed, bulletSize, BULLET_COLOR);
				bullet.setDamage(bulletDamage); // @Time 2026-01-23 设置魔理沙子弹伤害
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

		if (isSlowMode() && shouldRender) {
			g.setColor(Color.WHITE);
			g.fillOval((int)(screenX - getHitboxRadius()), (int)(screenY - getHitboxRadius()),
			          (int)(getHitboxRadius() * 2), (int)(getHitboxRadius() * 2));
		}

		// 魔理沙外观：金黄色球体
		if (shouldRender) {
			g.setColor(MARISA_COLOR);
			g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
			          (int)(getSize() * 2), (int)(getSize() * 2));

			// 添加魔理沙的帽子装饰
			g.setColor(new Color(50, 50, 50));
			g.fillRect((int)(screenX - getSize() + 4), (int)(screenY - getSize() - 3),
			          (int)(getSize() * 2 - 8), 5);
		}
	}
}
