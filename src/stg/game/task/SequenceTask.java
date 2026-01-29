package stg.game.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 序列任务 - 按顺序执行多个任务
 */
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