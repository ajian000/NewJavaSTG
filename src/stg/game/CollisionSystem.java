package stg.game;

import stg.game.bullet.Bullet;
import stg.game.enemy.Enemy;
import user.enemy.EnemyBullet;
import user.item.Item;
import user.laser.EnemyLaser;
import user.player.Player;

/**
 * 碰撞检测系统 - 处理游戏中的碰撞检测
 */
public class CollisionSystem {
    private GameWorld world;
    private Player player;
    private static final int DEFAULT_BULLET_DAMAGE = 8;
    
    /**
     * 构造函数
     */
    public CollisionSystem(GameWorld world, Player player) {
        this.world = world;
        this.player = player;
    }
    
    /**
     * 执行碰撞检测
     */
    public void checkCollisions() {
        checkPlayerBulletsVsEnemies();
        checkEnemyBulletsVsPlayer();
        checkEnemyLasersVsPlayer();
        checkPlayerVsItems();
    }
    
    /**
     * 检测玩家子弹与敌人的碰撞
     */
    private void checkPlayerBulletsVsEnemies() {
        for (Bullet bullet : world.getPlayerBullets()) {
            for (Enemy enemy : world.getEnemies()) {
                if (checkCollision(bullet, enemy)) {
                    int damage = bullet.getDamage() > 0 ? bullet.getDamage() : DEFAULT_BULLET_DAMAGE;
                    enemy.takeDamage(damage);
                    // 注意：这里不能直接移除子弹，因为我们使用的是只读列表
                    // 子弹的移除应该由GameWorld在update时处理
                    break;
                }
            }
        }
    }
    
    /**
     * 检测敌方子弹与玩家的碰撞
     */
    private void checkEnemyBulletsVsPlayer() {
        if (player == null || player.isInvincible()) return;
        
        for (EnemyBullet bullet : world.getEnemyBullets()) {
            if (checkCollision(bullet, player)) {
                player.onHit();
                // 注意：这里不能直接移除子弹，因为我们使用的是只读列表
            }
        }
    }
    
    /**
     * 检测敌方激光与玩家的碰撞
     */
    private void checkEnemyLasersVsPlayer() {
        if (player == null || player.isInvincible()) return;
        
        for (EnemyLaser laser : world.getEnemyLasers()) {
            if (laser.canHit() && laser.checkCollision(player.getX(), player.getY())) {
                player.onHit();
                laser.onHitPlayer(); // 启动冷却
            }
        }
    }
    
    /**
     * 检测玩家与物品的碰撞
     */
    private void checkPlayerVsItems() {
        if (player == null) return;
        
        for (Item item : world.getItems()) {
            if (!item.isActive()) continue;
            
            float dx = item.getX() - player.getX();
            float dy = item.getY() - player.getY();
            float distance = (float)Math.sqrt(dx * dx + dy * dy);
            
            if (distance < item.getHitboxRadius() + player.getSize()) {
                item.onCollect();
                // 注意：这里不能直接移除物品，因为我们使用的是只读列表
            }
        }
    }
    
    /**
     * 检测两个圆形对象的碰撞
     */
    private boolean checkCollision(Object obj1, Object obj2) {
        float x1, y1, size1;
        float x2, y2, size2;
        
        // 获取对象1的属性
        if (obj1 instanceof Bullet) {
            Bullet bullet = (Bullet)obj1;
            x1 = bullet.getX();
            y1 = bullet.getY();
            size1 = bullet.getHitboxRadius() > 0 ? bullet.getHitboxRadius() : bullet.getSize();
        } else if (obj1 instanceof EnemyBullet) {
            EnemyBullet bullet = (EnemyBullet)obj1;
            x1 = bullet.getX();
            y1 = bullet.getY();
            size1 = bullet.getHitboxRadius() > 0 ? bullet.getHitboxRadius() : bullet.getSize();
        } else {
            return false;
        }
        
        // 获取对象2的属性
        if (obj2 instanceof Enemy) {
            Enemy enemy = (Enemy)obj2;
            x2 = enemy.getX();
            y2 = enemy.getY();
            size2 = enemy.getSize();
        } else if (obj2 instanceof Player) {
            Player player = (Player)obj2;
            x2 = player.getX();
            y2 = player.getY();
            size2 = player.getHitboxRadius();
        } else {
            return false;
        }
        
        // 计算距离
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        
        // 判断是否碰撞
        return distance < (size1 + size2);
    }
    
    /**
     * 设置游戏世界
     */
    public void setWorld(GameWorld world) {
        this.world = world;
    }
    
    /**
     * 设置玩家
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}