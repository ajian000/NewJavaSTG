package stg.game.task;

import java.awt.*;
import stg.game.enemy.Enemy;
import stg.game.enemy.EnemyBullet;
import stg.game.ui.GameCanvas;

/**
 * 射击任务 - 已弃用
 * 
 * @deprecated 此类用于实现敌人射击行为，已被新的抽象方法架构取代。
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
 *         // 射击逻辑
 *         if (frame % 120 == 0) {
 *             shoot();
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
public class ShootTask extends BaseTask {
    private Enemy enemy;
    private GameCanvas gameCanvas;
    private float bulletSpeed;
    private int bulletCount;
    private float angleSpread;
    private Color bulletColor;
    private float bulletSize;
    private int bulletDamage;
    
    /**
     * 构造函数
     * @param enemy 敌人对象
     * @param gameCanvas 游戏画布
     * @param bulletSpeed 子弹速度
     * @param bulletCount 子弹数量
     * @param angleSpread 角度扩散
     * @param bulletColor 子弹颜色
     * @param bulletSize 子弹大小
     * @param bulletDamage 子弹伤害
     */
    public ShootTask(Enemy enemy, GameCanvas gameCanvas, float bulletSpeed, int bulletCount, 
                    float angleSpread, Color bulletColor, float bulletSize, int bulletDamage) {
        super();
        this.enemy = enemy;
        this.gameCanvas = gameCanvas;
        this.bulletSpeed = bulletSpeed;
        this.bulletCount = bulletCount;
        this.angleSpread = angleSpread;
        this.bulletColor = bulletColor;
        this.bulletSize = bulletSize;
        this.bulletDamage = bulletDamage;
    }
    
    /**
     * 简化构造函数
     */
    public ShootTask(Enemy enemy, GameCanvas gameCanvas, float bulletSpeed) {
        this(enemy, gameCanvas, bulletSpeed, 1, 0, Color.CYAN, 5, 10);
    }
    
    @Override
    public boolean update(int deltaTime) {
        if (!started) {
            start();
        }
        
        if (cancelled || completed) {
            return completed;
        }
        
        if (enemy == null || gameCanvas == null) {
            complete();
            return true;
        }
        
        // 计算角度
        float angleStep = bulletCount > 1 ? angleSpread / (bulletCount - 1) : 0;
        float startAngle = -angleSpread / 2;
        
        // 发射子弹
        for (int i = 0; i < bulletCount; i++) {
            float angle = startAngle + i * angleStep;
            float vx = (float)Math.sin(angle) * bulletSpeed;
            float vy = (float)Math.cos(angle) * bulletSpeed;
            
            // 创建EnemyBullet
            EnemyBullet bullet = new EnemyBullet(
                enemy.getX(), enemy.getY(),
                vx, vy,
                bulletSize, bulletColor,
                bulletDamage
            );
            
            bullet.setGameCanvas(gameCanvas);
            gameCanvas.addEnemyBullet(bullet);
        }
        
        complete();
        return true;
    }
    
    @Override
    public void reset() {
        super.reset();
    }

    /**
     * 设置子弹参数
     */
    public void setBulletParams(float bulletSpeed, int bulletCount, float angleSpread) {
        this.bulletSpeed = bulletSpeed;
        this.bulletCount = bulletCount;
        this.angleSpread = angleSpread;
    }

    /**
     * 设置子弹外观
     */
    public void setBulletAppearance(Color bulletColor, float bulletSize, int bulletDamage) {
        this.bulletColor = bulletColor;
        this.bulletSize = bulletSize;
        this.bulletDamage = bulletDamage;
    }
}
