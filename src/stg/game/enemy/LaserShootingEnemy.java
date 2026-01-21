package stg.game.enemy;

import stg.game.ui.GameCanvas;
import stg.game.laser.*;
import java.awt.*;

/**
 * 激光射击敌人 - 能够发射直线激光和曲线激光
 * @Time 2026-01-20
 */
public class LaserShootingEnemy extends Enemy {
	private float shootTimer; // 射击计时器
	private float shootInterval; // 射击间隔
	private float moveSpeed; // 移动速度
	private float moveAngle; // 移动方向
	private int pattern; // 攻击模式: 0=直线激光, 1=曲线激光, 2=混合
	private int patternTimer; // 模式切换计时器
	private float patternInterval; // 模式切换间隔

	/**
	 * 构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param moveSpeed 移动速度
	 * @param gameCanvas 游戏画布引用
	 * @param pattern 攻击模式
	 */
	public LaserShootingEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas, int pattern) {
		this(x, y, moveSpeed, gameCanvas, pattern, 90, 300);
	}

	/**
	 * 完整构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param moveSpeed 移动速度
	 * @param gameCanvas 游戏画布引用
	 * @param pattern 攻击模式
	 * @param shootInterval 射击间隔
	 * @param patternInterval 模式切换间隔
	 */
	public LaserShootingEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas,
							int pattern, float shootInterval, float patternInterval) {
		super(x, y, moveSpeed, 0, 30, Color.MAGENTA, 200, gameCanvas);
		this.moveSpeed = moveSpeed;
		this.moveAngle = (float)Math.PI / 2; // 向下移动
		this.pattern = pattern;
		this.shootInterval = shootInterval;
		this.patternInterval = patternInterval;
		this.shootTimer = 0;
		this.patternTimer = 0;
	}

	/**
	 * 更新敌人状态
	 */
	@Override
	public void update() {
		super.update();

		// 移动逻辑
		x += (float)Math.cos(moveAngle) * moveSpeed;
		y += (float)Math.sin(moveAngle) * moveSpeed;

		// 边界检查 - 简单的左右反弹
		int canvasWidth = gameCanvas != null ? gameCanvas.getWidth() : 548;
		float leftBound = -canvasWidth / 2.0f + size;
		float rightBound = canvasWidth / 2.0f - size;

		if (x <= leftBound) {
			moveAngle = (float)Math.PI - moveAngle;
		} else if (x >= rightBound) {
			moveAngle = (float)Math.PI - moveAngle;
		}

		// 更新射击计时器
		shootTimer++;

		// 更新模式切换计时器
		patternTimer++;

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
	 * 射击 - 根据当前模式发射激光
	 */
	private void shoot() {
		if (!alive || gameCanvas == null) return;

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
	 * 发射直线激光
	 */
	private void shootLinearLaser() {
		// 创建旋转直线激光
		float angle = (float)(Math.random() * Math.PI * 2);
		EnemyLinearLaser laser = new EnemyLinearLaser(
			x, y, angle, 300, 6, Color.RED,
			60, 15,  // 预警60帧, 伤害15
			0.01f    // 旋转速度
		);
		gameCanvas.addEnemyLaser(laser);
	}

	/**
	 * 发射曲线激光
	 */
	private void shootCurvedLaser() {
		// 创建曲线激光
		float baseAngle = (float)(Math.PI / 2 + (Math.random() - 0.5) * Math.PI / 2);
		float speed = 1.5f + (float)Math.random();
		EnemyCurvedLaser laser = new EnemyCurvedLaser(
			x, y, baseAngle, 250, 5, Color.ORANGE,
			60, 10,      // 预警60帧, 伤害10
			(float)Math.cos(baseAngle) * speed,
			(float)Math.sin(baseAngle) * speed,
			60           // 拖尾60帧
		);
		gameCanvas.addEnemyLaser(laser);
	}

	/**
	 * 重写渲染方法,自定义外观
	 */
	@Override
	public void render(Graphics2D g) {
		int canvasWidth = gameCanvas != null ? gameCanvas.getWidth() : 548;
		int canvasHeight = gameCanvas != null ? gameCanvas.getHeight() : 921;
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;

		// 绘制六边形
		int[] xPoints = new int[6];
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
		return "LaserShootingEnemy[pos=(" + x + ", " + y + "), hp=" + hp + "/" + maxHp +
			   ", pattern=" + pattern + ", alive=" + alive + "]";
	}
}
