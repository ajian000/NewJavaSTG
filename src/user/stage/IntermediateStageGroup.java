package user.stage;

import stg.game.ui.GameCanvas;

/**
 * 中级关卡�?- 适合有一定经验的玩家
 * @since 2026-01-30
 */
public class IntermediateStageGroup extends StageGroup {

    /**
     * 构造函�?     * @param gameCanvas 游戏画布引用
     */
    public IntermediateStageGroup(GameCanvas gameCanvas) {
        super("中级关卡", "适合有一定经验的玩家，包含更多挑战性的敌人和波�?, Difficulty.NORMAL, gameCanvas);
        initStages();
    }

    @Override
    protected void initStages() {
        // 添加中级关卡
        addStage(new WaveBasedStage(1, "进阶挑战", getGameCanvas()));
        addStage(new WaveBasedStage(2, "战术训练", getGameCanvas()));
        addStage(new WaveBasedStage(3, "波次挑战", getGameCanvas()));
        addStage(new WaveBasedStage(4, "中级Boss�?, getGameCanvas()));
    }

    @Override
    public boolean isUnlockable() {
        // 中级关卡组始终可解锁
        return true;
    }

    @Override
    public String getDisplayInfo() {
        return "中级关卡 - 普�?;
    }
}

