package Main;

import java.awt.EventQueue;
import stg.base.Window;

/**
 * STG游戏引擎主类
 */
public class Main {
	private static Window window; // 窗口对象

	/**
	 * 程序入口
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		System.out.println("启动 STG 游戏引擎");

		// 在事件分发线程中创建窗口
		EventQueue.invokeLater(() -> {
			window = new Window();

			System.out.println("System initialized - 窗口已创建");
		});
	}

	/**
	 * 获取窗口对象
	 * @return 窗口对象
	 */
	public static Window getWindow() {
		return window;
	}
}
