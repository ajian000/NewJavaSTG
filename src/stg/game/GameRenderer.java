package stg.game;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import user.player.Player;
import stg.util.CoordinateSystem;

/**
 * 游戏渲染�?- 处理游戏的渲染逻辑
 */
public class GameRenderer {
    private GameWorld world;
    private Player player;
    private CoordinateSystem coordinateSystem;
    
    /**
     * 构造函�?     */
    public GameRenderer(GameWorld world, Player player, CoordinateSystem coordinateSystem) {
        this.world = world;
        this.player = player;
        this.coordinateSystem = coordinateSystem;
    }
    
    /**
     * 渲染游戏画面
     */
    public void render(Graphics2D g) {
        enableAntiAliasing(g);
        
        renderEnemies(g);
        renderEnemyBullets(g);
        renderEnemyLasers(g);
        renderItems(g);
        renderPlayerBullets(g);
        renderPlayer(g);
    }
    
    /**
     * 启用抗锯�?     */
    private void enableAntiAliasing(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    /**
     * 渲染敌人
     */
    private void renderEnemies(Graphics2D g) {
        for (stg.game.enemy.Enemy enemy : world.getEnemies()) {
            enemy.render(g);
        }
    }
    
    /**
     * 渲染敌方子弹
     */
    private void renderEnemyBullets(Graphics2D g) {
        for (user.enemy.EnemyBullet bullet : world.getEnemyBullets()) {
            bullet.render(g);
        }
    }
    
    /**
     * 渲染敌方激�?     */
    private void renderEnemyLasers(Graphics2D g) {
        for (user.laser.EnemyLaser laser : world.getEnemyLasers()) {
            laser.render(g);
        }
    }
    
    /**
     * 渲染物品
     */
    private void renderItems(Graphics2D g) {
        for (stg.game.item.Item item : world.getItems()) {
            item.render(g);
        }
    }
    
    /**
     * 渲染玩家子弹
     */
    private void renderPlayerBullets(Graphics2D g) {
        for (stg.game.bullet.Bullet bullet : world.getPlayerBullets()) {
            bullet.render(g);
        }
    }
    
    /**
     * 渲染玩家
     */
    private void renderPlayer(Graphics2D g) {
        if (player != null) {
            player.render(g);
        }
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
    public void setPlayer(user.player.Player player) {
        this.player = player;
    }
    
    /**
     * 设置坐标系统
     */
    public void setCoordinateSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }
}
