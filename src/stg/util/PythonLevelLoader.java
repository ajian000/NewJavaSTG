package stg.util;

import java.io.*;

/**
 * Python关卡加载器 - 通过Process调用Python脚本
 */
public class PythonLevelLoader implements LevelLoader {
	private String pythonPath; // Python可执行文件路径
	
	public PythonLevelLoader() {
		// 尝试查找Python可执行文件
		pythonPath = findPythonPath();
	}
	
	public PythonLevelLoader(String pythonPath) {
		this.pythonPath = pythonPath;
	}
	
	@Override
	public LevelData loadLevel(String scriptFile) {
		if (pythonPath == null) {
			throw new RuntimeException("Python not found. Please install Python or set the path.");
		}
		
		LevelData levelData = new LevelData();
		
		try {
			// 执行Python脚本
			ProcessBuilder pb = new ProcessBuilder(pythonPath, scriptFile);
			pb.redirectErrorStream(true);
			Process process = pb.start();
			
			// 读取输出
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder output = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}
			
			// 等待脚本完成
			int exitCode = process.waitFor();
			if (exitCode != 0) {
				System.err.println("Python script execution failed with exit code: " + exitCode);
				System.err.println("Output: " + output.toString());
				return levelData;
			}
			
			// 解析JSON输出
			String jsonOutput = output.toString().trim();
			levelData = parseLevelJson(jsonOutput);
			
		} catch (IOException e) {
			System.err.println("Process execution error: " + e.getMessage());
		} catch (InterruptedException e) {
			System.err.println("Process interrupted: " + e.getMessage());
			Thread.currentThread().interrupt();
		}
		
		return levelData;
	}
	
	@Override
	public Object executeScript(String script) {
		if (pythonPath == null) {
			throw new RuntimeException("Python not found. Please install Python or set the path.");
		}
		
		try {
			// 创建临时脚本文件
			File tempFile = File.createTempFile("temp_script", ".py");
			tempFile.deleteOnExit();
			
			try (FileWriter writer = new FileWriter(tempFile)) {
				writer.write(script);
			}
			
			// 执行Python脚本
			ProcessBuilder pb = new ProcessBuilder(pythonPath, tempFile.getAbsolutePath());
			pb.redirectErrorStream(true);
			Process process = pb.start();
			
			// 读取输出
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder output = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}
			
			// 等待脚本完成
			int exitCode = process.waitFor();
			if (exitCode != 0) {
				System.err.println("Python script execution failed with exit code: " + exitCode);
				return null;
			}
			
			return output.toString();
			
		} catch (IOException e) {
			System.err.println("Process execution error: " + e.getMessage());
			return null;
		} catch (InterruptedException e) {
			System.err.println("Process interrupted: " + e.getMessage());
			Thread.currentThread().interrupt();
			return null;
		}
	}
	
	private LevelData parseLevelJson(String json) {
		// 简单JSON解析(生产环境建议使用Gson或Jackson)
		LevelData levelData = new LevelData();
		
		// 这里简化处理,实际应该使用JSON解析库
		// 假设Python脚本输出标准格式: {"name": "...", "enemies": [...]}
		
		// 提取name
		if (json.contains("\"name\"")) {
			int start = json.indexOf("\"name\":") + 7;
			int end = json.indexOf("\"", start + 1);
			if (end > start) {
				levelData.setName(json.substring(start + 1, end));
			}
		}
		
		return levelData;
	}
	
	private String findPythonPath() {
		String[] possiblePaths = {"python", "python3", "py"};
		
		for (String path : possiblePaths) {
			try {
				ProcessBuilder pb = new ProcessBuilder(path, "--version");
				Process process = pb.start();
				int exitCode = process.waitFor();
				if (exitCode == 0) {
					System.out.println("Found Python: " + path);
					return path;
				}
			} catch (Exception e) {
				// 继续尝试下一个
			}
		}
		
		return null;
	}
}
