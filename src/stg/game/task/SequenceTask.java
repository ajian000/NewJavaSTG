package stg.game.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 序列任务 - 已弃用
 * 
 * @deprecated 此类用于按顺序执行多个任务，已被新的抽象方法架构取代。
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
 *     private int phase = 0;
 *     private int phaseTimer = 0;
 *     
 *     @Override
 *     protected void initBehavior() {
 *         vx = 2.0f;
 *         vy = 0;
 *         phase = 0;
 *         phaseTimer = 0;
 *     }
 *     
 *     @Override
 *     protected void onUpdate() {
 *         phaseTimer++;
 *         
 *         // 序列行为：移动 -> 等待 -> 射击
 *         switch (phase) {
 *             case 0:
 *                 if (phaseTimer >= 60) {
 *                     phase = 1;
 *                     phaseTimer = 0;
 *                 }
 *                 break;
 *             case 1:
 *                 if (phaseTimer >= 30) {
 *                     shoot();
 *                     phase = 2;
 *                     phaseTimer = 0;
 *                 }
 *                 break;
 *             case 2:
 *                 if (phaseTimer >= 60) {
 *                     phase = 0;
 *                     phaseTimer = 0;
 *                 }
 *                 break;
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
public class SequenceTask extends BaseTask {
    private List<Task> tasks;
    private int currentTaskIndex;
    
    public SequenceTask() {
        super();
        this.tasks = new ArrayList<>();
        this.currentTaskIndex = 0;
    }
    
    /**
     * 添加任务到序列
     * @param task 要添加的任务
     * @return 当前序列任务，支持链式调用
     */
    public SequenceTask addTask(Task task) {
        tasks.add(task);
        return this;
    }
    
    /**
     * 添加多个任务到序列
     * @param taskList 任务列表
     * @return 当前序列任务，支持链式调用
     */
    public SequenceTask addTasks(List<Task> taskList) {
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
        
        if (currentTaskIndex >= tasks.size()) {
            complete();
            return true;
        }
        
        Task currentTask = tasks.get(currentTaskIndex);
        
        if (!currentTask.isCompleted() && !currentTask.isCancelled()) {
            currentTask.update(deltaTime);
        }
        
        if (currentTask.isCompleted()) {
            currentTaskIndex++;
        } else if (currentTask.isCancelled()) {
            // 如果当前任务被取消，整个序列也被取消
            cancel();
            return true;
        }
        
        return false;
    }
    
    @Override
    public void start() {
        super.start();
        currentTaskIndex = 0;
    }
    
    @Override
    public void reset() {
        super.reset();
        currentTaskIndex = 0;
        for (Task task : tasks) {
            task.reset();
        }
    }
    
    @Override
    public void cancel() {
        super.cancel();
        if (currentTaskIndex < tasks.size()) {
            Task currentTask = tasks.get(currentTaskIndex);
            currentTask.cancel();
        }
    }
}
