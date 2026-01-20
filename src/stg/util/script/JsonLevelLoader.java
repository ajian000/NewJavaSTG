package stg.util.script;

import stg.util.LevelData;
import stg.util.LevelLoader;

/**
 * JSON关卡加载器 - 委托给SimpleJsonLoader
 * @Time 2026-01-20 将关卡加载器移动到stg.util.script包
 */
public class JsonLevelLoader implements LevelLoader {
	private SimpleJsonLoader simpleLoader = new SimpleJsonLoader();
	
	@Override
	public LevelData loadLevel(String jsonFile) {
		return simpleLoader.loadLevel(jsonFile);
	}
	
	@Override
	public Object executeScript(String script) {
		return simpleLoader.executeScript(script);
	}
}

