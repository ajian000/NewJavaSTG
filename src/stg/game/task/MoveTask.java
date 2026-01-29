package stg.game.task;

import stg.game.enemy.Enemy;

/**
 * 移动任务 - 已弃用
 * 
 * @deprecated 此类用于实现敌人移动行为，已被新的抽象方法架构取代。
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
 *         if (gameCanvas != null) {
 *             int canvasWidth = gameCanvas.getWidth();
 *             float leftBound = -canvasWidth / 2.0f + size;
 *             float rightBound = canvasWidth / 2.0f - size;
 * 
 *             if (x <= leftBound) {
 *                 vx = Math.abs(moveSpeed);
 *             } else if (x >= rightBound) {
 *                 vx = -Math.abs(moveSpeed);
 *             }
 *         }
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
public class MoveTask extends BaseTask {
    private Enemy enemy;
    private float targetX;
    private float targetY;
    private float speed;
    private boolean smooth;
    
    /**
     * 构造函数
     * @param enemy 要移动的敌人
     * @param targetX 目标X坐标
     * @param targetY 目标Y坐标
     * @param speed 移动速度
     * @param smooth 是否平滑移动
     */
    public MoveTask(Enemy enemy, float targetX, float targetY, float speed, boolean smooth) {
        super();
        this.enemy = enemy;
        this.targetX = targetX;
        this.targetY = targetY;
        this.speed = speed;
        this.smooth = smooth;
    }
    
    @Override
    public boolean update(int deltaTime) {
        if (!started) {
            start();
        }
        
        if (cancelled || completed) {
            return completed;
        }
        
        if (enemy == null) {
            complete();
            return true;
        }
        
        float currentX = enemy.getX();
        float currentY = enemy.getY();
        
        // 计算距离
        float dx = targetX - currentX;
        float dy = targetY - currentY;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        
        if (distance < speed * deltaTime) {
            // 到达目标
            enemy.setPosition(targetX, targetY);
            complete();
            return true;
        }
        
        // 计算移动方向
        float vx = (dx / distance) * speed;
        float vy = (dy / distance) * speed;
        
        // 更新位置
        enemy.setPosition(currentX + vx * deltaTime, currentY + vy * deltaTime);
        
        return false;
    }
    
    @Override
    public void reset() {
        super.reset();
    }
    
    /**
     * 设置目标位置
     * @param targetX 目标X坐标
     * @param targetY 目标Y坐标
     */
    public void setTarget(float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }
    
    /**
     * 设置移动速度
     * @param speed 移动速度
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
