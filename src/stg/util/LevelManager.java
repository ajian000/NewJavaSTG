package stg.util;

import java.util.HashMap;
import java.util.Map;
import stg.util.script.JsonLevelLoader;
import stg.util.script.SimpleJsonLoader;

/**
 * 关卡管理器 - 管理关卡加载和执行
 * 从user目录读取level.json
 * @Time 2026-01-23 移除JS/Py脚本支持，仅保留JSON加载器
 */
public class LevelManager {
	private static LevelManager instance;
	private Map<String, LevelData> loadedLevels;
	private LevelLoader currentLoader;
	
	private LevelManager() {
		this.loadedLevels = new HashMap<>();
		// 默认使用JSON加载器
		this.currentLoader = new JsonLevelLoader();
	}
	
	/**
	 * 获取单例实例
	 */
	public static LevelManager getInstance() {
		if (instance == null) {
			instance = new LevelManager();
		}
		return instance;
	}
	
	/**
	 * 设置脚本语言
	 * @param language 目前仅支持 "json"
	 */
	public void setScriptLanguage(String language) {
		switch (language.toLowerCase()) {
			case "json":
				currentLoader = new SimpleJsonLoader();
				System.out.println("Using JSON level loader (src/user/level.json)");
				break;
			default:
				System.err.println("Unknown script language: " + language + ", using JSON");
				currentLoader = new SimpleJsonLoader();
		}
	}
	
	/**
	 * 加载关卡
	 * @param levelId 关卡ID
	 * @param scriptFile 脚本文件路径
	 * @return 关卡数据
	 */
	public LevelData loadLevel(String levelId, String scriptFile) {
		LevelData data = currentLoader.loadLevel(scriptFile);
		loadedLevels.put(levelId, data);
		System.out.println("Loaded level: " + levelId);
		return data;
	}
	
	/**
	 * 从user目录加载关卡
	 * 根据当前脚本语言自动选择index.js或index.py
	 * @return 关卡数据
	 */
	public LevelData loadLevelFromUser() {
		// 根据当前加载器确定脚本文件路径
		String scriptFile = determineScriptFile();
		return loadLevel("main", scriptFile);
	}
	
	/**
	 * 确定要使用的脚本文件
	 * @return 脚本文件路径
	 */
	private String determineScriptFile() {
		return "src/user/level.json";
	}
	
	/**
	 * 获取已加载的关卡
	 * @param levelId 关卡ID
	 * @return 关卡数据,如果不存在返回null
	 */
	public LevelData getLevel(String levelId) {
		return loadedLevels.get(levelId);
	}
	
	/**
	 * 执行脚本
	 * @param script 脚本内容
	 * @return 执行结果
	 */
	public Object executeScript(String script) {
		return currentLoader.executeScript(script);
	}
	
	/**
	 * 检查加载器是否可用
	 */
	public boolean isLoaderAvailable() {
		return currentLoader != null;
	}
}
