package stg.game.task;

import stg.game.enemy.Enemy;

/**
 * 移动任务 - 让游戏对象移动到指定位置
 */
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