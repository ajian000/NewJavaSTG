package user.player;

import java.awt.*;
import user.bullet.SimpleBullet;
import user.bullet.CircularBullet;
import stg.game.ui.GameCanvas;

/**
 * 自定义子机示�?- 演示如何创建自定义子�? */
public class CustomOption extends Option {
	private int shootMode; // 射击模式
	private int modeTimer; // 模式计时�?	private static final int MODE_SWITCH_INTERVAL = 120; // 模式切换间隔�?秒）

	public CustomOption(Player player, float offsetX, float offsetY, GameCanvas gameCanvas) {
		super(player, offsetX, offsetY, gameCanvas);
		
		// 自定义配�?		setSize(12.0f);
		setColor(new Color(100, 255, 100));
		setShootInterval(2);
		setBulletDamage(2);
		setFollowSpeed(0.15f);
		
		this.shootMode = 0;
		this.modeTimer = 0;
	}

	@Override
	protected void shoot() {
		if (gameCanvas == null) return;

		// 更新模式计时�?		modeTimer++;
		if (modeTimer >= MODE_SWITCH_INTERVAL) {
			modeTimer = 0;
			shootMode = (shootMode + 1) % 3; // 循环切换模式
			System.out.println("子机切换到模�? " + shootMode);
		}

		// 根据模式发射不同类型的子�?		switch (shootMode) {
			case 0:
				shootMode0();
				break;
			case 1:
				shootMode1();
				break;
			case 2:
				shootMode2();
				break;
		}
	}

	/**
	 * 模式0：直线弹
	 */
	private void shootMode0() {
		SimpleBullet bullet = new SimpleBullet(x, y, 0, 50.0f, 4.0f, Color.GREEN);
		bullet.setGameCanvas(gameCanvas);
		bullet.setDamage(bulletDamage);
		gameCanvas.addBullet(bullet);
	}

	/**
	 * 模式1：三向弹
	 */
	private void shootMode1() {
		float bulletSpeed = 45.0f;
		float spreadAngle = 0.2f;

		for (int i = -1; i <= 1; i++) {
			float angle = i * spreadAngle;
			float vx = (float)Math.sin(angle) * bulletSpeed;
			float vy = bulletSpeed;

			SimpleBullet bullet = new SimpleBullet(x, y, vx, vy, 3.5f, Color.CYAN);
			bullet.setGameCanvas(gameCanvas);
			bullet.setDamage(bulletDamage);
			gameCanvas.addBullet(bullet);
		}
	}

	/**
	 * 模式2：圆形扩散弹
	 */
	private void shootMode2() {
		int bulletCount = 6;
		float spreadAngle = (float)(2 * Math.PI / bulletCount);
		float bulletSpeed = 40.0f;

		for (int i = 0; i < bulletCount; i++) {
			float angle = i * spreadAngle;
			float vx = (float)Math.sin(angle) * bulletSpeed;
			float vy = (float)Math.cos(angle) * bulletSpeed;

			CircularBullet bullet = new CircularBullet(x, y, vx, vy, 3.0f, Color.MAGENTA, Color.WHITE, bulletDamage - 1);
			bullet.setGameCanvas(gameCanvas);
			gameCanvas.addBullet(bullet);
		}
	}

	@Override
	public void render(Graphics2D g) {
		float screenX = x + gameCanvas.getWidth() / 2.0f;
		float screenY = gameCanvas.getHeight() / 2.0f - y;

		stg.util.RenderUtils.enableAntiAliasing(g);

		// 绘制子机主体（绿色）
		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size),
		          (int)(size * 2), (int)(size * 2));

		// 绘制模式指示器（根据当前模式显示不同颜色�?		Color modeColor;
		switch (shootMode) {
			case 0:
				modeColor = Color.GREEN;
				break;
			case 1:
				modeColor = Color.CYAN;
				break;
			case 2:
				modeColor = Color.MAGENTA;
				break;
			default:
				modeColor = Color.WHITE;
		}

		g.setColor(modeColor);
		g.drawOval((int)(screenX - size - 2), (int)(screenY - size - 2),
		          (int)(size * 2 + 4), (int)(size * 2 + 4));

		// 绘制子机核心（白色亮点）
		g.setColor(new Color(255, 255, 255, 220));
		g.fillOval((int)(screenX - size * 0.5f), (int)(screenY - size * 0.5f),
		          (int)(size), (int)(size));

		// 绘制模式编号
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.drawString(String.valueOf(shootMode), (int)(screenX - 3), (int)(screenY + 3));
	}

	@Override
	public void reset() {
		super.reset();
		modeTimer = 0;
		shootMode = 0;
	}
}

