package stg.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.*;

/**
 * JavaScript关卡加载器 - 使用Nashorn引擎执行JS脚本
 */
public class JavaScriptLevelLoader implements LevelLoader {
	private ScriptEngine engine;
	
	public JavaScriptLevelLoader() {
		ScriptEngineManager manager = new ScriptEngineManager();
		this.engine = manager.getEngineByName("nashorn");

		if (engine == null) {
			System.err.println("JavaScript engine not available. Please use Java 8 or higher.");
			System.err.println("Available engines: " + manager.getEngineFactories().toString());
		} else {
			System.out.println("JavaScript engine loaded successfully: " + engine.getFactory().getEngineName());
		}
	}
	
	@Override
	public LevelData loadLevel(String scriptFile) {
		if (engine == null) {
			throw new RuntimeException("JavaScript engine not available");
		}

		LevelData levelData = new LevelData();

		try {
			// 读取脚本文件
			System.out.println("Reading script file: " + scriptFile);
			String scriptContent = readFile(scriptFile);
			System.out.println("Script content length: " + scriptContent.length());

			// 执行脚本,获取关卡数据
			Object result = engine.eval(scriptContent);
			System.out.println("Script execution result: " + result);

			// 解析结果
			if (result instanceof Map) {
				Map<String, Object> data = (Map<String, Object>) result;

				// 解析元数据
				if (data.containsKey("name")) {
					levelData.setName(data.get("name").toString());
					System.out.println("Level name: " + data.get("name"));
				}

				// 解析敌人数据
				if (data.containsKey("enemies")) {
					Object enemiesObj = data.get("enemies");
					if (enemiesObj instanceof List) {
						List<Map<String, Object>> enemiesList = (List<Map<String, Object>>) enemiesObj;
						System.out.println("Found " + enemiesList.size() + " enemies in script");
						for (Map<String, Object> enemyData : enemiesList) {
							String type = enemyData.get("type").toString();
							float x = Float.parseFloat(enemyData.get("x").toString());
							float y = Float.parseFloat(enemyData.get("y").toString());
							float speed = Float.parseFloat(enemyData.get("speed").toString());
							int frame = Integer.parseInt(enemyData.get("frame").toString());

							EnemySpawnData spawn = new EnemySpawnData(type, x, y, speed, frame);

							// 添加额外参数
							for (Map.Entry<String, Object> entry : enemyData.entrySet()) {
								if (!Arrays.asList("type", "x", "y", "speed", "frame").contains(entry.getKey())) {
									spawn.addParam(entry.getKey(), entry.getValue());
								}
							}

							levelData.addEnemy(spawn);
						}
					}
				}

				// 解析事件
				if (data.containsKey("events")) {
					Object eventsObj = data.get("events");
					if (eventsObj instanceof List) {
						List<Map<String, Object>> eventsList = (List<Map<String, Object>>) eventsObj;
						System.out.println("Found " + eventsList.size() + " events in script");
						for (Map<String, Object> eventData : eventsList) {
							levelData.addEvent(eventData);
						}
					}
				}
			} else {
				System.err.println("Script result is not a Map, type: " + result.getClass().getName());
			}

		} catch (ScriptException e) {
			System.err.println("Script execution error: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("File read error: " + e.getMessage());
			e.printStackTrace();
		}

		return levelData;
	}
	
	@Override
	public Object executeScript(String script) {
		if (engine == null) {
			throw new RuntimeException("JavaScript engine not available");
		}
		
		try {
			return engine.eval(script);
		} catch (ScriptException e) {
			System.err.println("Script execution error: " + e.getMessage());
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
}
