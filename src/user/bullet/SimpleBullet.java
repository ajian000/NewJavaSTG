package user.bullet;

import java.awt.Color;
import stg.game.bullet.Bullet;

/**import stg.game.bullet.Bullet;

/**
 * 简单子弹类 - 提供空的task实现
 * 用于不需要特殊task行为的子�? * @since 2026-01-23 创建默认实现
 */
public class SimpleBullet extends Bullet {

	/**
	 * 构造函�?	 * @param x 初始X坐标
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
	 * 任务开始时触发的方�?- 空实�?	 */
	@Override
	protected void onTaskStart() {
		// 空实现，不需要特殊行�?	}

	/**
	 * 任务结束时触发的方法 - 空实�?	 */
	@Override
	protected void onTaskEnd() {
		// 空实现，不需要特殊行�?	}

}


