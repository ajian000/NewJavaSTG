package user.stage;

import java.util.ArrayList;
import java.util.List;
import user.enemy.*;
import stg.game.ui.GameCanvas;
import stg.game.stage.Stage;

/**
 * 基于波次的关卡类 - 实现波次管理和敌人生成逻辑
 * @since 2026-01-30
 */
public class WaveBasedStage extends Stage {
    // 波次配置
    private static final int WAVE_DELAY = 30;
    private static final int WAVE_1_END_FRAME = 1800;
    private static final int WAVE_2_END_FRAME = 3000;
    private static final int WAVE_3_END_FRAME = 4200;
    private static final int WAVE_4_END_FRAME = 5400;
    private static final int WAVE_5_END_FRAME = 6600;
    private static final int WAVE_6_END_FRAME = 7200;
    private static final int WAVE_COUNT = 6;
    
    // 波次管理相关字段
    private int waveCooldown = 0;
    private int activeWaveNumber = 0;
    private boolean waveStarted[];

    /**
     * 构造函�?     * @param stageId 关卡ID
     * @param stageName 关卡名称
     * @param gameCanvas 游戏画布引用
     */
    public WaveBasedStage(int stageId, String stageName, GameCanvas gameCanvas) {
        super(stageId, stageName, gameCanvas);
        this.waveStarted = new boolean[WAVE_COUNT + 1];
    }

    @Override
    protected void initStage() {
        System.out.println("Initializing wave-based stage: " + getStageName());
    }

    @Override
    public Stage nextStage() {
        // 返回下一关，这里简单返回一个新的WaveBasedStage
        return new WaveBasedStage(getStageId() + 1, "Stage " + (getStageId() + 1), getGameCanvas());
    }

    @Override
    public void load() {
        System.out.println("Loading wave-based stage: " + getStageName());
        
        // 直接设置关卡状态为LOADED
        setLoaded();
    }

    @Override
    protected void updateWaveLogic() {
        if (currentFrame % 60 == 0) {
            System.out.println("【游戏帧】帧: " + currentFrame + ", 活跃波次: " + activeWaveNumber + 
                ", 场上敌人: " + getEnemies().size() + ", 冷却: " + waveCooldown);
        }
        
        // 如果波次冷却�?减少冷却时间
        if (waveCooldown > 0) {
            waveCooldown--;
            if (waveCooldown == 0) {
                // 冷却结束�?切换到下一�?                if (activeWaveNumber < WAVE_COUNT) {
                    int oldWave = activeWaveNumber;
                    activeWaveNumber++;
                    waveStarted[activeWaveNumber] = true;
                    System.out.println("【波次切换】第" + oldWave + "波结�?-> �? + activeWaveNumber + "波开�?);
                } else {
                    System.out.println("【波次切换】所有波次已完成");
                }
            }
            return;
        }
        
        // 如果还没有活跃波�?开始第一�?        if (activeWaveNumber == 0) {
            activeWaveNumber = 1;
            waveStarted[1] = true;
            System.out.println("【波次开始】第1波开�?);
            return;
        }
        
        // 尝试生成当前波次的敌�?        boolean spawned = trySpawnWaveEnemies(activeWaveNumber);
        
