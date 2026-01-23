package stg.game.bullet;

import stg.game.ui.GameCanvas;
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
	protected int damage = 0; // @Time 2026-01-23 子弹伤害，默认0（由玩家统一控制）
	protected float hitboxRadius = 0; // @Time 2026-01-23 碰撞判定半径，默认为0表示使用size

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
		// @Time 2026-01-23 设置碰撞判定半径为size的5倍，确保高速子弹不会穿透敌人
		this.hitboxRadius = size * 5.0f;
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
	 * 转向功能 - 旋转速度方向
	 * @param angle 转向角度(弧度),逆时针为正,顺时针为负
	 * @Time 2026-01-22 添加转向控制
	 */
	public void turnBy(float angle) {
		// 将当前速度向量旋转指定角度
		// 旋转公式:
		// new_vx = vx * cos(angle) - vy * sin(angle)
		// new_vy = vx * sin(angle) + vy * cos(angle)
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
	 * @Time 2026-01-22 添加加速度控制(绝对值+角度模式)
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
	 * @Time 2026-01-22 添加加速度控制(分量模式)
	 */
	public void accelerateByComponent(float ax, float ay) {
		this.vx += ax;
		this.vy += ay;
	}

	// ========== 速度查询 ==========

	/**
	 * 【模式1: 绝对值+角度】获取当前速度角度(弧度)
	 * @return 速度角度,x轴正轴为0,向第一象限为正
	 * @Time 2026-01-22 添加获取速度方向
	 */
	public float getVelocityAngle() {
		return (float)Math.atan2(vy, vx);
	}

	/**
	 * 【模式2: 分量】获取X轴速度分量
	 * @return X轴速度(像素/帧)
	 * @Time 2026-01-22 添加获取速度分量
	 */
	public float getVelocityX() {
		return vx;
	}

	/**
	 * 【模式2: 分量】获取Y轴速度分量
	 * @return Y轴速度(像素/帧)
	 * @Time 2026-01-22 添加获取速度分量
	 */
	public float getVelocityY() {
		return vy;
	}

	/**
	 * 【模式1: 绝对值+角度】获取当前速度大小(像素/帧)
	 * @return 速度大小
	 * @Time 2026-01-22 添加获取速度大小
	 */
	public float getVelocitySpeed() {
		return (float)Math.sqrt(vx * vx + vy * vy);
	}

	// ========== 伤害相关 ==========

	/**
	 * 获取子弹伤害
	 * @return 子弹伤害值
	 * @Time 2026-01-23 添加伤害获取方法
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * 设置子弹伤害
	 * @param damage 伤害值
	 * @Time 2026-01-23 添加伤害设置方法
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * 获取碰撞判定半径
	 * @return 碰撞判定半径，如果未设置则返回渲染大小
	 * @Time 2026-01-23 添加碰撞判定半径获取方法
	 */
	public float getHitboxRadius() {
		return hitboxRadius > 0 ? hitboxRadius : size;
	}

	/**
	 * 设置碰撞判定半径
	 * @param hitboxRadius 碰撞判定半径
	 * @Time 2026-01-23 添加碰撞判定半径设置方法
	 */
	public void setHitboxRadius(float hitboxRadius) {
		this.hitboxRadius = hitboxRadius;
	}

	// ========== 速度设置 ==========

	/**
	 * 【模式1: 绝对值+角度】设置速度方向和大小
	 * @param speed 速度大小(像素/帧)
	 * @param angle 速度方向角度(弧度),x轴正轴为0,向第一象限为正
	 * @Time 2026-01-22 添加设置速度(绝对值+角度模式)
	 */
	public void setVelocity(float speed, float angle) {
		this.vx = speed * (float)Math.cos(angle);
		this.vy = speed * (float)Math.sin(angle);
	}

	/**
	 * 【模式2: 分量】设置速度的x和y分量
	 * @param vx X方向速度(像素/帧)
	 * @param vy Y方向速度(像素/帧)
	 * @Time 2026-01-22 添加设置速度(分量模式)
	 */
	public void setVelocityByComponent(float vx, float vy) {
		this.vx = vx;
		this.vy = vy;
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
