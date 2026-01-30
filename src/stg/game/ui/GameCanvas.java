package stg.game.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import stg.base.KeyStateProvider;
import stg.game.*;
import stg.game.bullet.Bullet;
import stg.game.enemy.Enemy;
import stg.game.enemy.EnemyBullet;
import stg.game.laser.EnemyLaser;
import stg.game.item.Item;
import stg.game.player.Player;
import stg.game.player.PlayerType;
import stg.game.player.ReimuPlayer;
import stg.game.player.MarisaPlayer;
import stg.util.CoordinateSystem;
import java.util.List;

/**
 * 游戏画布类 - 游戏主界面的协调器
 * @since 2026-01-19 使用画布中心原点坐标系
 * @since 2026-01-20 将类移动到stg.game.ui包
 * @since 2026-01-29 重构为轻量级协调器，委托功能到各个管理类
 */
public class GameCanvas extends JPanel implements KeyStateProvider {
	private static final long serialVersionUID = 1L;
	private static final float PLAYER_START_Y_OFFSET = 40f;

	// 管理器组件
	private GameWorld world;
	private GameRenderer renderer;
	private InputHandler inputHandler;
	private CollisionSystem collisionSystem;
	private GameStateManager gameStateManager;
	private GameLevelManager levelManager;
	private PauseMenu pauseMenu;
	
	// 核心组件
	private Player player;
	private CoordinateSystem coordinateSystem;
	private GameStatusPanel gameStatusPanel;
	
	/**
	 * 构造函数
	 */
	public GameCanvas() {
		setBackground(new Color(20, 20, 30));
		setDoubleBuffered(true);
		setFocusable(true);
		
		// 初始化组件
		world = new GameWorld();
		coordinateSystem = new CoordinateSystem(0, 0);
		gameStateManager = new GameStateManager();
		pauseMenu = new PauseMenu(gameStateManager);
		inputHandler = new InputHandler(player, gameStateManager);
		
		// 设置键盘输入
		setupInput();
		
		// 渲染器和碰撞系统稍后在setPlayer时初始化
	}

