package stg.game.task;

/**
 * 任务接口 - 已弃用
 * 
 * @deprecated 此接口是旧Task系统的核心接口，已被新的抽象方法架构取代。
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
