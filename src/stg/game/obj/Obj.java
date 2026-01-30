package stg.game.obj;

import java.awt.*;
import stg.game.ui.GameCanvas;

/**
 * 游戏物体基类 - 所有游戏中的物体都继承自此类
 * @since 2026-01-29
 */
public abstract class Obj {
	protected float x; // X坐标
	protected float y; // Y坐标
	protected float vx; // X方向速度
	protected float vy; // Y方向速度
	protected float size; // 物体大小
	protected Color color; // 物体颜色
	protected GameCanvas gameCanvas; // 游戏画布引用
	protected float hitboxRadius; // 碰撞判定半径
	protected boolean active; // 激活状态
	protected int frame; // 帧计数器
	
	// 默认画布尺寸常量
	protected static final float DEFAULT_CANVAS_WIDTH = 548;
	protected static final float DEFAULT_CANVAS_HEIGHT = 921;

	/**
	 * 将游戏坐标转换为屏幕坐标
	 * @param worldX 游戏世界X坐标
	 * @param worldY 游戏世界Y坐标
	 * @return 屏幕坐标数组 [x, y]
	 */
	protected float[] toScreenCoords(float worldX, float worldY) {
		if (gameCanvas != null) {
			return gameCanvas.getCoordinateSystem().toScreenCoords(worldX, worldY);
		}
		return new float[]{
			worldX + DEFAULT_CANVAS_WIDTH / 2.0f,
			DEFAULT_CANVAS_HEIGHT / 2.0f - worldY
		};
	}

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 物体大小
	 * @param color 物体颜色
	 * @param gameCanvas 游戏画布引用
	 */
	public Obj(float x, float y, float vx, float vy, float size, Color color, GameCanvas gameCanvas) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.size = size;
		this.color = color;
		this.gameCanvas = gameCanvas;
		this.hitboxRadius = size;
		this.active = true;
		this.frame = 0;
		initBehavior();
	}

	/**
	 * 初始化行为参数
	 * 在构造函数中调用，用于初始化行为参数
	 */
	protected void initBehavior() {
		// 子类可以重写此方法初始化行为参数
	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	protected void onUpdate() {
		// 子类可以重写此方法实现每帧的自定义更新逻辑
	}

	/**
	 * 实现自定义移动逻辑
	 */
	protected void onMove() {
		// 子类可以重写此方法实现自定义移动逻辑
	}

	/**
	 * 更新物体状态
	 */
	public void update() {
		frame++;

		// 调用自定义更新逻辑
		onUpdate();

		// 调用自定义移动逻辑
		onMove();

		// 更新位置
		x += vx;
		y += vy;
	}

	/**
	 * 渲染物体
	 * @param g 图形上下文
	 */
	public void render(Graphics2D g) {
		if (!active) return;

		float[] screenCoords = toScreenCoords(x, y);
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		g.setColor(color);
		g.fillOval((int)(screenX - size/2), (int)(screenY - size/2), (int)size, (int)size);
	}

	/**
	 * 检查物体是否超出边界
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
	 * 移动到指定坐标
	 * @param x 目标X坐标
	 * @param y 目标Y坐标
	 */
	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 在现有坐标基础上增加对应值
	 * @param dx X方向增量
	 * @param dy Y方向增量
	 */
	public void moveOn(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}

	/**
	 * 转向功能 - 旋转速度方向
	 * @param angle 转向角度(弧度),逆时针为正,顺时针为负
	 */
	public void turnBy(float angle) {
		// 将当前速度向量旋转指定角度
		float cosAngle = (float)Math.cos(angle);
		float sinAngle = (float)Math.sin(angle);
		float newVx = vx * cosAngle - vy * sinAngle;
		float newVy = vx * sinAngle + vy * cosAngle;
		this.vx = newVx;
		this.vy = newVy;
	}

	// ========== 加速度控制 ==========

	/**
	 * 【模式1: 绝对值+角度】加速功能 - 在指定方向上增加速度
	 * @param acceleration 加速度值(像素/帧)
	 * @param angle 加速度方向角度(弧度),x轴正轴为0,向第一象限为正
	 */
	public void accelerate(float acceleration, float angle) {
		// 将加速度分解为x和y分量
		float ax = acceleration * (float)Math.cos(angle);
		float ay = acceleration * (float)Math.sin(angle);

		// 应用加速度到速度
		this.vx += ax;
		this.vy += ay;
	}

	/**
	 * 【模式2: 分量】加速功能 - 按x和y分量直接增加速度
	 * @param ax X方向加速度(像素/帧²)
	 * @param ay Y方向加速度(像素/帧²)
	 */
	public void accelerateByComponent(float ax, float ay) {
		this.vx += ax;
		this.vy += ay;
	}

	// ========== 速度查询 ==========

	/**
	 * 【模式1: 绝对值+角度】获取当前速度角度(弧度)
	 * @return 速度角度,x轴正轴为0,向第一象限为正
	 */
	public float getVelocityAngle() {
		return (float)Math.atan2(vy, vx);
	}

	/**
	 * 【模式2: 分量】获取X轴速度分量
	 * @return X轴速度(像素/帧)
	 */
	public float getVelocityX() {
		return vx;
	}

	/**
	 * 【模式2: 分量】获取Y轴速度分量
	 * @return Y轴速度(像素/帧)
	 */
	public float getVelocityY() {
		return vy;
	}

	/**
	 * 【模式1: 绝对值+角度】获取当前速度大小(像素/帧)
	 * @return 速度大小
	 */
	public float getVelocitySpeed() {
		return (float)Math.sqrt(vx * vx + vy * vy);
	}

	// ========== 速度设置 ==========

	/**
	 * 【模式1: 绝对值+角度】设置速度方向和大小
	 * @param speed 速度大小(像素/帧)
	 * @param angle 速度方向角度(弧度),x轴正轴为0,向第一象限为正
	 */
	public void setVelocity(float speed, float angle) {
		this.vx = speed * (float)Math.cos(angle);
		this.vy = speed * (float)Math.sin(angle);
	}

	/**
	 * 【模式2: 分量】设置速度的x和y分量
	 * @param vx X方向速度(像素/帧)
	 * @param vy Y方向速度(像素/帧)
	 */
	public void setVelocityByComponent(float vx, float vy) {
		this.vx = vx;
		this.vy = vy;
	}

	/**
	 * 重置物体状态
	 */
	public void reset() {
		this.frame = 0;
		this.active = true;
		initBehavior();
	}

	// ========== Getter 和 Setter 方法 ==========

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
	 * 获取物体大小
	 * @return 物体大小
	 */
	public float getSize() {
		return size;
	}

	/**
	 * 设置物体大小
	 * @param size 物体大小
	 */
	public void setSize(float size) {
		this.size = size;
	}

	/**
	 * 获取物体颜色
	 * @return 物体颜色
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 设置物体颜色
	 * @param color 物体颜色
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 获取碰撞判定半径
	 * @return 碰撞判定半径，如果未设置则返回渲染大小
	 */
	public float getHitboxRadius() {
		return hitboxRadius > 0 ? hitboxRadius : size;
	}

	/**
	 * 设置碰撞判定半径
	 * @param hitboxRadius 碰撞判定半径
	 */
	public void setHitboxRadius(float hitboxRadius) {
		this.hitboxRadius = hitboxRadius;
	}

	/**
	 * 设置游戏画布
	 * @param gameCanvas 游戏画布
	 */
	public void setGameCanvas(GameCanvas gameCanvas) {
		this.gameCanvas = gameCanvas;
	}

	/**
	 * 获取游戏画布
	 * @return 游戏画布
	 */
	public GameCanvas getGameCanvas() {
		return gameCanvas;
	}

	/**
	 * 检查物体是否激活
	 * @return 是否激活
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * 设置物体激活状态
	 * @param active 激活状态
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * 获取帧计数器
	 * @return 帧计数器
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * 任务开始时触发的方法 - 用于处理开局对话等
	 */
	protected void onTaskStart() {
		// 默认实现为空，子类可以选择重写
	}

	/**
	 * 任务结束时触发的方法 - 用于处理boss击破对话和道具掉落
	 */
	protected void onTaskEnd() {
		// 默认实现为空，子类可以选择重写
	}
}