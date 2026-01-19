package stg.game;

import java.awt.*;

/**
 * 弯曲弹幕类 - EnemyBullet的子类
 * @Time 2026-01-19 带有加速度的弯曲弹幕
 */
public class CurvingBullet extends EnemyBullet {
	private float ax; // X方向加速度
	private float ay; // Y方向加速度
	private float maxSpeed; // 最大速度

	/**
	 * 构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param ax X方向加速度
	 * @param ay Y方向加速度
	 */
	public CurvingBullet(float x, float y, float vx, float vy, float ax, float ay) {
		super(x, y, vx, vy, 6, Color.MAGENTA, 15);
		this.ax = ax;
		this.ay = ay;
		this.maxSpeed = 8.0f;
	}

	/**
	 * 完整构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param ax X方向加速度
	 * @param ay Y方向加速度
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 * @param maxSpeed 最大速度
	 * @param damage 伤害值
	 */
	public CurvingBullet(float x, float y, float vx, float vy, float ax, float ay,
	                  float size, Color color, float maxSpeed, int damage) {
		super(x, y, vx, vy, size, color, damage);
		this.ax = ax;
		this.ay = ay;
		this.maxSpeed = maxSpeed;
	}

	/**
	 * @Time 2026-01-19 重写update方法,添加加速度逻辑
	 */
	@Override
	public void update() {
		// 应用加速度
		vx += ax;
		vy += ay;

		// 限制最大速度
		float currentSpeed = (float)Math.sqrt(vx * vx + vy * vy);
		if (currentSpeed > maxSpeed) {
			float scale = maxSpeed / currentSpeed;
			vx *= scale;
			vy *= scale;
		}

		// 更新位置
		x += vx;
		y += vy;
	}

	/**
	 * @Time 2026-01-19 重写渲染方法,绘制旋转效果
	 */
	@Override
	public void render(Graphics2D g) {
		g.setColor(color);

		// 绘制旋转的菱形
		int[] xPoints = {
			(int)x,
			(int)(x + size * 0.7f),
			(int)x,
			(int)(x - size * 0.7f)
		};
		int[] yPoints = {
			(int)(y - size),
			(int)y,
			(int)(y + size),
			(int)y
		};
		g.fillPolygon(xPoints, yPoints, 4);
	}
}
