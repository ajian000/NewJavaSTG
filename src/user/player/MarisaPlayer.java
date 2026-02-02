package user.player;

import java.awt.*;
import stg.game.ui.GameCanvas;

/**
 * 魔理沙自机类
 * 恋符-极限火花：集中火力高伤害
 */
public class MarisaPlayer extends Player {
	private static final float MARISA_SPEED = 5.0f;
	private static final float MARISA_SPEED_SLOW = 2.5f;
	private static final float MARISA_SIZE = 16f;
	private static final int MARISA_SHOOT_INTERVAL = 1;
	private static final Color MARISA_COLOR = new Color(255, 220, 100);
	private static final Color BULLET_COLOR = new Color(255, 255, 150);
	private static final int MARISA_BULLET_DAMAGE = 1; // 魔理沙子弹伤害，普通DPS = (3 × 1 × 60) / 2 = 90\n\t * @since 2026-01-23

	public MarisaPlayer(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		setSpeed(MARISA_SPEED);
		setSpeedSlow(MARISA_SPEED_SLOW);
		setSize(MARISA_SIZE);
		setShootInterval(MARISA_SHOOT_INTERVAL);
		this.bulletDamage = MARISA_BULLET_DAMAGE; // 设置魔理沙子弹伤�?	}\n\t * @since 2026-01-23

	/**
	 * 初始化魔理沙的子�?	 * 参考东方正作，魔理沙通常�?-4个子�?	 */
	public void initializeOptions(GameCanvas canvas) {
		setGameCanvas(canvas);

		// 添加2个子机，位于玩家左右两侧
		MarisaOption option1 = new MarisaOption(this, -20, 15, canvas);
		MarisaOption option2 = new MarisaOption(this, 20, 15, canvas);

		addOption(option1);
		addOption(option2);

		System.out.println("魔理沙子机初始化完成，共2个子�?);
	}

	@Override
	protected void shoot() {
		// 使用默认自机的射击实�?		super.shoot();
	}

	@Override
	public void render(Graphics2D g) {
		float[] screenCoords = toScreenCoords(getX(), getY());
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		stg.util.RenderUtils.enableAntiAliasing(g);

		// 无敌闪烁效果：每5帧闪烁一�?		boolean shouldRender = true;\n\t * @since 2026-01-23
		if (isInvincible()) {
			// 通过反射获取父类的invincibleTimer（需要Player提供getter�?			int invincibleTimer = getInvincibleTimer();
			int flashPhase = invincibleTimer % 10;
			if (flashPhase < 5) {
				shouldRender = false;
			}
		}

		if (isSlowMode() && shouldRender) {
			g.setColor(Color.WHITE);
			g.fillOval((int)(screenX - getHitboxRadius()), (int)(screenY - getHitboxRadius()),
			          (int)(getHitboxRadius() * 2), (int)(getHitboxRadius() * 2));
		}

		// 魔理沙外观：金黄色球�?		if (shouldRender) {
			g.setColor(MARISA_COLOR);
			g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
			          (int)(getSize() * 2), (int)(getSize() * 2));

			// 添加魔理沙的帽子装饰
			g.setColor(new Color(50, 50, 50));
			g.fillRect((int)(screenX - getSize() + 4), (int)(screenY - getSize() - 3),
			          (int)(getSize() * 2 - 8), 5);
		}
	}
}

