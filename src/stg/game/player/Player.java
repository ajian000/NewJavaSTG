package stg.game.player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import stg.game.bullet.SimpleBullet;
import stg.game.task.TaskManager;
import stg.game.ui.GameCanvas;

/**
 * 玩家类 - 自机角色
 */
public class Player {
	private float x; // X坐标
	private float y; // Y坐标
	private float vx; // X方向速度
	private float vy; // Y方向速度
	private float speed; // 普通移动速度
	private float speedSlow; // 低速移动速度
	private float size; // 角色大小
	protected GameCanvas gameCanvas; // 游戏画布引用（子类可访问）

	private boolean slowMode; // 低速模式标志
	private boolean shooting; // 射击标志
	private int shootCooldown; // 射击冷却时间
	private int shootInterval = 1;
	private int respawnTimer; // @Time 2026-01-19 重生计时器
	private int respawnTime = 60; // @Time 2026-01-19 重生等待时间(帧数)
	private float spawnX; // @Time 2026-01-19 重生X坐标
	private float spawnY; // @Time 2026-01-19 重生Y坐标
	private boolean respawning; // @Time 2026-01-19 重生动画标志
	private float respawnSpeed; // @Time 2026-01-19 重生移动速度
	private float hitboxRadius; // 受击判定半径
	private int invincibleTimer; // 无敌时间计时器(帧数)
	private int invincibleTime = 120; // 无敌时间(120帧=2秒)
	protected int bulletDamage = 2; // @Time 2026-01-23 子弹伤害，DPS = (2 × 2 × 60) / 2 = 120
	protected List<Option> options; // 子机列表
	protected TaskManager taskManager; // 任务管理器

	public Player() {
		this(0, 0, 5.0f, 2.0f, 20, null);
	}

	public Player(float spawnX, float spawnY) {
		this(spawnX, spawnY, 5.0f, 2.0f, 20, null);
	}

	public Player(float x, float y, float speed, float speedSlow, float size, GameCanvas gameCanvas) {
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = 0;
		this.speed = speed;
		this.speedSlow = speedSlow;
		this.size = size;
		this.gameCanvas = gameCanvas;
		this.slowMode = false;
		this.shooting = false;
		this.shootCooldown = 0;
		this.respawnTimer = 0;
		this.spawnX = x;
		this.spawnY = y;
		this.respawning = false;
		this.respawnSpeed = 8.0f;
		this.hitboxRadius = 2.0f;
		this.invincibleTimer = invincibleTime; // @Time 2026-01-23 初始无敌时间
		this.options = new ArrayList<>(); // 初始化子机列表
		this.taskManager = new TaskManager(); // 初始化任务管理器
		initTasks(); // 初始化任务
	}

	/**
	 * 更新玩家状态 - @Time 2026-01-19 使用中心原点坐标系
	 * 坐标系: 右上角(+,+),左下角(-,-)
	 */
	public void update() {
		// 更新任务管理器
		if (taskManager != null) {
			taskManager.update(1);
		}

		// @Time 2026-01-19 处理重生等待计时
		if (respawnTimer > 0) {
			respawnTimer--;
			if (respawnTimer == 0) {
				respawning = true;
				x = spawnX;
				int canvasHeight = gameCanvas.getHeight();
				y = -canvasHeight / 2.0f - size;
				vx = 0;
				vy = respawnSpeed;
			}
			return;
		}

		// @Time 2026-01-19 处理重生动画
		if (respawning) {
			// 更新位置
			y += vy;

			// 检查是否到达重生位置
			if (y >= spawnY) {
				y = spawnY;
				respawning = false;
				vy = 0;
				invincibleTimer = invincibleTime; // @Time 2026-01-23 重生后获得无敌时间
				System.out.println("Player respawned at: (" + x + ", " + y + ") with " + invincibleTime + " frames invincible");
			}
			return; // 重生动画期间不接受玩家输入
		}

		// 更新位置
		x += vx;
		y += vy;

		// 获取画布尺寸和坐标系统
		int canvasWidth = gameCanvas.getWidth();
		int canvasHeight = gameCanvas.getHeight();
		float leftBound = -canvasWidth / 2.0f;
		float rightBound = canvasWidth / 2.0f;
		float bottomBound = -canvasHeight / 2.0f;
		float topBound = canvasHeight / 2.0f;

		// @Time 2026-01-19 边界限制(中心原点坐标系,右上角(+,+),左下角(-,-))
		if (x < leftBound + size) x = leftBound + size;
		if (x > rightBound - size) x = rightBound - size;
		if (y < bottomBound + size) y = bottomBound + size;
		if (y > topBound - size) y = topBound - size;

		// 更新射击冷却
		if (shootCooldown > 0) {
			shootCooldown--;
		}

		// @Time 2026-01-23 更新无敌时间计时器
		if (invincibleTimer > 0) {
			invincibleTimer--;
		}

		// 射击逻辑
		if (shooting && shootCooldown == 0) {
			shoot();
			shootCooldown = shootInterval;
		}

		// 更新所有子机
		for (Option option : options) {
			option.setShooting(shooting);
			option.update();
		}
	}

