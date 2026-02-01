package stg.game;

import stg.game.bullet.IBullet;
import stg.game.enemy.IEnemy;
import user.enemy.EnemyBullet;
import stg.game.item.IItem;
import user.laser.EnemyLaser;
import stg.game.player.IPlayer;
import stg.util.CoordinateSystem;

/**
 * 游戏世界接口 - 定义游戏世界的行为和属性
 */
public interface IGameWorld {
    /**
     * 添加敌人
     */
    void addEnemy(IEnemy enemy);
    
    /**
     * 添加玩家子弹
     */
    void addPlayerBullet(IBullet bullet);
    
    /**
     * 添加敌方子弹
     */
    void addEnemyBullet(EnemyBullet bullet);
    
    /**
     * 添加物品
     */
    void addItem(IItem item);
    
    /**
     * 添加敌方激光
     */
    void addEnemyLaser(EnemyLaser laser);
    
    /**
     * 获取玩家
     */
    IPlayer getPlayer();
    
    /**
     * 获取敌人列表
     */
    java.util.List<IEnemy> getEnemies();
    
    /**
     * 获取敌方子弹列表
     */
    java.util.List<EnemyBullet> getEnemyBullets();
    
    /**
     * 获取玩家子弹列表
     */
    java.util.List<IBullet> getPlayerBullets();
    
    /**
     * 获取物品列表
     */
    java.util.List<IItem> getItems();
    
    /**
     * 获取敌方激光列表
     */
    java.util.List<EnemyLaser> getEnemyLasers();
    
    /**
     * 获取画布宽度
     */
    int getWidth();
    
    /**
     * 获取画布高度
     */
    int getHeight();
    
    /**
     * 获取坐标系统
     */
    CoordinateSystem getCoordinateSystem();
    
    /**
     * 更新游戏世界
     */
    void update();
    
    /**
     * 清除所有实体
     */
    void clear();
}