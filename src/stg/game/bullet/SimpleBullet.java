package stg.game.bullet;

import java.awt.Color;

/**
 * 简单子弹类 - 提供空的task实现
 * 用于不需要特殊task行为的子弹
 * @Time 2026-01-23 创建默认实现
 */
public class SimpleBullet extends Bullet {

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 */
	public SimpleBullet(float x, float y, float vx, float vy, float size, Color color) {
		super(x, y, vx, vy, size, color);
	}

	/**
	 * 简单子弹不需要特殊的独立线程逻辑
	 * 保留空的task方法以兼容父类设计
	 */
	protected void task() {
		// 空实现，不需要特殊行为
	}
}
