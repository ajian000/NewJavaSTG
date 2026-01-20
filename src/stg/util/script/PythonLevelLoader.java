package stg.util.script;

import stg.util.LevelData;
import stg.util.LevelLoader;
import java.io.*;

/**
 * Python关卡加载器 - 使用Python脚本加载关卡
 * @Time 2026-01-20 将关卡加载器移动到stg.util.script包
 */
public class PythonLevelLoader implements LevelLoader {
	
	public PythonLevelLoader() {
		try {
			// 尝试启动Python解释器
			ProcessBuilder pb = new ProcessBuilder("python", "--version");
			pb.redirectErrorStream(true);
			Process p = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String version = reader.readLine();
			p.waitFor();
			System.out.println("Python version: " + version);
		} catch (Exception e) {
			System.err.println("Python not available: " + e.getMessage());
		}
	}
	
	@Override
	public LevelData loadLevel(String scriptFile) {
		LevelData levelData = new LevelData();
		
		try {
			// 读取Python脚本文件
			String scriptContent = readFile(scriptFile);
			scriptContent = "import json\n" +
				"level = " + scriptContent + "\n" +
				"print(json.dumps(level))";
			
			// 执行Python脚本
			ProcessBuilder pb = new ProcessBuilder("python", "-c", scriptContent);
			pb.redirectErrorStream(true);
			Process p = pb.start();
			
			// 读取输出
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String output = "";
			String line;
			while ((line = reader.readLine()) != null) {
				output += line;
			}
			p.waitFor();
			
			// 解析JSON输出
			if (output.startsWith("{") && output.endsWith("}")) {
				JsonLevelLoader jsonLoader = new JsonLevelLoader();
				LevelData data = (LevelData)jsonLoader.executeScript(output);
				if (data != null) {
					levelData = data;
				}
			}
			
		} catch (Exception e) {
			System.err.println("Error loading Python level: " + e.getMessage());
			e.printStackTrace();
		}
		
		return levelData;
	}
	
	@Override
	public Object executeScript(String script) {
		try {
			String wrappedScript = "import json\nprint(json.dumps(" + script + "))";
			ProcessBuilder pb = new ProcessBuilder("python", "-c", wrappedScript);
			pb.redirectErrorStream(true);
			Process p = pb.start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String output = "";
			String line;
			while ((line = reader.readLine()) != null) {
				output += line;
			}
			p.waitFor();
			
			JsonLevelLoader jsonLoader = new JsonLevelLoader();
			return jsonLoader.executeScript(output);
		} catch (Exception e) {
			System.err.println("Error executing Python script: " + e.getMessage());
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

