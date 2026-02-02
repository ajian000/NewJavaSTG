package user.bullet;

import java.awt.*;
import user.enemy.EnemyBullet;

/**
 * 弯曲弹幕�?- EnemyBullet的子�?
 * @since 2026-01-29 带有加速度的弯曲弹�?
 */
public class CurvingBullet extends EnemyBullet {
	private static final float DIAMOND_WIDTH_RATIO = 0.7f;
	
	private float ax;
	private float ay;
	private float maxSpeed;

	/**
	 * 构造函�?
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
	 * 完整构造函�?
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param ax X方向加速度
	 * @param ay Y方向加速度
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 * @param maxSpeed 最大速度
	 * @param damage 伤害�?
	 */
	public CurvingBullet(float x, float y, float vx, float vy, float ax, float ay,
	                  float size, Color color, float maxSpeed, int damage) {
		super(x, y, vx, vy, size, color, damage);
		if (maxSpeed <= 0) {
			throw new IllegalArgumentException("maxSpeed must be positive");
		}
		this.ax = ax;
		this.ay = ay;
		this.maxSpeed = maxSpeed;
	}

	@Override
	public void update() {
		super.update();
		
		vx += ax;
		vy += ay;

		float speedSquared = vx * vx + vy * vy;
		float maxSpeedSquared = maxSpeed * maxSpeed;
		if (speedSquared > maxSpeedSquared) {
			float currentSpeed = (float)Math.sqrt(speedSquared);
			float scale = maxSpeed / currentSpeed;
			vx *= scale;
			vy *= scale;
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(color);

		int[] xPoints = {
			(int)x,
			(int)(x + size * DIAMOND_WIDTH_RATIO),
			(int)x,
			(int)(x - size * DIAMOND_WIDTH_RATIO)
		};
		int[] yPoints = {
			(int)(y - size),
			(int)y,
			(int)(y + size),
			(int)y
		};
		g.fillPolygon(xPoints, yPoints, 4);
	}

	public float getAx() {
		return ax;
	}

	public void setAx(float ax) {
		this.ax = ax;
	}

	public float getAy() {
		return ay;
	}

	public void setAy(float ay) {
		this.ay = ay;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		if (maxSpeed <= 0) {
			throw new IllegalArgumentException("maxSpeed must be positive");
		}
		this.maxSpeed = maxSpeed;
	}

	/**
	 * 任务开始时触发的方�?
	 */
	@Override
	protected void onTaskStart() {
		// 空实�?
	}

	/**
	 * 任务结束时触发的方法
	 */
	@Override
	protected void onTaskEnd() {
		// 空实�?
	}
}

