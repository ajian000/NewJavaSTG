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
    private static final int WAVE_DELAY = 30;
    private static final int WAVE_1_END_FRAME = 1800;
    private static final int WAVE_2_END_FRAME = 3000;
    private static final int WAVE_3_END_FRAME = 4200;
    private static final int WAVE_4_END_FRAME = 5400;
    private static final int WAVE_5_END_FRAME = 6600;
    private static final int WAVE_6_END_FRAME = 7200;
    private static final int WAVE_COUNT = 6;
    
    private int waveCooldown = 0;
    private int activeWaveNumber = 0;
    private boolean waveStarted[];

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
        return new WaveBasedStage(getStageId() + 1, "Stage " + (getStageId() + 1), getGameCanvas());
    }

    @Override
    public void load() {
        System.out.println("Loading wave-based stage: " + getStageName());
        setLoaded();
    }

    @Override
    protected void updateWaveLogic() {
        if (currentFrame % 60 == 0) {
            System.out.println("【游戏帧】帧: " + currentFrame + ", 活跃波次: " + activeWaveNumber + 
                ", 场上敌人: " + getEnemies().size() + ", 冷却: " + waveCooldown);
        }
        
        if (waveCooldown > 0) {
            waveCooldown--;
            if (waveCooldown == 0) {
                if (activeWaveNumber < WAVE_COUNT) {
                    int oldWave = activeWaveNumber;
                    activeWaveNumber++;
                    waveStarted[activeWaveNumber] = true;
                    System.out.println("【波次切换】第" + oldWave + "波结束 -> 第" + activeWaveNumber + "波开始");
                } else {
                    System.out.println("【波次切换】所有波次已完成");
                }
            }
            return;
        }
        
        if (activeWaveNumber == 0) {
            activeWaveNumber = 1;
            waveStarted[1] = true;
            System.out.println("【波次开始】第1波开始");
            return;
        }
        
        boolean spawned = trySpawnWaveEnemies(activeWaveNumber);
        
        if (getEnemies().isEmpty() && isWaveComplete(activeWaveNumber) && !spawned) {
            System.out.println("【波次完成】第" + activeWaveNumber + "波完成，开始冷却" + WAVE_DELAY + "帧");
            waveCooldown = WAVE_DELAY;
            return;
        }
    }

    private boolean isWaveComplete(int wave) {
        return true;
    }

    private boolean trySpawnWaveEnemies(int wave) {
        boolean spawned = false;
        System.out.println("【波次生成】波次: " + wave + ", 当前帧: " + currentFrame);
        
        GameCanvas canvas = getGameCanvas();
        if (canvas != null) {
            switch (wave) {
                case 1:
                    if (currentFrame == 60) {
                        addEnemy(new BasicEnemy(canvas.getWidth() / 2, 50, 2, canvas));
                        spawned = true;
                    }
                    break;
                case 2:
                    if (currentFrame == 120) {
                        addEnemy(new BasicEnemy(canvas.getWidth() / 3, 50, 2, canvas));
                        addEnemy(new BasicEnemy(2 * canvas.getWidth() / 3, 50, 2, canvas));
                        spawned = true;
                    }
                    break;
                case 3:
                    if (currentFrame == 180) {
                        addEnemy(new LaserShootingEnemy(canvas.getWidth() / 2, 50, 2, canvas, 1));
                        spawned = true;
                    }
                    break;
                case 4:
                    if (currentFrame == 240) {
                        addEnemy(new SpiralEnemy(canvas.getWidth() / 2, 50, 2, canvas));
                        addEnemy(new BasicEnemy(canvas.getWidth() / 4, 50, 2, canvas));
                        addEnemy(new BasicEnemy(3 * canvas.getWidth() / 4, 50, 2, canvas));
                        spawned = true;
                    }
                    break;
                case 5:
                    if (currentFrame == 300) {
                        addEnemy(new SpreadEnemy(canvas.getWidth() / 2, 50, 2, canvas));
                        addEnemy(new LaserShootingEnemy(canvas.getWidth() / 3, 50, 2, canvas, 2));
                        addEnemy(new LaserShootingEnemy(2 * canvas.getWidth() / 3, 50, 2, canvas, 2));
                        spawned = true;
                    }
                    break;
                case 6:
                    if (currentFrame == 360) {
                        addEnemy(new TrackingEnemy(canvas.getWidth() / 2, 50, 1.5f, canvas));
                        addEnemy(new BasicEnemy(canvas.getWidth() / 4, 100, 2, canvas));
                        addEnemy(new BasicEnemy(3 * canvas.getWidth() / 4, 100, 2, canvas));
                        spawned = true;
                    }
                    break;
            }
        }
        
        System.out.println("【波次生成结束】波次: " + wave + ", 本次生成: " + spawned);
        return spawned;
    }

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
        if (activeWaveNumber >= WAVE_COUNT && isWaveComplete(WAVE_COUNT) && getEnemies().isEmpty()) {
            end();
        }
        
        super.checkCompletion();
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
