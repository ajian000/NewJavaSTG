package stg.game.enemy;

import stg.game.ui.GameCanvas;
import stg.game.bullet.CircularBullet;
import java.awt.*;

/**
 * 基础敌人类 - Enemy的子类
 * @Time 2026-01-19 在X轴上左右来回移动,Y轴不动
 */
public class BasicEnemy extends Enemy {
	private float shootTimer; // 射击计时器
	private float shootInterval; // 射击间隔
	private float moveSpeed; // X轴移动速度

	/**
	 * 构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param moveSpeed X方向移动速度
	 * @param gameCanvas 游戏画布引用
	 */
	public BasicEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 20, Color.BLUE, 100, gameCanvas);
		this.shootTimer = 0;
		this.shootInterval = 60; // 60帧(约1秒)发射一次
		this.moveSpeed = moveSpeed;
	}

	/**
	 * @Time 2026-01-19 重写update方法,添加X轴左右移动和射击逻辑
	 */
	@Override
	public void update() {
		super.update();

		// X轴左右移动逻辑
		int canvasWidth = gameCanvas.getWidth();
		float leftBound = -canvasWidth / 2.0f + size;
		float rightBound = canvasWidth / 2.0f - size;

		if (x <= leftBound) {
			vx = Math.abs(moveSpeed);
		} else if (x >= rightBound) {
			vx = -Math.abs(moveSpeed);
		}

		// 更新射击计时器
		shootTimer++;

		// 射击逻辑
		if (shootTimer >= shootInterval) {
			shoot();
			shootTimer = 0;
		}
	}

	/**
	 * 射击 - 向下方发射圆形子弹
	 * @Time 2026-01-19 Y负方向表示向下
	 */
	private void shoot() {
		if (!alive || gameCanvas == null) return;

		float bulletSpeed = -10.0f;
		CircularBullet bullet = new CircularBullet(x, y, 0, bulletSpeed);
		gameCanvas.addEnemyBullet(bullet);
	}

	/**
	 * @Time 2026-01-19 重写渲染方法,自定义外观
	 */
	@Override
	public void render(Graphics2D g) {
		int canvasWidth = gameCanvas != null ? gameCanvas.getWidth() : 548;
		int canvasHeight = gameCanvas != null ? gameCanvas.getHeight() : 921;
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;

		int[] xPoints = {
			(int)screenX,
			(int)(screenX - size),
			(int)(screenX + size)
		};
		int[] yPoints = {
			(int)(screenY - size),
			(int)(screenY + size),
			(int)(screenY + size)
		};

		g.setColor(color);
		g.fillPolygon(xPoints, yPoints, 3);

		g.setColor(Color.WHITE);
		g.drawPolygon(xPoints, yPoints, 3);

		renderHealthBar(g, screenX, screenY);
	}

	/**
	 * @Time 2026-01-19 重写死亡回调,添加简单的死亡效果
	 */
	@Override
	protected void onDeath() {
	}

	/**
	 * @Time 2026-01-19 重写toString,方便调试
	 */
	@Override
	public String toString() {
		return "BasicEnemy[pos=(" + x + ", " + y + "), hp=" + hp + "/" + maxHp + ", alive=" + alive + "]";
	}
}
