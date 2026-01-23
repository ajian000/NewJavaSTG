package stg.game.bullet;

import java.awt.Color;
import stg.game.enemy.EnemyBullet;
import stg.game.player.Player;

/**
 * 追踪子弹类 - 会追踪玩家的子弹
 * @Time 2026-01-23
 */
public class TrackingBullet extends EnemyBullet {
	private float speed;
	private float turnSpeed;
	private int frame;
	private int delayFrames;

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param speed 子弹速度
	 * @param initialAngle 初始角度
	 * @param turnSpeed 转向速度（弧度/帧）
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 */
	public TrackingBullet(float x, float y, float speed, float initialAngle,
						float turnSpeed, float size, Color color) {
		super(x, y, (float)Math.cos(initialAngle) * speed, (float)Math.sin(initialAngle) * speed, size, color, 10);
		this.speed = speed;
		this.turnSpeed = turnSpeed;
		this.frame = 0;
		this.delayFrames = 0;
	}

	/**
	 * 设置延迟帧数（延迟后才开始追踪）
	 * @param delayFrames 延迟帧数
	 */
	public void setDelayFrames(int delayFrames) {
		this.delayFrames = delayFrames;
	}

	/**
	 * 更新子弹位置
	 */
	@Override
	public void update() {
		frame++;

		if (frame > delayFrames && gameCanvas != null) {
			Player player = gameCanvas.getPlayer();
			if (player != null) {
				float targetX = player.getX();
				float targetY = player.getY();

				float dx = targetX - x;
				float dy = targetY - y;
				float targetAngle = (float)Math.atan2(dy, dx);

				float currentAngle = (float)Math.atan2(vy, vx);
				float angleDiff = targetAngle - currentAngle;

				while (angleDiff > Math.PI) angleDiff -= 2 * Math.PI;
				while (angleDiff < -Math.PI) angleDiff += 2 * Math.PI;

				if (angleDiff > turnSpeed) {
					angleDiff = turnSpeed;
				} else if (angleDiff < -turnSpeed) {
					angleDiff = -turnSpeed;
				}

				float newAngle = currentAngle + angleDiff;
				vx = (float)Math.cos(newAngle) * speed;
				vy = (float)Math.sin(newAngle) * speed;
			}
		}

		x += vx;
		y += vy;
	}

	/**
	 * 追踪子弹不需要特殊的独立线程逻辑
	 */
	protected void task() {
	}
}
