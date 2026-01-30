package stg.game.stage;

import java.util.ArrayList;
import java.util.List;
import stg.game.enemy.Enemy;
import stg.game.ui.GameCanvas;

/**
 * 关卡类 - 管理单个关卡的逻辑
 * @since 2026-01-30
 */
public abstract class Stage {
    private String stageName;
    private int stageId;
    private boolean completed;
    private boolean started;
    private List<Enemy> enemies;
    private Thread taskThread;
    private volatile boolean taskRunning = false;
    private GameCanvas gameCanvas;

    /**
     * 构造函数
     * @param stageId 关卡ID
     * @param stageName 关卡名称
     * @param gameCanvas 游戏画布引用
     */
    public Stage(int stageId, String stageName, GameCanvas gameCanvas) {
        this.stageId = stageId;
        this.stageName = stageName;
        this.gameCanvas = gameCanvas;
        this.completed = false;
        this.started = false;
        this.enemies = new ArrayList<>();
        initStage();
    }

    /**
     * 初始化关卡
     */
    protected void initStage() {
        // 子类可以重写此方法初始化关卡
    }

    /**
     * 开始关卡
     */
    public void start() {
        if (!started) {
            started = true;
            onStageStart();
            startTask();
        }
    }

    /**
     * 结束关卡
     */
    public void end() {
        if (!completed) {
            completed = true;
            onStageEnd();
            stopTask();
        }
    }

    /**
     * 跳转到下一关
     * @return 下一关的Stage对象
     */
    public abstract Stage nextStage();

    /**
     * 加载关卡
     */
    public abstract void load();

    /**
     * 清理关卡资源
     */
    public void cleanup() {
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                enemy.setActive(false);
            }
        }
        enemies.clear();
        stopTask();
    }

    /**
     * 添加敌人到关卡
     * @param enemy 敌人对象
     */
    public void addEnemy(Enemy enemy) {
        if (enemy != null) {
            enemies.add(enemy);
            if (gameCanvas != null) {
                gameCanvas.addEnemy(enemy);
            }
        }
    }

    /**
     * 移除敌人
     * @param enemy 敌人对象
     */
    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    /**
     * 检查关卡是否完成
     * @return 是否完成
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * 检查关卡是否已开始
     * @return 是否已开始
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * 获取关卡名称
     * @return 关卡名称
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * 获取关卡ID
     * @return 关卡ID
     */
    public int getStageId() {
        return stageId;
    }

    /**
     * 获取当前关卡的敌人列表
     * @return 敌人列表（不可修改）
     */
    public List<Enemy> getEnemies() {
        return java.util.Collections.unmodifiableList(enemies);
    }

    /**
     * 获取游戏画布
     * @return 游戏画布
     */
    protected GameCanvas getGameCanvas() {
        return gameCanvas;
    }

    // ========== Task机制 ==========

    /**
     * 启动task线程
     */
    private void startTask() {
        taskRunning = true;
        taskThread = new Thread(() -> {
            try {
                executeTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Stage-Task-" + System.currentTimeMillis());
        taskThread.start();
    }

    /**
     * 停止task线程
     */
    public void stopTask() {
        taskRunning = false;
        if (taskThread != null && taskThread.isAlive()) {
            try {
                taskThread.join(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 任务执行方法 - 子类必须实现
     */
    protected abstract void executeTask();

    /**
     * 检查task是否运行
     * @return 是否运行
     */
    protected boolean isTaskRunning() {
        return taskRunning;
    }

    /**
     * 关卡开始时调用
     */
    protected void onStageStart() {
        // 子类可以重写此方法处理关卡开始逻辑
    }

    /**
     * 关卡结束时调用
     */
    protected void onStageEnd() {
        // 子类可以重写此方法处理关卡结束逻辑
    }

    /**
     * 检查关卡完成条件
     */
    protected void checkCompletion() {
        // 子类可以重写此方法检查关卡完成条件
    }

    /**
     * 重置关卡
     */
    public void reset() {
        this.completed = false;
        this.started = false;
        this.enemies.clear();
        initStage();
    }
}