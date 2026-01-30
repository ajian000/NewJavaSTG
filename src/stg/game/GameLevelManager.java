package stg.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import stg.game.enemy.*;
import stg.game.enemy.Enemy;
import stg.game.ui.GameCanvas;
import stg.util.EnemySpawnData;
import stg.util.LevelData;

/**
 * 游戏关卡管理器 - 管理游戏关卡逻辑和敌人生成
 */
public class GameLevelManager {
    private LevelData currentLevel;
    private int currentFrame = 0;
    private int waveCooldown = 0;
    private List<EnemySpawnData> spawnedEnemies;
    private int activeWaveNumber = 0;
    private boolean waveStarted[];
    
    // 波次配置
    private static final int WAVE_DELAY = 30;
    private static final int WAVE_1_END_FRAME = 1800;
    private static final int WAVE_2_END_FRAME = 3000;
    private static final int WAVE_3_END_FRAME = 4200;
    private static final int WAVE_4_END_FRAME = 5400;
    private static final int WAVE_5_END_FRAME = 6600;
    private static final int WAVE_6_END_FRAME = 7200;
    private static final int WAVE_COUNT = 6;
    
	private GameWorld world;
	private stg.game.ui.GameCanvas canvas;
	
	/**
	 * 构造函数
	 */
	public GameLevelManager(GameWorld world, stg.game.ui.GameCanvas canvas) {
		this.world = world;
		this.canvas = canvas;
		this.spawnedEnemies = new ArrayList<>();
		this.waveStarted = new boolean[WAVE_COUNT + 1];
		loadLevel();
	}
    
    /**
     * 加载关卡
     */
    private void loadLevel() {
        stg.util.LevelManager manager = stg.util.LevelManager.getInstance();
        currentLevel = manager.loadLevelFromUser();
        if (currentLevel != null) {
            System.out.println("Loaded level: " + currentLevel.getName());
            System.out.println("Enemy count: " + currentLevel.getEnemies().size());
            for (EnemySpawnData data : currentLevel.getEnemies()) {
                System.out.println("  Enemy: " + data.getType() + " at frame " + data.getFrame());
            }
        }
    }
    
