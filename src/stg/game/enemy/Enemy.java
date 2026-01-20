package stg.game.enemy;

import stg.game.ui.GameCanvas;
import java.awt.*;

/**
 * 敌方类 - 所有敌人的基类
 * @Time 2026-01-19
 */
public class Enemy {
	protected float x; // X坐标
	protected float y; // Y坐标
	protected float vx; // X方向速度
	protected float vy; // Y方向速度
	protected float size; // 敌人大小
	protected Color color; // 敌人颜色
	protected int hp; // 生命值
	protected int maxHp; // 最大生命值
	protected GameCanvas gameCanvas; // 游戏画布引用
	protected boolean alive; // 存活状态

	public Enemy(int x, int y) {
		this(x, y, 0, 0, 20, Color.BLUE, 10, null);
	}

	public Enemy(float x, float y) {
		this(x, y, 0, 0, 20, Color.BLUE, 10, null);
	}

	public Enemy(float x, float y, float vx, float vy, float size, Color color, int hp, GameCanvas gameCanvas) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.size = size;
		this.color = color;
		this.hp = hp;
		this.maxHp = hp;
		this.gameCanvas = gameCanvas;
		this.alive = true;
	}

	/**
	 * 更新敌人状态
	 * @Time 2026-01-19 基类提供基本移动和存活检测
	 */
	public void update() {
		// 更新位置
		x += vx;
		y += vy;

		// 检查生命值
		if (hp <= 0) {
			alive = false;
		}

		// 子类可重写此方法添加特定行为
	}

	/**
	 * 渲染敌人 - @Time 2026-01-19 使用中心原点坐标系
	 * @param g 图形上下文
	 * @Time 2026-01-19 基类提供基本渲染,子类可自定义渲染
	 */
	public void render(Graphics2D g) {
		int canvasWidth = gameCanvas != null ? gameCanvas.getWidth() : 548;
		int canvasHeight = gameCanvas != null ? gameCanvas.getHeight() : 921;
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;

		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		renderHealthBar(g, screenX, screenY);
	}

	/**
	 * 渲染生命值条 - @Time 2026-01-19 使用屏幕坐标
	 * @param g 图形上下文
	 * @param screenX 屏幕X坐标
	 * @param screenY 屏幕Y坐标
	 * @Time 2026-01-19 在敌人上方显示生命值
	 */
	protected void renderHealthBar(Graphics2D g, float screenX, float screenY) {
		int barWidth = (int)(size * 2);
		int barHeight = 4;
		int barX = (int)(screenX - size);
		int barY = (int)(screenY - size - 8);

		// 背景
		g.setColor(Color.GRAY);
		g.fillRect(barX, barY, barWidth, barHeight);

		// 生命值
		float hpPercent = (float)hp / maxHp;
		g.setColor(Color.RED);
		g.fillRect(barX, barY, (int)(barWidth * hpPercent), barHeight);
	}

	/**
	 * 受到伤害
	 * @param damage 伤害值
	 * @Time 2026-01-19 处理伤害逻辑
	 */
	public void takeDamage(int damage) {
		hp -= damage;
		if (hp <= 0) {
			hp = 0;
			alive = false;
			onDeath(); // 调用死亡回调
		}
	}

	/**
	 * 死亡回调 - 子类可重写
	 * @Time 2026-01-19 敌人死亡时触发
	 */
	protected void onDeath() {
		// 子类可以重写此方法添加死亡特效、掉落物等
	}

	/**
	 * 检查是否越界 - @Time 2026-01-19 使用中心原点坐标系
	 * @param canvasWidth 画布宽度
	 * @param canvasHeight 画布高度
	 * @return 是否越界
	 */
	public boolean isOutOfBounds(int canvasWidth, int canvasHeight) {
		if (canvasWidth <= 0 || canvasHeight <= 0) {
			return false;
		}

		float leftBound = -canvasWidth / 2.0f - size * 2;
		float rightBound = canvasWidth / 2.0f + size * 2;
		float topBound = -canvasHeight / 2.0f - size * 2;
		float bottomBound = canvasHeight / 2.0f + size * 2;

		return x < leftBound || x > rightBound ||
		       y < topBound || y > bottomBound;
	}

	/**
	 * 检查是否存活
	 * @return 是否存活
	 */
	public boolean isAlive() {
		return alive;
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
	 * 设置位置
	 * @param x X坐标
	 * @param y Y坐标
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @Time 2026-01-19 移动到指定坐标
	 * @param x 目标X坐标
	 * @param y 目标Y坐标
	 */
	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @Time 2026-01-19 在现有坐标基础上增加对应值
	 * @param dx X方向增量
	 * @param dy Y方向增量
	 */
	public void moveOn(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}

	/**
	 * 获取敌人大小
	 * @return 大小
	 */
	public float getSize() {
		return size;
	}

	/**
	 * 设置游戏画布
	 * @param gameCanvas 游戏画布
	 */
	public void setGameCanvas(GameCanvas gameCanvas) {
		this.gameCanvas = gameCanvas;
	}

	/**
	 * 获取当前生命值
	 * @return 生命值
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * 设置生命值
	 * @param hp 生命值
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}

	/**
	 * 获取最大生命值
	 * @return 最大生命值
	 */
	public int getMaxHp() {
		return maxHp;
	}
}
