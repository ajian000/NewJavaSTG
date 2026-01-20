package stg.game;

/**
 * 游戏循环类 - 控制游戏主循环
 */
public class GameLoop implements Runnable {
	private GameCanvas canvas; // 游戏画布
	private boolean running; // 运行标志
	private int targetFPS = 60; // 目标帧率
	private static GameLoop activeLoop; // 当前活跃的游戏循环

	/**
	 * 构造函数
	 * @param canvas 游戏画布
	 */
	public GameLoop(GameCanvas canvas) {
		this.canvas = canvas;
		this.running = false;
	}

	/**
	 * 启动游戏循环
	 */
	public void start() {
		if (!running) {
			// 停止之前的循环（如果有）
			if (activeLoop != null) {
				activeLoop.stop();
			}
			activeLoop = this;
			running = true;
			new Thread(this).start();
		}
	}

	/**
	 * 游戏主循环
	 */
	@Override
	public void run() {
		while (running) {
			long startTime = System.nanoTime();

			canvas.update(); // 更新游戏状态

			// 计算休眠时间以维持目标帧率（使用纳秒精度）
			long elapsedTime = System.nanoTime() - startTime;
			long targetFrameTime = 1000000000L / targetFPS; // 纳秒
			long sleepTime = targetFrameTime - elapsedTime;

			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime / 1000000L, (int)(sleepTime % 1000000L));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 停止游戏循环
	 */
	public void stop() {
		running = false;
		if (activeLoop == this) {
			activeLoop = null;
		}
	}

	/**
	 * 停止所有游戏循环
	 */
	public static void stopAll() {
		if (activeLoop != null) {
			activeLoop.stop();
		}
	}
}
