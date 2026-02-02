package stg.game.laser;

import java.awt.*;
import stg.game.ui.GameCanvas;

/**
 * 激光基�?- 所有激光的父类
 * @since 2026-01-21
 */
public abstract class Laser {
	protected float x; // 激光起点X坐标
	protected float y; // 激光起点Y坐标
	protected float angle; // 激光角�?弧度)
	protected float length; // 激光长�?	protected float width; // 激光宽�?	protected Color color; // 激光颜�?	protected GameCanvas gameCanvas; // 画布引用
	protected boolean warningOnly; // 是否仅显示预警线
	protected int warningTime; // 预警持续时间(�?
	protected int warningTimer; // 预警计时�?	protected boolean active; // 激光是否激�?预警结束�?
	protected boolean visible; // 激光是否可�?	protected int damage; // 伤害�?
	/**
	 * 构造函�?	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 */
	public Laser(float x, float y, float angle, float length, float width, Color color) {
		this(x, y, angle, length, width, color, 60, 10);
	}

	/**
	 * 完整构造函�?	 * @param x 起点X坐标
	 * @param y 起点Y坐标
	 * @param angle 角度(弧度)
	 * @param length 长度
	 * @param width 宽度
	 * @param color 颜色
	 * @param warningTime 预警时间(�?
	 * @param damage 伤害�?	 */
	public Laser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.length = length;
		this.width = width;
		this.color = color;
		this.warningTime = warningTime;
		this.warningTimer = warningTime;
		this.damage = damage;
		this.warningOnly = false;
		this.active = false;
		this.visible = true;
		initBehavior();
	}
	
	/**
	 * 初始化行为参�?	 * 在构造函数中调用，用于初始化行为参数
	 */
	protected abstract void initBehavior();

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	protected abstract void onUpdate();

	/**
	 * 实现自定义移动逻辑
	 */
	protected abstract void onMove();

	/**
	 * 更新激光状�?	 */
	public void update() {
		// 调用自定义更新逻辑
		onUpdate();

		// 调用自定义移动逻辑
		onMove();

		if (warningTimer > 0) {
			warningTimer--;
			if (warningTimer == 0 && !warningOnly) {
				active = true;
			}
		}
	}

	/**
	 * 渲染激�?	 * @param g 图形上下�?	 */
	public void render(Graphics g) {
		if (!visible) return;

		// 设置抗锯�?		Graphics2D g2d = (Graphics2D) g.create();
		stg.util.RenderUtils.enableAntiAliasing(g2d);

		if (!active) {
			// 渲染预警�?			renderWarningLine(g2d);
		} else {
			// 渲染实际激�?			renderLaser(g2d);
		}

		g2d.dispose();
	}

	/**
	 * 渲染预警�?	 * @param g2d 图形上下�?	 */
	protected void renderWarningLine(Graphics2D g2d) {
		float[] screenStart = gameCanvas.toScreenCoords(x, y);
		float endX = x + (float)(Math.cos(angle) * length);
		float endY = y + (float)(Math.sin(angle) * length);
		float[] screenEnd = gameCanvas.toScreenCoords(endX, endY);

		// 绘制虚线预警
		g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
		g2d.setStroke(new BasicStroke(width * 0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.drawLine((int)screenStart[0], (int)screenStart[1], (int)screenEnd[0], (int)screenEnd[1]);
	}

	/**
	 * 渲染实际激�?	 * @param g2d 图形上下�?	 */
	protected void renderLaser(Graphics2D g2d) {
		float[] screenStart = gameCanvas.toScreenCoords(x, y);
		float endX = x + (float)(Math.cos(angle) * length);
		float endY = y + (float)(Math.sin(angle) * length);
		float[] screenEnd = gameCanvas.toScreenCoords(endX, endY);

		// 绘制激光核�?		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.drawLine((int)screenStart[0], (int)screenStart[1], (int)screenEnd[0], (int)screenEnd[1]);

		// 绘制高光
		g2d.setColor(new Color(255, 255, 255, 150));
		g2d.setStroke(new BasicStroke(width * 0.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.drawLine((int)screenStart[0], (int)screenStart[1], (int)screenEnd[0], (int)screenEnd[1]);
	}

	/**
	 * 检查点是否在激光碰撞体�?	 * @param px 点X坐标
	 * @param py 点Y坐标
	 * @return 是否碰撞
	 */
	public boolean checkCollision(float px, float py) {
		if (!active || !visible) return false;
		return pointToLineDistance(px, py, x, y, angle, length) < width / 2.0f;
	}

	/**
	 * 检查激光是否超出边�?	 * @param width 画布宽度
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

	/**
	 * 计算点到线段的距�?	 * @param px 点X
	 * @param py 点Y
	 * @param lx 线段起点X
	 * @param ly 线段起点Y
	 * @param angle 线段角度
	 * @param len 线段长度
	 * @return 距离
	 */
	protected float pointToLineDistance(float px, float py, float lx, float ly, float angle, float len) {
		// 将点转换到激光的局部坐标系
		float dx = px - lx;
		float dy = py - ly;
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);

		// 旋转坐标
		float localX = dx * cos + dy * sin;
		float localY = -dx * sin + dy * cos;

		// 检查点是否在线段范围内
		if (localX < 0) {
			return (float)Math.sqrt(dx * dx + dy * dy);
		} else if (localX > len) {
			float endX = lx + cos * len;
			float endY = ly + sin * len;
			return (float)Math.sqrt(Math.pow(px - endX, 2) + Math.pow(py - endY, 2));
		} else {
			return Math.abs(localY);
		}
	}

	// Getter和Setter方法
	public float getX() { return x; }
	public float getY() { return y; }
	public float getAngle() { return angle; }
	public float getLength() { return length; }
	public float getWidth() { return width; }
	public Color getColor() { return color; }
	public boolean isWarningOnly() { return warningOnly; }
	public int getWarningTime() { return warningTime; }
	public boolean isActive() { return active; }
	public boolean isVisible() { return visible; }
	public int getDamage() { return damage; }

	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	public void setAngle(float angle) { this.angle = angle; }
	public void setLength(float length) { this.length = length; }
	public void setWidth(float width) { this.width = width; }
	public void setColor(Color color) { this.color = color; }
	public void setWarningOnly(boolean warningOnly) { this.warningOnly = warningOnly; }
	public void setWarningTime(int warningTime) { this.warningTime = warningTime; }
	public void setVisible(boolean visible) { this.visible = visible; }
	public void setDamage(int damage) { this.damage = damage; }

	/**
 * 重置激光状�? * @since 2026-01-29
 */
	public void reset() {
		warningTimer = warningTime;
		active = false;
		visible = true;
		initBehavior();
	}

	/**
	 * 设置画布引用
	 */
	public void setGameCanvas(GameCanvas gameCanvas) {
		this.gameCanvas = gameCanvas;
	}

	/**
	 * 任务开始时触发的方�?- 用于处理开局对话�?	 */
	protected abstract void onTaskStart();

	/**
	 * 任务结束时触发的方法 - 用于处理boss击破对话和道具掉�?	 */
	protected abstract void onTaskEnd();
}

