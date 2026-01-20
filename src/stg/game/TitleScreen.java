package stg.game;

import stg.game.player.PlayerType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TitleScreen extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color BG_COLOR = new Color(10, 10, 20);
	private static final Color SELECTED_COLOR = new Color(255, 200, 100);
	private static final Color UNSELECTED_COLOR = new Color(200, 200, 200);

	private enum MenuState {
		MAIN_MENU,
		PLAYER_SELECT
	}

	private static final String[] MAIN_MENU_ITEMS = {
		"开始游戏",
		"退出游戏"
	};

	private int selectedIndex = 0;
	private PlayerType selectedPlayerType = PlayerType.DEFAULT;
	private MenuState currentState = MenuState.MAIN_MENU;
	private Timer animationTimer;
	private int animationFrame = 0;

	public interface TitleCallback {
		void onGameStart(stg.game.player.PlayerType playerType);
		void onExit();
	}

	private TitleCallback callback;

	public TitleScreen(TitleCallback callback) {
		this.callback = callback;
		initialize();
	}

	private void initialize() {
		setBackground(BG_COLOR);
		setFocusable(true);
		setLayout(null);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						if (currentState == MenuState.MAIN_MENU) {
							selectedIndex = (selectedIndex - 1 + MAIN_MENU_ITEMS.length) % MAIN_MENU_ITEMS.length;
						} else {
							PlayerType[] types = PlayerType.values();
							selectedIndex = (selectedIndex - 1 + types.length) % types.length;
						}
						repaint();
						break;
					case KeyEvent.VK_DOWN:
						if (currentState == MenuState.MAIN_MENU) {
							selectedIndex = (selectedIndex + 1) % MAIN_MENU_ITEMS.length;
						} else {
							PlayerType[] types = PlayerType.values();
							selectedIndex = (selectedIndex + 1) % types.length;
						}
						repaint();
						break;
					case KeyEvent.VK_Z:
					case KeyEvent.VK_ENTER:
						executeMenuAction();
						break;
					case KeyEvent.VK_ESCAPE:
						if (currentState == MenuState.PLAYER_SELECT) {
							currentState = MenuState.MAIN_MENU;
							selectedIndex = 0;
							repaint();
						} else {
							if (callback != null) callback.onExit();
						}
						break;
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_RIGHT:
						if (currentState == MenuState.PLAYER_SELECT) {
							PlayerType[] types = PlayerType.values();
							selectedIndex = (selectedIndex + 1) % types.length;
							repaint();
						}
						break;
				}
			}
		});

		animationTimer = new Timer(50, e -> {
			animationFrame = (animationFrame + 1) % 60;
			repaint();
		});
		animationTimer.start();

		requestFocusInWindow();
	}

	private void executeMenuAction() {
		if (currentState == MenuState.MAIN_MENU) {
			switch (selectedIndex) {
				case 0: // 开始游戏 - 进入角色选择
					currentState = MenuState.PLAYER_SELECT;
					selectedIndex = 0;
					repaint();
					break;
				case 1: // 退出游戏
					if (callback != null) callback.onExit();
					break;
			}
		} else if (currentState == MenuState.PLAYER_SELECT) {
			PlayerType[] types = PlayerType.values();
			selectedPlayerType = types[selectedIndex];
			callback.onGameStart(selectedPlayerType);
		}
	}

	public void setPlayerType(stg.game.player.PlayerType type) {
		this.selectedPlayerType = type;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int width = getWidth();
		int height = getHeight();

		drawBackground(g2d, width, height);

		if (currentState == MenuState.MAIN_MENU) {
			drawTitle(g2d, width);
			drawMainMenu(g2d, width, height);
		} else if (currentState == MenuState.PLAYER_SELECT) {
			drawTitle(g2d, width);
			drawPlayerSelectMenu(g2d, width, height);
		}
	}

	private void drawBackground(Graphics2D g, int width, int height) {
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, width, height);
	}

	private void drawTitle(Graphics2D g, int width) {
		g.setFont(new Font("Microsoft YaHei", Font.BOLD, 72));
		g.setColor(SELECTED_COLOR);

		String title = "JAVA STG";
		FontMetrics fm = g.getFontMetrics();
		int titleWidth = fm.stringWidth(title);
		int titleX = (width - titleWidth) / 2;
		int titleY = 200;

		g.drawString(title, titleX, titleY);
	}

	private void drawMainMenu(Graphics2D g, int width, int height) {
		int menuStartX = width / 2 - 100;
		int menuStartY = 300;
		int menuItemHeight = 60;

		g.setFont(new Font("Microsoft YaHei", Font.BOLD, 28));

		for (int i = 0; i < MAIN_MENU_ITEMS.length; i++) {
			int y = menuStartY + i * menuItemHeight;

			if (i == selectedIndex) {
				g.setColor(SELECTED_COLOR);
			} else {
				g.setColor(UNSELECTED_COLOR);
			}

			g.drawString(MAIN_MENU_ITEMS[i], menuStartX, y + 28);
		}
	}

	private void drawPlayerSelectMenu(Graphics2D g, int width, int height) {
		PlayerType[] playerTypes = PlayerType.values();
		int menuStartX = width / 2 - 150;
		int menuStartY = 200;
		int menuItemHeight = 100;

		g.setFont(new Font("Microsoft YaHei", Font.BOLD, 28));

		for (int i = 0; i < playerTypes.length; i++) {
			int y = menuStartY + i * menuItemHeight;
			PlayerType type = playerTypes[i];

			// 绘制背景框
			Color bgColor;
			if (i == selectedIndex) {
				bgColor = new Color(60, 60, 80, 200);
				g.setColor(new Color(255, 200, 100, 150));
				g.fillRect(menuStartX - 10, y - 10, 320, menuItemHeight - 5);
			} else {
				bgColor = new Color(40, 40, 50, 200);
			}
			g.setColor(bgColor);
			g.fillRect(menuStartX - 10, y - 10, 300, menuItemHeight - 5);
			g.setColor(new Color(100, 100, 120));
			g.drawRect(menuStartX - 10, y - 10, 300, menuItemHeight - 5);

			// 绘制玩家颜色预览
			Color playerColor = getPlayerColor(type);
			g.setColor(playerColor);
			g.fillOval(menuStartX + 10, y + 10, 50, 50);

			// 绘制玩家名称
			g.setColor(i == selectedIndex ? SELECTED_COLOR : Color.WHITE);
			g.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
			g.drawString(type.getName(), menuStartX + 80, y + 35);

			// 绘制玩家描述
			g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
			g.setColor(new Color(180, 180, 200));
			String[] lines = type.getDescription().split("，");
			g.drawString(lines[0], menuStartX + 80, y + 60);
			if (lines.length > 1) {
				g.drawString(lines[1], menuStartX + 80, y + 80);
			}

			// 选中标记
			if (i == selectedIndex) {
				g.setFont(new Font("Microsoft YaHei", Font.BOLD, 32));
				g.setColor(SELECTED_COLOR);
				g.drawString("▶", menuStartX - 40, y + 40);
			}
		}

		// 操作提示
		g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		g.setColor(new Color(150, 150, 150));
		g.drawString("↑ ↓  切换自机", width / 2 - 100, height - 60);
		g.drawString("Z/Enter 确认选择", width / 2 - 100, height - 40);
		g.drawString("ESC  返回主菜单", width / 2 - 100, height - 20);
	}

	private Color getPlayerColor(PlayerType type) {
		if (type == PlayerType.REIMU) {
			return new Color(255, 200, 220);
		} else if (type == PlayerType.MARISA) {
			return new Color(255, 220, 100);
		} else {
			return new Color(255, 100, 100);
		}
	}
}
