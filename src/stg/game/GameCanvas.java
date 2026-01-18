package stg.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends JPanel {
	private static final long serialVersionUID = 1L;
	private Player player;

	public GameCanvas() {
		setBackground(new Color(20, 20, 30));
		setDoubleBuffered(true);
		setFocusable(true);

		setupInput();

		System.out.println("GameCanvas created");
	}

	private void setupInput() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (player == null) return;

				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						player.moveUp();
						break;
					case KeyEvent.VK_DOWN:
						player.moveDown();
						break;
					case KeyEvent.VK_LEFT:
						player.moveLeft();
						break;
					case KeyEvent.VK_RIGHT:
						player.moveRight();
						break;
					case KeyEvent.VK_SHIFT:
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
					case KeyEvent.VK_SHIFT:
						player.setSlowMode(false);
						break;
				}
			}
		});
	}

	public void setPlayer(Player player) {
		this.player = player;
		player.setGameCanvas(this);
	}

	public void update() {
		if (player != null) {
			player.update();
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (player != null) {
			player.render(g2d);
		}
	}
}
