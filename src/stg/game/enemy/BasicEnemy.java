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

		// 碰到左边界,向右移动
		if (x <= leftBound) {
			vx = Math.abs(moveSpeed);
			System.out.println("Enemy hit left boundary, moving right");
		}
		// 碰到右边界,向左移动
		else if (x >= rightBound) {
			vx = -Math.abs(moveSpeed);
			System.out.println("Enemy hit right boundary, moving left");
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

		float bulletSpeed = -10.0f; // 向下发射(Y负方向)

		// 创建圆形弹幕,初始位置为敌人位置
		CircularBullet bullet = new CircularBullet(x, y, 0, bulletSpeed);
		gameCanvas.addEnemyBullet(bullet);
		System.out.println("Enemy fired bullet at position: (" + x + ", " + y + "), speed: (0, " + bulletSpeed + ")");
	}

	/**
	 * @Time 2026-01-19 重写渲染方法,自定义外观
	 */
	@Override
	public void render(Graphics2D g) {
		// @Time 2026-01-19 将中心原点坐标转换为屏幕坐标
		// @Time 2026-01-19 从gameCanvas获取实际画布尺寸
		// 坐标系: 右上角为(+,+),左下角为(-,-)
		int canvasWidth = gameCanvas != null ? gameCanvas.getWidth() : 548;
		int canvasHeight = gameCanvas != null ? gameCanvas.getHeight() : 921;
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;

		// @Time 2026-01-19 调试输出(首次渲染)
		if (gameCanvas != null && gameCanvas.getEnemies().contains(this)) {
			System.out.println("Rendering enemy at center coords: (" + x + ", " + y + "), screen coords: (" +
			                   screenX + ", " + screenY + "), canvas size: " + canvasWidth + "x" + canvasHeight);
		}

		// 绘制敌人主体(三角形)
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

		// 绘制边框
		g.setColor(Color.WHITE);
		g.drawPolygon(xPoints, yPoints, 3);

		// 绘制生命值条
		renderHealthBar(g, screenX, screenY);
	}

	/**
	 * @Time 2026-01-19 重写死亡回调,添加简单的死亡效果
	 */
	@Override
	protected void onDeath() {
		// 可以在这里添加爆炸特效等
		System.out.println("BasicEnemy destroyed at (" + x + ", " + y + ")");
	}

	/**
	 * @Time 2026-01-19 重写toString,方便调试
	 */
	@Override
	public String toString() {
		return "BasicEnemy[pos=(" + x + ", " + y + "), hp=" + hp + "/" + maxHp + ", alive=" + alive + "]";
	}
}
