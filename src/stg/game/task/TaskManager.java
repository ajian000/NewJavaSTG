package stg.game.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务管理器 - 已弃用
 * 
 * @deprecated 此类是旧Task系统的核心管理类，已被新的抽象方法架构取代。
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
 *     @Override
 *     protected void initBehavior() {
 *         vx = 2.0f;
 *         vy = 0;
 *     }
 *     
 *     @Override
 *     protected void onUpdate() {
 *         if (frame % 120 == 0) {
 *             shoot();
 *         }
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
public class TaskManager {
    private List<Task> tasks;
    private List<Task> pendingAdd;
    private List<Task> pendingRemove;
    
    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.pendingAdd = new ArrayList<>();
        this.pendingRemove = new ArrayList<>();
    }
    
    /**
     * 添加任务
     * @param task 要添加的任务
     */
    public void addTask(Task task) {
        if (task != null) {
            pendingAdd.add(task);
        }
    }
    
    /**
     * 移除任务
     * @param task 要移除的任务
     */
    public void removeTask(Task task) {
        if (task != null) {
            pendingRemove.add(task);
        }
    }
    
    /**
     * 清除所有任务
     */
    public void clearTasks() {
        pendingRemove.addAll(tasks);
    }
    
    /**
     * 更新所有任务
     * @param deltaTime 时间增量（帧数）
     */
    public void update(int deltaTime) {
        // 处理待添加的任务
        if (!pendingAdd.isEmpty()) {
            for (Task task : pendingAdd) {
                tasks.add(task);
                task.start();
            }
            pendingAdd.clear();
        }
        
        // 处理待移除的任务
        if (!pendingRemove.isEmpty()) {
            for (Task task : pendingRemove) {
                tasks.remove(task);
                task.cancel();
            }
            pendingRemove.clear();
        }
        
        // 更新所有任务
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tasks) {
            boolean completed = task.update(deltaTime);
            if (completed || task.isCancelled()) {
                completedTasks.add(task);
            }
        }
        
        // 移除已完成或已取消的任务
        for (Task task : completedTasks) {
            tasks.remove(task);
        }
    }
    
    /**
     * 获取当前任务数量
     * @return 任务数量
     */
    public int getTaskCount() {
        return tasks.size();
    }
    
    /**
     * 检查是否有任务在执行
     * @return 是否有任务
     */
    public boolean hasTasks() {
        return !tasks.isEmpty() || !pendingAdd.isEmpty();
    }
}
