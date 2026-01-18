package stg.base;

import stg.game.*;
import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel leftPanel;
	private JPanel centerPanel;
	private JPanel rightPanel;
	private GameCanvas gameCanvas;
	private int totalWidth = 1280;
	private int totalHeight = 960;

	public Window() {
		initialize();
	}

	private void initialize() {
		setTitle("STG Game Engine");
		setSize(totalWidth, totalHeight);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());

		int leftWidth = (int)(totalWidth / 3.5);
		int centerWidth = (int)(totalWidth * 1.5 / 3.5);
		int rightWidth = totalWidth - leftWidth - centerWidth;

		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(leftWidth, totalHeight));
		leftPanel.setMinimumSize(new Dimension(leftWidth, totalHeight));
		leftPanel.setMaximumSize(new Dimension(leftWidth, totalHeight));
		leftPanel.setBackground(new Color(30, 30, 40));

		centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(centerWidth, totalHeight));
		centerPanel.setMinimumSize(new Dimension(centerWidth, totalHeight));
		centerPanel.setMaximumSize(new Dimension(centerWidth, totalHeight));
		centerPanel.setBackground(new Color(20, 20, 30));
		centerPanel.setLayout(new BorderLayout());

		gameCanvas = new GameCanvas();
		centerPanel.add(gameCanvas, BorderLayout.CENTER);

		Player player = new Player(centerWidth / 2, totalHeight - 150);
		gameCanvas.setPlayer(player);

		new GameLoop(gameCanvas).start();

		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(rightWidth, totalHeight));
		rightPanel.setMinimumSize(new Dimension(rightWidth, totalHeight));
		rightPanel.setMaximumSize(new Dimension(rightWidth, totalHeight));
		rightPanel.setBackground(new Color(30, 30, 40));

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

	public JPanel getLeftPanel() {
		return leftPanel;
	}

	public JPanel getCenterPanel() {
		return centerPanel;
	}

	public JPanel getRightPanel() {
		return rightPanel;
	}

	public GameCanvas getGameCanvas() {
		return gameCanvas;
	}

	public int getTotalWidth() {
		return totalWidth;
	}

	public int getTotalHeight() {
		return totalHeight;
	}
}
