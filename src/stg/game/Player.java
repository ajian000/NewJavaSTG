package stg.game;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 玩家类 - 自机角色
 */
public class Player {
	private float x; // X坐标
	private float y; // Y坐标
	private float vx; // X方向速度
	private float vy; // Y方向速度
	private float speed; // 普通移动速度
	private float speedSlow; // 低速移动速度
	private float size; // 角色大小
	private GameCanvas gameCanvas; // 游戏画布引用

	private boolean slowMode; // 低速模式标志
	private boolean shooting; // 射击标志
	private boolean bombing; // Bomb标志
	private int shootCooldown; // 射击冷却时间
	private int shootInterval = 8; // 射击间隔(帧数)

	/**
	 * 构造函数
	 * @param spawnX 初始X坐标
	 * @param spawnY 初始Y坐标
	 */
	public Player(float spawnX, float spawnY) {
		this.x = spawnX;
		this.y = spawnY;
		this.vx = 0;
		this.vy = 0;
		this.speed = 5.0f;
		this.speedSlow = 2.0f;
		this.size = 20;
		this.slowMode = false;
		this.shooting = false;
		this.bombing = false;
		this.shootCooldown = 0;
	}

	/**
	 * 更新玩家状态
	 */
	public void update() {
		// 更新位置
		x += vx;
		y += vy;

		// 获取画布尺寸
		int canvasWidth = gameCanvas.getWidth();
		int canvasHeight = gameCanvas.getHeight();

		// 边界限制
		if (x < size) x = size;
		if (x > canvasWidth - size) x = canvasWidth - size;
		if (y < size) y = size;
		if (y > canvasHeight - size) y = canvasHeight - size;

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
	 * 发射子弹
	 */
	private void shoot() {
		float bulletSpeed = 10.0f; // 子弹速度
		Color bulletColor = Color.WHITE; // 子弹颜色
		float bulletSize = slowMode ? 6.0f : 4.0f; // 子弹大小(低速模式更大)

		// 发射两发子弹,从玩家中心位置
		Bullet bullet1 = new Bullet(x - 5, y, 0, -bulletSpeed, bulletSize, bulletColor);
		Bullet bullet2 = new Bullet(x + 5, y, 0, -bulletSpeed, bulletSize, bulletColor);

		// 添加到画布
		gameCanvas.addBullet(bullet1);
		gameCanvas.addBullet(bullet2);
	}

	/**
	 * 渲染玩家
	 * @param g 图形上下文
	 */
	public void render(Graphics2D g) {
		// 绘制角色主体(红色圆形)
		g.setColor(Color.RED);
		g.fillOval((int)(x - size), (int)(y - size), (int)(size * 2), (int)(size * 2));

		// 绘制头带(白色矩形)
		g.setColor(Color.WHITE);
		g.fillRect((int)(x - size), (int)(y - size), (int)(size * 2), 4);

		// 低速模式指示器(黄色圆点)
		if (slowMode) {
			g.setColor(Color.YELLOW);
			g.fillOval((int)(x - 3), (int)(y - 3), 6, 6);
		}
	}

	/**
	 * 向上移动
	 */
	public void moveUp() {
		vy = slowMode ? -speedSlow : -speed;
	}

	/**
	 * 向下移动
	 */
	public void moveDown() {
		vy = slowMode ? speedSlow : speed;
	}

	/**
	 * 向左移动
	 */
	public void moveLeft() {
		vx = slowMode ? -speedSlow : -speed;
	}

	/**
	 * 向右移动
	 */
	public void moveRight() {
		vx = slowMode ? speedSlow : speed;
	}

	/**
	 * 停止垂直移动
	 */
	public void stopVertical() {
		vy = 0;
	}

	/**
	 * 停止水平移动
	 */
	public void stopHorizontal() {
		vx = 0;
	}

	/**
	 * 设置射击状态
	 * @param shooting 是否射击
	 */
	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	/**
	 * 设置低速模式
	 * @param slow 是否低速模式
	 */
	public void setSlowMode(boolean slow) {
		this.slowMode = slow;
	}

	/**
	 * 设置游戏画布
	 * @param canvas 游戏画布
	 */
	public void setGameCanvas(GameCanvas canvas) {
		this.gameCanvas = canvas;
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
	 * 获取角色大小
	 * @return 角色大小
	 */
	public float getSize() {
		return size;
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
	 * 获取普通移动速度
	 * @return 移动速度
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * 获取低速移动速度
	 * @return 低速移动速度
	 */
	public float getSpeedSlow() {
		return speedSlow;
	}

	/**
	 * 是否低速模式
	 * @return 是否低速模式
	 */
	public boolean isSlowMode() {
		return slowMode;
	}
}
