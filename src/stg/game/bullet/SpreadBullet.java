package stg.game.bullet;

import stg.game.enemy.EnemyBullet;
import java.awt.*;

/**
 * 散射弹幕类 - EnemyBullet的子类
 * @Time 2026-01-19 带有尾迹效果的弹幕
 */
public class SpreadBullet extends EnemyBullet {
	private float trailLength; // 尾迹长度
	private Color trailColor; // 尾迹颜色

	/**
	 * 构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 */
	public SpreadBullet(float x, float y, float vx, float vy) {
		super(x, y, vx, vy, 4, Color.ORANGE, 10);
		this.trailLength = 10;
		this.trailColor = new Color(255, 165, 0, 100); // 半透明橙色
	}

	/**
	 * 完整构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 * @param trailLength 尾迹长度
	 * @param damage 伤害值
	 */
	public SpreadBullet(float x, float y, float vx, float vy, float size, Color color, float trailLength, int damage) {
		super(x, y, vx, vy, size, color, damage);
		this.trailLength = trailLength;
		this.trailColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
	}

	/**
	 * @Time 2026-01-19 重写渲染方法,添加尾迹效果
	 */
	@Override
	public void render(Graphics2D g) {
		// 绘制尾迹
		g.setColor(trailColor);
		float trailVx = vx * -0.1f;
		float trailVy = vy * -0.1f;
		g.drawLine((int)x, (int)y, (int)(x + trailVx * trailLength), (int)(y + trailVy * trailLength));

		// 绘制子弹主体
		g.setColor(color);
		g.fillOval((int)(x - size), (int)(y - size), (int)(size * 2), (int)(size * 2));
	}
}
