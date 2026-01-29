package stg.game.item;

import java.awt.*;
import stg.game.ui.GameCanvas;

/**
 * 物品类 - 所有物品的基类
 * 包括道具、掉落物、特殊物品等
 */
public class Item {
	protected float x; // X坐标
	protected float y; // Y坐标
	protected float vx; // X方向速度
	protected float vy; // Y方向速度
	protected float size; // 物品大小
	protected Color color; // 物品颜色
	protected GameCanvas gameCanvas; // 游戏画布引用
	protected boolean active; // 激活状态
	protected float hitboxRadius; // 碰撞判定半径
	protected int frame; // 帧计数器

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param size 物品大小
	 * @param color 物品颜色
	 */
	public Item(float x, float y, float size, Color color) {
		this(x, y, 0, 0, size, color, null);
	}

	/**
	 * 构造函数（带游戏画布）
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param size 物品大小
	 * @param color 物品颜色
	 * @param gameCanvas 游戏画布引用
	 */
	public Item(float x, float y, float size, Color color, GameCanvas gameCanvas) {
		this(x, y, 0, 0, size, color, gameCanvas);
	}

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 物品大小
	 * @param color 物品颜色
	 */
	public Item(float x, float y, float vx, float vy, float size, Color color) {
		this(x, y, vx, vy, size, color, null);
	}

	/**
	 * 完整构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 物品大小
	 * @param color 物品颜色
	 * @param gameCanvas 游戏画布引用
	 */
	public Item(float x, float y, float vx, float vy, float size, Color color, GameCanvas gameCanvas) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.size = size;
		this.color = color;
		this.gameCanvas = gameCanvas;
		this.active = true;
		this.hitboxRadius = size;
		this.frame = 0;
	}

	/**
	 * 更新物品状态
	 * 子类可重写此方法添加特定行为
	 */
	public void update() {
		frame++;

		x += vx;
		y += vy;

		// 检查是否超出边界
		if (gameCanvas != null && isOutOfBounds(gameCanvas.getWidth(), gameCanvas.getHeight())) {
			active = false;
		}
	}

	/**
	 * 渲染物品
	 * @param g 图形上下文
	 */
	public void render(Graphics2D g) {
		if (!active) return;

		float screenX = x;
		float screenY = y;

		if (gameCanvas != null) {
			float[] coords = gameCanvas.getCoordinateSystem().toScreenCoords(x, y);
			screenX = coords[0];
			screenY = coords[1];
		} else {
			screenX = x + 548 / 2.0f;
			screenY = 921 / 2.0f - y;
		}

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		// 绘制高光效果
		g.setColor(new Color(255, 255, 255, 150));
		g.fillOval((int)(screenX - size * 0.4f), (int)(screenY - size * 0.4f), (int)(size * 0.8f), (int)(size * 0.8f));
	}

	/**
	 * 检查物品是否超出边界
	 * @param width 画布宽度
	 * @param height 画布高度
	 * @return 是否超出边界
	 */
	public boolean isOutOfBounds(int width, int height) {
		float leftBound = -width / 2.0f - size;
		float rightBound = width / 2.0f + size;
		float topBound = -height / 2.0f - size;
		float bottomBound = height / 2.0f + size;
		return x < leftBound || x > rightBound || y < topBound || y > bottomBound;
	}

	/**
	 * 设置游戏画布
	 * @param gameCanvas 游戏画布
	 */
	public void setGameCanvas(GameCanvas gameCanvas) {
		this.gameCanvas = gameCanvas;
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
	 * 获取物品大小
	 * @return 物品大小
	 */
	public float getSize() {
		return size;
	}

	/**
	 * 获取碰撞判定半径
	 * @return 碰撞判定半径
	 */
	public float getHitboxRadius() {
		return hitboxRadius;
	}

	/**
	 * 设置碰撞判定半径
	 * @param hitboxRadius 碰撞判定半径
	 */
	public void setHitboxRadius(float hitboxRadius) {
		this.hitboxRadius = hitboxRadius;
	}

	/**
	 * 获取物品颜色
	 * @return 物品颜色
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 设置物品颜色
	 * @param color 物品颜色
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 检查物品是否激活
	 * @return 是否激活
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * 设置物品激活状态
	 * @param active 激活状态
	 */
	public void setActive(boolean active) {
		this.active = active;
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
	 * 设置速度
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 */
	public void setVelocity(float vx, float vy) {
		this.vx = vx;
		this.vy = vy;
	}

	/**
	 * 获取帧计数器
	 * @return 帧计数器
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * 物品被玩家拾取时的处理
	 * 子类可重写此方法实现特定效果
	 */
	public void onCollect() {
		active = false;
	}

	/**
	 * 重置物品状态
	 */
	public void reset() {
		frame = 0;
		active = true;
	}
}
