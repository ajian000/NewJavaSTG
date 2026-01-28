package stg.game.player;

import java.awt.*;
import stg.game.bullet.CircularBullet;
import stg.game.bullet.SimpleBullet;
import stg.game.ui.GameCanvas;

/**
 * 魔理沙子机类
 * 特点：发射高威力集中弹，激光攻击
 */
public class MarisaOption extends Option {
	private static final float MARISA_OPTION_SIZE = 10.0f;
	private static final Color MARISA_OPTION_COLOR = new Color(180, 200, 255);
	private static final int MARISA_OPTION_SHOOT_INTERVAL = 3;
	private static final int MARISA_OPTION_BULLET_DAMAGE = 2;
	private static final float BULLET_SPEED = 52.0f;
	private static final float BULLET_SIZE = 4.0f;
	private static final Color BULLET_COLOR = new Color(100, 200, 255);
	private static final int LASER_INTERVAL = 15; // 激光发射间隔
	private int laserCooldown; // 激光冷却

	public MarisaOption(Player player, float offsetX, float offsetY, GameCanvas gameCanvas) {
		super(player, offsetX, offsetY, gameCanvas);
		setSize(MARISA_OPTION_SIZE);
		setColor(MARISA_OPTION_COLOR);
		setShootInterval(MARISA_OPTION_SHOOT_INTERVAL);
		setBulletDamage(MARISA_OPTION_BULLET_DAMAGE);
		setFollowSpeed(0.18f);
		this.laserCooldown = 0;
	}

	@Override
	public void update() {
		super.update();

		// 更新激光冷却
		if (laserCooldown > 0) {
			laserCooldown--;
		}
	}

	@Override
	protected void shoot() {
		if (gameCanvas == null) return;

		// 发射高威力集中弹
		SimpleBullet bullet = new SimpleBullet(x, y, 0, BULLET_SPEED, BULLET_SIZE, BULLET_COLOR);
		bullet.setGameCanvas(gameCanvas);
		bullet.setDamage(bulletDamage);
		gameCanvas.addBullet(bullet);

		// 定期发射圆形弹幕（增加攻击范围）
		if (laserCooldown == 0) {
			shootCircularSpread();
			laserCooldown = LASER_INTERVAL;
		}
	}

	/**
	 * 发射圆形扩散弹幕
	 */
	private void shootCircularSpread() {
		int bulletCount = 8;
		float spreadAngle = (float)(2 * Math.PI / bulletCount);
		float bulletSpeed = 40.0f;
		float bulletSize = 3.0f;

		for (int i = 0; i < bulletCount; i++) {
			float angle = i * spreadAngle;
			float vx = (float)Math.sin(angle) * bulletSpeed;
			float vy = (float)Math.cos(angle) * bulletSpeed;

			CircularBullet bullet = new CircularBullet(x, y, vx, vy, bulletSize, BULLET_COLOR, Color.WHITE, bulletDamage - 1);
			bullet.setGameCanvas(gameCanvas);
			gameCanvas.addBullet(bullet);
		}
	}

	@Override
	public void render(Graphics2D g) {
		float screenX = x + gameCanvas.getWidth() / 2.0f;
		float screenY = gameCanvas.getHeight() / 2.0f - y;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 绘制魔理沙子机主体（蓝色）
		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size),
		          (int)(size * 2), (int)(size * 2));

		// 绘制魔理沙标志性的金色边框
		g.setColor(new Color(255, 215, 0));
		g.drawOval((int)(screenX - size), (int)(screenY - size),
		          (int)(size * 2), (int)(size * 2));

		// 绘制子机核心（白色亮点）
		g.setColor(new Color(255, 255, 255, 220));
		g.fillOval((int)(screenX - size * 0.5f), (int)(screenY - size * 0.5f),
		          (int)(size), (int)(size));

		// 绘制魔理沙的星星图案（简化版）
		g.setColor(new Color(255, 255, 255, 180));
		int starSize = (int)(size * 0.3f);
		g.fillOval((int)(screenX - starSize), (int)(screenY - starSize),
		          starSize * 2, starSize * 2);
	}

	@Override
	public void reset() {
		super.reset();
		laserCooldown = 0;
	}
}
