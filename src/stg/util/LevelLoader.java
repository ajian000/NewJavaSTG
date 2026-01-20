package stg.util;

/**
 * 关卡加载器接口 - 支持JavaScript和Python脚本
 */
public interface LevelLoader {
	/**
	 * 加载关卡
	 * @param scriptFile 脚本文件路径
	 * @return 关卡数据
	 */
	LevelData loadLevel(String scriptFile);
	
	/**
	 * 执行关卡脚本
	 * @param script 脚本内容
	 * @return 执行结果
	 */
	Object executeScript(String script);
}
