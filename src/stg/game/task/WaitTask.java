package stg.game.task;

/**
 * 等待任务 - 已弃用
 * 
 * @deprecated 此类用于实现等待行为，已被新的抽象方法架构取代。
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
 *     private int shootCooldown = 120;
 *     private int shootTimer = 0;
 *     
 *     @Override
 *     protected void initBehavior() {
 *         vx = 2.0f;
 *         vy = 0;
 *         shootTimer = 0;
 *     }
 *     
 *     @Override
 *     protected void onUpdate() {
 *         // 等待逻辑
 *         shootTimer++;
 *         if (shootTimer >= shootCooldown) {
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
