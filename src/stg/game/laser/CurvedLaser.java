package stg.game.laser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 曲线激光类 - 继承自Laser，具有可调节长度的拖尾系统
 * @Time 2026-01-21
 */
public class CurvedLaser extends Laser {
	private float vx; // X方向速度
	private float vy; // Y方向速度
	private List<Point> trailPoints; // 拖尾轨迹点
	private int maxTrailLength; // 最大拖尾长度(帧)
	private float trailWidth; // 拖尾宽度

	/**
	 * 轨迹点类 - 记录激光的历史位置
	 */
	private static class Point {
		float x;
		float y;
		float width;

		Point(float x, float y, float width) {
			this.x = x;
			this.y = y;
			this.width = width;
		}
	}

	/**
	 * 构造函数
	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param maxTrailLength 最大拖尾长度(帧)
	 */
	public CurvedLaser(float x, float y, float angle, float length, float width, Color color,
					   float vx, float vy, int maxTrailLength) {
		this(x, y, angle, length, width, color, 60, 10, vx, vy, maxTrailLength);
	}

	/**
	 * 完整构造函数
	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 * @param warningTime 预警时间
	 * @param damage 伤害值
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param maxTrailLength 最大拖尾长度(帧)
	 */
	public CurvedLaser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage, float vx, float vy, int maxTrailLength) {
		super(x, y, angle, length, width, color, warningTime, damage);
		this.vx = vx;
		this.vy = vy;
		this.maxTrailLength = maxTrailLength;
		this.trailWidth = width;
		this.trailPoints = new ArrayList<>();
	}

	/**
	 * 初始化行为参数
	 */
	@Override
	protected void initBehavior() {
		// 初始化行为参数
	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		// 添加轨迹点
		if (active) {
			trailPoints.add(new Point(x, y, width));

			// 限制轨迹长度
			if (trailPoints.size() > maxTrailLength) {
				trailPoints.remove(0);
			}
		}
	}

	/**
	 * 实现自定义移动逻辑
	 */
	@Override
	protected void onMove() {
		if (active) {
			// 更新位置
			x += vx;
			y += vy;

			// 更新角度
			if (vx != 0 || vy != 0) {
				angle = (float)Math.atan2(vy, vx);
			}
		}
	}

	/**
	 * 更新激光状态
	 */
	@Override
	public void update() {
		super.update();
	}

	/**
	 * 渲染激光
	 * @param g2d 图形上下文
	 */
	@Override
	protected void renderLaser(Graphics2D g2d) {
		// 渲染拖尾(从后向前)
		for (int i = 0; i < trailPoints.size() - 1; i++) {
			Point p1 = trailPoints.get(i);
			Point p2 = trailPoints.get(i + 1);

			float[] screenStart = gameCanvas.toScreenCoords(p1.x, p1.y);
			float[] screenEnd = gameCanvas.toScreenCoords(p2.x, p2.y);

			// 渐变透明度
			float alpha = (float)(i + 1) / trailPoints.size();
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();

			// 绘制拖尾段
			g2d.setColor(new Color(r, g, b, (int)(alpha * 200)));
			float segmentWidth = p1.width * alpha;
			g2d.setStroke(new BasicStroke(segmentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.drawLine((int)screenStart[0], (int)screenStart[1], (int)screenEnd[0], (int)screenEnd[1]);
		}

		// 渲染头部
		if (!trailPoints.isEmpty()) {
			Point head = trailPoints.get(trailPoints.size() - 1);
			float[] screenHead = gameCanvas.toScreenCoords(head.x, head.y);

			g2d.setColor(color);
			g2d.fillOval((int)(screenHead[0] - head.width/2), (int)(screenHead[1] - head.width/2),
						(int)head.width, (int)head.width);
		}
	}

	/**
	 * 检查点是否在激光碰撞体内
	 * @param px 点X坐标
	 * @param py 点Y坐标
	 * @return 是否碰撞
	 */
	@Override
	public boolean checkCollision(float px, float py) {
		if (!active || !visible) return false;

		// 检查每个拖尾段的碰撞
		for (int i = 0; i < trailPoints.size() - 1; i++) {
			Point p1 = trailPoints.get(i);
			Point p2 = trailPoints.get(i + 1);

			// 计算线段角度和长度
			float dx = p2.x - p1.x;
			float dy = p2.y - p1.y;
			float segmentAngle = (float)Math.atan2(dy, dx);
			float segmentLength = (float)Math.sqrt(dx * dx + dy * dy);

			// 检查点到线段的距离
			float distance = pointToLineDistance(px, py, p1.x, p1.y, segmentAngle, segmentLength);
			if (distance < p1.width / 2.0f) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 检查激光是否超出边界
	 * @param width 画布宽度
	 * @param height 画布高度
	 * @return 是否超出边界
	 */
	public boolean isOutOfBounds(int width, int height) {
		float leftBound = -width / 2.0f - length;
		float rightBound = width / 2.0f + length;
		float topBound = -height / 2.0f - length;
		float bottomBound = height / 2.0f + length;
		return x < leftBound || x > rightBound || y < topBound || y > bottomBound;
	}

	// Getter和Setter方法
	public float getVx() { return vx; }
	public float getVy() { return vy; }
	public int getMaxTrailLength() { return maxTrailLength; }
	public float getTrailWidth() { return trailWidth; }
	public int getCurrentTrailLength() { return trailPoints.size(); }

	public void setVx(float vx) { this.vx = vx; }
	public void setVy(float vy) { this.vy = vy; }
	public void setMaxTrailLength(int maxTrailLength) { this.maxTrailLength = maxTrailLength; }
	public void setTrailWidth(float trailWidth) { this.trailWidth = trailWidth; }

	/**
	 * 设置速度
	 * @param speed 速度大小
	 * @param angle 角度(弧度)
	 */
	public void setSpeed(float speed, float angle) {
		this.vx = (float)Math.cos(angle) * speed;
		this.vy = (float)Math.sin(angle) * speed;
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
