package stg.game.task;

/**
 * 基础任务类 - 已弃用
 * 
 * @deprecated 此类是旧Task系统的核心抽象类，已被新的抽象方法架构取代。
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
    
    /**
     * 创建延迟任务
     * @param frames 延迟的帧数
     * @return 延迟任务
     */
    public static BaseTask delay(int frames) {
        return new BaseTask() {
            private int remainingFrames = frames;
            
            @Override
            public boolean update(int deltaTime) {
                remainingFrames -= deltaTime;
                if (remainingFrames <= 0) {
                    complete();
                    return true;
                }
                return false;
            }
            
            @Override
            public void reset() {
                super.reset();
                remainingFrames = frames;
            }
        };
    }
}
