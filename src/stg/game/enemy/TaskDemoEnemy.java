package stg.game.enemy;

import java.awt.*;
import stg.game.task.*;
import stg.game.ui.GameCanvas;

/**
 * Task机制演示敌人 - 完全基于Task系统实现所有行为
 * @Time 2026-01-28 使用MoveTask、ShootTask、WaitTask、SequenceTask、ParallelTask等实现复杂行为
 */
public class TaskDemoEnemy extends Enemy {
	private float moveSpeed;
	private String pattern; // 行为模式：sine, circle, spiral, figure8

	public TaskDemoEnemy(float x, float y, float moveSpeed, String pattern, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 25, Color.MAGENTA, 200, gameCanvas);
		this.moveSpeed = moveSpeed;
		this.pattern = pattern;
	}

	@Override
	protected void initTasks() {
		switch (pattern) {
			case "sine":
				initSinePattern();
				break;
			case "circle":
				initCirclePattern();
				break;
			case "spiral":
				initSpiralPattern();
				break;
			case "figure8":
				initFigure8Pattern();
				break;
			case "complex":
				initComplexPattern();
				break;
			default:
				initSinePattern();
		}
	}

	/**
	 * 正弦波模式 - 左右移动并发射扇形弹
	 */
	private void initSinePattern() {
		// 创建循环任务
		SequenceTask mainTask = new SequenceTask();
		
		// 移动到左侧
		mainTask.addTask(new MoveTask(this, -150, getY(), moveSpeed * 1.5f, true));
		mainTask.addTask(new WaitTask(30));
		
		// 移动到右侧
		mainTask.addTask(new MoveTask(this, 150, getY(), moveSpeed * 1.5f, true));
		mainTask.addTask(new WaitTask(30));
		
		// 移动到中心
		mainTask.addTask(new MoveTask(this, 0, getY(), moveSpeed * 1.5f, true));
		
		// 并行执行：继续移动任务 + 射击任务
		ParallelTask parallelTask = new ParallelTask();
		parallelTask.addTask(mainTask);
		parallelTask.addTask(createLoopingShootTask(60, 5, 0.4f, Color.MAGENTA));
		
		getTaskManager().addTask(parallelTask);
	}

	/**
	 * 圆形移动模式 - 绕圈移动并向四周发射子弹
	 */
	private void initCirclePattern() {
		// 创建自定义移动任务：圆形轨迹
		Task circleMoveTask = new BaseTask() {
			private int frame = 0;
			private float centerX = getX();
			private float centerY = getY();
			private float radius = 80;
			
			@Override
			public boolean update(int deltaTime) {
				frame += deltaTime;
				float angle = (float)(frame * 0.05);
				float newX = centerX + (float)(Math.cos(angle) * radius);
				float newY = centerY + (float)(Math.sin(angle) * radius);
				setPosition(newX, newY);
				return false;
			}
		};
		
		// 创建射击任务：每隔90帧发射一圈子弹
		Task circleShootTask = new BaseTask() {
			private int frame = 0;
			
			@Override
			public boolean update(int deltaTime) {
				frame += deltaTime;
				if (frame >= 90) {
					shootCircle(12, Color.CYAN);
					frame = 0;
				}
				return false;
			}
		};
		
		// 并行执行
		ParallelTask parallelTask = new ParallelTask();
		parallelTask.addTask(circleMoveTask);
		parallelTask.addTask(circleShootTask);
		
		getTaskManager().addTask(parallelTask);
	}

	/**
	 * 螺旋模式 - 从外向内螺旋移动并发射螺旋弹
	 */
	private void initSpiralPattern() {
		Task spiralMoveTask = new BaseTask() {
			private int frame = 0;
			private float radius = 150;
			
			@Override
			public boolean update(int deltaTime) {
				frame += deltaTime;
				float angle = (float)(frame * 0.03);
				radius -= 0.1f * deltaTime;
				
				if (radius < 20) {
					radius = 150; // 重置半径
				}
				
				float newX = (float)(Math.cos(angle) * radius);
				float newY = (float)(Math.sin(angle) * radius) + 100;
				setPosition(newX, newY);
				return false;
			}
		};
		
		// 螺旋弹幕
		Task spiralShootTask = new BaseTask() {
			private int frame = 0;
			
			@Override
			public boolean update(int deltaTime) {
				frame += deltaTime;
				float angle = (float)(frame * 0.08);
				shootBullet(angle, 7.0f, Color.MAGENTA);
				return false;
			}
		};
		
		ParallelTask parallelTask = new ParallelTask();
		parallelTask.addTask(spiralMoveTask);
		parallelTask.addTask(spiralShootTask);
		
		getTaskManager().addTask(parallelTask);
	}

	/**
	 * 8字形模式 - 走8字并发射跟踪弹
	 */
	private void initFigure8Pattern() {
		Task figure8MoveTask = new BaseTask() {
			private int frame = 0;
			
			@Override
			public boolean update(int deltaTime) {
				frame += deltaTime;
				float t = (float)(frame * 0.03);
				float scale = 120;
				float newX = (float)(Math.sin(t) * scale);
				float newY = (float)(Math.sin(t * 2) * scale * 0.5f) + 100;
				setPosition(newX, newY);
				return false;
			}
		};
		
		// 跟踪弹幕
		Task trackingShootTask = new BaseTask() {
			private int frame = 0;
			
			@Override
			public boolean update(int deltaTime) {
				frame += deltaTime;
				if (frame >= 45) {
					shootTrackingBullet(Color.ORANGE);
					frame = 0;
				}
				return false;
			}
		};
		
		ParallelTask parallelTask = new ParallelTask();
		parallelTask.addTask(figure8MoveTask);
		parallelTask.addTask(trackingShootTask);
		
		getTaskManager().addTask(parallelTask);
	}

	/**
	 * 复杂模式 - 结合多种任务类型
	 */
	private void initComplexPattern() {
		// 第一阶段：入场
		SequenceTask phase1 = new SequenceTask();
		phase1.addTask(new MoveTask(this, 0, 150, 3.0f, true));
		phase1.addTask(new WaitTask(30));
		
		// 第二阶段：序列攻击
		SequenceTask phase2 = new SequenceTask();
		
		// 扇形弹
		phase2.addTask(new BaseTask() {
			@Override
			public boolean update(int deltaTime) {
				shootSpread(7, 0.5f, Color.RED);
				return true;
			}
		});
		phase2.addTask(new WaitTask(60));
		
		// 圆形弹
		phase2.addTask(new BaseTask() {
			@Override
			public boolean update(int deltaTime) {
				shootCircle(16, Color.MAGENTA);
				return true;
			}
		});
		phase2.addTask(new WaitTask(60));
		
		// 螺旋弹
		Task spiralPhase = new BaseTask() {
			private int frame = 0;
			@Override
			public boolean update(int deltaTime) {
				frame += deltaTime;
				float angle = (float)(frame * 0.1);
				shootBullet(angle, 8.0f, Color.CYAN);
				return frame >= 180; // 3秒后完成
			}
		};
		phase2.addTask(spiralPhase);
		
		// 第三阶段：组合移动和射击
		ParallelTask phase3 = new ParallelTask();
		
		// 左右移动
		Task moveTask = new BaseTask() {
			private int frame = 0;
			@Override
			public boolean update(int deltaTime) {
				frame += deltaTime;
				float x = (float)(Math.sin(frame * 0.05) * 150);
				setPosition(x, 150);
				return false;
			}
		};
		
		// 连续发射
		Task shootTask = createLoopingShootTask(30, 3, 0.3f, Color.ORANGE);
		
		phase3.addTask(moveTask);
		phase3.addTask(shootTask);
		
		// 组合所有阶段
		SequenceTask allPhases = new SequenceTask();
		allPhases.addTask(phase1);
		allPhases.addTask(phase2);
		allPhases.addTask(phase3);
		
		getTaskManager().addTask(allPhases);
	}

	/**
	 * 创建循环射击任务
	 */
	private Task createLoopingShootTask(int interval, int bulletCount, float angleSpread, Color color) {
		return new BaseTask() {
			private int frame = 0;
			
			@Override
			public boolean update(int deltaTime) {
				frame += deltaTime;
				if (frame >= interval) {
					shootSpread(bulletCount, angleSpread, color);
					frame = 0;
				}
				return false;
			}
		};
	}

	/**
	 * 发射扇形弹
	 */
	private void shootSpread(int bulletCount, float angleSpread, Color color) {
		if (gameCanvas == null) return;
		
		for (int i = 0; i < bulletCount; i++) {
			float angleOffset = (i - bulletCount / 2.0f) * angleSpread;
			shootBullet(angleOffset, 7.0f, color);
		}
	}

	/**
	 * 发射圆形弹
	 */
	private void shootCircle(int bulletCount, Color color) {
		if (gameCanvas == null) return;
		
		float angleStep = (float)(Math.PI * 2 / bulletCount);
		for (int i = 0; i < bulletCount; i++) {
			float angle = i * angleStep;
			shootBullet(angle, 6.0f, color);
		}
	}

	/**
	 * 发射跟踪弹
	 */
	private void shootTrackingBullet(Color color) {
		if (gameCanvas == null || gameCanvas.getPlayer() == null) return;
		
		float playerX = gameCanvas.getPlayer().getX();
		float playerY = gameCanvas.getPlayer().getY();
		
		float dx = playerX - getX();
		float dy = playerY - getY();
		float angle = (float)Math.atan2(dx, -dy);
		
		shootBullet(angle, 8.0f, color);
	}

	/**
	 * 发射子弹
	 */
	private void shootBullet(float angle, float speed, Color color) {
		if (gameCanvas == null) return;
		
		float vx = (float)Math.sin(angle) * speed;
		float vy = (float)Math.cos(angle) * speed;
		
		stg.game.enemy.EnemyBullet bullet = new stg.game.enemy.EnemyBullet(
			getX(), getY(), vx, vy,
			6, color, 10
		);
		bullet.setGameCanvas(gameCanvas);
		gameCanvas.addEnemyBullet(bullet);
	}

	@Override
	public void render(Graphics2D g) {
		int canvasWidth = gameCanvas != null ? gameCanvas.getWidth() : 548;
		int canvasHeight = gameCanvas != null ? gameCanvas.getHeight() : 921;
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;

		// 绘制六边形
		int sides = 6;
		int[] xPoints = new int[sides];
		int[] yPoints = new int[sides];
		
		for (int i = 0; i < sides; i++) {
			float angle = (float)(i * 2 * Math.PI / sides - Math.PI / 2);
			xPoints[i] = (int)(screenX + Math.cos(angle) * size * 1.3f);
			yPoints[i] = (int)(screenY + Math.sin(angle) * size * 1.3f);
		}

		g.setColor(color);
		g.fillPolygon(xPoints, yPoints, sides);
		
		g.setColor(Color.WHITE);
		g.drawPolygon(xPoints, yPoints, sides);
		
		// 绘制中心点
		g.setColor(Color.WHITE);
		g.fillOval((int)(screenX - 3), (int)(screenY - 3), 6, 6);

		renderHealthBar(g, screenX, screenY);
	}

	@Override
	public String toString() {
		return "TaskDemoEnemy[pattern=" + pattern + ", pos=(" + x + ", " + y + "), hp=" + hp + "/" + maxHp + "]";
	}
}
