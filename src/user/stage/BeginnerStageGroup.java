package user.stage;

import stg.game.ui.GameCanvas;

/**
 * 初学者关卡组 - 适合新手玩家的简单关卡组
 * @since 2026-01-30
 */
public class BeginnerStageGroup extends StageGroup {

    /**
     * 构造函�?     * @param gameCanvas 游戏画布引用
     */
    public BeginnerStageGroup(GameCanvas gameCanvas) {
        super("初学者关�?, "适合新手玩家的简单关卡组，循序渐进地学习游戏机制", Difficulty.EASY, gameCanvas);
        initStages();
    }

    @Override
    protected void initStages() {
        // 添加初学者关�?        addStage(new WaveBasedStage(1, "新手入门", getGameCanvas()));
        addStage(new WaveBasedStage(2, "基础训练", getGameCanvas()));
        addStage(new WaveBasedStage(3, "简单挑�?, getGameCanvas()));
    }

    @Override
    public boolean isUnlockable() {
        // 初学者关卡组始终可解�?        return true;
    }

    @Override
    public String getDisplayInfo() {
        return "初学者关�?- 简�?;
    }
}

