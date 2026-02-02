package user.bullet;

import java.awt.Color;
import user.enemy.EnemyBullet;

/**
 * 螺旋子弹�?- 会螺旋前进的子弹
 * */\n\t * @since 2026-01-23
public class SpiralBullet extends EnemyBullet {
	private float angle;
	private float radius;
	private float angleSpeed;
	private float radiusSpeed;
	private float baseSpeed;
	private float baseAngle;
	private int frame;

	/**
	 * 构造函�?	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param baseSpeed 基础速度
	 * @param baseAngle 基础方向角度
	 * @param radius 螺旋半径
	 * @param angleSpeed 角度变化速度
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 */
	public SpiralBullet(float x, float y, float baseSpeed, float baseAngle,
				  float radius, float angleSpeed, float size, Color color) {
		super(x, y, 0, 0, size, color, 10);
		this.baseSpeed = baseSpeed;
		this.baseAngle = baseAngle;
		this.radius = radius;
		this.angleSpeed = angleSpeed;
		this.angle = 0;
		this.radiusSpeed = 0;
		this.frame = 0;
	}

	/**
	 * 设置螺旋半径变化速度
	 * @param radiusSpeed 半径变化速度
	 */
	public void setRadiusSpeed(float radiusSpeed) {
		this.radiusSpeed = radiusSpeed;
	}

	/**
	 * 更新子弹位置
	 */
	@Override
	public void update() {
		frame++;
		angle += angleSpeed;
		radius += radiusSpeed;

		if (radius < 0) radius = 0;

		float baseVx = (float)Math.cos(baseAngle) * baseSpeed;
		float baseVy = (float)Math.sin(baseAngle) * baseSpeed;

		float spiralVx = (float)Math.cos(angle) * radius;
		float spiralVy = (float)Math.sin(angle) * radius;

		vx = baseVx + spiralVx;
		vy = baseVy + spiralVy;

		x += vx;
		y += vy;
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

