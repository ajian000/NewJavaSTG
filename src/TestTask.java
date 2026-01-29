import stg.game.task.BaseTask;
import stg.game.task.TaskManager;

/**
 * Task系统测试类 - 已弃用
 * 
 * @deprecated 此类用于演示旧的Task系统功能，已被新的抽象方法架构取代。
 *             自v2.1版本起，游戏引擎已迁移到"抽象方法 + 构造函数初始化"架构。
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
public class TestTask {
    public static void main(String[] args) {
        System.out.println("Testing TaskManager...");
        
        // 创建任务管理器
        TaskManager taskManager = new TaskManager();
        System.out.println("TaskManager created successfully.");
        
        // 创建一个简单的任务
        BaseTask task = new BaseTask() {
            @Override
            public boolean update(int deltaTime) {
                System.out.println("Task updated with deltaTime: " + deltaTime);
                return true; // 任务完成
            }
        };
        
        // 添加任务到任务管理器
        taskManager.addTask(task);
        System.out.println("Task added to TaskManager.");
        
        // 更新任务管理器
        taskManager.update(1);
        System.out.println("TaskManager updated.");
        
        // 测试delay方法
        BaseTask delayTask = BaseTask.delay(60); // 延迟60帧
        taskManager.addTask(delayTask);
        System.out.println("Delay task added to TaskManager.");
        
        System.out.println("Test completed successfully!");
    }
}
