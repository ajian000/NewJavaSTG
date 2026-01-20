package stg.game.ui;

import stg.util.CoordinateSystem;
import stg.util.LevelData;
import stg.util.EnemySpawnData;
import stg.util.LevelManager;
import stg.game.bullet.Bullet;
import stg.game.enemy.Enemy;
import stg.game.enemy.EnemyBullet;
import stg.game.enemy.BasicEnemy;
import stg.game.player.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 游戏画布类 - 处理游戏渲染和输入
 * @Time 2026-01-19 使用画布中心原点坐标系
 */
public class GameCanvas extends JPanel {
	private static final long serialVersionUID = 1L;
	private stg.game.player.Player player; // 玩家对象
	private List<stg.game.player.Player> players; // 玩家列表（支持多玩家）
	private List<Bullet> bullets; // 玩家子弹列表
	private List<Bullet> enemyBullets; // 敌方子弹列表
	private List<Enemy> enemies; // 敌人列表
	private CoordinateSystem coordinateSystem; // @Time 2026-01-19 坐标系统

	// @Time 2026-01-19 按键状态跟踪 - 用于优化按键扫描逻辑
	private boolean upPressed = false; // 上键状态
	private boolean downPressed = false; // 下键状态
	private boolean leftPressed = false; // 左键状态
	private boolean rightPressed = false; // 右键状态
	private boolean zPressed = false; // Z键状态
	private boolean xPressed = false; // X键状态
	private boolean shiftPressed = false; // Shift键状态

	// 关卡执行相关
	private LevelData currentLevel; // 当前关卡数据
	private int currentFrame = 0; // 当前帧数
	private int waveCooldown = 0; // 波次冷却帧数
	private int waveDelay = 30; // 波次延迟帧数
	private List<EnemySpawnData> spawnedEnemies; // 已生成的敌人列表
	private int activeWaveNumber = 0; // 当前活跃的波次号
	private boolean waveStarted[] = new boolean[5]; // 波次是否已开始(1-4)

	// 暂停菜单相关
	private boolean paused = false;
	private static final String[] PAUSE_MENU_ITEMS = {
		"继续游戏",
		"重新开始",
		"返回主菜单"
	};
	private int pauseMenuIndex = 0;