	/**
 * @since 2026-01-19 更新坐标系统尺寸
 */
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		if (coordinateSystem != null) {
			coordinateSystem.updateCanvasSize(width, height);
		}
	}

	/**
 * @since 2026-01-19 获取坐标系统
 */
	public CoordinateSystem getCoordinateSystem() {
		return coordinateSystem;
	}

	/**
 * @since 2026-01-24 设置游戏状态面板
 */
	public void setGameStatusPanel(GameStatusPanel gameStatusPanel) {
		this.gameStatusPanel = gameStatusPanel;
	}

	/**
 * 获取游戏状态面板
 * @since 2026-01-24
 */
	public GameStatusPanel getGameStatusPanel() {
		return gameStatusPanel;
	}

	/**
 * 委托到InputHandler的按键状态查询
 */
	@Override
	public boolean isUpPressed() { return inputHandler.isUpPressed(); }
	@Override
	public boolean isDownPressed() { return inputHandler.isDownPressed(); }
	@Override
	public boolean isLeftPressed() { return inputHandler.isLeftPressed(); }
	@Override
	public boolean isRightPressed() { return inputHandler.isRightPressed(); }
	@Override
	public boolean isZPressed() { return inputHandler.isZPressed(); }

	/**
 * @since 2026-01-19 将中心原点坐标转换为屏幕坐标
 * 坐标系: 右上角为(+,+),左下角为(-,-)
 * @param x 中心原点X坐标(向右为正)
 * @param y 中心原点Y坐标(向上为正)
 * @return 屏幕坐标 [screenX, screenY]
 */
	public float[] toScreenCoords(float x, float y) {
		return coordinateSystem.toScreenCoords(x, y);
	}

	/**
 * 设置键盘输入监听
 */
	private void setupInput() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// 暂停菜单按键处理
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (gameStateManager.isPlaying()) {
						gameStateManager.togglePause();
					}
					repaint();
					return;
				}

				// 如果暂停，只处理菜单按键
				if (gameStateManager.isPaused()) {
					switch (e.getKeyCode()) {
						case KeyEvent.VK_UP:
							pauseMenu.moveUp();
							break;
						case KeyEvent.VK_DOWN:
							pauseMenu.moveDown();
							break;
						case KeyEvent.VK_Z:
						case KeyEvent.VK_ENTER:
							pauseMenu.executeSelectedAction();
							break;
					}
					repaint();
					return;
				}

				// 委托给InputHandler
				inputHandler.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// 暂停时不处理按键释放
				if (gameStateManager.isPaused()) return;
				
				// 委托给InputHandler
				inputHandler.keyReleased(e);
			}
		});
	}

	/**
	 * 重置游戏
	 */
	public void resetGame() {
		world.clear();
		gameStateManager.reset();
		levelManager.reset();
		
		if (player != null) {
			int canvasHeight = getHeight();
			float actualPlayerX = 0;
			float actualPlayerY = -canvasHeight / 2.0f + PLAYER_START_Y_OFFSET;
			player.setPosition(actualPlayerX, actualPlayerY);
			player.reset();
		}
	}

	/**
	 * 设置玩家
	 */
	public void setPlayer(Player player) {
		this.player = player;
		inputHandler.setPlayer(player);
		player.setGameCanvas(this);
		
		// 初始化渲染器和碰撞系统
		renderer = new GameRenderer(world, player, coordinateSystem);
		collisionSystem = new CollisionSystem(world, player);
		levelManager = new GameLevelManager(world, this);
		
		// 初始化游戏状态面板
		if (gameStatusPanel != null) {
			gameStatusPanel.setScore(0);
			gameStatusPanel.setLives(-1);
			gameStatusPanel.setSpellCards(0);
			gameStatusPanel.setMaxScore(10000);
		}
	}

	/**
	 * 根据类型设置玩家
	 */
	public void setPlayer(PlayerType type, float spawnX, float spawnY) {
		stg.game.player.PlayerFactory factory = stg.game.player.PlayerFactory.getInstance();
		Player newPlayer = factory.createPlayer(type, spawnX, spawnY);
		setPlayer(newPlayer);
		
		// 初始化子机
		if (player instanceof ReimuPlayer) {
			((ReimuPlayer) player).initializeOptions(this);
		} else if (player instanceof MarisaPlayer) {
			((MarisaPlayer) player).initializeOptions(this);
		}
	}

	/**
	 * 获取当前玩家类型
	 */
	public PlayerType getPlayerType() {
		if (player == null) {
			return PlayerType.DEFAULT;
		}
		if (player instanceof ReimuPlayer) return PlayerType.REIMU;
		if (player instanceof MarisaPlayer) return PlayerType.MARISA;
		return PlayerType.DEFAULT;
	}

	/**
	 * 获取当前玩家
	 */
	public Player getPlayer() {
		return player;
	}

	/**
 * 更新游戏状态
 */
	public void update() {
		// 暂停时不更新游戏逻辑
		if (gameStateManager.isPaused()) return;

		// 更新玩家
		if (player != null) {
			player.update();
		}

		// 更新关卡逻辑
		levelManager.update();

		// 更新游戏世界中的所有实体
		int canvasWidth = getWidth();
		int canvasHeight = getHeight();
		boolean canvasReady = canvasWidth > 0 && canvasHeight > 0;
		
		if (canvasReady) {
			world.update(canvasWidth, canvasHeight);
			// 执行碰撞检测
			collisionSystem.checkCollisions();
		}

		repaint();
	}

	/**
 * 绘制游戏画面
 * @param g 图形上下文
 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		// 委托给渲染器绘制游戏内容
		if (renderer != null) {
			renderer.render(g2d);
		}

		// 绘制暂停菜单
		if (gameStateManager.isPaused()) {
			pauseMenu.render(g2d, getWidth(), getHeight());
		}
	}

	/**
	 * 添加子弹(玩家子弹)
	 */
	public void addBullet(Bullet bullet) {
		world.addPlayerBullet(bullet);
	}

	/**
	 * 添加敌方子弹
	 */
	public void addEnemyBullet(EnemyBullet bullet) {
		world.addEnemyBullet(bullet);
	}

	/**
	 * 添加敌人
	 */
	public void addEnemy(Enemy enemy) {
		world.addEnemy(enemy);
	}

	/**
	 * 获取敌人列表
	 */
	public List<Enemy> getEnemies() {
		return world.getEnemies();
	}

	/**
	 * 获取敌方子弹列表
	 */
	public List<EnemyBullet> getEnemyBullets() {
		return world.getEnemyBullets();
	}

	/**
	 * 获取玩家子弹列表
	 */
	public List<Bullet> getPlayerBullets() {
		return world.getPlayerBullets();
	}

	/**
	 * 添加物品
	 */
	public void addItem(Item item) {
		world.addItem(item);
	}

	/**
	 * 移除物品
	 */
	public void removeItem(Item item) {
		world.removeItem(item);
	}

	/**
	 * 获取物品列表
	 */
	public List<Item> getItems() {
		return world.getItems();
	}

	/**
	 * 清除所有物品
	 */
	public void clearItems() {
		world.clearItems();
	}

	/**
	 * 添加敌方激光
	 */
	public void addEnemyLaser(EnemyLaser laser) {
		if (laser != null) {
			world.addEnemyLaser(laser);
		}
	}

	/**
	 * 清除所有敌方激光
	 */
	public void clearEnemyLasers() {
		world.clearEnemyLasers();
	}

	/**
	 * 移除指定的敌方激光列表
	 */
	public void removeEnemyLasers(List<EnemyLaser> lasersToRemove) {
		world.removeEnemyLasers(lasersToRemove);
	}
	
	/**
	 * 获取游戏状态管理器
	 */
	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}
}
