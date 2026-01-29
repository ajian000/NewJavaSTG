package stg.game.task;

/**
 * 等待任务 - 等待指定的帧数
 */
public class WaitTask extends BaseTask {
    private int waitFrames;
    private int currentFrames;
    
    /**
     * 构造函数
     * @param waitFrames 等待的帧数
     */
    public WaitTask(int waitFrames) {
        super();
        this.waitFrames = waitFrames;
        this.currentFrames = 0;
    }
    
    @Override
    public boolean update(int deltaTime) {
        if (!started) {
            start();
        }
        
        if (cancelled || completed) {
            return completed;
        }
        
        currentFrames += deltaTime;
        
        if (currentFrames >= waitFrames) {
            complete();
            return true;
        }
        
        return false;
    }
    
    @Override
    public void reset() {
        super.reset();
        currentFrames = 0;
    }
    
    /**
     * 设置等待帧数
     * @param waitFrames 等待帧数
     */
    public void setWaitFrames(int waitFrames) {
        this.waitFrames = waitFrames;
    }
    
    /**
     * 获取当前等待帧数
     * @return 当前帧数
     */
    public int getCurrentFrames() {
        return currentFrames;
    }
}