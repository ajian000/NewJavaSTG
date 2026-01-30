package stg.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import stg.game.bullet.Bullet;
import stg.game.enemy.Enemy;
import stg.game.enemy.EnemyBullet;
import stg.game.item.Item;
import stg.game.laser.EnemyLaser;

/**
 * 游戏世界类 - 管理游戏中的所有实体
 */
public class GameWorld {
    private List<Enemy> enemies = new ArrayList<>();
    private List<Bullet> playerBullets = new ArrayList<>();
    private List<EnemyBullet> enemyBullets = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<EnemyLaser> enemyLasers = new ArrayList<>();
    
    /**
     * 添加敌人
     */
    public void addEnemy(Enemy enemy) {
        if (enemy != null) {
            enemies.add(enemy);
        }
    }
    
    /**
     * 添加玩家子弹
     */
    public void addPlayerBullet(Bullet bullet) {
        if (bullet != null) {
            playerBullets.add(bullet);
        }
    }
    
    /**
     * 添加敌方子弹
     */
    public void addEnemyBullet(EnemyBullet bullet) {
        if (bullet != null) {
            enemyBullets.add(bullet);
        }
    }
    
    /**
     * 添加物品
     */
    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }
    
    /**
     * 添加敌方激光
     */
    public void addEnemyLaser(EnemyLaser laser) {
        if (laser != null) {
            enemyLasers.add(laser);
        }
    }
    
    /**
     * 更新所有实体
     */
    public void update(int canvasWidth, int canvasHeight) {
        updateEnemies(canvasWidth, canvasHeight);
        updateBullets(canvasWidth, canvasHeight);
        updateItems(canvasWidth, canvasHeight);
        updateLasers(canvasWidth, canvasHeight);
    }
    
    /**
     * 更新敌人
     */
    private void updateEnemies(int canvasWidth, int canvasHeight) {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update();
            
            if (!enemy.isAlive() || enemy.isOutOfBounds(canvasWidth, canvasHeight)) {
                iterator.remove();
            }
        }
    }
    
    /**
     * 更新子弹
     */
    private void updateBullets(int canvasWidth, int canvasHeight) {
        // 更新玩家子弹
        Iterator<Bullet> playerBulletIterator = playerBullets.iterator();
        while (playerBulletIterator.hasNext()) {
            Bullet bullet = playerBulletIterator.next();
            bullet.update();
            if (bullet.isOutOfBounds(canvasWidth, canvasHeight)) {
                playerBulletIterator.remove();
            }
        }
        
        // 更新敌方子弹
        Iterator<EnemyBullet> enemyBulletIterator = enemyBullets.iterator();
        while (enemyBulletIterator.hasNext()) {
            EnemyBullet bullet = enemyBulletIterator.next();
            bullet.update();
            if (bullet.isOutOfBounds(canvasWidth, canvasHeight)) {
                enemyBulletIterator.remove();
            }
        }
    }
    
    /**
     * 更新物品
     */
    private void updateItems(int canvasWidth, int canvasHeight) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            item.update();
            if (!item.isActive() || item.isOutOfBounds(canvasWidth, canvasHeight)) {
                iterator.remove();
            }
        }
    }
    
    /**
     * 更新激光
     */
    private void updateLasers(int canvasWidth, int canvasHeight) {
        Iterator<EnemyLaser> iterator = enemyLasers.iterator();
        while (iterator.hasNext()) {
            EnemyLaser laser = iterator.next();
            laser.update();
            if (laser.isOutOfBounds(canvasWidth, canvasHeight)) {
                iterator.remove();
            }
        }
    }
    
    /**
     * 获取敌人列表（只读）
     */
    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }
    
    /**
     * 获取玩家子弹列表（只读）
     */
    public List<Bullet> getPlayerBullets() {
        return Collections.unmodifiableList(playerBullets);
    }
    
    /**
     * 获取敌方子弹列表（只读）
     */
    public List<EnemyBullet> getEnemyBullets() {
        return Collections.unmodifiableList(enemyBullets);
    }
    
    /**
     * 获取物品列表（只读）
     */
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    /**
     * 获取敌方激光列表（只读）
     */
    public List<EnemyLaser> getEnemyLasers() {
        return Collections.unmodifiableList(enemyLasers);
    }
    
    /**
     * 清除所有实体
     */
    public void clear() {
        enemies.clear();
        playerBullets.clear();
        enemyBullets.clear();
        items.clear();
        enemyLasers.clear();
    }
    
    /**
     * 移除指定的敌方激光
     */
    public void removeEnemyLasers(List<EnemyLaser> lasersToRemove) {
        enemyLasers.removeAll(lasersToRemove);
    }
    
    /**
     * 清除所有物品
     */
    public void clearItems() {
        items.clear();
    }
    
    /**
     * 清除所有敌方激光
     */
    public void clearEnemyLasers() {
        enemyLasers.clear();
    }
    
    /**
     * 移除指定的物品
     */
    public void removeItem(Item item) {
        items.remove(item);
    }
}