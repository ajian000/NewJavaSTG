package stg.game;

import stg.util.CoordinateSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 游戏画布类 - 处理游戏渲染和输入
 * @Time 2026-01-19 使用画布中心原点坐标系
 */
public class GameCanvas extends JPanel {
	private static final long serialVersionUID = 1L;
	private Player player; // 玩家对象
	private List<Bullet> bullets; // 玩家子弹列表
	private List<Bullet> enemyBullets; // 敌方子弹列表
	private List<Enemy> enemies; // 敌人列表
	private CoordinateSystem coordinateSystem; // @Time 2026-01-19 坐标系统

	// @Time 2026-01-19 按键状态跟踪 - 用于优化按键扫描逻辑
	private boolean upPressed = false; // 上键状态
	private boolean downPressed = false; // 下键状态
	private boolean leftPressed = false; // 左键状态
	private boolean rightPressed = false; // 右键状态

	/**
	 * 构造函数
	 */
	public GameCanvas() {
		setBackground(new Color(20, 20, 30)); // 设置背景色
		setDoubleBuffered(true); // 启用双缓冲
		setFocusable(true); // 允许获取焦点
		bullets = new ArrayList<>(); // 初始化玩家子弹列表
		enemyBullets = new ArrayList<>(); // @Time 2026-01-19 初始化敌方子弹列表
		enemies = new ArrayList<>(); // @Time 2026-01-19 初始化敌人列表
		coordinateSystem = new CoordinateSystem(0, 0); // @Time 2026-01-19 初始化坐标系统

		setupInput(); // 设置键盘输入

		System.out.println("GameCanvas created, initial size: " + getWidth() + "x" + getHeight());
		System.out.println("GameCanvas center origin: (0, 0) = screen center");
	}

	/**
	 * @Time 2026-01-19 更新坐标系统尺寸
	 */
	@Override
	public void setBounds(int x, int y, int width, int height) {
		int oldWidth = getWidth();
		int oldHeight = getHeight();
		super.setBounds(x, y, width, height);
		if (coordinateSystem != null) {
			coordinateSystem.updateCanvasSize(width, height);
		}
		if (oldWidth == 0 && oldHeight == 0 && width > 0 && height > 0) {
			// @Time 2026-01-19 画布尺寸首次确定,初始化敌人
			BasicEnemy enemy = new BasicEnemy(0, 200, 2.0f, this);
			enemies.add(enemy);
			System.out.println("Canvas size set to: " + width + "x" + height);
			System.out.println("Enemy created at center origin coords: (0, 200)");
			System.out.println("Enemy screen coords: (" + (0 + width/2.0f) + ", " + (200 + height/2.0f) + ")");
		}
	}

	/**
	 * @Time 2026-01-19 获取坐标系统
	 */
	public CoordinateSystem getCoordinateSystem() {
		return coordinateSystem;
	}

	/**
	 * @Time 2026-01-19 将中心原点坐标转换为屏幕坐标
	 * 坐标系: 右上角为(+,+),左下角为(-,-)
	 * @param x 中心原点X坐标(向右为正)
	 * @param y 中心原点Y坐标(向上为正)
	 * @return 屏幕坐标 [screenX, screenY]
	 */
	public float[] toScreenCoords(float x, float y) {
		return coordinateSystem.toScreenCoords(x, y);
	}

