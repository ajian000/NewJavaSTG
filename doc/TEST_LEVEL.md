# 测试关卡说明

## 问题诊断

原level.json包含27个敌人，第1-4波使用BasicEnemy，第5波使用LaserShootingEnemy。
游戏在第一个敌人生成时卡死，可能原因：
1. 数据量过大导致性能问题
2. LaserShootingEnemy的代码有问题
3. 波次管理逻辑有bug

## 测试关卡配置

已创建简化版 `level.json`：
- 只包含1个BasicEnemy
- 在60帧生成（约1秒）
- 使用简单模式（1个波次）
- 便于排查问题

## 测试步骤

### 1. 测试单敌人场景
- 运行游戏
- 等待60帧（约1秒）
- 观察是否正常生成1个BasicEnemy
- 检查敌人是否正常移动和射击

### 2. 如果单敌人测试通过
- 逐步添加更多敌人
- 测试多个敌人场景
- 测试波次切换

### 3. 测试LaserShootingEnemy
- 添加1个LaserShootingEnemy
- 观察是否能正常生成
- 检查是否发射激光
- 确认激光渲染和碰撞检测

## 已知问题和修复

### 问题1: 数组越界
**现象：** 第4波完成后卡死
**原因：** `waveStarted[activeWaveNumber]` 当activeWaveNumber=4时访问索引5
**修复：** 将条件改为 `activeWaveNumber < WAVE_COUNT`
**状态：** ✅ 已修复

### 问题2: getWaveByFrame边界
**现象：** 第5波敌人不被识别
**原因：** `frame < WAVE_5_END_FRAME` 当frame=1300时返回0
**修复：** 已添加第5波判断
**状态：** ✅ 已修复

### 问题3: 激光碰撞无冷却
**现象：** 玩家被连续击中
**原因：** 每帧都检测碰撞，没有冷却
**修复：** 添加hitCooldown和canHit()方法
**状态：** ✅ 已修复

## 可能剩余问题

### 1. LaserShootingEnemy构造函数
检查构造函数链是否正确：
```java
public LaserShootingEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas, int pattern) {
    this(x, y, moveSpeed, gameCanvas, pattern, 90, 300);
}
```
调用另一个构造函数时，参数类型必须匹配。

### 2. 敌人update()中的逻辑
LaserShootingEnemy.update()中：
- 获取canvasWidth时可能为null或0
- 边界检查逻辑可能导致异常

### 3. 激光生成
gameCanvas.addEnemyLaser() 可能：
- gameCanvas为null
- enemyLasers未初始化

## 调试建议

在GameCanvas的update()方法开头添加：
```java
System.out.println("Frame: " + currentFrame + ", ActiveWave: " + activeWaveNumber + 
                 ", Enemies: " + enemies.size() + ", Lasers: " + enemyLasers.size());
```

在spawnEnemy()方法开头添加：
```java
System.out.println("Spawning enemy: " + type + " at frame " + data.getFrame());
```

## 回滚原关卡

如果测试关卡正常，可以使用以下命令恢复原关卡（在Git中）：
```bash
git checkout HEAD -- src/user/level.json
```

或手动从level_backup.json复制内容。
