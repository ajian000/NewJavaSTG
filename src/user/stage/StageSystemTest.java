package user.stage;

/**
 * 独立的Stage系统测试类
 * 不依赖于现有Enemy类，只测试Stage和StageGroup的基本功能
 * @since 2026-01-30
 */
public class StageSystemTest {

    public static void main(String[] args) {
        // 创建一个简单的测试
        System.out.println("=== Stage System Test ===");
        
        // 测试Stage类的基本功能
        testStageClass();
        
        // 测试StageGroup类的基本功能
        testStageGroupClass();
        
        System.out.println("=== Test Completed ===");
    }

    /**
     * 测试Stage类的基本功能
     */
    private static void testStageClass() {
        System.out.println("\n--- Testing Stage Class ---");
        
        // 创建一个测试用的Stage子类
        TestStage stage = new TestStage(1, "Test Stage");
        
        System.out.println("1. Created stage: " + stage.getStageName());
        System.out.println("2. Stage ID: " + stage.getStageId());
        System.out.println("3. Stage active: " + stage.isActive());
        System.out.println("4. Stage completed: " + stage.isCompleted());
        System.out.println("5. Stage started: " + stage.isStarted());
        
        // 测试关卡开始
        stage.start();
        System.out.println("6. After start() - started: " + stage.isStarted());
        
        // 测试关卡结束
        stage.end();
        System.out.println("7. After end() - completed: " + stage.isCompleted());
        
        // 测试关卡重置
        stage.reset();
        System.out.println("8. After reset() - active: " + stage.isActive());
        System.out.println("9. After reset() - completed: " + stage.isCompleted());
        System.out.println("10. After reset() - started: " + stage.isStarted());
        
        // 测试nextStage方法
        Stage nextStage = stage.nextStage();
        if (nextStage != null) {
            System.out.println("11. Next stage created: " + nextStage.getStageName());
        }
        
        // 测试load方法
        stage.load();
        
        // 清理资源
        stage.cleanup();
        System.out.println("12. Stage cleanup completed");
    }

    /**
     * 测试StageGroup类的基本功能
     */
    private static void testStageGroupClass() {
        System.out.println("\n--- Testing StageGroup Class ---");
        
        // 创建一个StageGroup对象
        StageGroup stageGroup = new StageGroup("Test Group", null);
        
        System.out.println("1. Created stage group: " + stageGroup.getGroupName());
        
        // 添加关卡
        TestStage stage1 = new TestStage(1, "Stage 1");
        TestStage stage2 = new TestStage(2, "Stage 2");
        TestStage stage3 = new TestStage(3, "Stage 3");
        
        stageGroup.addStage(stage1);
        stageGroup.addStage(stage2);
        stageGroup.addStage(stage3);
        
        System.out.println("2. Added " + stageGroup.getStageCount() + " stages to group");
        
        // 测试关卡组开始
        stageGroup.start();
        
        Stage currentStage = stageGroup.getCurrentStage();
        if (currentStage != null) {
            System.out.println("3. Current stage after start: " + currentStage.getStageName());
        }
        
        // 测试进入下一关
        boolean hasNext = stageGroup.nextStage();
        System.out.println("4. Has next stage: " + hasNext);
        
        currentStage = stageGroup.getCurrentStage();
        if (currentStage != null) {
            System.out.println("5. Current stage after nextStage(): " + currentStage.getStageName());
        }
        
        // 测试进入指定关卡
        boolean goToSuccess = stageGroup.goToStage(0);
        System.out.println("6. Go to stage 0 successful: " + goToSuccess);
        
        currentStage = stageGroup.getCurrentStage();
        if (currentStage != null) {
            System.out.println("7. Current stage after goToStage(0): " + currentStage.getStageName());
        }
        
        // 测试关卡组完成
        // 连续调用nextStage直到所有关卡完成
        while (stageGroup.nextStage()) {
            currentStage = stageGroup.getCurrentStage();
            if (currentStage != null) {
                System.out.println("8. Advanced to stage: " + currentStage.getStageName());
            }
        }
        
        System.out.println("9. Stage group completed: " + stageGroup.isCompleted());
        
        // 清理资源
        stageGroup.cleanup();
        System.out.println("10. Stage group cleanup completed");
    }

    /**
     * 测试用的Stage子类
     */
    private static class TestStage extends Stage {

        public TestStage(int stageId, String stageName) {
            super(stageId, stageName, null);
        }

        @Override
        protected void initStage() {
            System.out.println("  TestStage.initStage() called");
        }

        @Override
        public Stage nextStage() {
            return new TestStage(getStageId() + 1, "Stage " + (getStageId() + 1));
        }

        @Override
        public void load() {
            System.out.println("  TestStage.load() called for: " + getStageName());
        }

        @Override
        protected void task() {
            // 空的task实现，避免线程阻塞
        }

        @Override
        protected void onTaskStart() {
            System.out.println("  TestStage.onTaskStart() called for: " + getStageName());
        }

        @Override
        protected void onTaskEnd() {
            System.out.println("  TestStage.onTaskEnd() called for: " + getStageName());
        }
    }
}
