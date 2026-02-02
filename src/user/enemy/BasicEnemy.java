 package user.enemy;

import java.awt.*;
import stg.game.enemy.Enemy;
import stg.game.ui.GameCanvas;
import user.bullet.CircularBullet;

/**
 * 基础敌人�?- Enemy的子�? * 在X轴上左右来回移动,Y轴不�? * @since 2026-01-19
 */
public class BasicEnemy extends Enemy {
	private float moveSpeed; // X轴移动速度

	/**
	 * 构造函�?	 * @param x X坐标
	 * @param y Y坐标
	 * @param moveSpeed X方向移动速度
	 * @param gameCanvas 游戏画布引用
	 */
	public BasicEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 20, Color.BLUE, 120, gameCanvas);
		this.moveSpeed = moveSpeed;
	}

	/**
	 * 初始化行为参�?	 */
	@Override
	protected void initBehavior() {
		// 初始化行为参�?		vx = moveSpeed;
		vy = 0;
	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		// 射击逻辑
		if (frame % 120 == 0) {
			shoot();
		}
	}

	/**
	 * 实现自定义移动逻辑
	 */
	@Override
	protected void onMove() {
		// X轴左右移动逻辑
		if (gameCanvas != null) {
			int canvasWidth = gameCanvas.getWidth();
			float leftBound = -canvasWidth / 2.0f + size;
			float rightBound = canvasWidth / 2.0f - size;

			if (x <= leftBound) {
				vx = Math.abs(moveSpeed);
			} else if (x >= rightBound) {
				vx = -Math.abs(moveSpeed);
			}
		}
	}

	/**
	 * 重写update方法
	 * @since 2026-01-19
	 */
	@Override
	public void update() {
		super.update();
	}

	/**
	 * 射击 - 向下方发射圆形子�?	 * Y负方向表示向�?	 * @since 2026-01-19
	 */
	private void shoot() {
		if (!isActive() || gameCanvas == null) return;

		float bulletSpeed = -10.0f;
		CircularBullet bullet = new CircularBullet(x, y, 0, bulletSpeed);
		gameCanvas.addEnemyBullet(bullet);
	}

	/**
	 * 重写渲染方法,自定义外�?	 * @since 2026-01-19
	 */
	@Override
	public void render(Graphics2D g) {
		float[] screenCoords = toScreenCoords(x, y);
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

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
	 * 重写死亡回调,添加简单的死亡效果
	 * @since 2026-01-19
	 */
	@Override
	protected void onDeath() {
	}

	/**
	 * 重写toString,方便调试
	 * @since 2026-01-19
	 */
	@Override
	public String toString() {
		return "BasicEnemy[pos=(" + x + ", " + y + "), hp=" + hp + "/" + maxHp + ", alive=" + isActive() + "]";
	}

	/**
	 * 任务开始时触发的方�?	 */
	@Override
	protected void onTaskStart() {
		// 空实�?	}

	/**
	 * 任务结束时触发的方法
	 */
	@Override
	protected void onTaskEnd() {
		// 空实�?	}
}

