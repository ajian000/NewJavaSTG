package stg.game.laser;

import java.awt.Color;

/**
 * 简单激光类 - 提供空的task实现
 * 用于不需要特殊task行为的激光
 * @Time 2026-01-23 创建默认实现
 */
public class SimpleLaser extends Laser {

	/**
	 * 构造函数
	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 */
	public SimpleLaser(float x, float y, float angle, float length, float width, Color color) {
		super(x, y, angle, length, width, color);
	}

	/**
	 * 完整构造函数
	 */
	public SimpleLaser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage) {
		super(x, y, angle, length, width, color, warningTime, damage);
	}

	/**
	 * 简单激光不需要特殊的独立线程逻辑
	 * 保留空的task方法以兼容父类设计
	 */
	protected void task() {
		// 空实现，不需要特殊行为
	}
}
