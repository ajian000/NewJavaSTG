package stg.game;

/**
 * 游戏循环类 - 控制游戏主循环
 */
public class GameLoop implements Runnable {
	private GameCanvas canvas; // 游戏画布
	private boolean running; // 运行标志
	private int targetFPS = 60; // 目标帧率

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
			long startTime = System.currentTimeMillis();

			canvas.update(); // 更新游戏状态

			// 计算休眠时间以维持目标帧率
			long endTime = System.currentTimeMillis();
			long sleepTime = (1000 / targetFPS) - (endTime - startTime);

			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
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
	}
}
