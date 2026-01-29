package stg.game.item;

import java.awt.*;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;

/**
 * 生命值道具类 - 恢复玩家生命
 */
public class LifeUp extends Item {
	private static final float LIFEUP_SIZE = 12.0f;
	private static final Color LIFEUP_COLOR = new Color(50, 255, 100);
	private static final int LIFE_VALUE = 1; // 恢复的生命值

	public LifeUp(float x, float y) {
		super(x, y, LIFEUP_SIZE, LIFEUP_COLOR);
	}

	public LifeUp(float x, float y, float vx, float vy) {
		super(x, y, vx, vy, LIFEUP_SIZE, LIFEUP_COLOR);
	}

	public LifeUp(float x, float y, GameCanvas gameCanvas) {
		super(x, y, LIFEUP_SIZE, LIFEUP_COLOR, gameCanvas);
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

		// 绘制心形
		g.setColor(color);
		int heartSize = (int)(size * 2);
		int heartX = (int)(screenX - size);
		int heartY = (int)(screenY - size);

		g.fillOval(heartX, heartY, heartSize / 2, heartSize / 2);
		g.fillOval(heartX + heartSize / 2, heartY, heartSize / 2, heartSize / 2);

		int[] triangleX = {heartX, heartX + heartSize, heartX + heartSize / 2};
		int[] triangleY = {heartY + heartSize / 4, heartY + heartSize / 4, heartY + heartSize};
		g.fillPolygon(triangleX, triangleY, 3);
	}

	@Override
	public void onCollect() {
		super.onCollect();

		if (gameCanvas != null) {
			Player player = gameCanvas.getPlayer();
			if (player != null) {
				// 恢复玩家生命（这里可以扩展Player类来支持生命系统）
				System.out.println("LifeUp collected! Life +1");
			}
		}
	}

	public int getLifeValue() {
		return LIFE_VALUE;
	}
}
