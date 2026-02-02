package user.stage;

import stg.game.stage.Stage;

/**
 * 关卡完成条件接口
 * 用于定义关卡完成的条�? */
public interface StageCompletionCondition {
    
    /**
     * 检查关卡是否完�?     * @param stage 关卡对象
     * @return 是否完成
     */
    boolean isCompleted(Stage stage);
}

