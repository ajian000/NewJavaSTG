package user.enemy;

import java.awt.Color;
import java.util.Random;

/**
 * 基础敌人行为示例
 * 展示如何在新架构下实现简单的敌人行为
 */
public class BasicEnemyExample extends stg.game.enemy.Enemy {
	private Random random;
	private float moveSpeed;
	private int shootCooldown;
	private int shootTimer;

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 */
	public BasicEnemyExample(float x, float y) {
		super(x, y);
		// 设置大小和颜色
		size = 20;
		color = Color.RED;
	}

	/**
	 * 初始化行为参数
	 */
	@Override
	protected void initBehavior() {
		random = new Random();
		moveSpeed = 1.0f;
		shootCooldown = 60; // 60帧发射一次
		shootTimer = 0;
		
		// 初始化速度，向下移动
		vx = 0;
		vy = moveSpeed;
	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		// 发射子弹逻辑
		shootTimer++;
		if (shootTimer >= shootCooldown) {
			shootBullet();
			shootTimer = 0;
		}
	}

	/**
	 * 实现自定义移动逻辑
	 */
	@Override
	protected void onMove() {
		// 简单的左右随机移动
		if (frame % 30 == 0) {
			vx = (random.nextFloat() - 0.5f) * 2;
		}
	}

	/**
	 * 发射子弹
	 */
	private void shootBullet() {
		// 这里可以实现具体的子弹发射逻辑
		// 例如创建EnemyBullet实例并添加到游戏画布
	}

	/**
	 * 任务开始时触发的方法
	 */
	@Override
	protected void onTaskStart() {
		// 空实现
	}

	/**
	 * 任务结束时触发的方法
	 */
	@Override
	protected void onTaskEnd() {
		// 空实现
	}
}
