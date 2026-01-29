 package stg.game.enemy;

import java.awt.*;
import stg.game.bullet.CircularBullet;
import stg.game.task.*;
import stg.game.ui.GameCanvas;

/**
 * 基础敌人类 - Enemy的子类
 * @Time 2026-01-19 在X轴上左右来回移动,Y轴不动
 */
public class BasicEnemy extends Enemy {
	private float moveSpeed; // X轴移动速度

	/**
	 * 构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param moveSpeed X方向移动速度
	 * @param gameCanvas 游戏画布引用
	 */
	public BasicEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 20, Color.BLUE, 120, gameCanvas);
		this.moveSpeed = moveSpeed;
	}

	/**
	 * 初始化任务
	 */
	@Override
	protected void initTasks() {
		// 创建移动和射击的序列任务
		SequenceTask mainTask = new SequenceTask();
		
		// 添加移动任务
		mainTask.addTask(new MoveTask(this, 200, getY(), moveSpeed, true));
		mainTask.addTask(new WaitTask(60));
		mainTask.addTask(new MoveTask(this, -200, getY(), moveSpeed, true));
		mainTask.addTask(new WaitTask(60));
		
		// 添加射击任务
		if (gameCanvas != null) {
			ShootTask shootTask = new ShootTask(this, gameCanvas, -10, 3, (float)Math.PI/4, 
					Color.CYAN, 5, 10);
			mainTask.addTask(shootTask);
		} else {
			// 备用方案
			mainTask.addTask(new BaseTask() {
				@Override
				public boolean update(int deltaTime) {
					shoot();
					return true;
				}
			});
		}
		
		// 循环执行
		SequenceTask loopTask = new SequenceTask();
		loopTask.addTask(mainTask);
		loopTask.addTask(new BaseTask() {
			@Override
			public boolean update(int deltaTime) {
				// 重新初始化任务
				if (getTaskManager() != null) {
					getTaskManager().clearTasks();
					initTasks();
				}
				return true;
			}
		});
		
		getTaskManager().addTask(loopTask);
	}

	/**
	 * @Time 2026-01-19 重写update方法,使用任务管理器处理行为
	 */
	@Override
	public void update() {
		super.update();

		// X轴左右移动逻辑（作为备用）
		if (gameCanvas != null) {
			int canvasWidth = gameCanvas.getWidth();
			float leftBound = -canvasWidth / 2.0f + size;
			float rightBound = canvasWidth / 2.0f - size;

			if (x <= leftBound) {
				vx = Math.abs(moveSpeed);
			} else if (x >= rightBound) {
				vx = -Math.abs(moveSpeed);
			}
		}
	}

	/**
	 * 射击 - 向下方发射圆形子弹
	 * @Time 2026-01-19 Y负方向表示向下
	 */
	private void shoot() {
		if (!alive || gameCanvas == null) return;

		float bulletSpeed = -10.0f;
		CircularBullet bullet = new CircularBullet(x, y, 0, bulletSpeed);
		gameCanvas.addEnemyBullet(bullet);
	}

	/**
	 * @Time 2026-01-19 重写渲染方法,自定义外观
	 */
	@Override
	public void render(Graphics2D g) {
		int canvasWidth = gameCanvas != null ? gameCanvas.getWidth() : 548;
		int canvasHeight = gameCanvas != null ? gameCanvas.getHeight() : 921;
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;

		int[] xPoints = {
			(int)screenX,
			(int)(screenX - size),
			(int)(screenX + size)
		};
		int[] yPoints = {
			(int)(screenY - size),
			(int)(screenY + size),
			(int)(screenY + size)
		};

		g.setColor(color);
		g.fillPolygon(xPoints, yPoints, 3);

		g.setColor(Color.WHITE);
		g.drawPolygon(xPoints, yPoints, 3);

		renderHealthBar(g, screenX, screenY);
	}

	/**
	 * @Time 2026-01-19 重写死亡回调,添加简单的死亡效果
	 */
	@Override
	protected void onDeath() {
	}

	/**
	 * @Time 2026-01-19 重写toString,方便调试
	 */
	@Override
	public String toString() {
		return "BasicEnemy[pos=(" + x + ", " + y + "), hp=" + hp + "/" + maxHp + ", alive=" + alive + "]";
	}
}
