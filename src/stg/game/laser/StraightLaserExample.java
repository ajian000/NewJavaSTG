package stg.game.laser;

import java.awt.Color;

/**
 * 直线激光行为示例
 * 展示如何在新架构下实现简单的直线激光
 */
public class StraightLaserExample extends Laser {

	/**
	 * 构造函数
	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 激光角度（弧度）
	 */
	public StraightLaserExample(float x, float y, float angle) {
		super(x, y, angle, 500, 8, Color.RED, 60, 50);
	}

	/**
	 * 初始化行为参数
	 */
	@Override
	protected void initBehavior() {
		// 初始化激光行为参数
		// 例如设置激光长度、宽度、颜色等
	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		// 激光的更新逻辑
		// 例如添加激光强度变化、持续时间管理等
	}

	/**
	 * 实现自定义移动逻辑
	 */
	@Override
	protected void onMove() {
		// 激光的移动逻辑
		// 例如跟踪玩家、旋转角度等
		// 这里实现一个简单的旋转激光效果
		angle += 0.01f; // 每帧旋转一定角度
	}
}
