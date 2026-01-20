package stg.util;

/**
 * 关卡系统使用示例
 */
public class LevelExample {
	public static void main(String[] args) {
		// 获取关卡管理器实例
		LevelManager levelManager = LevelManager.getInstance();
		
		// 检查加载器是否可用
		if (!levelManager.isLoaderAvailable()) {
			System.err.println("Level loader not available!");
			return;
		}
		
		// 方法1: 使用JavaScript从user/index.js加载关卡
		System.out.println("=== Loading level with JavaScript (src/user/index.js) ===");
		levelManager.setScriptLanguage("javascript");
		LevelData level1 = levelManager.loadLevelFromUser();
		System.out.println("Level name: " + level1.getName());
		System.out.println("Enemies count: " + level1.getEnemies().size());
		
		// 方法2: 使用Python从user/index.py加载关卡
		System.out.println("\n=== Loading level with Python (src/user/index.py) ===");
		levelManager.setScriptLanguage("python");
		LevelData level2 = levelManager.loadLevelFromUser();
		if (level2 != null) {
			System.out.println("Level name: " + level2.getName());
			System.out.println("Enemies count: " + level2.getEnemies().size());
		} else {
			System.out.println("Failed to load level with Python (Python may not be installed)");
		}
		
		// 方法3: 直接执行脚本
		System.out.println("\n=== Direct script execution ===");
		levelManager.setScriptLanguage("javascript");
		Object result = levelManager.executeScript("1 + 1");
		System.out.println("Script result: " + result);
		
		// 方法4: 动态计算敌人数量
		String dynamicScript = "3 * 5 + 2";
		Object count = levelManager.executeScript(dynamicScript);
		System.out.println("Enemy count: " + count);
	}
}
