package stg.game.item;

import java.awt.*;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;

/**
 * 炸弹道具类 - 增加玩家炸弹数量
 */
public class BombUp extends Item {
	private static final float BOMBUP_SIZE = 12.0f;
	private static final Color BOMBUP_COLOR = new Color(255, 100, 100);
	private static final int BOMB_VALUE = 1; // 增加的炸弹数量

	public BombUp(float x, float y) {
		super(x, y, BOMBUP_SIZE, BOMBUP_COLOR);
	}

	public BombUp(float x, float y, float vx, float vy) {
		super(x, y, vx, vy, BOMBUP_SIZE, BOMBUP_COLOR);
	}

	public BombUp(float x, float y, GameCanvas gameCanvas) {
		super(x, y, BOMBUP_SIZE, BOMBUP_COLOR, gameCanvas);
	}

	@Override
	public void update() {
		super.update();

		// 如果有游戏画布，向玩家方向缓慢移动
		if (gameCanvas != null) {
			Player player = gameCanvas.getPlayer();
			if (player != null && player.isSlowMode()) {
				float dx = player.getX() - x;
				float dy = player.getY() - y;
				float distance = (float)Math.sqrt(dx * dx + dy * dy);

				if (distance < 150.0f) {
					float attractionSpeed = 3.0f;
					vx = (dx / distance) * attractionSpeed;
					vy = (dy / distance) * attractionSpeed;
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		if (!active) return;

		float screenX = x;
		float screenY = y;

		if (gameCanvas != null) {
			float[] coords = gameCanvas.getCoordinateSystem().toScreenCoords(x, y);
			screenX = coords[0];
			screenY = coords[1];
		} else {
			screenX = x + 548 / 2.0f;
			screenY = 921 / 2.0f - y;
		}

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 绘制B标志
		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, (int)(size * 1.2f)));
		String text = "B";
		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getHeight();
		g.drawString(text, (int)(screenX - textWidth / 2), (int)(screenY + textHeight / 4));
	}

	@Override
	public void onCollect() {
		super.onCollect();

		if (gameCanvas != null) {
			Player player = gameCanvas.getPlayer();
			if (player != null) {
				// 增加玩家炸弹数量（这里可以扩展Player类来支持炸弹系统）
				System.out.println("BombUp collected! Bomb +1");
			}
		}
	}

	public int getBombValue() {
		return BOMB_VALUE;
	}
}
