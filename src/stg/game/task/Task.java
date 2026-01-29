package stg.game.task;

/**
 * 任务接口 - 定义所有任务的基本方法
 * 任务是游戏对象行为的基本单位，如移动、等待、攻击等
 */
public interface Task {
    /**
     * 更新任务状态
     * @param deltaTime 时间增量（帧数）
     * @return 任务是否完成
     */
    boolean update(int deltaTime);
    
    /**
     * 开始任务
     */
    void start();
    
    /**
     * 取消任务
     */
    void cancel();
    
    /**
     * 重置任务
     */
    void reset();
    
    /**
     * 检查任务是否完成
     * @return 是否完成
     */
    boolean isCompleted();
    
    /**
     * 检查任务是否已取消
     * @return 是否已取消
     */
    boolean isCancelled();
}