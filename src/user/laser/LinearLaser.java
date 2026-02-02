package user.laser;

import java.awt.*;
import stg.game.laser.Laser;

/**
 * 直线激光类 - 继承自Laser
 * */\n\t * @since 2026-01-21
public class LinearLaser extends Laser {
	private float rotationSpeed; // 旋转速度(弧度/�?
	private boolean moving; // 是否移动
	private float moveSpeed; // 移动速度
	private float moveAngle; // 移动方向

	/**
	 * 构造函�?	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 */
	public LinearLaser(float x, float y, float angle, float length, float width, Color color) {
		super(x, y, angle, length, width, color);
		this.rotationSpeed = 0;
		this.moving = false;
		this.moveSpeed = 0;
		this.moveAngle = 0;
	}

	/**
	 * 完整构造函�?	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 * @param warningTime 预警时间
	 * @param damage 伤害�?	 * @param rotationSpeed 旋转速度
	 */
	public LinearLaser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage, float rotationSpeed) {
		super(x, y, angle, length, width, color, warningTime, damage);
		this.rotationSpeed = rotationSpeed;
		this.moving = false;
		this.moveSpeed = 0;
		this.moveAngle = 0;
	}

	/**
	 * 初始化行为参�?	 */
	@Override
	protected void initBehavior() {
		// 初始化行为参�?	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		// 激光的更新逻辑
	}

	/**
	 * 实现自定义移动逻辑
	 */
	@Override
	protected void onMove() {
		if (active) {
			// 旋转
			angle += rotationSpeed;

			// 移动
			if (moving) {
				x += (float)Math.cos(moveAngle) * moveSpeed;
				y += (float)Math.sin(moveAngle) * moveSpeed;
			}
		}
	}

	/**
	 * 更新激光状�?	 */
	@Override
	public void update() {
		super.update();
	}

	/**
	 * 设置移动参数
	 * @param moveSpeed 移动速度
	 * @param moveAngle 移动角度
	 */
	public void setMovement(float moveSpeed, float moveAngle) {
		this.moveSpeed = moveSpeed;
		this.moveAngle = moveAngle;
		this.moving = moveSpeed != 0;
	}

	/**
	 * 设置旋转速度
	 * @param rotationSpeed 旋转速度(弧度/�?
	 */
	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	/**
	 * 获取旋转速度
	 */
	public float getRotationSpeed() {
		return rotationSpeed;
	}

	/**
	 * 检查激光是否超出边�?	 * @param width 画布宽度
	 * @param height 画布高度
	 * @return 是否超出边界
	 */
	public boolean isOutOfBounds(int width, int height) {
		float leftBound = -width / 2.0f - length;
		float rightBound = width / 2.0f + length;
		float topBound = -height / 2.0f - length;
		float bottomBound = height / 2.0f + length;
		return x < leftBound || x > rightBound || y < topBound || y > bottomBound;
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

