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
	 * 设置键盘输入监听
	 */
	private void setupInput() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (player == null) return;

				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: // 上方向键
						player.moveUp();
						break;
					case KeyEvent.VK_DOWN: // 下方向键
						player.moveDown();
						break;
					case KeyEvent.VK_LEFT: // 左方向键
						player.moveLeft();
						break;
					case KeyEvent.VK_RIGHT: // 右方向键
						player.moveRight();
						break;
					case KeyEvent.VK_Z: // Z键 - 射击
						player.setShooting(true);
						break;
					case KeyEvent.VK_SHIFT: // Shift键 - 低速模式
						player.setSlowMode(true);
						break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (player == null) return;

				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
					case KeyEvent.VK_DOWN:
						player.stopVertical();
						break;
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_RIGHT:
						player.stopHorizontal();
						break;
					case KeyEvent.VK_Z:
						player.setShooting(false);
						break;
					case KeyEvent.VK_SHIFT:
						player.setSlowMode(false);
						break;
				}
			}
		});
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
