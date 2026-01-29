package stg.game.item;

import java.awt.*;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;

public class ScorePoint extends Item {
	private static final float SCORE_POINT_SIZE = 8.0f;
	private static final Color SCORE_POINT_COLOR = new Color(100, 150, 255);
	private static final int SCORE_VALUE = 100;

	private int scoreValue;
	private boolean isLarge;

	public ScorePoint(float x, float y) {
		this(x, y, SCORE_VALUE, false, null);
	}

	public ScorePoint(float x, float y, int scoreValue) {
		this(x, y, scoreValue, false, null);
	}

	public ScorePoint(float x, float y, boolean isLarge) {
		this(x, y, SCORE_VALUE, isLarge, null);
	}

	public ScorePoint(float x, float y, int scoreValue, boolean isLarge) {
		this(x, y, scoreValue, isLarge, null);
	}

	public ScorePoint(float x, float y, GameCanvas gameCanvas) {
		this(x, y, SCORE_VALUE, false, gameCanvas);
	}

	public ScorePoint(float x, float y, int scoreValue, GameCanvas gameCanvas) {
		this(x, y, scoreValue, false, gameCanvas);
	}

	public ScorePoint(float x, float y, boolean isLarge, GameCanvas gameCanvas) {
		this(x, y, SCORE_VALUE, isLarge, gameCanvas);
	}

	public ScorePoint(float x, float y, int scoreValue, boolean isLarge, GameCanvas gameCanvas) {
		super(x, y, 0, 0, isLarge ? SCORE_POINT_SIZE * 1.5f : SCORE_POINT_SIZE, SCORE_POINT_COLOR, gameCanvas);
		this.scoreValue = scoreValue;
		this.isLarge = isLarge;
	}

	public ScorePoint(float x, float y, float vx, float vy, GameCanvas gameCanvas) {
		this(x, y, vx, vy, SCORE_VALUE, false, gameCanvas);
	}

	public ScorePoint(float x, float y, float vx, float vy, int scoreValue, GameCanvas gameCanvas) {
		this(x, y, vx, vy, scoreValue, false, gameCanvas);
	}

	public ScorePoint(float x, float y, float vx, float vy, boolean isLarge, GameCanvas gameCanvas) {
		this(x, y, vx, vy, SCORE_VALUE, isLarge, gameCanvas);
	}

	public ScorePoint(float x, float y, float vx, float vy, int scoreValue, boolean isLarge, GameCanvas gameCanvas) {
		super(x, y, vx, vy, isLarge ? SCORE_POINT_SIZE * 1.5f : SCORE_POINT_SIZE, SCORE_POINT_COLOR, gameCanvas);
		this.scoreValue = scoreValue;
		this.isLarge = isLarge;
	}

	/**
	 * 初始化行为参数
	 */
	@Override
	protected void initBehavior() {
		// 初始化行为参数
	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		// 如果有游戏画布，向玩家方向缓慢移动
		if (gameCanvas != null) {
			Player player = gameCanvas.getPlayer();
			if (player != null && player.isSlowMode()) {
				float dx = player.getX() - x;
				float dy = player.getY() - y;
				float distance = (float)Math.sqrt(dx * dx + dy * dy);

				if (distance < 200.0f) {
					float attractionSpeed = 4.0f;
					vx = (dx / distance) * attractionSpeed;
					vy = (dy / distance) * attractionSpeed;
				}
			}
		}
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

		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		g.setColor(new Color(150, 200, 255, 100));
		g.fillOval((int)(screenX - size * 1.2f), (int)(screenY - size * 1.2f), (int)(size * 2.4f), (int)(size * 2.4f));

		g.setColor(new Color(255, 255, 255, 200));
		g.fillOval((int)(screenX - size * 0.3f), (int)(screenY - size * 0.3f), (int)(size * 0.6f), (int)(size * 0.6f));

		if (isLarge) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, (int)(size * 0.8f)));
			String text = String.valueOf(scoreValue);
			FontMetrics fm = g.getFontMetrics();
			int textWidth = fm.stringWidth(text);
			int textHeight = fm.getHeight();
			g.drawString(text, (int)(screenX - textWidth / 2), (int)(screenY + textHeight / 4));
		}
	}

	@Override
	public void onCollect() {
		super.onCollect();

		if (gameCanvas != null) {
			Player player = gameCanvas.getPlayer();
			if (player != null) {
				System.out.println("ScorePoint collected! Score +" + scoreValue);
			}
		}
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}

	public boolean isLarge() {
		return isLarge;
	}

	public void setLarge(boolean isLarge) {
		this.isLarge = isLarge;
		this.size = isLarge ? SCORE_POINT_SIZE * 1.5f : SCORE_POINT_SIZE;
	}

	public static ScorePoint createSmall(float x, float y, GameCanvas gameCanvas) {
		return new ScorePoint(x, y, SCORE_VALUE, false, gameCanvas);
	}

	public static ScorePoint createLarge(float x, float y, GameCanvas gameCanvas) {
		return new ScorePoint(x, y, SCORE_VALUE * 10, true, gameCanvas);
	}

	public static ScorePoint createCustom(float x, float y, int scoreValue, GameCanvas gameCanvas) {
		boolean isLarge = scoreValue >= SCORE_VALUE * 10;
		return new ScorePoint(x, y, scoreValue, isLarge, gameCanvas);
	}
}