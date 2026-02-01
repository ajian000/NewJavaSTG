package user;

import user.player.Player;

/**
 * 基础玩家行为示例
 * 展示如何在新架构下实现简单的玩家行为
 */
public class BasicPlayerExample extends Player {
	private float moveSpeed;
	private int shootCooldown;
	private int shootTimer;

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 */
	public BasicPlayerExample(float x, float y) {
		super(x, y);
		// 设置大小
		setSize(15);
	}

	/**
	 * 初始化行为参数
	 */
	@Override
	protected void initBehavior() {
		moveSpeed = 3.0f;
		shootCooldown = 10; // 10帧发射一次
		shootTimer = 0;
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
		// 玩家移动逻辑由外部输入控制
		// 这里可以添加移动限制、加速度等
	}

	/**
	 * 发射子弹
	 */
	private void shootBullet() {
		// 这里可以实现具体的子弹发射逻辑
		// 例如创建PlayerBullet实例并添加到游戏画布
	}

	/**
	 * 设置玩家移动方向
	 * @param dx X方向输入
	 * @param dy Y方向输入
	 */
	public void setInput(float dx, float dy) {
		// 根据输入设置速度
		// 首先停止当前移动
		stopHorizontal();
		stopVertical();
		
		// 然后根据输入设置新的移动
		if (dx > 0) {
			moveRight();
		} else if (dx < 0) {
			moveLeft();
		}
		
		if (dy > 0) {
			moveUp();
		} else if (dy < 0) {
			moveDown();
		}
	}

	// 以下方法调用父类的移动控制方法
	public void moveRight() {
		// 调用父类的向右移动方法
		super.moveRight();
	}

	public void moveLeft() {
		// 调用父类的向左移动方法
		super.moveLeft();
	}

	public void moveUp() {
		// 调用父类的向上移动方法
		super.moveUp();
	}

	public void moveDown() {
		// 调用父类的向下移动方法
		super.moveDown();
	}

	public void stopHorizontal() {
		// 调用父类的停止水平移动方法
		super.stopHorizontal();
	}

	public void stopVertical() {
		// 调用父类的停止垂直移动方法
		super.stopVertical();
	}
}