package stg.util;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * 简化JSON关卡加载器 - 使用正则表达式解析
 */
public class SimpleJsonLoader implements LevelLoader {

	@Override
	public LevelData loadLevel(String scriptFile) {
		LevelData levelData = new LevelData();

		try {
			// 读取脚本文件
			System.out.println("Reading JSON file: " + scriptFile);
			String jsonContent = readFile(scriptFile);
			System.out.println("JSON content length: " + jsonContent.length());

			// 使用正则表达式提取name
			Pattern namePattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");
			Matcher nameMatcher = namePattern.matcher(jsonContent);
			if (nameMatcher.find()) {
				String name = nameMatcher.group(1);
				levelData.setName(name);
				System.out.println("Level name: " + name);
			} else {
				System.err.println("Could not find name in JSON");
			}

			// 使用正则表达式提取enemies数组
			// 需要使用更精确的匹配,避免匹配到后续的数组
			int enemiesStart = jsonContent.indexOf("\"enemies\"");
			int enemiesArrayStart = jsonContent.indexOf('[', enemiesStart);
			int enemiesArrayEnd = findMatchingBracket(jsonContent, enemiesArrayStart);

			if (enemiesArrayStart != -1 && enemiesArrayEnd != -1) {
				String enemiesArray = jsonContent.substring(enemiesArrayStart + 1, enemiesArrayEnd);
				// 手动解析JSON对象,使用栈匹配花括号
				List<String> enemyObjects = extractJsonObjects(enemiesArray);

				System.out.println("Extracted " + enemyObjects.size() + " enemy objects from JSON");

				int enemyCount = 0;
				for (String enemyJson : enemyObjects) {
					// 提取敌人属性
					String type = extractValue(enemyJson, "type");
					String xStr = extractValue(enemyJson, "x");
					String yStr = extractValue(enemyJson, "y");
					String speedStr = extractValue(enemyJson, "speed");
					String frameStr = extractValue(enemyJson, "frame");

					// 跳过无效数据
					if (type == null || xStr == null || yStr == null || speedStr == null || frameStr == null) {
						System.err.println("Skipping invalid enemy data: " + enemyJson);
						continue;
					}

					float x = Float.parseFloat(xStr);
					float y = Float.parseFloat(yStr);
					float speed = Float.parseFloat(speedStr);
					int frame = Integer.parseInt(frameStr);

					EnemySpawnData spawn = new EnemySpawnData(type, x, y, speed, frame);
					levelData.addEnemy(spawn);
					enemyCount++;
				}
				System.out.println("Successfully parsed " + enemyCount + " enemies in JSON");
			}

			// 使用正则表达式提取events数组
			int eventsStart = jsonContent.indexOf("\"events\"");
			int eventsArrayStart = jsonContent.indexOf('[', eventsStart);
			int eventsArrayEnd = findMatchingBracket(jsonContent, eventsArrayStart);

			if (eventsArrayStart != -1 && eventsArrayEnd != -1) {
				String eventsArray = jsonContent.substring(eventsArrayStart + 1, eventsArrayEnd);
				// 手动解析JSON对象,使用栈匹配花括号
				List<String> eventObjects = extractJsonObjects(eventsArray);

				int eventCount = 0;
				for (String eventJson : eventObjects) {
					Map<String, Object> eventData = new HashMap<>();

					// 提取事件属性
					String type = extractValue(eventJson, "type");
					String frameStr = extractValue(eventJson, "frame");
					String content = extractValue(eventJson, "content");

					// 跳过无效数据
					if (type == null || frameStr == null) {
						continue;
					}

					int frame = Integer.parseInt(frameStr);

					eventData.put("type", type);
					eventData.put("frame", frame);
					eventData.put("content", content);

					levelData.addEvent(eventData);
					eventCount++;
				}
				System.out.println("Found " + eventCount + " events in JSON");
			}

		} catch (IOException e) {
			System.err.println("File read error: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Parse error: " + e.getMessage());
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
	 * 提取JSON值
	 */
	private String extractValue(String json, String key) {
		// 首先尝试字符串值
		Pattern strPattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]+)\"");
		Matcher strMatcher = strPattern.matcher(json);
		if (strMatcher.find()) {
			return strMatcher.group(1);
		}

		// 尝试数值类型(支持负数和小数)
		Pattern numPattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?[\\d.]+)");
		Matcher numMatcher = numPattern.matcher(json);
		if (numMatcher.find()) {
			return numMatcher.group(1);
		}

		// 尝试布尔值
		Pattern boolPattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(true|false)");
		Matcher boolMatcher = boolPattern.matcher(json);
		if (boolMatcher.find()) {
			return boolMatcher.group(1);
		}

		return null;
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

	/**
	 * 从JSON数组字符串中提取所有JSON对象
	 * 使用栈匹配花括号
	 */
	private List<String> extractJsonObjects(String arrayContent) {
		List<String> objects = new ArrayList<>();
		int braceLevel = 0;
		int start = -1;

		for (int i = 0; i < arrayContent.length(); i++) {
			char c = arrayContent.charAt(i);

			if (c == '{') {
				if (braceLevel == 0) {
					start = i;
				}
				braceLevel++;
			} else if (c == '}') {
				braceLevel--;
				if (braceLevel == 0 && start != -1) {
					objects.add(arrayContent.substring(start, i + 1));
					start = -1;
				}
			}
		}

		return objects;
	}

	/**
	 * 查找匹配的方括号
	 */
	private int findMatchingBracket(String content, int start) {
		int level = 1;
		for (int i = start + 1; i < content.length(); i++) {
			char c = content.charAt(i);
			if (c == '[') {
				level++;
			} else if (c == ']') {
				level--;
				if (level == 0) {
					return i;
				}
			}
		}
		return -1;
	}
}
