package user.stage;

import stg.game.ui.GameCanvas;

/**
 * 专家关卡�?- 适合顶级玩家
 * @since 2026-01-30
 */
public class ExpertStageGroup extends StageGroup {

    /**
     * 构造函�?     * @param gameCanvas 游戏画布引用
     */
    public ExpertStageGroup(GameCanvas gameCanvas) {
        super("专家关卡", "适合顶级玩家的极限挑战，包含最强大的敌人和最复杂的波�?, Difficulty.LUNATIC, gameCanvas);
        initStages();
    }

    @Override
    protected void initStages() {
        // 添加专家关卡
        addStage(new WaveBasedStage(1, "专家挑战", getGameCanvas()));
        addStage(new WaveBasedStage(2, "极限波次", getGameCanvas()));
        addStage(new WaveBasedStage(3, "终极敌人", getGameCanvas()));
        addStage(new WaveBasedStage(4, "专家Boss�?, getGameCanvas()));
        addStage(new WaveBasedStage(5, "地狱挑战", getGameCanvas()));
        addStage(new WaveBasedStage(6, "最终试�?, getGameCanvas()));
    }

    @Override
    public boolean isUnlockable() {
        // 专家关卡组始终可解锁
        return true;
    }

    @Override
    public String getDisplayInfo() {
        return "专家关卡 - Lunatic";
    }
}

