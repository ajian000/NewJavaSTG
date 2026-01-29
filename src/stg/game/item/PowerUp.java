package stg.game.item;

import java.awt.*;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;

/**
 * 强化道具类 - 增加玩家火力
 */
public class PowerUp extends Item {
	private static final float POWERUP_SIZE = 12.0f;
	private static final Color POWERUP_COLOR = new Color(255, 200, 50);
	private static final int POWER_VALUE = 1; // 增加的火力值

	public PowerUp(float x, float y) {
		super(x, y, POWERUP_SIZE, POWERUP_COLOR);
	}

	public PowerUp(float x, float y, float vx, float vy) {
		super(x, y, vx, vy, POWERUP_SIZE, POWERUP_COLOR);
	}

	public PowerUp(float x, float y, GameCanvas gameCanvas) {
		super(x, y, POWERUP_SIZE, POWERUP_COLOR, gameCanvas);
	}

	@Override
	public void update() {
		super.update();

		// 如果有游戏画布，向玩家方向缓慢移动
		if (gameCanvas != null) {
			Player player = gameCanvas.getPlayer();
			if (player != null && player.isSlowMode()) {
				// 低速模式下，道具向玩家移动
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

		// 绘制P标志
		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, (int)(size * 1.2f)));
		String text = "P";
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
				// 增加玩家火力（这里可以扩展Player类来支持火力系统）
				System.out.println("PowerUp collected! Power +1");
			}
		}
	}

	public int getPowerValue() {
		return POWER_VALUE;
	}
}
