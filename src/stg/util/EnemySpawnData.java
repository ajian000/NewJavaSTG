package stg.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 敌人生成数据
 */
public class EnemySpawnData {
	private String type; // 敌人类型
	private float x; // X坐标
	private float y; // Y坐标
	private float speed; // 速度
	private int frame; // 生成帧数
	private Map<String, Object> params; // 额外参数

	public EnemySpawnData(String type, float x, float y, float speed, int frame) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.frame = frame;
		this.params = new HashMap<>();
	}

	// Getters and Setters
	public String getType() { return type; }
	public float getX() { return x; }
	public float getY() { return y; }
	public float getSpeed() { return speed; }
	public int getFrame() { return frame; }
	public Map<String, Object> getParams() { return params; }
	public void addParam(String key, Object value) { params.put(key, value); }
}
