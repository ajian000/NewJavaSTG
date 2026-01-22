package stg.game.laser;

import java.awt.*;

/**
 * 敌人直线激光类 - 继承自EnemyLaser和LinearLaser
 * @Time 2026-01-21
 */
public class EnemyLinearLaser extends EnemyLaser {
	private LinearLaser linearLaser; // 实际的直线激光

	/**
	 * 构造函数
	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 */
	public EnemyLinearLaser(float x, float y, float angle, float length, float width, Color color) {
		super(x, y, angle, length, width, color);
		this.linearLaser = new LinearLaser(x, y, angle, length, width, color);
	}

	/**
	 * 完整构造函数
	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 * @param warningTime 预警时间
	 * @param damage 伤害值
	 * @param rotationSpeed 旋转速度
	 */
	public EnemyLinearLaser(float x, float y, float angle, float length, float width, Color color,
						   int warningTime, int damage, float rotationSpeed) {
		super(x, y, angle, length, width, color, warningTime, damage);
		this.linearLaser = new LinearLaser(x, y, angle, length, width, color, warningTime, damage, rotationSpeed);
	}

	@Override
	public void update() {
		super.update();
		linearLaser.update();
		// 同步位置和状态
		this.x = linearLaser.getX();
		this.y = linearLaser.getY();
		this.angle = linearLaser.getAngle();
		this.active = linearLaser.isActive();
	}

	public void render(java.awt.Graphics g) {
		linearLaser.setGameCanvas(gameCanvas);
		linearLaser.render(g);
	}

	public boolean checkCollision(float px, float py) {
		return linearLaser.checkCollision(px, py);
	}

	public boolean isOutOfBounds(int width, int height) {
		return linearLaser.isOutOfBounds(width, height);
	}

	// 委托方法到linearLaser
	public void setMovement(float moveSpeed, float moveAngle) {
		linearLaser.setMovement(moveSpeed, moveAngle);
	}

	public void setRotationSpeed(float rotationSpeed) {
		linearLaser.setRotationSpeed(rotationSpeed);
	}

	public float getRotationSpeed() {
		return linearLaser.getRotationSpeed();
	}
}