    /**
     * 更新关卡逻辑
     */
    public void update() {
        if (currentLevel == null) {
            System.out.println("【关卡更新】当前关卡为null");
            return;
        }
        
        currentFrame++;
        if (currentFrame % 60 == 0) {
            System.out.println("【游戏帧】帧: " + currentFrame + ", 活跃波次: " + activeWaveNumber + 
                ", 场上敌人: " + world.getEnemies().size() + ", 冷却: " + waveCooldown);
        }
        
        // 如果波次冷却中,减少冷却时间
        if (waveCooldown > 0) {
            waveCooldown--;
            if (waveCooldown == 0) {
                // 冷却结束后,切换到下一波
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
        
        // 如果还没有活跃波次,开始第一波
        if (activeWaveNumber == 0) {
            activeWaveNumber = 1;
            waveStarted[1] = true;
            System.out.println("【波次开始】第1波开始");
            return;
        }
        
        // 尝试生成当前波次的敌人
        boolean spawned = trySpawnWaveEnemies(activeWaveNumber);
        
        if (world.getEnemies().isEmpty() && isWaveComplete(activeWaveNumber) && !spawned) {
            System.out.println("【波次完成】第" + activeWaveNumber + "波完成, 开始冷却(" + WAVE_DELAY + "帧)");
            waveCooldown = WAVE_DELAY;
            return;
        }
        
        // 触发关卡事件
        triggerLevelEvents();
    }
    
    /**
     * 触发关卡事件
     */
    private void triggerLevelEvents() {
        for (Map<String, Object> event : currentLevel.getEvents()) {
            Object frameObj = event.get("frame");
            if (frameObj instanceof Number) {
                int eventFrame = ((Number)frameObj).intValue();
                // 只在帧数刚到达时触发事件
                if (currentFrame == eventFrame) {
                    String type = (String)event.get("type");
                    if ("message".equals(type)) {
                        System.out.println("Event: " + event.get("content"));
                    }
                }
            }
        }
    }
    
    /**
     * 检查指定波次是否完成
     */
    private boolean isWaveComplete(int wave) {
        for (EnemySpawnData enemyData : currentLevel.getEnemies()) {
            int enemyWave = getWaveByFrame(enemyData.getFrame());
            if (enemyWave == wave && !spawnedEnemies.contains(enemyData)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 尝试生成指定波次的敌人
     */
    private boolean trySpawnWaveEnemies(int wave) {
        boolean spawned = false;
        System.out.println("【波次生成】波次: " + wave + ", 当前帧: " + currentFrame + 
            ", 关卡敌人总数: " + currentLevel.getEnemies().size());
        
        for (EnemySpawnData enemyData : currentLevel.getEnemies()) {
            int enemyWave = getWaveByFrame(enemyData.getFrame());
            
            if (enemyWave != wave) {
                continue;
            }
            
            System.out.println("【波次" + wave + "】发现敌人: " + enemyData.getType() + 
                ", 目标帧: " + enemyData.getFrame() + ", 当前帧: " + currentFrame);
            
            if (spawnedEnemies.contains(enemyData)) {
                System.out.println("【波次" + wave + "】敌人已生成过, 跳过");
                continue;
            }
            
            if (currentFrame >= enemyData.getFrame()) {
                System.out.println("【波次" + wave + "】到达生成帧, 生成敌人");
                spawnEnemy(enemyData);
                spawnedEnemies.add(enemyData);
                spawned = true;
            } else {
                System.out.println("【波次" + wave + "】未到达生成帧, 等待 (差: " + 
                    (enemyData.getFrame() - currentFrame) + "帧)");
            }
        }
        
        System.out.println("【波次生成结束】波次: " + wave + ", 本次生成: " + spawned);
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
    
    /**
     * 生成敌人
     */
    private void spawnEnemy(EnemySpawnData data) {
        if (canvas.getWidth() <= 0 || canvas.getHeight() <= 0) {
            System.out.println("【敌人生成失败】画布未准备好");
            return;
        }
        
        Enemy enemy = createEnemy(data);
        if (enemy != null) {
            world.addEnemy(enemy);
            System.out.println("【敌人生成成功】当前敌人总数: " + world.getEnemies().size() + 
                ", 敌人HP: " + enemy.getHp() + "/" + enemy.getMaxHp() + ", 存活: " + enemy.isAlive());
        }
    }
    
    /**
     * 根据数据创建敌人
     */
    private Enemy createEnemy(EnemySpawnData data) {
        String type = data.getType();
        Enemy enemy = null;
        
        System.out.println("【敌人生成】类型: " + type + ", 位置: (" + data.getX() + ", " + data.getY() + 
            "), 速度: " + data.getSpeed() + ", 帧: " + data.getFrame());
        
        switch (type) {
            case "BasicEnemy":
                enemy = new BasicEnemy(data.getX(), data.getY(), data.getSpeed(), canvas);
                break;
            case "LaserShootingEnemy":
                int pattern = 2;
                if (data.getParams().containsKey("pattern")) {
                    pattern = (Integer)data.getParams().get("pattern");
                }
                System.out.println("【LaserShootingEnemy】攻击模式: " + pattern);
                enemy = new LaserShootingEnemy(data.getX(), data.getY(), data.getSpeed(), canvas, pattern);
                break;
            case "SpiralEnemy":
                enemy = new SpiralEnemy(data.getX(), data.getY(), data.getSpeed(), canvas);
                break;
            case "SpreadEnemy":
                enemy = new SpreadEnemy(data.getX(), data.getY(), data.getSpeed(), canvas);
                break;
            case "TrackingEnemy":
                enemy = new TrackingEnemy(data.getX(), data.getY(), data.getSpeed(), canvas);
                break;
            case "RapidFireEnemy":
                enemy = new RapidFireEnemy(data.getX(), data.getY(), data.getSpeed(), canvas);
                break;
            case "OrbitEnemy":
                float orbitRadius = 100.0f;
                if (data.getParams().containsKey("orbitRadius")) {
                    orbitRadius = ((Number)data.getParams().get("orbitRadius")).floatValue();
                }
                enemy = new OrbitEnemy(data.getX(), data.getY(), orbitRadius, data.getSpeed(), canvas);
                break;
            default:
                System.out.println("【警告】未知敌人类型: " + type + ", 使用BasicEnemy");
                enemy = new BasicEnemy(data.getX(), data.getY(), data.getSpeed(), canvas);
        }
        
        return enemy;
    }
    
    /**
     * 重置关卡状态
     */
    public void reset() {
        currentFrame = 0;
        waveCooldown = 0;
        activeWaveNumber = 0;
        for (int i = 0; i < waveStarted.length; i++) {
            waveStarted[i] = false;
        }
        spawnedEnemies.clear();
    }
    
    /**
     * 获取当前帧数
     */
    public int getCurrentFrame() {
        return currentFrame;
    }
    
    /**
     * 设置游戏世界
     */
    public void setWorld(GameWorld world) {
        this.world = world;
    }
    
	/**
	 * 设置游戏画布
	 */
	public void setCanvas(stg.game.ui.GameCanvas canvas) {
		this.canvas = canvas;
	}
}
