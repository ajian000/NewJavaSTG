package stg.game.stage;

import stg.game.enemy.BasicEnemy;
import stg.game.ui.GameCanvas;

/**
 * 简单关卡示例类
 * @since 2026-01-30
 */
public class SimpleStage extends Stage {
    private int enemyCount;
    private int enemiesSpawned;

    /**
     * 构造函数
     * @param stageId 关卡ID
     * @param stageName 关卡名称
     * @param gameCanvas 游戏画布引用
     */
    public SimpleStage(int stageId, String stageName, GameCanvas gameCanvas) {
        super(stageId, stageName, gameCanvas);
        this.enemyCount = 5;
        this.enemiesSpawned = 0;
    }

    @Override
    protected void initStage() {
        System.out.println("Initializing stage: " + getStageName());
    }

    @Override
    public Stage nextStage() {
        // 返回下一关，这里简单返回一个新的SimpleStage
        return new SimpleStage(getStageId() + 1, "Stage " + (getStageId() + 1), getGameCanvas());
    }

    @Override
    public void load() {
        System.out.println("Loading stage: " + getStageName());
    }

    @Override
    protected void task() {
        while (isTaskRunning()) {
            try {
                // 每2秒生成一个敌人
                Thread.sleep(2000);

                if (enemiesSpawned < enemyCount && isActive()) {
                    spawnEnemy();
                    enemiesSpawned++;
                }

                // 检查关卡完成条件
                if (enemiesSpawned >= enemyCount && getEnemies().isEmpty()) {
                    end();
                }

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * 生成敌人
     */
    private void spawnEnemy() {
        GameCanvas canvas = getGameCanvas();
        if (canvas != null) {
            // 从屏幕上方随机位置生成敌人
            float x = (float)(Math.random() * canvas.getWidth() - canvas.getWidth() / 2);
            float y = canvas.getHeight() / 2 + 50;
            
            BasicEnemy enemy = new BasicEnemy(x, y, 2, canvas);
            addEnemy(enemy);
            System.out.println("Spawned enemy at: (" + x + ", " + y + ")");
        }
    }

    @Override
    protected void checkCompletion() {
        // 检查关卡完成条件
        if (enemiesSpawned >= enemyCount && getEnemies().isEmpty()) {
            end();
        }
    }

    @Override
    protected void onTaskStart() {
        System.out.println("Stage started: " + getStageName());
    }

    @Override
    protected void onTaskEnd() {
        System.out.println("Stage completed: " + getStageName());
    }
}
