package stg.game.task;

/**
 * 基础任务类 - 实现Task接口的通用功能
 */
public abstract class BaseTask implements Task {
    protected boolean completed;
    protected boolean cancelled;
    protected boolean started;
    
    public BaseTask() {
        this.completed = false;
        this.cancelled = false;
        this.started = false;
    }
    
    @Override
    public void start() {
        if (!started && !cancelled) {
            started = true;
            onStart();
        }
    }
    
    @Override
    public void cancel() {
        if (!completed) {
            cancelled = true;
            onCancel();
        }
    }
    
    @Override
    public void reset() {
        completed = false;
        cancelled = false;
        started = false;
    }
    
    @Override
    public boolean isCompleted() {
        return completed;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    /**
     * 任务开始时的回调
     */
    protected void onStart() {
    }
    
    /**
     * 任务取消时的回调
     */
    protected void onCancel() {
    }
    
    /**
     * 标记任务完成
     */
    protected void complete() {
        if (!completed && !cancelled) {
            completed = true;
            onComplete();
        }
    }
    
    /**
     * 任务完成时的回调
     */
    protected void onComplete() {
    }
}