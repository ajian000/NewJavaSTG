package stg.game.stage;

import java.util.ArrayList;
import java.util.List;
import stg.game.ui.GameCanvas;

/**
 * 关卡组类 - 管理多个关卡的顺序、切换和状态
 * @since 2026-01-30
 */
public class StageGroup {
    private String groupName;
    private List<Stage> stages;
    private int currentStageIndex;
    private boolean completed;
    private GameCanvas gameCanvas;

    /**
     * 构造函数
     * @param groupName 关卡组名称
     * @param gameCanvas 游戏画布引用
     */
    public StageGroup(String groupName, GameCanvas gameCanvas) {
        this.groupName = groupName;
        this.stages = new ArrayList<>();
        this.currentStageIndex = -1;
        this.completed = false;
        this.gameCanvas = gameCanvas;
    }

    /**
     * 添加关卡到关卡组
     * @param stage 关卡对象
     */
    public void addStage(Stage stage) {
        if (stage != null) {
            stages.add(stage);
        }
    }

    /**
     * 开始关卡组
     */
    public void start() {
        if (!stages.isEmpty() && currentStageIndex == -1) {
            currentStageIndex = 0;
            Stage firstStage = stages.get(currentStageIndex);
            firstStage.load();
            firstStage.start();
        }
    }

    /**
     * 进入下一关
     * @return 是否成功进入下一关
     */
    public boolean nextStage() {
        if (currentStageIndex < stages.size() - 1) {
            // 清理当前关卡
            Stage currentStage = stages.get(currentStageIndex);
            currentStage.cleanup();
            currentStage.end();

            // 进入下一关
            currentStageIndex++;
            Stage nextStage = stages.get(currentStageIndex);
            nextStage.load();
            nextStage.start();
            return true;
        } else {
            // 所有关卡已完成
            completed = true;
            return false;
        }
    }

    /**
     * 进入指定关卡
     * @param stageIndex 关卡索引
     * @return 是否成功进入指定关卡
     */
    public boolean goToStage(int stageIndex) {
        if (stageIndex >= 0 && stageIndex < stages.size()) {
            // 清理当前关卡
            if (currentStageIndex >= 0) {
                Stage currentStage = stages.get(currentStageIndex);
                currentStage.cleanup();
                currentStage.end();
            }

            // 进入指定关卡
            currentStageIndex = stageIndex;
            Stage targetStage = stages.get(currentStageIndex);
            targetStage.load();
            targetStage.start();
            return true;
        }
        return false;
    }

    /**
     * 获取当前关卡
     * @return 当前关卡对象
     */
    public Stage getCurrentStage() {
        if (currentStageIndex >= 0 && currentStageIndex < stages.size()) {
            return stages.get(currentStageIndex);
        }
        return null;
    }

    /**
     * 获取关卡组中的关卡数量
     * @return 关卡数量
     */
    public int getStageCount() {
        return stages.size();
    }

    /**
     * 获取当前关卡索引
     * @return 当前关卡索引
     */
    public int getCurrentStageIndex() {
        return currentStageIndex;
    }

    /**
     * 检查关卡组是否完成
     * @return 是否完成
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * 获取关卡组名称
     * @return 关卡组名称
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 清理关卡组资源
     */
    public void cleanup() {
        for (Stage stage : stages) {
            if (stage != null) {
                stage.cleanup();
            }
        }
        stages.clear();
        currentStageIndex = -1;
        completed = false;
    }

    /**
     * 更新关卡组状态
     */
    public void update() {
        Stage currentStage = getCurrentStage();
        if (currentStage != null) {
            currentStage.update();

            // 检查当前关卡是否完成
            if (currentStage.isCompleted()) {
                nextStage();
            }
        }
    }

    /**
     * 重置关卡组
     */
    public void reset() {
        cleanup();
        completed = false;
        currentStageIndex = -1;
    }

    /**
     * 获取游戏画布引用
     * @return 游戏画布引用
     */
    public GameCanvas getGameCanvas() {
        return gameCanvas;
    }
}