	/**
	 * 发射子弹 - @Time 2026-01-19 向上发射(Y正方向)
	 * 子类可重写此方法实现不同的射击模式
	 */
	protected void shoot() {
		float bulletSpeed = 46.0f;
		Color bulletColor = Color.WHITE; // 子弹颜色
		float bulletSize = slowMode ? 6.0f : 4.0f; // 子弹大小(低速模式更大)

		// 发射两发子弹,从玩家中心位置,向上发射(Y为正)
		SimpleBullet bullet1 = new SimpleBullet(x - 5, y, 0, bulletSpeed, bulletSize, bulletColor);
		SimpleBullet bullet2 = new SimpleBullet(x + 5, y, 0, bulletSpeed, bulletSize, bulletColor);

		// @Time 2026-01-19 设置画布引用
		bullet1.setGameCanvas(gameCanvas);
		bullet2.setGameCanvas(gameCanvas);

		// 添加到画布
		gameCanvas.addBullet(bullet1);
		gameCanvas.addBullet(bullet2);
	}

	/**
	 * 渲染玩家 - 简化版本：仅渲染为一个球体
	 * @param g 图形上下文
	 */
	public void render(Graphics2D g) {
		// 将中心原点坐标转换为屏幕坐标
		float screenX = x + gameCanvas.getWidth() / 2.0f;
		float screenY = gameCanvas.getHeight() / 2.0f - y;

		// 开启抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// @Time 2026-01-23 无敌闪烁效果：每5帧闪烁一次
		boolean shouldRender = true;
		if (invincibleTimer > 0) {
			int flashPhase = invincibleTimer % 10; // 10帧为一个闪烁周期
			if (flashPhase < 5) {
				shouldRender = false;
			}
		}

		// 绘制角色主体（仅为一个简单的红色球体）
		if (shouldRender) {
			g.setColor(new Color(255, 100, 100));
			g.fillOval((int)(screenX - size), (int)(screenY - size),
			          (int)(size * 2), (int)(size * 2));
		}

		// 低速模式时显示受击判定点（在球体上方）
		if (slowMode && shouldRender) {
			g.setColor(Color.WHITE);
			g.fillOval((int)(screenX - hitboxRadius), (int)(screenY - hitboxRadius),
			          (int)(hitboxRadius * 2), (int)(hitboxRadius * 2));
		}

		// 渲染所有子机
		for (Option option : options) {
			option.render(g);
		}
	}

	/**
	 * 向上移动 - @Time 2026-01-19 Y正方向
	 */
	public void moveUp() {
		vy = slowMode ? speedSlow : speed;
	}

	/**
	 * 向下移动 - @Time 2026-01-19 Y负方向
	 */
	public void moveDown() {
		vy = slowMode ? -speedSlow : -speed;
	}

	/**
	 * 向左移动
	 */
	public void moveLeft() {
		vx = slowMode ? -speedSlow : -speed;
	}

	/**
	 * 向右移动
	 */
	public void moveRight() {
		vx = slowMode ? speedSlow : speed;
	}

	/**
	 * 停止垂直移动
	 */
	public void stopVertical() {
		vy = 0;
	}

	/**
	 * 停止水平移动
	 */
	public void stopHorizontal() {
		vx = 0;
	}

	/**
	 * 设置射击状态
	 * @param shooting 是否射击
	 */
	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	/**
	 * 设置低速模式
	 * @param slow 是否低速模式
	 */
	public void setSlowMode(boolean slow) {
		this.slowMode = slow;
	}

	/**
	 * 设置游戏画布
	 * @param canvas 游戏画布
	 */
	public void setGameCanvas(GameCanvas canvas) {
		this.gameCanvas = canvas;
	}

