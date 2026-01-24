package stg.game.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import stg.base.KeyStateProvider;
import stg.game.player.PlayerType;
import stg.util.ResourceManager;

/**
 * 标题界面类 - 游戏主菜单和角色选择
 * @Time 2026-01-20 将类移动到stg.game.ui包
 * @Time 2026-01-20 实现KeyStateProvider以支持虚拟键盘
 * @Time 2026-01-24 添加背景图片支持
 */
public class TitleScreen extends JPanel implements KeyStateProvider {
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
	private BufferedImage backgroundImage;
	private ResourceManager resourceManager;

	// 按键状态跟踪 - 供虚拟键盘使用
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean zPressed = false;

	public interface TitleCallback {
		void onGameStart(stg.game.player.PlayerType playerType);
		void onExit();
	}

	private TitleCallback callback;

	public TitleScreen(TitleCallback callback) {
		this.callback = callback;
		this.resourceManager = ResourceManager.getInstance();
		loadBackgroundImage();
		
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyPress(e);
			}
		});

		animationTimer = new Timer(16, e -> {
			animationFrame++;
			repaint();
		});
		animationTimer.start();
	}
	
	private void loadBackgroundImage() {
		// 尝试从文件系统加载
		backgroundImage = resourceManager.loadImage("ui_bg.png", "images");
		if (backgroundImage == null) {
			System.out.println("【警告】UI背景图片加载失败，使用默认背景色");
		} else {
			System.out.println("【资源】UI背景图片加载成功: " + 
				backgroundImage.getWidth() + "x" + backgroundImage.getHeight());
		}
	}

	private void handleKeyPress(KeyEvent e) {
		switch (currentState) {
			case MAIN_MENU:
				handleMainMenuKey(e);
				break;
			case PLAYER_SELECT:
				handlePlayerSelectKey(e);
				break;
		}
	}

	private void handleMainMenuKey(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				selectedIndex = (selectedIndex - 1 + MAIN_MENU_ITEMS.length) % MAIN_MENU_ITEMS.length;
				repaint();
				break;
			case KeyEvent.VK_DOWN:
				selectedIndex = (selectedIndex + 1) % MAIN_MENU_ITEMS.length;
				repaint();
				break;
			case KeyEvent.VK_Z:
			case KeyEvent.VK_ENTER:
				handleMainMenuSelection();
				break;
			case KeyEvent.VK_ESCAPE:
				callback.onExit();
				break;
		}
	}

	private void handlePlayerSelectKey(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_RIGHT:
				selectedPlayerType = PlayerType.values()[(selectedPlayerType.ordinal() + 1) % PlayerType.values().length];
				repaint();
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_LEFT:
				int prevIndex = selectedPlayerType.ordinal() - 1;
				if (prevIndex < 0) {
					prevIndex = PlayerType.values().length - 1;
				}
				selectedPlayerType = PlayerType.values()[prevIndex];
				repaint();
				break;
			case KeyEvent.VK_Z:
			case KeyEvent.VK_ENTER:
				callback.onGameStart(selectedPlayerType);
				break;
			case KeyEvent.VK_ESCAPE:
				currentState = MenuState.MAIN_MENU;
				repaint();
				break;
		}
	}

	private void handleMainMenuSelection() {
		switch (selectedIndex) {
			case 0:
				currentState = MenuState.PLAYER_SELECT;
				break;
			case 1:
				callback.onExit();
				break;
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int width = getWidth();
		int height = getHeight();

		// 绘制背景图片
		if (backgroundImage != null) {
			// 缩放图片以适应窗口
			g2d.drawImage(backgroundImage, 0, 0, width, height, null);
		} else {
			// 默认背景色
			g2d.setColor(BG_COLOR);
			g2d.fillRect(0, 0, width, height);
		}

		// 绘制标题
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 48));
		g2d.setColor(Color.WHITE);
		String title = "东方STG引擎";
		int titleWidth = g2d.getFontMetrics().stringWidth(title);
		g2d.drawString(title, width / 2 - titleWidth / 2, 100);

		// 绘制版本信息
		g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		g2d.setColor(Color.GRAY);
		g2d.drawString("Version 1.0", width - 80, height - 10);

		// 绘制菜单
		switch (currentState) {
			case MAIN_MENU:
				drawMainMenu(g2d, width, height);
				break;
			case PLAYER_SELECT:
				drawPlayerSelect(g2d, width, height);
				break;
		}
	}

	private void drawMainMenu(Graphics2D g2d, int width, int height) {
		g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));

		for (int i = 0; i < MAIN_MENU_ITEMS.length; i++) {
			String item = MAIN_MENU_ITEMS[i];
			int itemWidth = g2d.getFontMetrics().stringWidth(item);
			int x = width / 2 - itemWidth / 2;
			int y = height / 2 + i * 40;

			if (i == selectedIndex) {
				g2d.setColor(SELECTED_COLOR);
				// 绘制选中效果
				g2d.drawString("▶", x - 30, y);
			} else {
				g2d.setColor(UNSELECTED_COLOR);
			}
			g2d.drawString(item, x, y);
		}

		// 绘制操作提示
		g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		g2d.setColor(Color.GRAY);
		g2d.drawString("↑ ↓  选择菜单", width / 2 - 80, height - 40);
		g2d.drawString("Z/Enter 确认", width / 2 - 80, height - 20);
	}

	private void drawPlayerSelect(Graphics2D g2d, int width, int height) {
		g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
		g2d.setColor(Color.WHITE);
		String title = "选择自机";
		int titleWidth = g2d.getFontMetrics().stringWidth(title);
		g2d.drawString(title, width / 2 - titleWidth / 2, height / 2 - 80);

		// 绘制玩家类型
		PlayerType[] playerTypes = PlayerType.values();
		for (int i = 0; i < playerTypes.length; i++) {
			PlayerType type = playerTypes[i];
			String typeName = type.name();
			int x = width / 2 + (i - playerTypes.length / 2) * 150;
			int y = height / 2;

			if (type == selectedPlayerType) {
				g2d.setColor(SELECTED_COLOR);
				// 绘制选中框
				g2d.drawRect(x - 60, y - 20, 120, 40);
			} else {
				g2d.setColor(UNSELECTED_COLOR);
			}
			g2d.drawString(typeName, x - g2d.getFontMetrics().stringWidth(typeName) / 2, y);
		}

		// 绘制操作提示
		g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		g2d.setColor(Color.GRAY);
		g2d.drawString("↑ ↓  切换自机", width / 2 - 100, height - 60);
		g2d.drawString("Z/Enter 确认选择", width / 2 - 100, height - 40);
		g2d.drawString("ESC  返回主菜单", width / 2 - 100, height - 20);
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

	// 虚拟键盘接口实现
	@Override
	public boolean isUpPressed() { return upPressed; }
	@Override
	public boolean isDownPressed() { return downPressed; }
	@Override
	public boolean isLeftPressed() { return leftPressed; }
	@Override
	public boolean isRightPressed() { return rightPressed; }
	@Override
	public boolean isZPressed() { return zPressed; }

	public void setUpPressed(boolean upPressed) { this.upPressed = upPressed; }
	public void setDownPressed(boolean downPressed) { this.downPressed = downPressed; }
	public void setLeftPressed(boolean leftPressed) { this.leftPressed = leftPressed; }
	public void setRightPressed(boolean rightPressed) { this.rightPressed = rightPressed; }
	public void setZPressed(boolean zPressed) { this.zPressed = zPressed; }
}
