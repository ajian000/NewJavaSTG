package stg.base;

import stg.game.ui.GameCanvas;
import javax.swing.*;
import java.awt.*;

/**
 * 虚拟键盘面板 - 显示按键状态
 */
public class VirtualKeyboardPanel extends JPanel {
	private GameCanvas gameCanvas;
	private Timer updateTimer;
	private int keyWidth = 45;
	private int keyHeight = 45;
	private int keyGap = 6;

	// 按键状态
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean shiftPressed = false;
	private boolean zPressed = false;
	private boolean xPressed = false;

	public VirtualKeyboardPanel(GameCanvas gameCanvas) {
		this.gameCanvas = gameCanvas;
		setBackground(new Color(30, 30, 40));
		setLayout(new BorderLayout());

		// 创建虚拟键盘画布
		JPanel keyboardPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				drawKeyboard(g2d);
			}
		};
		keyboardPanel.setBackground(new Color(30, 30, 40));
		add(keyboardPanel, BorderLayout.CENTER);

		// 启动更新定时器（每50毫秒更新一次）
		updateTimer = new Timer(50, e -> {
			updateKeyStates();
			repaint();
		});
		updateTimer.start();
	}

	/**
	 * 更新按键状态
	 */
	private void updateKeyStates() {
		if (gameCanvas == null) return;
		upPressed = gameCanvas.isUpPressed();
		downPressed = gameCanvas.isDownPressed();
		leftPressed = gameCanvas.isLeftPressed();
		rightPressed = gameCanvas.isRightPressed();
		shiftPressed = gameCanvas.isShiftPressed();
		zPressed = gameCanvas.isZPressed();
		xPressed = gameCanvas.isXPressed();
	}

	/**
	 * 绘制虚拟键盘
	 */
	private void drawKeyboard(Graphics2D g) {
		int panelWidth = getWidth();
		int panelHeight = getHeight();

		// 计算中心位置
		int centerX = panelWidth / 2;
		int centerY = panelHeight / 2;

		// 方向键区域（标准的T型布局）
		// ↑键位置
		int upX = centerX - 55;
		int upY = centerY - 50;

		// ↓键位置
		int downX = upX;
		int downY = upY + keyHeight + keyGap;

		// ←键位置
		int leftX = upX - keyWidth - keyGap;
		int leftY = downY;

		// →键位置
		int rightX = upX + keyWidth + keyGap;
		int rightY = downY;

		// 绘制方向键
		drawKey(g, upX, upY, keyWidth, keyHeight, "↑", upPressed);
		drawKey(g, downX, downY, keyWidth, keyHeight, "↓", downPressed);
		drawKey(g, leftX, leftY, keyWidth, keyHeight, "←", leftPressed);
		drawKey(g, rightX, rightY, keyWidth, keyHeight, "→", rightPressed);

		// 功能键区域
		int funcX = centerX + 25;
		int funcY = centerY - 50;

		// Shift键（长键）
		drawKey(g, funcX, funcY, keyWidth * 2 + keyGap, keyHeight, "Shift", shiftPressed);

		// Z和X键
		int zxY = funcY + keyHeight + keyGap + 15;
		drawKey(g, funcX, zxY, keyWidth, keyHeight, "Z", zPressed);
		drawKey(g, funcX + keyWidth + keyGap, zxY, keyWidth, keyHeight, "X", xPressed);
	}

	/**
	 * 绘制单个按键
	 */
	private void drawKey(Graphics2D g, int x, int y, int width, int height, String text, boolean pressed) {
		// 绘制按键背景
		if (pressed) {
			g.setColor(new Color(100, 200, 100)); // 按下时为绿色
			g.fillRect(x, y, width, height);

			// 按下效果（内阴影）
			g.setColor(new Color(80, 180, 80));
			g.fillRect(x + 2, y + 2, width - 4, height - 4);

			// 按下效果（进一步缩小）
			g.setColor(new Color(60, 160, 60));
			g.fillRect(x + 4, y + 4, width - 8, height - 8);
		} else {
			// 正常状态 - 渐变效果
			g.setColor(new Color(60, 60, 80));
			g.fillRect(x, y, width, height);

			// 边框
			g.setColor(new Color(100, 100, 120));
			g.drawRect(x, y, width, height);
		}

		// 绘制按键文字
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getHeight();

		// 文字位置（按下时文字稍微偏移）
		int textX = x + (width - textWidth) / 2;
		int textY = y + (height - textHeight) / 2 + fm.getAscent();

		if (pressed) {
			textX += 2;
			textY += 2;
		}

		g.drawString(text, textX, textY);
	}

	/**
	 * 停止更新定时器
	 */
	public void stop() {
		if (updateTimer != null) {
			updateTimer.stop();
		}
	}
}
