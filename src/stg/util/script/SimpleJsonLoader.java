package stg.util.script;

import stg.util.LevelData;
import stg.util.LevelLoader;
import stg.util.EnemySpawnData;
import java.io.*;
import java.util.*;

/**
 * 简易JSON加载器 - 不依赖外部库
 * @Time 2026-01-20 将关卡加载器移动到stg.util.script包
 * @Time 2026-01-20 移除调试输出
 */
public class SimpleJsonLoader implements LevelLoader {
	@Override
	public LevelData loadLevel(String jsonFile) {
		LevelData levelData = new LevelData();

		try {
			String jsonContent = readFile(jsonFile);
			Map<String, Object> json = parseJson(jsonContent);

			if (json.containsKey("name")) {
				levelData.setName(json.get("name").toString());
			}

			if (json.containsKey("enemies")) {
				Object enemiesObj = json.get("enemies");

				if (enemiesObj instanceof List) {
					@SuppressWarnings("unchecked")
					List<Object> enemiesList = (List<Object>)enemiesObj;

					for (Object enemyObj : enemiesList) {
						if (enemyObj instanceof Map) {
							@SuppressWarnings("unchecked")
							Map<String, Object> enemy = (Map<String, Object>)enemyObj;

							String type = enemy.get("type").toString();
							float x = Float.parseFloat(enemy.get("x").toString());
							float y = Float.parseFloat(enemy.get("y").toString());
							float speed = Float.parseFloat(enemy.get("speed").toString());
							int frame = Integer.parseInt(enemy.get("frame").toString());

							EnemySpawnData spawn = new EnemySpawnData(type, x, y, speed, frame);

							for (Map.Entry<String, Object> entry : enemy.entrySet()) {
								if (!Arrays.asList("type", "x", "y", "speed", "frame").contains(entry.getKey())) {
									spawn.addParam(entry.getKey(), entry.getValue());
								}
							}

							levelData.addEnemy(spawn);
						}
					}
				}
			}

			if (json.containsKey("events")) {
				@SuppressWarnings("unchecked")
				List<Object> eventsList = (List<Object>)json.get("events");
				for (Object eventObj : eventsList) {
					if (eventObj instanceof Map) {
						@SuppressWarnings("unchecked")
						Map<String, Object> event = (Map<String, Object>)eventObj;
						levelData.addEvent(event);
					}
				}
			}

		} catch (Exception e) {
			System.err.println("Error loading JSON level: " + e.getMessage());
			e.printStackTrace();
			return levelData;
		}

		return levelData;
	}
	
	@Override
	public Object executeScript(String script) {
		try {
			return parseJson(script);
		} catch (Exception e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
			return null;
		}
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
	
	private Map<String, Object> parseJson(String json) {
		json = json.trim();
		if (json.startsWith("{")) {
			return parseObject(json);
		}
		return new HashMap<>();
	}
	
	private Map<String, Object> parseObject(String json) {
		Map<String, Object> map = new LinkedHashMap<>();
		json = json.substring(1, json.length() - 1).trim();

		if (json.isEmpty()) {
			return map;
		}

		int depth = 0;
		int start = 0;
		boolean inString = false;

		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);
			if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
				inString = !inString;
			} else if (!inString) {
				if (c == '{' || c == '[') {
					depth++;
				} else if (c == '}' || c == ']') {
					depth--;
				} else if (c == ':' && depth == 0) {
					String keyStr = json.substring(start, i).trim();
					if (keyStr.startsWith("\"") && keyStr.endsWith("\"")) {
						keyStr = keyStr.substring(1, keyStr.length() - 1);
					}
					start = i + 1;

					int valueStart = i + 1;
					int valueDepth = 0;
					boolean valueInString = false;
					int j = valueStart;
					for (; j < json.length(); j++) {
						char vc = json.charAt(j);
						if (vc == '"' && (j == valueStart || json.charAt(j - 1) != '\\')) {
							valueInString = !valueInString;
						} else if (!valueInString) {
							if (vc == '{' || vc == '[') {
								valueDepth++;
							} else if (vc == '}' || vc == ']') {
								valueDepth--;
							} else if (vc == ',' && valueDepth == 0) {
								break;
							}
						}
					}

					// 处理最后一个值（没有逗号）
					if (j == json.length()) {
						j = json.length() - 1;
					}

					int valueEnd = (j < json.length()) ? j + 1 : json.length();
					String valueStr = json.substring(valueStart, valueEnd).trim();
					if (valueStr.endsWith(",")) {
						valueStr = valueStr.substring(0, valueStr.length() - 1);
					}
					Object value = parseValue(valueStr);
					map.put(keyStr, value);
					start = j + 1;
					i = j;
				}
			}
		}

		return map;
	}
	
	private List<Object> parseArray(String json) {
		List<Object> list = new ArrayList<>();
		String original = json;
		json = json.substring(1, json.length() - 1).trim();

		if (json.isEmpty()) {
			return list;
		}

		int depth = 0;
		int start = 0;
		boolean inString = false;

		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);
			if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
				inString = !inString;
			} else if (!inString) {
				if (c == '{' || c == '[') {
					depth++;
				} else if (c == '}' || c == ']') {
					depth--;
				}

				// 当depth为0且遇到逗号或到达末尾时，提取元素
				if (depth == 0 && (c == ',' || i == json.length() - 1)) {
					int end = (c == ',') ? i : i + 1;
					String valueStr = json.substring(start, end).trim();
					if (!valueStr.isEmpty()) {
						Object value = parseValue(valueStr);
						list.add(value);
					}
					start = i + 1;
				}
			}
		}

		return list;
	}
	
	private Object parseValue(String value) {
		if (value == null) {
			return null;
		}
		value = value.trim();
		if (value.startsWith("{")) {
			return parseObject(value);
		} else if (value.startsWith("[")) {
			return parseArray(value);
		} else if (value.startsWith("\"")) {
			return value.substring(1, value.length() - 1);
		} else if (value.equalsIgnoreCase("true")) {
			return Boolean.TRUE;
		} else if (value.equalsIgnoreCase("false")) {
			return Boolean.FALSE;
		} else if (value.equalsIgnoreCase("null")) {
			return null;
		} else {
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
}
