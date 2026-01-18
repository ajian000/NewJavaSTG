package stg.base;

import stg.game.*;
import javax.swing.*;
import java.awt.*;

/**
 * 窗口类 - STG游戏主窗口
 */
public class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel leftPanel; // 左侧面板
	private JPanel centerPanel; // 中间面板(游戏区域)
	private JPanel rightPanel; // 右侧面板
	private GameCanvas gameCanvas; // 游戏画布
	private int totalWidth = 1280; // 窗口总宽度
	private int totalHeight = 960; // 窗口总高度

	/**
	 * 构造函数
	 */
	public Window() {
		initialize();
	}

	/**
	 * 初始化窗口
	 */
	private void initialize() {
		setTitle("STG Game Engine"); // 设置标题
		setSize(totalWidth, totalHeight); // 设置大小
		setLocationRelativeTo(null); // 居中显示
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 关闭操作
		setResizable(false); // 禁止调整大小
		setLayout(new BorderLayout());

		// 计算三个面板的宽度 (1:1.5:1)
		int leftWidth = (int)(totalWidth / 3.5); // 左侧面板宽度
		int centerWidth = (int)(totalWidth * 1.5 / 3.5); // 中间面板宽度
		int rightWidth = totalWidth - leftWidth - centerWidth; // 右侧面板宽度

		// 创建左侧面板
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(leftWidth, totalHeight));
		leftPanel.setMinimumSize(new Dimension(leftWidth, totalHeight));
		leftPanel.setMaximumSize(new Dimension(leftWidth, totalHeight));
		leftPanel.setBackground(new Color(30, 30, 40));

		// 创建中间面板
		centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(centerWidth, totalHeight));
		centerPanel.setMinimumSize(new Dimension(centerWidth, totalHeight));
		centerPanel.setMaximumSize(new Dimension(centerWidth, totalHeight));
		centerPanel.setBackground(new Color(20, 20, 30));
		centerPanel.setLayout(new BorderLayout());

		// 创建游戏画布
		gameCanvas = new GameCanvas();
		centerPanel.add(gameCanvas, BorderLayout.CENTER);

		// 创建玩家并设置到画布
		Player player = new Player(centerWidth / 2, totalHeight);
		gameCanvas.setPlayer(player);

		// 启动游戏循环
		new GameLoop(gameCanvas).start();

		// 创建右侧面板
		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(rightWidth, totalHeight));
		rightPanel.setMinimumSize(new Dimension(rightWidth, totalHeight));
		rightPanel.setMaximumSize(new Dimension(rightWidth, totalHeight));
		rightPanel.setBackground(new Color(30, 30, 40));

		// 创建容器面板
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(leftPanel);
		mainPanel.add(centerPanel);
		mainPanel.add(rightPanel);

		add(mainPanel, BorderLayout.CENTER);
		setVisible(true);

		System.out.println("Created window with size: " + totalWidth + "x" + totalHeight);
		System.out.println("Left panel: " + leftWidth + "px");
		System.out.println("Center panel: " + centerWidth + "px");
		System.out.println("Right panel: " + rightWidth + "px");
	}

	/**
	 * 获取左侧面板
	 */
	public JPanel getLeftPanel() {
		return leftPanel;
	}

	/**
	 * 获取中间面板
	 */
	public JPanel getCenterPanel() {
		return centerPanel;
	}

	/**
	 * 获取右侧面板
	 */
	public JPanel getRightPanel() {
		return rightPanel;
	}

	/**
	 * 获取游戏画布
	 */
	public GameCanvas getGameCanvas() {
		return gameCanvas;
	}

	/**
	 * 获取窗口总宽度
	 */
	public int getTotalWidth() {
		return totalWidth;
	}

	/**
	 * 获取窗口总高度
	 */
	public int getTotalHeight() {
		return totalHeight;
	}
}
