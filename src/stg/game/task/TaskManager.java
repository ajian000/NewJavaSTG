package stg.game.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务管理器 - 管理游戏对象的所有任务
 */
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