package stg.game.item;

import java.awt.Color;

/**
 * 能量道具行为示例
 * 展示如何在新架构下实现简单的道具行为
 */
public class PowerUpExample extends Item {
	private float fallSpeed;
	private float floatAmplitude;
	private float floatFrequency;

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 */
	public PowerUpExample(float x, float y) {
		super(x, y, 12, Color.GREEN);
	}

	/**
	 * 初始化行为参数
	 */
	@Override
	protected void initBehavior() {
		fallSpeed = 1.0f;
		floatAmplitude = 0.5f;
		floatFrequency = 0.1f;
		
		// 初始化速度，向下掉落并左右浮动
		vx = 0;
		vy = fallSpeed;
	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		// 道具的更新逻辑
		// 例如添加闪烁效果、生命周期管理等
	}

	/**
	 * 实现自定义移动逻辑
	 */
	@Override
	protected void onMove() {
		// 添加左右浮动效果
		vx = (float) Math.sin(frame * floatFrequency) * floatAmplitude;
	}

	/**
	 * 道具被玩家拾取时的处理
	 */
	@Override
	public void onCollect() {
		super.onCollect();
		// 实现道具被拾取后的效果
		// 例如增加玩家能量、得分等
	}
}
