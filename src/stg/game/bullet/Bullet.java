package stg.game.bullet;

import stg.game.GameCanvas;
import java.awt.*;

/**
 * 子弹类 - @Time 2026-01-19 使用中心原点坐标系
 */
public class Bullet {
	protected float x; // X坐标
	protected float y; // Y坐标
	protected float vx; // X方向速度
	protected float vy; // Y方向速度
	protected float size; // 子弹大小
	protected Color color; // 子弹颜色
	protected GameCanvas gameCanvas; // @Time 2026-01-19 画布引用,用于坐标转换

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 */
	public Bullet(float x, float y, float vx, float vy, float size, Color color) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.size = size;
		this.color = color;
	}

	/**
	 * 更新子弹位置
	 */
	public void update() {
		x += vx;
		y += vy;
	}

	/**
	 * 渲染子弹 - @Time 2026-01-19 使用中心原点坐标系
	 * @param g 图形上下文
	 */
	public void render(Graphics2D g) {
		// @Time 2026-01-19 转换为屏幕坐标(右上角为(+,+))
		int canvasWidth = gameCanvas != null ? gameCanvas.getWidth() : 548;
		int canvasHeight = gameCanvas != null ? gameCanvas.getHeight() : 921;
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;

		g.setColor(color);
		g.fillOval((int)(screenX - size/2), (int)(screenY - size/2), (int)size, (int)size);
	}

	/**
	 * @Time 2026-01-19 设置画布引用
	 */
	public void setGameCanvas(GameCanvas gameCanvas) {
		this.gameCanvas = gameCanvas;
	}

	/**
	 * 获取X坐标
	 */
	public float getX() {
		return x;
	}

	/**
	 * 获取Y坐标
	 */
	public float getY() {
		return y;
	}

	/**
	 * 获取子弹大小
	 */
	public float getSize() {
		return size;
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
	 * 检查子弹是否超出边界 - @Time 2026-01-19 使用中心原点坐标系
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
}
