package user.stage;

/**
 * 简化的Stage系统测试类
 * @since 2026-01-30
 */
public class SimpleStageTest {

    public static void main(String[] args) {
        // 创建一个简单的测试
        System.out.println("=== Stage System Test ===");
        
        // 测试Stage类的基本功能
        testStageBasic();
        
        // 测试StageGroup类的基本功能
        testStageGroupBasic();
        
        System.out.println("=== Test Completed ===");
    }

    /**
     * 测试Stage类的基本功能
     */
    private static void testStageBasic() {
        System.out.println("\n--- Testing Stage Basic Functionality ---");
        
        // 创建一个简单的Stage对象
        // 注意：这里我们使用null作为GameCanvas，因为这只是一个基本测试
        SimpleStage stage = new SimpleStage(1, "Test Stage", null);
        
        System.out.println("Created stage: " + stage.getStageName());
        System.out.println("Stage ID: " + stage.getStageId());
        System.out.println("Stage active: " + stage.isActive());
        System.out.println("Stage completed: " + stage.isCompleted());
        
        // 测试关卡开始
        stage.start();
        System.out.println("Stage started: " + stage.isStarted());
        
        // 测试关卡结束
        stage.end();
        System.out.println("Stage completed after end(): " + stage.isCompleted());
        
        // 测试关卡重置
        stage.reset();
        System.out.println("Stage reset - active: " + stage.isActive());
        System.out.println("Stage reset - completed: " + stage.isCompleted());
        System.out.println("Stage reset - started: " + stage.isStarted());
        
        // 测试nextStage方法
        Stage nextStage = stage.nextStage();
        if (nextStage != null) {
            System.out.println("Next stage created: " + nextStage.getStageName());
        }
        
        // 清理资源
        stage.cleanup();
        System.out.println("Stage cleanup completed");
    }

    /**
     * 测试StageGroup类的基本功能
     */
    private static void testStageGroupBasic() {
        System.out.println("\n--- Testing StageGroup Basic Functionality ---");
        
        // 创建一个StageGroup对象
        // 注意：这里我们使用null作为GameCanvas，因为这只是一个基本测试
        StageGroup stageGroup = new StageGroup("Test Group", null);
        
        System.out.println("Created stage group: " + stageGroup.getGroupName());
        
        // 添加关卡
        SimpleStage stage1 = new SimpleStage(1, "Stage 1", null);
        SimpleStage stage2 = new SimpleStage(2, "Stage 2", null);
        SimpleStage stage3 = new SimpleStage(3, "Stage 3", null);
        
        stageGroup.addStage(stage1);
        stageGroup.addStage(stage2);
        stageGroup.addStage(stage3);
        
        System.out.println("Added " + stageGroup.getStageCount() + " stages to group");
        
        // 测试关卡组开始
        stageGroup.start();
        
        Stage currentStage = stageGroup.getCurrentStage();
        if (currentStage != null) {
            System.out.println("Current stage after start: " + currentStage.getStageName());
        }
        
        // 测试进入下一关
        boolean hasNext = stageGroup.nextStage();
        System.out.println("Has next stage: " + hasNext);
        
        currentStage = stageGroup.getCurrentStage();
        if (currentStage != null) {
            System.out.println("Current stage after nextStage(): " + currentStage.getStageName());
        }
        
        // 测试进入指定关卡
        boolean goToSuccess = stageGroup.goToStage(0);
        System.out.println("Go to stage 0 successful: " + goToSuccess);
        
        currentStage = stageGroup.getCurrentStage();
        if (currentStage != null) {
            System.out.println("Current stage after goToStage(0): " + currentStage.getStageName());
        }
        
        // 清理资源
        stageGroup.cleanup();
        System.out.println("Stage group cleanup completed");
    }
}
