package stg.util;

import java.io.*;
import java.util.*;

/**
 * JSON关卡加载器 - 手动解析JSON格式关卡文件
 * 不依赖脚本引擎,兼容所有Java版本
 */
public class JsonLevelLoader implements LevelLoader {

	@Override
	public LevelData loadLevel(String scriptFile) {
		LevelData levelData = new LevelData();

		try {
			// 读取脚本文件
			System.out.println("Reading JSON file: " + scriptFile);
			String jsonContent = readFile(scriptFile);
			System.out.println("JSON content length: " + jsonContent.length());

			// 解析JSON
			Map<String, Object> data = parseJson(jsonContent);

			// 解析关卡名称
			if (data.containsKey("name")) {
				String name = data.get("name").toString();
				levelData.setName(name);
				System.out.println("Level name: " + name);
			}

			// 解析敌人数据
			if (data.containsKey("enemies")) {
				Object enemiesObj = data.get("enemies");
				if (enemiesObj instanceof List) {
					List<Map<String, Object>> enemiesList = (List<Map<String, Object>>) enemiesObj;
					System.out.println("Found " + enemiesList.size() + " enemies in JSON");
					for (Map<String, Object> enemyData : enemiesList) {
						String type = enemyData.get("type").toString();
						float x = Float.parseFloat(enemyData.get("x").toString());
						float y = Float.parseFloat(enemyData.get("y").toString());
						float speed = Float.parseFloat(enemyData.get("speed").toString());
						int frame = Integer.parseInt(enemyData.get("frame").toString());

						EnemySpawnData spawn = new EnemySpawnData(type, x, y, speed, frame);
						levelData.addEnemy(spawn);
					}
				}
			}

			// 解析事件
			if (data.containsKey("events")) {
				Object eventsObj = data.get("events");
				if (eventsObj instanceof List) {
					List<Map<String, Object>> eventsList = (List<Map<String, Object>>) eventsObj;
					System.out.println("Found " + eventsList.size() + " events in JSON");
					for (Map<String, Object> eventData : eventsList) {
						levelData.addEvent(eventData);
					}
				}
			}

		} catch (IOException e) {
			System.err.println("File read error: " + e.getMessage());
			e.printStackTrace();
		}

		return levelData;
	}

	@Override
	public Object executeScript(String script) {
		// JSON加载器不支持直接执行脚本
		System.err.println("JSON loader does not support direct script execution");
		return null;
	}

	/**
	 * 简单JSON解析器(仅支持标准格式)
	 */
	private Map<String, Object> parseJson(String json) {
		json = json.trim();
		// 移除最外层的大括号
		if (json.startsWith("{") && json.endsWith("}")) {
			String inner = json.substring(1, json.length() - 1).trim();
			return parseObject(inner);
		}
		return new HashMap<>();
	}

	/**
	 * 解析JSON对象
	 */
	private Map<String, Object> parseObject(String json) {
		Map<String, Object> map = new HashMap<>();
		int depth = 0;
		StringBuilder currentKey = new StringBuilder();
		StringBuilder currentValue = new StringBuilder();
		boolean inString = false;
		boolean inKey = true;

		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);

			if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
				inString = !inString;
			}

			if (!inString) {
				if (c == '{') {
					depth++;
					inKey = true;
				} else if (c == '}') {
					depth--;
					if (depth == 0) {
						// 对象结束
						if (currentKey.length() > 0 && currentValue.length() > 0) {
							map.put(currentKey.toString().trim(), parseValue(currentValue.toString()));
						}
						return map;
					}
				} else if (c == ':' && depth == 0) {
					inKey = false;
				} else if (c == ',' && depth == 0) {
					if (currentKey.length() > 0 && currentValue.length() > 0) {
						map.put(currentKey.toString().trim(), parseValue(currentValue.toString()));
					}
					currentKey.setLength(0);
					currentValue.setLength(0);
					inKey = true;
				} else if (c == '[' || c == ']') {
					if (inKey) {
						currentKey.append(c);
					} else {
						currentValue.append(c);
					}
				} else {
					if (inKey) {
						currentKey.append(c);
					} else {
						currentValue.append(c);
					}
				}
			} else {
				if (inKey) {
					currentKey.append(c);
				} else {
					currentValue.append(c);
				}
			}
		}

		// 最后一个值
		if (currentKey.length() > 0 && currentValue.length() > 0) {
			map.put(currentKey.toString().trim(), parseValue(currentValue.toString()));
		}

		return map;
	}

	/**
	 * 解析JSON值
	 */
	private Object parseValue(String value) {
		value = value.trim();
		if (value.startsWith("{") && value.endsWith("}")) {
			return parseObject(value.substring(1, value.length() - 1));
		} else if (value.startsWith("[") && value.endsWith("]")) {
			return parseArray(value.substring(1, value.length() - 1));
		} else if (value.startsWith("\"") && value.endsWith("\"")) {
			return value.substring(1, value.length() - 1);
		} else if (value.equals("true")) {
			return Boolean.TRUE;
		} else if (value.equals("false")) {
			return Boolean.FALSE;
		} else if (value.equals("null")) {
			return null;
		} else {
			// 尝试解析为数字
			try {
				if (value.contains(".")) {
					return Double.parseDouble(value);
				} else {
					return Integer.parseInt(value);
				}
			} catch (NumberFormatException e) {
				return value;
			}
		}
	}

	/**
	 * 解析JSON数组
	 */
	private List<Object> parseArray(String json) {
		List<Object> list = new ArrayList<>();
		StringBuilder currentValue = new StringBuilder();
		boolean inString = false;
		int depth = 0;

		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);

			if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
				inString = !inString;
			}

			if (!inString) {
				if (c == '{' || c == '[') {
					depth++;
					currentValue.append(c);
				} else if (c == '}' || c == ']') {
					depth--;
					currentValue.append(c);
					if (depth == 0) {
						list.add(parseValue(currentValue.toString()));
						currentValue.setLength(0);
					}
				} else if (c == ',' && depth == 0) {
					list.add(parseValue(currentValue.toString()));
					currentValue.setLength(0);
				} else {
					currentValue.append(c);
				}
			} else {
				currentValue.append(c);
			}
		}

		// 最后一个元素
		if (currentValue.length() > 0) {
			list.add(parseValue(currentValue.toString()));
		}

		return list;
	}

	private String readFile(String filePath) throws IOException {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		}
		return content.toString();
	}
}