	/**
	 * 设置键盘输入监听 - @Time 2026-01-19 优化按键扫描,支持同时按键检测
	 */
	private void setupInput() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// @Time 2026-01-19 更新按键状态
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						upPressed = true;
						break;
					case KeyEvent.VK_DOWN:
						downPressed = true;
						break;
					case KeyEvent.VK_LEFT:
						leftPressed = true;
						break;
					case KeyEvent.VK_RIGHT:
						rightPressed = true;
						break;
					case KeyEvent.VK_Z: // Z键 - 射击
						if (player != null) {
							player.setShooting(true);
						}
						break;
					case KeyEvent.VK_SHIFT: // Shift键 - 低速模式
						if (player != null) {
							player.setSlowMode(true);
						}
						break;
				}

				// @Time 2026-01-19 根据按键状态更新玩家移动方向
				updatePlayerMovement();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// @Time 2026-01-19 更新按键状态
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						upPressed = false;
						break;
					case KeyEvent.VK_DOWN:
						downPressed = false;
						break;
					case KeyEvent.VK_LEFT:
						leftPressed = false;
						break;
					case KeyEvent.VK_RIGHT:
						rightPressed = false;
						break;
					case KeyEvent.VK_Z: // Z键 - 射击
						if (player != null) {
							player.setShooting(false);
						}
						break;
					case KeyEvent.VK_SHIFT: // Shift键 - 低速模式
						if (player != null) {
							player.setSlowMode(false);
						}
						break;
				}

				// @Time 2026-01-19 根据按键状态更新玩家移动方向
				updatePlayerMovement();
			}
		});
	}

	/**
	 * @Time 2026-01-19 根据当前按键状态更新玩家移动方向
	 * 同时按下相反方向键时保持静止
	 */
	private void updatePlayerMovement() {
		if (player == null) return;

		// @Time 2026-01-19 水平方向:同时按下左右键时保持静止
		if (leftPressed && rightPressed) {
			player.stopHorizontal();
		} else if (leftPressed) {
			player.moveLeft();
		} else if (rightPressed) {
			player.moveRight();
		} else {
			player.stopHorizontal();
		}

		// @Time 2026-01-19 垂直方向:同时按下上下键时保持静止
		if (upPressed && downPressed) {
			player.stopVertical();
		} else if (upPressed) {
			player.moveUp();
		} else if (downPressed) {
			player.moveDown();
		} else {
			player.stopVertical();
		}
	}

	/**
	 * 设置玩家
	 * @param player 玩家对象
	 */
	public void setPlayer(Player player) {
		this.player = player;
		player.setGameCanvas(this);
	}

	/**
	 * 更新游戏状态 - @Time 2026-01-19 更新敌人系统
	 */
	public void update() {
		// 更新玩家
		if (player != null) {
			player.update();
		}

		// @Time 2026-01-19 更新敌人位置,移除死亡或越界的敌人
		// @Time 2026-01-19 只有当画布尺寸确定后才检查越界
		int canvasWidth = getWidth();
		int canvasHeight = getHeight();
		boolean canvasReady = canvasWidth > 0 && canvasHeight > 0;

		Iterator<Enemy> enemyIterator = enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy enemy = enemyIterator.next();
			enemy.update();

			// 只在画布准备好后检查越界
			if (canvasReady && (!enemy.isAlive() || enemy.isOutOfBounds(canvasWidth, canvasHeight))) {
				System.out.println("Enemy removed: " + enemy);
				enemyIterator.remove();
			}
		}

		// 更新玩家子弹位置,移除越界子弹
		Iterator<Bullet> bulletIterator = bullets.iterator();
		while (bulletIterator.hasNext()) {
			Bullet bullet = bulletIterator.next();
			bullet.update();
			if (bullet.isOutOfBounds(getWidth(), getHeight())) {
				bulletIterator.remove();
			}
		}

		// @Time 2026-01-19 更新敌方子弹位置,移除越界子弹
		Iterator<Bullet> enemyBulletIterator = enemyBullets.iterator();
		while (enemyBulletIterator.hasNext()) {
			Bullet bullet = enemyBulletIterator.next();
			bullet.update();
			if (bullet.isOutOfBounds(getWidth(), getHeight())) {
				enemyBulletIterator.remove();
			}
		}

		// @Time 2026-01-19 碰撞检测
		if (canvasReady) {
			checkCollisions();
		}

		repaint(); // 重绘画布
	}

	/**
	 * 绘制游戏画面 - @Time 2026-01-19 渲染敌人系统
	 * @param g 图形上下文
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// @Time 2026-01-19 绘制坐标系网格(调试用)
		drawCoordinateGrid(g2d);

		// @Time 2026-01-19 调试: 输出敌人数量
		if (enemies.size() > 0) {
			System.out.println("Rendering " + enemies.size() + " enemy(ies), " + enemyBullets.size() + " enemy bullet(s)");
		}

		// @Time 2026-01-19 绘制敌人
		for (Enemy enemy : enemies) {
			enemy.render(g2d);
		}

		// 绘制敌方子弹
		for (Bullet bullet : enemyBullets) {
			bullet.render(g2d);
		}

		// 绘制玩家子弹
		for (Bullet bullet : bullets) {
			bullet.render(g2d);
		}

		// 绘制玩家
		if (player != null) {
			player.render(g2d);
		}
	}

	/**
	 * @Time 2026-01-19 绘制坐标系网格,用于调试
	 * 坐标系: 右上角为(+,+),左下角为(-,-)
	 * @param g 图形上下文
	 */
	private void drawCoordinateGrid(Graphics2D g) {
		int width = getWidth();
		int height = getHeight();

		// 绘制X轴(水平中线) - 绿色
		g.setColor(new Color(0, 255, 0, 100));
		g.drawLine(0, height/2, width, height/2);

		// 绘制Y轴(垂直中线) - 绿色
		g.setColor(new Color(0, 255, 0, 100));
		g.drawLine(width/2, 0, width/2, height);

		// 绘制原点标记 - 红色十字
		g.setColor(Color.RED);
		int centerSize = 10;
		g.drawLine(width/2 - centerSize, height/2, width/2 + centerSize, height/2);
		g.drawLine(width/2, height/2 - centerSize, width/2, height/2 + centerSize);

		// 绘制边界框 - 黄色
		g.setColor(new Color(255, 255, 0, 100));
		g.drawRect(0, 0, width-1, height-1);
	}

	/**
	 * 添加子弹(玩家子弹)
	 * @param bullet 子弹对象
	 */
	public void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}

	/**
	 * @Time 2026-01-19 添加敌方子弹
	 * @param bullet 敌方子弹对象
	 */
	public void addEnemyBullet(EnemyBullet bullet) {
		enemyBullets.add(bullet);
	}

	/**
	 * @Time 2026-01-19 添加敌人
	 * @param enemy 敌人对象
	 */
	public void addEnemy(Enemy enemy) {
		enemies.add(enemy);
	}

	/**
	 * @Time 2026-01-19 获取敌人列表
	 * @return 敌人列表
	 */
	public List<Enemy> getEnemies() {
		return enemies;
	}

	/**
	 * @Time 2026-01-19 获取敌方子弹列表
	 * @return 敌方子弹列表
	 */
	public List<Bullet> getEnemyBullets() {
		return enemyBullets;
	}

	/**
	 * @Time 2026-01-19 获取玩家子弹列表
	 * @return 玩家子弹列表
	 */
	public List<Bullet> getPlayerBullets() {
		return bullets;
	}

	/**
	 * @Time 2026-01-19 碰撞检测
	 * 检测玩家子弹与敌人的碰撞,敌方子弹与玩家的碰撞
	 */
	private void checkCollisions() {
		// 检测玩家子弹与敌人的碰撞
		Iterator<Bullet> bulletIterator = bullets.iterator();
		while (bulletIterator.hasNext()) {
			Bullet bullet = bulletIterator.next();

			Iterator<Enemy> enemyIterator = enemies.iterator();
			while (enemyIterator.hasNext()) {
				Enemy enemy = enemyIterator.next();

				if (checkCollision(bullet, enemy)) {
					// 子弹击中敌人
					enemy.takeDamage(10); // 每发子弹造成10点伤害
					bulletIterator.remove();
					System.out.println("Player bullet hit enemy! Enemy HP: " + enemy.getHp() + "/" + enemy.getMaxHp());
					break; // 子弹移除,跳出循环
				}
			}
		}

		// 检测敌方子弹与玩家的碰撞
		Iterator<Bullet> enemyBulletIterator = enemyBullets.iterator();
		while (enemyBulletIterator.hasNext()) {
			Bullet bullet = enemyBulletIterator.next();

			if (player != null && checkCollision(bullet, player)) {
				// 敌方子弹击中玩家
				player.onHit();
				enemyBulletIterator.remove();
				System.out.println("Player hit by enemy bullet!");
			}
		}
	}

	/**
	 * @Time 2026-01-19 碰撞检测 - 圆形碰撞
	 * @param obj1 对象1(子弹)
	 * @param obj2 对象2(敌人或玩家)
	 * @return 是否碰撞
	 */
	private boolean checkCollision(Object obj1, Object obj2) {
		float x1, y1, size1;
		float x2, y2, size2;

		// 获取对象1的属性
		if (obj1 instanceof Bullet) {
			Bullet bullet = (Bullet)obj1;
			x1 = bullet.getX();
			y1 = bullet.getY();
			size1 = bullet.getSize();
		} else if (obj1 instanceof EnemyBullet) {
			EnemyBullet bullet = (EnemyBullet)obj1;
			x1 = bullet.getX();
			y1 = bullet.getY();
			size1 = bullet.getSize();
		} else {
			return false;
		}

		// 获取对象2的属性
		if (obj2 instanceof Enemy) {
			Enemy enemy = (Enemy)obj2;
			x2 = enemy.getX();
			y2 = enemy.getY();
			size2 = enemy.getSize();
		} else if (obj2 instanceof Player) {
			Player player = (Player)obj2;
			x2 = player.getX();
			y2 = player.getY();
			size2 = player.getSize();
		} else {
			return false;
		}

		// 计算距离
		float dx = x1 - x2;
		float dy = y1 - y2;
		float distance = (float)Math.sqrt(dx * dx + dy * dy);

		// 判断是否碰撞(两个圆半径之和)
		return distance < (size1 + size2);
	}
}
