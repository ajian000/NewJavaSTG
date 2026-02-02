package user.laser;

import java.awt.*;
import stg.game.laser.Laser;

/**
 * 敌方激光基�?- 所有敌方激光的父类
 * */\n\t * @since 2026-01-21
public abstract class EnemyLaser extends Laser {
	private int hitCooldown; // 击中冷却计时�?	private static final int HIT_COOLDOWN_TIME = 30; // 击中�?0帧冷�?
	/**
	 * 构造函�?	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 */
	public EnemyLaser(float x, float y, float angle, float length, float width, Color color) {
		super(x, y, angle, length, width, color, 60, 10);
		this.hitCooldown = 0;
	}

	/**
	 * 完整构造函�?	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 * @param warningTime 预警时间
	 * @param damage 伤害�?	 */
	public EnemyLaser(float x, float y, float angle, float length, float width, Color color,
					 int warningTime, int damage) {
		super(x, y, angle, length, width, color, warningTime, damage);
		this.hitCooldown = 0;
	}

	/**
	 * 更新击中冷却
	 */
	@Override
	public void update() {
		super.update();
		if (hitCooldown > 0) {
			hitCooldown--;
		}
	}

	/**
	 * 检查是否能击中玩家（考虑冷却时间�?	 */
	public boolean canHit() {
		return hitCooldown == 0 && isActive();
	}

	/**
	 * 标记已击中玩家，启动冷却
	 */
	public void onHitPlayer() {
		hitCooldown = HIT_COOLDOWN_TIME;
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

