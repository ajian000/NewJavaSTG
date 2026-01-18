package stg.game;

public class GameLoop implements Runnable {
	private GameCanvas canvas;
	private boolean running;
	private int targetFPS = 60;

	public GameLoop(GameCanvas canvas) {
		this.canvas = canvas;
		this.running = false;
	}

	public void start() {
		if (!running) {
			running = true;
			new Thread(this).start();
		}
	}

	@Override
	public void run() {
		while (running) {
			long startTime = System.currentTimeMillis();

			canvas.update();

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

	public void stop() {
		running = false;
	}
}
