package user.item;

import java.awt.*;
import user.player.Player;
import stg.game.ui.GameCanvas;
import stg.game.item.Item;

/**
 * 生命值道具类 - 恢复玩家生命
 */
public class LifeUp extends Item {
	private static final float LIFEUP_SIZE = 12.0f;
	private static final Color LIFEUP_COLOR = new Color(50, 255, 100);
	private static final int LIFE_VALUE = 1; // 恢复的生命�?
	public LifeUp(float x, float y) {
		super(x, y, LIFEUP_SIZE, LIFEUP_COLOR);
		setAttractionParams(150.0f, 3.0f);
	}

	public LifeUp(float x, float y, float vx, float vy) {
		super(x, y, vx, vy, LIFEUP_SIZE, LIFEUP_COLOR);
		setAttractionParams(150.0f, 3.0f);
	}

	public LifeUp(float x, float y, GameCanvas gameCanvas) {
		super(x, y, LIFEUP_SIZE, LIFEUP_COLOR, gameCanvas);
		setAttractionParams(150.0f, 3.0f);
	}

	/**
	 * 初始化行为参�?	 */
	@Override
	protected void initBehavior() {
		// 初始化行为参�?	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		applyAttraction();
	}

	/**
	 * 实现自定义移动逻辑
	 */
	@Override
	protected void onMove() {
		// 自定义移动逻辑
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void render(Graphics2D g) {
		if (!active) return;

		float[] screenCoords = toScreenCoords(x, y);
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		stg.util.RenderUtils.enableAntiAliasing(g);

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
				// 恢复玩家生命（这里可以扩展Player类来支持生命系统�?				System.out.println("LifeUp collected! Life +1");
			}
		}
	}

	public int getLifeValue() {
		return LIFE_VALUE;
	}
}