	/**
	 * 构造函数
	 */
	public GameCanvas() {
		setBackground(new Color(20, 20, 30)); // 设置背景色
		setDoubleBuffered(true); // 启用双缓冲
		setFocusable(true); // 允许获取焦点
		players = new ArrayList<>(); // 初始化玩家列表
		bullets = new ArrayList<>(); // 初始化玩家子弹列表
		enemyBullets = new ArrayList<>(); // @Time 2026-01-19 初始化敌方子弹列表
		enemies = new ArrayList<>(); // @Time 2026-01-19 初始化敌人列表
		coordinateSystem = new CoordinateSystem(0, 0); // @Time 2026-01-19 初始化坐标系统
		spawnedEnemies = new ArrayList<>(); // 初始化已生成敌人列表

		setupInput(); // 设置键盘输入
		loadLevel(); // 加载关卡

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
			// @Time 2026-01-19 画布尺寸首次确定
			System.out.println("Canvas size set to: " + width + "x" + height);
			System.out.println("Coordinate system ready, level execution started");
		}
	}

	/**
	 * @Time 2026-01-19 获取坐标系统
	 */
	public CoordinateSystem getCoordinateSystem() {
		return coordinateSystem;
	}

	/**
	 * 获取按键状态
	 */
	public boolean isUpPressed() {
		return upPressed;
	}

	public boolean isDownPressed() {
		return downPressed;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public boolean isZPressed() {
		return zPressed;
	}

	public boolean isXPressed() {
		return xPressed;
	}

	public boolean isShiftPressed() {
		return shiftPressed;
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
				// 暂停菜单按键处理
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					paused = !paused;
					pauseMenuIndex = 0;
					repaint();
					return;
				}

				// 如果暂停，只处理菜单按键
				if (paused) {
					switch (e.getKeyCode()) {
						case KeyEvent.VK_UP:
							pauseMenuIndex = (pauseMenuIndex - 1 + PAUSE_MENU_ITEMS.length) % PAUSE_MENU_ITEMS.length;
							break;
						case KeyEvent.VK_DOWN:
							pauseMenuIndex = (pauseMenuIndex + 1) % PAUSE_MENU_ITEMS.length;
							break;
						case KeyEvent.VK_Z:
						case KeyEvent.VK_ENTER:
							executePauseMenuAction();
							break;
					}
					repaint();
					return;
				}

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
						zPressed = true;
						if (player != null) {
							player.setShooting(true);
						}
						break;
					case KeyEvent.VK_SHIFT: // Shift键 - 低速模式
						shiftPressed = true;
						if (player != null) {
							player.setSlowMode(true);
						}
						break;
					case KeyEvent.VK_X: // X键
						xPressed = true;
						break;
				}

				// @Time 2026-01-19 根据按键状态更新玩家移动方向
				updatePlayerMovement();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// 暂停时不处理按键释放
				if (paused) return;

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
						zPressed = false;
						if (player != null) {
							player.setShooting(false);
						}
						break;
					case KeyEvent.VK_SHIFT: // Shift键 - 低速模式
						shiftPressed = false;
						if (player != null) {
							player.setSlowMode(false);
						}
						break;
					case KeyEvent.VK_X: // X键
						xPressed = false;
						break;
				}

				// @Time 2026-01-19 根据按键状态更新玩家移动方向
				updatePlayerMovement();
			}
		});
	}

	/**
	 * 执行暂停菜单操作
	 */
	private void executePauseMenuAction() {
		switch (pauseMenuIndex) {
			case 0: // 继续游戏
				paused = false;
				break;
			case 1: // 重新开始
				resetGame();
				paused = false;
				break;
			case 2: // 返回主菜单
				Main.Main.returnToTitle();
				break;
		}
	}

	/**
	 * 重置游戏
	 */
	public void resetGame() {
		currentFrame = 0;
		waveCooldown = 0;
		activeWaveNumber = 0;
		for (int i = 0; i < waveStarted.length; i++) {
			waveStarted[i] = false;
		}
		spawnedEnemies.clear();
		enemies.clear();
		bullets.clear();
		enemyBullets.clear();
		paused = false;
		pauseMenuIndex = 0;

		if (player != null) {
			int canvasHeight = getHeight();
			float actualPlayerX = 0;
			float actualPlayerY = -canvasHeight / 2.0f + 40;
			player.setPosition(actualPlayerX, actualPlayerY);
			player.reset();
		}

		System.out.println("游戏已重置");
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
	public void setPlayer(stg.game.player.Player player) {
		this.player = player;
		this.players.clear();
		this.players.add(player);
		player.setGameCanvas(this);
	}

	/**
	 * 根据类型设置玩家
	 * @param type 自机类型
	 * @param spawnX 初始X坐标
	 * @param spawnY 初始Y坐标
	 */
	public void setPlayer(stg.game.player.PlayerType type, float spawnX, float spawnY) {
		stg.game.player.PlayerFactory factory = stg.game.player.PlayerFactory.getInstance();
		this.player = factory.createPlayer(type, spawnX, spawnY);
		this.players.clear();
		this.players.add(this.player);
		this.player.setGameCanvas(this);
	}

	/**
	 * 获取当前玩家类型
	 * @return 玩家类型
	 */
	public stg.game.player.PlayerType getPlayerType() {
		if (player == null) {
			return stg.game.player.PlayerType.DEFAULT;
		}
		if (player instanceof stg.game.player.ReimuPlayer) return stg.game.player.PlayerType.REIMU;
		if (player instanceof stg.game.player.MarisaPlayer) return stg.game.player.PlayerType.MARISA;
		return stg.game.player.PlayerType.DEFAULT;
	}

	/**
	 * 获取玩家列表
	 * @return 玩家列表
	 */
	public List<stg.game.player.Player> getPlayersList() {
		return players;
	}

	/**
	 * 获取当前玩家
	 * @return 玩家对象
	 */
	public stg.game.player.Player getPlayer() {
		return player;
	}

	/**
	 * 更新游戏状态 - @Time 2026-01-19 更新敌人系统
	 */
	public void update() {
		// 暂停时不更新游戏逻辑
		if (paused) return;

		// 更新玩家
		if (player != null) {
			player.update();
		}

		// 更新关卡逻辑
		updateLevel();

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

		currentFrame++; // 增加帧计数
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

		// 绘制暂停菜单
		if (paused) {
			drawPauseMenu(g2d);
		}
	}

	/**
	 * 绘制暂停菜单
	 */
	private void drawPauseMenu(Graphics2D g) {
		int width = getWidth();
		int height = getHeight();

		// 半透明遮罩
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, width, height);

		// 菜单框
		int menuWidth = 300;
		int menuHeight = 250;
		int menuX = (width - menuWidth) / 2;
		int menuY = (height - menuHeight) / 2;

		g.setColor(new Color(30, 30, 50, 230));
		g.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
		g.setColor(new Color(100, 100, 150));
		g.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);

		// 标题
		g.setFont(new Font("Microsoft YaHei", Font.BOLD, 32));
		g.setColor(new Color(255, 200, 100));
		String title = "暂停";
		FontMetrics fm = g.getFontMetrics();
		int titleWidth = fm.stringWidth(title);
		g.drawString(title, menuX + (menuWidth - titleWidth) / 2, menuY + 50);

		// 菜单项
		int menuItemHeight = 50;
		g.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));

		for (int i = 0; i < PAUSE_MENU_ITEMS.length; i++) {
			int itemY = menuY + 80 + i * menuItemHeight;

			if (i == pauseMenuIndex) {
				g.setColor(new Color(255, 200, 100));
				g.drawString("▶", menuX + 40, itemY + 8);
			}

			g.setColor(i == pauseMenuIndex ? new Color(255, 200, 100) : new Color(220, 220, 220));
			g.drawString(PAUSE_MENU_ITEMS[i], menuX + 80, itemY + 8);
		}

		// 操作提示
		g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		g.setColor(new Color(150, 150, 150));
		g.drawString("↑ ↓  选择", menuX + 40, menuY + menuHeight - 40);
		g.drawString("Z/Enter 确认", menuX + 40, menuY + menuHeight - 20);
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
			size2 = player.getHitboxRadius(); // 使用受击判定半径而非渲染大小
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

	/**
	 * 加载关卡
	 */
	private void loadLevel() {
		LevelManager manager = LevelManager.getInstance();
		currentLevel = manager.loadLevelFromUser();
		if (currentLevel != null) {
			System.out.println("Level loaded: " + currentLevel.getName());
		} else {
			System.err.println("Failed to load level!");
		}
	}

	/**
	 * 更新关卡逻辑
	 * 敌人生成和波次管理
	 */
	private void updateLevel() {
		if (currentLevel == null) {
			return;
		}

		// 如果波次冷却中,减少冷却时间
		if (waveCooldown > 0) {
			waveCooldown--;
			if (waveCooldown == 0) {
				System.out.println("Wave cooldown finished, can now spawn next wave enemies");
				// 冷却结束后,切换到下一波
				if (activeWaveNumber < 4) {
					activeWaveNumber++;
					waveStarted[activeWaveNumber] = true;
					System.out.println("Switching to Wave " + activeWaveNumber);
				}
			}
			return;
		}

		// 如果还没有活跃波次,开始第一波
		if (activeWaveNumber == 0) {
			activeWaveNumber = 1;
			waveStarted[1] = true;
			System.out.println("Starting Wave 1");
			return;
		}

		// 尝试生成当前波次的敌人
		boolean spawned = trySpawnWaveEnemies(activeWaveNumber);

		// 检查当前波次是否完成
		// 条件: 场上无敌人 + 该波次所有敌人都已生成 + 当前帧没有生成新敌人
		if (enemies.isEmpty() && isWaveComplete(activeWaveNumber) && !spawned) {
			// 波次完成,进入冷却期
			waveCooldown = waveDelay;
			System.out.println("Wave " + activeWaveNumber + " completed! All enemies defeated. Waiting " + waveDelay + " ticks for next wave...");
			return;
		}

		// 触发关卡事件
		for (Map<String, Object> event : currentLevel.getEvents()) {
			Object frameObj = event.get("frame");
			if (frameObj instanceof Number) {
				int eventFrame = ((Number)frameObj).intValue();
				// 只在帧数刚到达时触发事件
				if (currentFrame == eventFrame) {
					String type = (String)event.get("type");
					if ("message".equals(type)) {
						System.out.println("Event: " + event.get("content"));
					}
				}
			}
		}
	}

	/**
	 * 检查指定波次是否完成(所有敌人都已生成)
	 */
	private boolean isWaveComplete(int wave) {
		// 检查是否还有该波次的敌人未生成
		for (EnemySpawnData enemyData : currentLevel.getEnemies()) {
			int enemyWave = getWaveByFrame(enemyData.getFrame());
			if (enemyWave == wave && !spawnedEnemies.contains(enemyData)) {
				return false; // 该波次还有敌人未生成
			}
		}
		return true; // 该波次所有敌人都已生成
	}

	/**
	 * 尝试生成指定波次的敌人
	 * @return 是否生成了敌人
	 */
	private boolean trySpawnWaveEnemies(int wave) {
		boolean spawned = false;

		// 生成当前波次的敌人
		for (EnemySpawnData enemyData : currentLevel.getEnemies()) {
			int enemyWave = getWaveByFrame(enemyData.getFrame());

			// 只处理当前波次的敌人
			if (enemyWave != wave) {
				continue;
			}

			// 检查是否已生成过
			if (spawnedEnemies.contains(enemyData)) {
				continue;
			}

			// 检查是否到达生成帧
			if (currentFrame >= enemyData.getFrame()) {
				spawnEnemy(enemyData);
				spawnedEnemies.add(enemyData);
				spawned = true;
				System.out.println("Spawned Wave " + wave + " enemy at frame " + currentFrame + ": " + enemyData.getType());
			}
		}
		return spawned;
	}

	/**
	 * 根据帧数判断波次
	 * Wave 1: frame 0-200
	 * Wave 2: frame 200-500
	 * Wave 3: frame 500-700
	 * Wave 4: frame 700+
	 */
	private int getWaveByFrame(int frame) {
		if (frame < 200) return 1;
		if (frame < 500) return 2;
		if (frame < 700) return 3;
		if (frame < 1000) return 4;
		return 0;
	}

	/**
	 * 生成敌人
	 */
	private void spawnEnemy(EnemySpawnData data) {
		if (getWidth() <= 0 || getHeight() <= 0) {
			return; // 画布未准备好
		}

		Enemy enemy = new BasicEnemy(data.getX(), data.getY(), data.getSpeed(), this);
		enemies.add(enemy);
	}
}
