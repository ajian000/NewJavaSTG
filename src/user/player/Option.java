package user.player;

import java.awt.*;
import stg.game.ui.GameCanvas;

/**
 * 子机基类 - 所有子机的父类
 * 参考东方正作设计：
 * - 子机跟随玩家移动
 * - 子机可以发射子弹
 * - 不同角色的子机有不同的行为模�? * - 子机通常有相对位置和延迟跟随
 */
public abstract class Option {
	protected float x; // 子机X坐标
	protected float y; // 子机Y坐标
	protected float targetX; // 目标X坐标
	protected float targetY; // 目标Y坐标
	protected float offsetX; // 相对于玩家的X偏移
	protected float offsetY; // 相对于玩家的Y偏移
	protected float followSpeed; // 跟随速度(0-1,越小越慢)
	protected float size; // 子机大小
	protected Color color; // 子机颜色
	protected GameCanvas gameCanvas; // 游戏画布引用
	protected Player player; // 玩家引用
	protected boolean shooting; // 射击标志
	protected int shootCooldown; // 射击冷却时间
	protected int shootInterval; // 射击间隔
	protected int bulletDamage; // 子弹伤害

	public Option(Player player, float offsetX, float offsetY, GameCanvas gameCanvas) {
		this.player = player;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.gameCanvas = gameCanvas;
		this.followSpeed = 0.25f; // 默认跟随速度（提高响应速度�?		this.size = 8.0f;
		this.color = new Color(150, 200, 255);
		this.shooting = false;
		this.shootCooldown = 0;
		this.shootInterval = 1;
		this.bulletDamage = 1;

		// 初始位置设为玩家位置
		this.x = player.getX() + offsetX;
		this.y = player.getY() + offsetY;
		this.targetX = x;
		this.targetY = y;
	}

	/**
	 * 更新子机状�?	 */
	public void update() {
		// 更新目标位置（玩家位�?+ 偏移�?		targetX = player.getX() + offsetX;
		targetY = player.getY() + offsetY;

		// 平滑跟随玩家
		float dx = targetX - x;
		float dy = targetY - y;
		x += dx * followSpeed;
		y += dy * followSpeed;

		// 更新射击冷却
		if (shootCooldown > 0) {
			shootCooldown--;
		}

		// 射击逻辑
		if (shooting && shootCooldown == 0) {
			shoot();
			shootCooldown = shootInterval;
		}
	}

	/**
	 * 发射子弹 - 子类重写实现不同射击模式
	 */
	protected abstract void shoot();

	/**
	 * 渲染子机
	 * @param g 图形上下�?	 */
	public void render(Graphics2D g) {
		float screenX = x + gameCanvas.getWidth() / 2.0f;
		float screenY = gameCanvas.getHeight() / 2.0f - y;

		stg.util.RenderUtils.enableAntiAliasing(g);

		// 绘制子机主体
		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size),
		          (int)(size * 2), (int)(size * 2));

		// 绘制子机核心（亮点）
		g.setColor(new Color(255, 255, 255, 200));
		g.fillOval((int)(screenX - size * 0.4f), (int)(screenY - size * 0.4f),
		          (int)(size * 0.8f), (int)(size * 0.8f));
	}

	/**
	 * 设置射击状�?	 * @param shooting 是否射击
	 */
	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	/**
	 * 设置相对偏移
	 * @param offsetX X偏移
	 * @param offsetY Y偏移
	 */
	public void setOffset(float offsetX, float offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	/**
	 * 设置跟随速度
	 * @param speed 跟随速度(0-1)
	 * @throws IllegalArgumentException 如果速度不在0-1之间
	 */
	public void setFollowSpeed(float speed) {
		if (speed < 0 || speed > 1) {
			throw new IllegalArgumentException("跟随速度必须�?-1之间，当前�? " + speed);
		}
		this.followSpeed = speed;
	}

	/**
	 * 设置子机大小
	 * @param size 大小
	 * @throws IllegalArgumentException 如果大小为负数或�?	 */
	public void setSize(float size) {
		if (size <= 0) {
			throw new IllegalArgumentException("子机大小必须为正数，当前�? " + size);
		}
		this.size = size;
	}

	/**
	 * 设置子机颜色
	 * @param color 颜色
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 设置射击间隔
	 * @param interval 射击间隔(帧数)
	 * @throws IllegalArgumentException 如果间隔为负�?	 */
	public void setShootInterval(int interval) {
		if (interval < 0) {
			throw new IllegalArgumentException("射击间隔不能为负数，当前�? " + interval);
		}
		this.shootInterval = interval;
	}

	/**
	 * 设置子弹伤害
	 * @param damage 伤害�?	 * @throws IllegalArgumentException 如果伤害值为负数
	 */
	public void setBulletDamage(int damage) {
		if (damage < 0) {
			throw new IllegalArgumentException("子弹伤害不能为负数，当前�? " + damage);
		}
		this.bulletDamage = damage;
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
	 * 获取子机大小
	 * @return 大小
	 */
	public float getSize() {
		return size;
	}

	/**
	 * 获取子弹伤害
	 * @return 伤害�?	 */
	public int getBulletDamage() {
		return bulletDamage;
	}

	/**
	 * 重置子机状�?	 */
	public void reset() {
		x = player.getX() + offsetX;
		y = player.getY() + offsetY;
		targetX = x;
		targetY = y;
		shootCooldown = 0;
	}
}

