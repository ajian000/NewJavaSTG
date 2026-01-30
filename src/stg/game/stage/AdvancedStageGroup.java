package stg.game.stage;

import stg.game.ui.GameCanvas;

/**
 * 高级关卡组 - 适合经验丰富的玩家
 * @since 2026-01-30
 */
public class AdvancedStageGroup extends StageGroup {

    /**
     * 构造函数
     * @param gameCanvas 游戏画布引用
     */
    public AdvancedStageGroup(GameCanvas gameCanvas) {
        super("高级关卡", "适合经验丰富的玩家，包含高强度的敌人和复杂的波次", Difficulty.HARD, gameCanvas);
        initStages();
    }

    @Override
    protected void initStages() {
        // 添加高级关卡
        addStage(new WaveBasedStage(1, "高级挑战", getGameCanvas()));
        addStage(new WaveBasedStage(2, "密集波次", getGameCanvas()));
        addStage(new WaveBasedStage(3, "精英敌人", getGameCanvas()));
        addStage(new WaveBasedStage(4, "高级Boss战", getGameCanvas()));
        addStage(new WaveBasedStage(5, "终极挑战", getGameCanvas()));
    }

    @Override
    public boolean isUnlockable() {
        // 高级关卡组始终可解锁
        return true;
    }

    @Override
    public String getDisplayInfo() {
        return "高级关卡 - 困难";
    }
}
