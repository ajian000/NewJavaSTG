package stg.game.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 并行任务 - 同时执行多个任务
 */
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