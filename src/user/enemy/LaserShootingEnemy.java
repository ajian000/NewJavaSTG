package user.enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import stg.game.enemy.Enemy;
import stg.game.ui.GameCanvas;
import user.laser.*;

/**
 * 激光射击敌�?- 能够发射直线激光和曲线激�? * */\n\t * @since 2026-01-20
public class LaserShootingEnemy extends Enemy {
	private float shootTimer;
	private float shootInterval;
	private float moveSpeed;
	private float moveAngle;
	private int pattern;
	private int patternTimer;
	private float patternInterval;
	private List<EnemyLaser> firedLasers;

	/**
	 * 构造函�?	 * @param x X坐标
	 * @param y Y坐标
	 * @param moveSpeed 移动速度
	 * @param gameCanvas 游戏画布引用
	 * @param pattern 攻击模式
	 */
	public LaserShootingEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas, int pattern) {
		this(x, y, moveSpeed, gameCanvas, pattern, 90, 300);
	}

	/**
	 * 完整构造函�?	 * @param x X坐标
	 * @param y Y坐标
	 * @param moveSpeed 移动速度
	 * @param gameCanvas 游戏画布引用
	 * @param pattern 攻击模式
	 * @param shootInterval 射击间隔
	 * @param patternInterval 模式切换间隔
	 */
	public LaserShootingEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas,
							int pattern, float shootInterval, float patternInterval) {
		super(x, y, moveSpeed, 0, 30, Color.MAGENTA, 480, gameCanvas);
		this.moveSpeed = moveSpeed;
		this.moveAngle = (float)Math.PI / 2;
		this.pattern = pattern;
		this.shootInterval = shootInterval;
		this.patternInterval = patternInterval;
		this.shootTimer = 0;
		this.patternTimer = 0;
		this.firedLasers = new ArrayList<>();
	}

	/**
	 * 更新敌人状�?	 */
	@Override
	public void update() {
		super.update();

		// 安全检�?		if (getGameCanvas() == null || !isAlive()) {
			return;
		}

		// 移动逻辑
		int canvasWidth = getGameCanvas().getWidth();
		if (canvasWidth <= 0) {
			return; // 画布未准备好
		}

		float leftBound = -canvasWidth / 2.0f + getSize();
		float rightBound = canvasWidth / 2.0f - getSize();

		if (getX() <= leftBound) {
			moveAngle = (float)Math.PI - moveAngle;
		} else if (getX() >= rightBound) {
			moveAngle = (float)Math.PI - moveAngle;
		}

		// 更新射击计时�?		shootTimer++;

		// 更新模式切换计时�?		patternTimer++;

		// 切换攻击模式
		if (pattern == 2 && patternTimer >= patternInterval) {
			patternTimer = 0;
			// 随机切换模式
			pattern = (int)(Math.random() * 2);
		}

		// 射击逻辑
		if (shootTimer >= shootInterval) {
			shoot();
			shootTimer = 0;
		}
	}

	/**
	 * 射击 - 根据当前模式发射激�?	 */
	private void shoot() {
		if (!isAlive() || gameCanvas == null) return;

		switch (pattern) {
			case 0:
				shootLinearLaser();
				break;
			case 1:
				shootCurvedLaser();
				break;
			default:
				// 随机选择
				if (Math.random() < 0.5) {
					shootLinearLaser();
				} else {
					shootCurvedLaser();
				}
		}
	}

	/**
	 * 发射直线激�?	 */
	private void shootLinearLaser() {
		float angle = (float)(Math.random() * Math.PI * 2);
		EnemyLinearLaser laser = new EnemyLinearLaser(
			x, y, angle, 300, 6, Color.RED,
			60, 15,
			0.01f
		);
		gameCanvas.addEnemyLaser(laser);
		firedLasers.add(laser);
	}

	private void shootCurvedLaser() {
		float baseAngle = (float)(Math.PI / 2 + (Math.random() - 0.5) * Math.PI / 2);
		float speed = 1.5f + (float)Math.random();
		EnemyCurvedLaser laser = new EnemyCurvedLaser(
			x, y, baseAngle, 250, 5, Color.ORANGE,
			60, 10,
			(float)Math.cos(baseAngle) * speed,
			(float)Math.sin(baseAngle) * speed,
			60
		);
		gameCanvas.addEnemyLaser(laser);
		firedLasers.add(laser);
	}

	/**
	 * 重写渲染方法,自定义外�?	 */
	@Override
	public void render(Graphics2D g) {
		float[] screenCoords = toScreenCoords(x, y);
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		// 绘制六边�?		int[] xPoints = new int[6];
		int[] yPoints = new int[6];
		for (int i = 0; i < 6; i++) {
			double angle = 2 * Math.PI * i / 6;
			xPoints[i] = (int)(screenX + size * 1.2 * Math.cos(angle));
			yPoints[i] = (int)(screenY + size * 1.2 * Math.sin(angle));
		}

		g.setColor(color);
		g.fillPolygon(xPoints, yPoints, 6);

		g.setColor(Color.WHITE);
		g.drawPolygon(xPoints, yPoints, 6);

		// 绘制中心发光效果
		g.setColor(new Color(255, 255, 0, 100));
		g.fillOval((int)(screenX - size/2), (int)(screenY - size/2), (int)size, (int)size);

		renderHealthBar(g, screenX, screenY);
	}

	/**
	 * 重写toString,方便调试
	 */
	@Override
	public String toString() {
		return "LaserShootingEnemy[pos=(" + getX() + ", " + getY() + "), hp=" + hp + "/" + maxHp +
			   ", pattern=" + pattern + ", alive=" + isAlive() + "]";
	}

	/**
	 * 重写死亡回调，清除所有激�?	 */
	@Override
	protected void onDeath() {
		if (getGameCanvas() != null && firedLasers != null) {
			getGameCanvas().removeEnemyLasers(firedLasers);
			firedLasers.clear();
		}
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