	/**
	 * 获取X坐标
	 * @return X坐标
	 */
	public float getX() {
		return x;
	}

	/**
	 * 获取Y坐标
	 * @return Y坐标
	 */
	public float getY() {
		return y;
	}

	/**
	 * 获取角色大小
	 * @return 角色大小
	 */
	public float getSize() {
		return size;
	}

	/**
	 * 获取受击判定半径
	 * @return 受击判定半径
	 */
	public float getHitboxRadius() {
		return hitboxRadius;
	}

	/**
	 * 设置位置
	 * @param x X坐标
	 * @param y Y坐标
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		// @Time 2026-01-19 保存初始位置用于重生
		this.spawnX = x;
		this.spawnY = y;
	}

	/**
	 * 获取普通移动速度
	 * @return 移动速度
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * 获取低速移动速度
	 * @return 低速移动速度
	 */
	public float getSpeedSlow() {
		return speedSlow;
	}

	/**
	 * 设置普通移动速度
	 * @param speed 移动速度
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * 设置低速移动速度
	 * @param speedSlow 低速移动速度
	 */
	public void setSpeedSlow(float speedSlow) {
		this.speedSlow = speedSlow;
	}

	/**
	 * 设置角色大小
	 * @param size 角色大小
	 */
	public void setSize(float size) {
		this.size = size;
	}

	/**
	 * 设置射击间隔
	 * @param interval 射击间隔(帧数)
	 */
	public void setShootInterval(int interval) {
		this.shootInterval = interval;
	}

	/**
	 * 获取游戏画布
	 * @return 游戏画布
	 */
	protected GameCanvas getGameCanvas() {
		return gameCanvas;
	}

	/**
	 * 是否低速模式
	 * @return 是否低速模式
	 */
	public boolean isSlowMode() {
		return slowMode;
	}

	/**
	 * @Time 2026-01-19 受击处理
	 * 玩家中弹后立即移到屏幕下方,然后等待重生
	 */
	public void onHit() {
		// 立即移动到屏幕下方
		int canvasHeight = gameCanvas.getHeight();
		y = -canvasHeight / 2.0f - size;
		vx = 0;
		vy = 0;
		respawning = false;

		// 启动重生等待计时器
		respawnTimer = respawnTime;

		System.out.println("Player hit! Moved off-screen. Respawn animation in " + respawnTime + " frames");
	}

	/**
	 * 初始化任务（子类可重写以添加自定义任务）
	 * @Time 2026-01-29
	 */
	protected void initTasks() {
		// 默认实现为空，子类可重写添加自定义任务
	}

	/**
	 * 获取任务管理器
	 * @return 任务管理器
	 * @Time 2026-01-29
	 */
	public TaskManager getTaskManager() {
		return taskManager;
	}

	/**
	 * 重置玩家状态（用于重新开始游戏）
	 */
	public void reset() {
		vx = 0;
		vy = 0;
		slowMode = false;
		shooting = false;
		shootCooldown = 0;
		respawnTimer = 0;
		respawning = false;
		invincibleTimer = invincibleTime; // @Time 2026-01-23 重置时获得无敌时间
		// x 和 y 由 GameCanvas.resetGame() 设置

		// 重置任务管理器
		if (taskManager != null) {
			taskManager.clearTasks();
			initTasks();
		}

		// 重置所有子机
		for (Option option : options) {
			option.reset();
		}
	}

	/**
	 * @Time 2026-01-23 检查玩家是否处于无敌状态
	 * @return 是否无敌
	 */
	public boolean isInvincible() {
		return invincibleTimer > 0;
	}

	/**
	 * @Time 2026-01-23 获取无敌计时器剩余帧数
	 * @return 无敌剩余帧数
	 */
	protected int getInvincibleTimer() {
		return invincibleTimer;
	}

	/**
	 * @Time 2026-01-23 设置无敌时间
	 * @param frames 无敌帧数
	 */
	public void setInvincibleTime(int frames) {
		this.invincibleTime = frames;
	}

	/**
	 * 添加子机
	 * @param option 子机对象
	 */
	public void addOption(Option option) {
		options.add(option);
	}

	/**
	 * 移除子机
	 * @param option 子机对象
	 */
	public void removeOption(Option option) {
		options.remove(option);
	}

	/**
	 * 获取子机列表
	 * @return 子机列表
	 */
	public List<Option> getOptions() {
		return options;
	}

	/**
	 * 清除所有子机
	 */
	public void clearOptions() {
		options.clear();
	}
}
