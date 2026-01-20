package stg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关卡数据类 - 存储关卡信息
 */
public class LevelData {
	private String name; // 关卡名称
	private Map<String, String> metadata; // 元数据
	private List<EnemySpawnData> enemies; // 敌人生成数据
	private List<Map<String, Object>> events; // 关卡事件

	public LevelData() {
		this.name = "";
		this.metadata = new HashMap<>();
		this.enemies = new ArrayList<>();
		this.events = new ArrayList<>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addMetadata(String key, String value) {
		metadata.put(key, value);
	}

	public String getMetadata(String key) {
		return metadata.get(key);
	}

	public void addEnemy(EnemySpawnData enemy) {
		enemies.add(enemy);
	}

	public List<EnemySpawnData> getEnemies() {
		return enemies;
	}

	public void addEvent(Map<String, Object> event) {
		events.add(event);
	}

	public List<Map<String, Object>> getEvents() {
		return events;
	}
}
