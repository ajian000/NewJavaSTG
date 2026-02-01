package user;

import java.awt.Color;
import user.bullet.SimpleBullet;

/**
 * 直线子弹行为示例
 * 展示如何在新架构下实现简单的直线飞行子弹
 */
public class StraightBulletExample extends SimpleBullet {
	private float angle;
	private float speed;

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param angle 飞行角度（弧度）
	 * @param speed 飞行速度
	 */
	public StraightBulletExample(float x, float y, float angle, float speed) {
		super(x, y, (float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed, 8, Color.YELLOW);
		this.angle = angle;
		this.speed = speed;
	}

	/**
	 * 初始化行为参数
	 */
	@Override
	protected void initBehavior() {
		// 初始化行为参数
	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		// 子弹的更新逻辑
		// 例如添加轨迹效果、速度变化等
	}

	/**
	 * 实现自定义移动逻辑
	 */
	@Override
	protected void onMove() {
		// 直线飞行，不需要额外的移动逻辑
		// 基础移动在父类的update()方法中处理
	}
}