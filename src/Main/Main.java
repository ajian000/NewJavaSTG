package Main;

import java.awt.EventQueue;
import stg.base.Window;
import stg.game.ui.GameCanvas;
import stg.game.ui.TitleScreen;
import stg.util.LevelManager;

public class Main {
	private static Window window;
	private static LevelManager levelManager;
	private static TitleScreen titleScreen;
	private static stg.game.player.PlayerType selectedPlayerType = stg.game.player.PlayerType.DEFAULT;

	public static void main(String[] args) {
		System.out.println("启动 STG 游戏引擎");

		levelManager = LevelManager.getInstance();

		EventQueue.invokeLater(() -> {
			window = new Window(false);
			showTitleScreen();
		});
	}

	private static void showTitleScreen() {
		titleScreen = new TitleScreen(new TitleScreen.TitleCallback() {
			@Override
			public void onGameStart(stg.game.player.PlayerType playerType) {
				System.out.println("开始游戏: " + playerType.getName());
				startGame(playerType);
			}

			@Override
			public void onExit() {
				System.out.println("退出游戏");
				System.exit(0);
			}
		});

		window.getCenterPanel().removeAll();
		window.getCenterPanel().add(titleScreen);

		// 更新虚拟键盘以显示标题界面的按键状态
		window.getVirtualKeyboardPanel().setKeyStateProvider(titleScreen);

		titleScreen.requestFocusInWindow();
		window.revalidate();
		window.repaint();
	}

	private static void startGame(stg.game.player.PlayerType playerType) {
		titleScreen.stopTitleMusic();
		
		window.initializePlayer(playerType);
		window.getCenterPanel().remove(titleScreen);

		// 获取游戏画布并更新虚拟键盘按键状态提供者
		GameCanvas gameCanvas = window.getGameCanvas();
		window.getVirtualKeyboardPanel().setKeyStateProvider(gameCanvas);

		window.getCenterPanel().add(gameCanvas);

		// 重置游戏状态为新对局
		gameCanvas.resetGame();

		// 设置玩家位置到屏幕底部中心
		stg.game.player.Player player = gameCanvas.getPlayer();
		if (player != null) {
			int canvasHeight = gameCanvas.getHeight();
			float actualPlayerX = 0; // 水平居中
			float actualPlayerY = -canvasHeight / 2.0f + 40; // 距离底部40像素(Y为负值)
			player.setPosition(actualPlayerX, actualPlayerY);
			System.out.println("玩家出生位置: (" + actualPlayerX + ", " + actualPlayerY + ")");
		}

		gameCanvas.requestFocusInWindow();
		new stg.game.GameLoop(gameCanvas).start();
		System.out.println("游戏开始，自机: " + playerType.getName());
	}

	public static Window getWindow() {
		return window;
	}

	public static LevelManager getLevelManager() {
		return levelManager;
	}

	public static stg.game.player.PlayerType getSelectedPlayerType() {
		return selectedPlayerType;
	}

	public static void returnToTitle() {
		// 停止游戏循环
		stg.game.GameLoop.stopAll();

		// 返回标题界面
		showTitleScreen();
	}
}
