# 波次机制说明

## 波次定义

根据JSON中的敌人`frame`属性,关卡分为4个波次:

| 波次 | Frame范围 | 敌人数量 | 说明 |
|------|-----------|---------|------|
| Wave 1 | 0-199 | 5 | 初始波次 |
| Wave 2 | 200-499 | 3 | 加速波次 |
| Wave 3 | 500-699 | 5 | 编队波次 |
| Wave 4 | 700-999 | 8 | 散开波次 |

## 敌人生成机制

### 核心规则

敌人生成必须同时满足以下**所有**条件:
1. `currentFrame >= enemy.frame` (到达指定帧数)
2. 该敌人尚未生成过
3. **该敌人所属波次是当前活跃波次**
4. **波次冷却未激活**

### 波次进度控制

1. **开始新波次**: 只在没有活跃波次或前一波次完成后开始
2. **生成敌人在当前波次**: 只有当前波次的敌人才会被考虑生成
3. **波次完成检测**: 当满足以下条件时判定波次完成:
   - 场上没有敌人
   - 该波次所有敌人都已生成
4. **冷却期**: 波次完成后等待30tick,期间不生成新敌人
5. **下一波开始**: 冷却期结束后,检查下一波敌人的frame是否到达

### 波次间隔保证

**保证波次不重叠的关键点:**

- 每个敌人都有唯一的`frame`值
- Wave 1的最大frame是160
- Wave 2的最小frame是420
- 因此,即使Wave 1的敌人在200帧内未被消灭,Wave 2的敌人也要等到420帧才会生成
- 不同波次之间有至少260帧的间隔,确保不会同时出现

## 波次冷却机制

当一波敌人被清除后:
1. 系统等待30tick的冷却期
2. 冷却期间(`waveCooldown > 0`),不检查敌人生成,直接返回
3. 冷却期结束后,检查是否有下一波敌人的frame到达
4. 由于波次之间的时间间隔足够,冷却期不会影响正常流程

## 关键实现细节

### `updateLevel()` 方法逻辑

```java
// 1. 检查冷却期
if (waveCooldown > 0) {
    waveCooldown--;
    return; // 冷却期间不生成敌人
}

// 2. 确定当前活跃波次
int activeWave = determineActiveWave();

// 3. 只生成当前波次的敌人
if (activeWave > 0) {
    trySpawnWaveEnemies(activeWave);

    // 4. 检查波次是否完成
    if (enemies.isEmpty() && isWaveComplete(activeWave)) {
        waveCooldown = waveDelay; // 开始冷却
        return;
    }
}
```

### `trySpawnWaveEnemies()` 方法

```java
// 只生成指定波次的敌人
for (EnemySpawnData enemyData : enemies) {
    int enemyWave = getWaveByFrame(enemyData.getFrame());

    // 关键: 跳过其他波次的敌人
    if (enemyWave != wave) continue;

    // 只生成当前波次的敌人
    if (currentFrame >= enemyData.getFrame()) {
        spawnEnemy(enemyData);
    }
}
```

## 测试验证

### 手动测试脚本

```bash
javac -d bin -encoding UTF-8 -sourcepath src src/stg/util/TestWaveLogic.java
java -cp bin stg.util.TestWaveLogic
```

输出示例:
```
Frame 120 -> Wave 1
Frame 160 -> Wave 1
Frame 420 -> Wave 2
Frame 440 -> Wave 2
Frame 600 -> Wave 3
Frame 720 -> Wave 4
```

### 运行时日志示例

```
Spawned Wave 1 enemy at frame 120: BasicEnemy
Spawned Wave 1 enemy at frame 140: BasicEnemy
Spawned Wave 1 enemy at frame 160: BasicEnemy
Wave 1 completed! All enemies defeated. Waiting 30 ticks for next wave...
Wave cooldown finished, can now spawn next wave
Spawned Wave 2 enemy at frame 420: BasicEnemy
...
```

## 结论

✅ **确认:不同波次的敌人不会同时出现**

✅ **确认:前置波次敌人全部击败后,等待30tick才会出现下一波**

原因是:
1. JSON中敌人的frame按波次分组,不同波次之间有足够的时间间隔
2. `trySpawnWaveEnemies()`只生成指定波次的敌人,其他波次被跳过
3. `isWaveComplete()`确保该波次所有敌人都已生成
4. 波次完成后强制进入30tick冷却期,冷却期禁止生成任何敌人
5. 只有冷却期结束后,下一波敌人才可能生成(还需要满足frame条件)

