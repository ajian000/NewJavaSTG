package stg.game;

import java.awt.*;

/**
 * 圆形弹幕类 - EnemyBullet的子类
 * @Time 2026-01-19 标准圆形弹幕,带有渐变效果
 */
public class CircularBullet extends EnemyBullet {
	private Color innerColor; // 内圈颜色

	/**
	 * 构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 */
	public CircularBullet(float x, float y, float vx, float vy) {
		super(x, y, vx, vy, 5, Color.CYAN, 10);
		this.innerColor = Color.WHITE;
	}

	/**
	 * 完整构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 子弹大小
	 * @param color 外圈颜色
	 * @param innerColor 内圈颜色
	 * @param damage 伤害值
	 */
	public CircularBullet(float x, float y, float vx, float vy, float size, Color color, Color innerColor, int damage) {
		super(x, y, vx, vy, size, color, damage);
		this.innerColor = innerColor;
	}

	/**
	 * @Time 2026-01-19 重写渲染方法,添加渐变效果
	 */
	@Override
	public void render(Graphics2D g) {
		// @Time 2026-01-19 将中心原点坐标转换为屏幕坐标
		// 坐标系: 右上角为(+,+),左下角为(-,-)
		int canvasWidth = 548;
		int canvasHeight = 921;
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;

		// 绘制外圈
		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		// 绘制内圈(渐变效果)
		g.setColor(innerColor);
		float innerSize = size * 0.6f;
		g.fillOval((int)(screenX - innerSize), (int)(screenY - innerSize), (int)(innerSize * 2), (int)(innerSize * 2));
	}
}
