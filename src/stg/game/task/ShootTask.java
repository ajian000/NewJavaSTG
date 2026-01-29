package stg.game.task;

import java.awt.*;
import stg.game.enemy.Enemy;
import stg.game.enemy.EnemyBullet;
import stg.game.ui.GameCanvas;

/**
 * 射击任务 - 让敌人发射子弹
 */
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