        if (getEnemies().isEmpty() && isWaveComplete(activeWaveNumber) && !spawned) {
            System.out.println("【波次完成】第" + activeWaveNumber + "波完�? 开始冷�?" + WAVE_DELAY + "�?");
            waveCooldown = WAVE_DELAY;
            return;
        }
    }

    /**
     * 检查指定波次是否完�?     */
    private boolean isWaveComplete(int wave) {
        return true;
    }

    /**
     * 尝试生成指定波次的敌�?     */
    private boolean trySpawnWaveEnemies(int wave) {
        boolean spawned = false;
        System.out.println("【波次生成】波�? " + wave + ", 当前�? " + currentFrame);
        
        // 根据波次和当前帧直接生成敌人
        GameCanvas canvas = getGameCanvas();
        if (canvas != null) {
            switch (wave) {
                case 1:
                    if (currentFrame == 60) {
                        // 生成第一波敌�?                        addEnemy(new BasicEnemy(canvas.getWidth() / 2, 50, 2, canvas));
                        spawned = true;
                    }
                    break;
                case 2:
                    if (currentFrame == 120) {
                        // 生成第二波敌�?                        addEnemy(new BasicEnemy(canvas.getWidth() / 3, 50, 2, canvas));
                        addEnemy(new BasicEnemy(2 * canvas.getWidth() / 3, 50, 2, canvas));
                        spawned = true;
                    }
                    break;
                case 3:
                    if (currentFrame == 180) {
                        // 生成第三波敌�?                        addEnemy(new LaserShootingEnemy(canvas.getWidth() / 2, 50, 2, canvas, 1));
                        spawned = true;
                    }
                    break;
                case 4:
                    if (currentFrame == 240) {
                        // 生成第四波敌�?                        addEnemy(new SpiralEnemy(canvas.getWidth() / 2, 50, 2, canvas));
                        addEnemy(new BasicEnemy(canvas.getWidth() / 4, 50, 2, canvas));
                        addEnemy(new BasicEnemy(3 * canvas.getWidth() / 4, 50, 2, canvas));
                        spawned = true;
                    }
                    break;
                case 5:
                    if (currentFrame == 300) {
                        // 生成第五波敌�?                        addEnemy(new SpreadEnemy(canvas.getWidth() / 2, 50, 2, canvas));
                        addEnemy(new LaserShootingEnemy(canvas.getWidth() / 3, 50, 2, canvas, 2));
                        addEnemy(new LaserShootingEnemy(2 * canvas.getWidth() / 3, 50, 2, canvas, 2));
                        spawned = true;
                    }
                    break;
                case 6:
                    if (currentFrame == 360) {
                        // 生成第六波敌人（ boss 波）
                        addEnemy(new TrackingEnemy(canvas.getWidth() / 2, 50, 1.5f, canvas));
                        addEnemy(new BasicEnemy(canvas.getWidth() / 4, 100, 2, canvas));
                        addEnemy(new BasicEnemy(3 * canvas.getWidth() / 4, 100, 2, canvas));
                        spawned = true;
                    }
                    break;
            }
        }
        
        System.out.println("【波次生成结束】波�? " + wave + ", 本次生成: " + spawned);
        return spawned;
    }

    /**
     * 根据帧数判断波次
     */
    private int getWaveByFrame(int frame) {
        if (frame < WAVE_1_END_FRAME) return 1;
        if (frame < WAVE_2_END_FRAME) return 2;
        if (frame < WAVE_3_END_FRAME) return 3;
        if (frame < WAVE_4_END_FRAME) return 4;
        if (frame < WAVE_5_END_FRAME) return 5;
        if (frame < WAVE_6_END_FRAME) return 6;
        return 0;
    }



    @Override
    protected void checkCompletion() {
        // 检查是否所有波次都已完成且敌人已全部消�?        if (activeWaveNumber >= WAVE_COUNT && isWaveComplete(WAVE_COUNT) && getEnemies().isEmpty()) {
            end();
        }
        
        // 调用父类的检查方法，支持自定义完成条�?        super.checkCompletion();
    }

    @Override
    protected void onStageStart() {
        System.out.println("Wave-based stage started: " + getStageName());
    }

    @Override
    protected void onStageEnd() {
        System.out.println("Wave-based stage completed: " + getStageName());
    }

    @Override
    public void reset() {
        super.reset();
        currentFrame = 0;
        waveCooldown = 0;
        activeWaveNumber = 0;
        for (int i = 0; i < waveStarted.length; i++) {
            waveStarted[i] = false;
        }
    }
}

