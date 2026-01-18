package Main;

import java.awt.EventQueue;
import stg.base.Window;

public class Main {
	private static Window window;

	public static void main(String[] args) {
		System.out.println("启动 STG 游戏引擎");

		EventQueue.invokeLater(() -> {
			window = new Window();

			System.out.println("System initialized - 窗口已创建");
		});
	}

	public static Window getWindow() {
		return window;
	}
}
