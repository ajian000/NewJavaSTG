package stg.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 游戏画布类 - 处理游戏渲染和输入
 */
public class GameCanvas extends JPanel {
	private static final long serialVersionUID = 1L;
	private Player player; // 玩家对象
	private List<Bullet> bullets; // 子弹列表

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
		bullets = new ArrayList<>(); // 初始化子弹列表

		setupInput(); // 设置键盘输入

		System.out.println("GameCanvas created");
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
	 * 更新游戏状态
	 */
	public void update() {
		// 更新玩家
		if (player != null) {
			player.update();
		}

		// 更新子弹位置,移除越界子弹
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			bullet.update();
			if (bullet.isOutOfBounds(getWidth(), getHeight())) {
				iterator.remove();
			}
		}

		repaint(); // 重绘画布
	}

	/**
	 * 绘制游戏画面
	 * @param g 图形上下文
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 绘制子弹
		for (Bullet bullet : bullets) {
			bullet.render(g2d);
		}

		// 绘制玩家
		if (player != null) {
			player.render(g2d);
		}
	}

	/**
	 * 添加子弹
	 * @param bullet 子弹对象
	 */
	public void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}
}
