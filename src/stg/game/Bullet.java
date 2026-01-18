package stg.game;

import java.awt.*;

/**
 * 子弹类
 */
public class Bullet {
	private float x; // X坐标
	private float y; // Y坐标
	private float vx; // X方向速度
	private float vy; // Y方向速度
	private float size; // 子弹大小
	private Color color; // 子弹颜色

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
	 * 渲染子弹
	 * @param g 图形上下文
	 */
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int)(x - size/2), (int)(y - size/2), (int)size, (int)size);
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
	 * 检查子弹是否超出边界
	 * @param width 画布宽度
	 * @param height 画布高度
	 * @return 是否超出边界
	 */
	public boolean isOutOfBounds(int width, int height) {
		return x < -size || x > width + size || y < -size || y > height + size;
	}
}
