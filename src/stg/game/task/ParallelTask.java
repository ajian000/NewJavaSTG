package stg.game.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 并行任务 - 已弃用
 * 
 * @deprecated 此类用于同时执行多个任务，已被新的抽象方法架构取代。
 *             游戏引擎已迁移到"抽象方法 + 构造函数初始化"架构。
 *             
 * <p>弃用原因：
 * <ul>
 *   <li>所有游戏实体（Enemy、Bullet、Player、Item、Laser）已迁移到新架构</li>
 *   <li>新架构性能更优，减少了内存和CPU开销</li>
 *   <li>新架构代码更清晰，便于理解和维护</li>
 *   <li>新架构开发更高效，简化了开发流程</li>
 * </ul>
 * 
 * <p>替代方案：
 * 请使用新的抽象方法架构实现游戏实体行为：
 * <ul>
 *   <li>重写 {@code initBehavior()} 方法初始化行为参数</li>
 *   <li>重写 {@code onUpdate()} 方法实现每帧更新逻辑</li>
 *   <li>重写 {@code onMove()} 方法实现移动逻辑</li>
 * </ul>
 * 
 * <p>示例：
 * <pre>{@code
 * public class MyEnemy extends Enemy {
 *     private int moveTimer = 0;
 *     private int shootTimer = 0;
 *     
 *     @Override
 *     protected void initBehavior() {
 *         vx = 2.0f;
 *         vy = 0;
 *         moveTimer = 0;
 *         shootTimer = 0;
 *     }
 *     
 *     @Override
 *     protected void onUpdate() {
 *         // 并行行为：同时执行移动和射击
 *         moveTimer++;
 *         shootTimer++;
 *         
 *         // 移动逻辑
 *         if (moveTimer >= 60) {
 *             vx = -vx;
 *             moveTimer = 0;
 *         }
 *         
 *         // 射击逻辑
 *         if (shootTimer >= 120) {
 *             shoot();
 *             shootTimer = 0;
 *         }
 *     }
 *     
 *     private void shoot() {
 *         if (!alive || gameCanvas == null) return;
 *         
 *         float bulletSpeed = -10.0f;
 *         CircularBullet bullet = new CircularBullet(x, y, 0, bulletSpeed);
 *         gameCanvas.addEnemyBullet(bullet);
 *     }
 *     
 *     @Override
 *     protected void onMove() {
 *         // 移动逻辑
 *     }
 * }
 * }</pre>
 * 
 * @see stg.game.enemy.Enemy
 * @see stg.game.bullet.Bullet
 * @see stg.game.player.Player
 * @see stg.game.item.Item
 * @see stg.game.laser.Laser
 * @since 2026-01-29
 * @deprecated 自2026-01-29起，请使用抽象方法架构
 */
@Deprecated
public class ParallelTask extends BaseTask {
    private List<Task> tasks;
    private int completedTasks;
    
    public ParallelTask() {
        super();
        this.tasks = new ArrayList<>();
        this.completedTasks = 0;
    }
    
    /**
     * 添加任务到并行执行
     * @param task 要添加的任务
     * @return 当前并行任务，支持链式调用
     */
    public ParallelTask addTask(Task task) {
        tasks.add(task);
        return this;
    }
    
    /**
     * 添加多个任务到并行执行
     * @param taskList 任务列表
     * @return 当前并行任务，支持链式调用
     */
    public ParallelTask addTasks(List<Task> taskList) {
        tasks.addAll(taskList);
        return this;
    }
    
    @Override
    public boolean update(int deltaTime) {
        if (!started) {
            start();
        }
        
        if (cancelled || completed) {
            return completed;
        }
        
        completedTasks = 0;
        boolean allCompleted = true;
        
        for (Task task : tasks) {
            if (!task.isCompleted() && !task.isCancelled()) {
                task.update(deltaTime);
                allCompleted = false;
            }
            if (task.isCompleted()) {
                completedTasks++;
            }
        }
        
        if (allCompleted || completedTasks == tasks.size()) {
            complete();
            return true;
        }
        
        return false;
    }
    
    @Override
    public void start() {
        super.start();
        completedTasks = 0;
    }
    
    @Override
    public void reset() {
        super.reset();
        completedTasks = 0;
        for (Task task : tasks) {
            task.reset();
        }
    }
    
    @Override
    public void cancel() {
        super.cancel();
        for (Task task : tasks) {
            task.cancel();
        }
    }
    
    /**
     * 获取已完成的任务数量
     * @return 已完成任务数
     */
    public int getCompletedTasks() {
        return completedTasks;
    }
    
    /**
     * 获取总任务数量
     * @return 总任务数
     */
    public int getTotalTasks() {
        return tasks.size();
    }
}
