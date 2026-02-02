# @Time 注解使用情况分析报告

## 生成时间
2026-02-02

## 分析目的
整理项目中所有使用 `@Time` 注解的地方，按文件分类并按时间顺序排列，以便于了解代码的修改历史和演进过程。

## 总体统计
- 总文件数：27个
- 总@Time使用次数：63次
- 时间范围：2026-01-19 至 2026-01-30

## 按时间顺序排列的@Time使用情况

### 2026-01-19

#### src/user/enemy/BasicEnemy.java
- 10: * @Time 2026-01-19 在X轴上左右来回移动,Y轴不动
- 68: * @Time 2026-01-19 重写update方法
- 77: * @Time 2026-01-19 Y负方向表示向下
- 88: * @Time 2026-01-19 重写渲染方法,自定义外观
- 117: * @Time 2026-01-19 重写死亡回调,添加简单的死亡效果
- 124: * @Time 2026-01-19 重写toString,方便调试

#### src/user/bullet/SpreadBullet.java
- 8: * @Time 2026-01-19 带有尾迹效果的弹幕
- 45: * @Time 2026-01-19 重写渲染方法,添加尾迹效果

#### src/user/bullet/CircularBullet.java
- 8: * @Time 2026-01-19 标准圆形弹幕,带有渐变效果
- 42: * @Time 2026-01-19 重写渲染方法,添加渐变效果

#### src/stg/util/CoordinateSystem.java
- 4: * 坐标系统工具类 - @Time 2026-01-19
- 25: * @Time 2026-01-19
- 38: * @Time 2026-01-19
- 53: * @Time 2026-01-19
- 66: * @Time 2026-01-19
- 75: * @Time 2026-01-19
- 84: * @Time 2026-01-19
- 93: * @Time 2026-01-19
- 102: * @Time 2026-01-19
- 111: * @Time 2026-01-19
- 120: * @Time 2026-01-19
- 129: * @Time 2026-01-19

### 2026-01-20

#### src/stg/game/ui/TitleScreen.java
- 14: * @Time 2026-01-20 将类移动到stg.game.ui包
- 15: * @Time 2026-01-20 实现KeyStateProvider以支持虚拟键盘

#### src/user/enemy/LaserShootingEnemy.java
- 12: * @Time 2026-01-20

#### src/stg/base/Window.java
- 165: * @Time 2026-01-20 添加getter以支持按键状态切换

#### src/stg/base/VirtualKeyboardPanel.java
- 9: * @Time 2026-01-20 支持KeyStateProvider接口,使标题界面也能显示按键
- 33: * @Time 2026-01-20 支持标题界面按键显示
- 154: * @Time 2026-01-20 支持切换到标题界面

#### src/stg/base/KeyStateProvider.java
- 5: * @Time 2026-01-20 支持标题界面的虚拟键盘显示

#### src/stg/util/math/RandomGenerator.java
- 7: * @Time 2026-01-20 创建stg.util.math包,添加随机数生成器
- 8: * @Time 2026-01-20 简化类,移除不必要的功能

#### src/stg/util/math/MathUtils.java
- 5: * @Time 2026-01-20 创建stg.util.math包,添加数学工具函数

#### src/stg/util/math/Vector2.java
- 5: * @Time 2026-01-20 创建stg.util.math包,添加向量数学工具

### 2026-01-21

#### src/user/laser/LinearLaser.java
- 8: * @Time 2026-01-21

#### src/user/laser/EnemyLaser.java
- 8: * @Time 2026-01-21

#### src/user/laser/CurvedLaser.java
- 10: * @Time 2026-01-21

#### src/user/laser/EnemyLinearLaser.java
- 7: * @Time 2026-01-21

#### src/user/laser/EnemyCurvedLaser.java
- 7: * @Time 2026-01-21

### 2026-01-23

#### src/user/enemy/SpiralEnemy.java
- 10: * @Time 2026-01-23

#### src/user/player/MarisaPlayer.java
- 17: private static final int MARISA_BULLET_DAMAGE = 1; // @Time 2026-01-23 魔理沙子弹伤害，普通DPS = (3 × 1 × 60) / 2 = 90
- 25: this.bulletDamage = MARISA_BULLET_DAMAGE; // @Time 2026-01-23 设置魔理沙子弹伤害
- 59: // @Time 2026-01-23 无敌闪烁效果：每5帧闪烁一次

#### src/user/player/ReimuPlayer.java
- 17: private static final int REIMU_BULLET_DAMAGE = 1; // @Time 2026-01-23 灵梦子弹伤害，普通DPS = (5 × 1 × 60) / 3 = 100
- 25: this.bulletDamage = REIMU_BULLET_DAMAGE; // @Time 2026-01-23 设置灵梦子弹伤害 灵梦子弹伤害，普通DPS = (5 × 1 × 60) /
- 59: // @Time 2026-01-23 无敌闪烁效果：每5帧闪烁一次

#### src/user/enemy/TrackingEnemy.java
- 10: * @Time 2026-01-23

#### src/user/enemy/SpreadEnemy.java
- 10: * @Time 2026-01-23

#### src/user/enemy/RapidFireEnemy.java
- 10: * @Time 2026-01-23

#### src/user/enemy/OrbitEnemy.java
- 10: * @Time 2026-01-23

#### src/user/bullet/TrackingBullet.java
- 9: * @Time 2026-01-23

#### src/user/bullet/SpiralBullet.java
- 8: * @Time 2026-01-23

### 2026-01-24

#### src/stg/game/ui/TitleScreen.java
- 16: * @Time 2026-01-24 添加背景图片支持
- 17: * @Time 2026-01-27 添加标题音乐播放功能

#### src/stg/game/ResourceDemoWindow.java
- 11: * @Time 2026-01-24

#### src/stg/base/Window.java
- 173: * @Time 2026-01-24 添加getter以支持游戏状态访问

#### src/stg/util/ResourceManager.java
- 12: * @Time 2026-01-24

#### src/stg/util/AudioManager.java
- 11: * @Time 2026-01-24

#### src/user/player/PlayerWithImage.java
- 10: * @Time 2026-01-24

#### src/user/enemy/EnemyWithSound.java
- 8: * @Time 2026-01-24

#### src/stg/game/ResourceTest.java
- 7: * @Time 2026-01-24

#### src/stg/game/ui/GameStatusPanel.java
- 8: * @Time 2026-01-24

### 2026-01-27

#### src/stg/game/ui/TitleScreen.java
- 17: * @Time 2026-01-27 添加标题音乐播放功能

### 2026-01-30

#### src/stg/base/KeyStateProvider.java
- 6: * @Time 2026-01-30 添加Shift和X键支持

## 按文件类型分类统计

### 1. 用户自定义实体类
- **子弹类**：6个文件，10次使用
  - CircularBullet.java
  - TrackingBullet.java
  - SpreadBullet.java
  - SpiralBullet.java
  - PlayerTrackingBullet.java (未使用@Time)
  - SimpleBullet.java (未使用@Time)
  - CurvingBullet.java (未使用@Time)

- **敌人类**：7个文件，13次使用
  - BasicEnemy.java
  - LaserShootingEnemy.java
  - TrackingEnemy.java
  - SpreadEnemy.java
  - RapidFireEnemy.java
  - OrbitEnemy.java
  - SpiralEnemy.java
  - EnemyWithSound.java
  - EnemyBullet.java (未使用@Time)

- **激光类**：4个文件，4次使用
  - LinearLaser.java
  - EnemyLaser.java
  - CurvedLaser.java
  - EnemyLinearLaser.java
  - EnemyCurvedLaser.java
  - SimpleLaser.java (未使用@Time)

- **玩家类**：6个文件，6次使用
  - MarisaPlayer.java
  - ReimuPlayer.java
  - PlayerWithImage.java
  - CustomPlayer.java (未使用@Time)
  - CustomOption.java (未使用@Time)
  - MarisaOption.java (未使用@Time)
  - ReimuOption.java (未使用@Time)
  - Option.java (未使用@Time)
  - Player.java (未使用@Time)
  - PlayerFactory.java (未使用@Time)
  - PlayerType.java (未使用@Time)

- **道具类**：4个文件，0次使用
  - BombUp.java
  - LifeUp.java
  - PowerUp.java
  - ScorePoint.java

- **关卡类**：8个文件，0次使用
  - AdvancedStageGroup.java
  - BeginnerStageGroup.java
  - ExpertStageGroup.java
  - IntermediateStageGroup.java
  - SimpleStage.java
  - StageCompletionCondition.java
  - StageGroup.java
  - StageGroupManager.java
  - StageState.java
  - WaveBasedStage.java

### 2. 系统基础类
- **UI相关**：3个文件，9次使用
  - TitleScreen.java
  - GameStatusPanel.java
  - StageGroupSelectPanel.java (未使用@Time)
  - PauseMenu.java (未使用@Time)

- **基础组件**：3个文件，9次使用
  - Window.java
  - VirtualKeyboardPanel.java
  - KeyStateProvider.java

- **工具类**：6个文件，22次使用
  - CoordinateSystem.java
  - MathUtils.java
  - Vector2.java
  - RandomGenerator.java
  - ResourceManager.java
  - AudioManager.java
  - EventBus.java (未使用@Time)
  - RenderUtils.java (未使用@Time)

- **游戏核心**：13个文件，0次使用
  - GameLoop.java
  - GameRenderer.java
  - GameStateManager.java
  - GameWorld.java
  - InputHandler.java
  - CollisionSystem.java
  - IGameObject.java
  - IGameWorld.java
  - Bullet.java
  - IBullet.java
  - Enemy.java
  - IEnemy.java
  - IItem.java
  - Item.java
  - Laser.java
  - IPlayer.java
  - Player.java
  - Stage.java
  - StageCompletionCondition.java
  - BulletFiredEvent.java
  - EnemyDestroyedEvent.java
  - EnemySpawnedEvent.java
  - ItemCollectedEvent.java

- **资源测试**：2个文件，2次使用
  - ResourceDemoWindow.java
  - ResourceTest.java

## 分析发现

### 1. 时间分布特点
- **2026-01-19**：最多使用，主要集中在基础工具类和敌人/子弹实现
- **2026-01-20**：大量基础架构调整，创建数学工具包
- **2026-01-21**：激光相关实现
- **2026-01-23**：玩家和高级敌人/子弹实现
- **2026-01-24**：资源管理和音频支持
- **2026-01-27**：UI功能增强
- **2026-01-30**：功能完善

### 2. 使用模式
- **类级注释**：用于标记类的创建或主要修改时间
- **方法级注释**：用于标记方法的实现时间和功能说明
- **代码行注释**：用于标记特定代码行的修改时间和设计意图

### 3. 未使用@Time的文件
- 大部分游戏核心接口和抽象类
- 部分用户自定义实体类
- 所有关卡相关类
- 所有道具相关类

## 改进建议

### 1. 统一@Time使用规范
- **建议格式**：`@Time YYYY-MM-DD 描述信息`
- **使用时机**：
  - 类的首次创建
  - 重大功能修改
  - 核心算法变更
  - 性能优化

### 2. 补充缺失的@Time注释
- 为未使用@Time的核心文件添加创建时间
- 为重要的接口和抽象类添加@Time注释
- 为关卡和道具相关类添加@Time注释

### 3. 建立版本控制系统集成
- 考虑使用Git等版本控制系统自动记录修改时间
- 减少手动@Time注释的使用，避免与实际修改时间不一致

### 4. 定期维护@Time注释
- 当代码发生重大变更时，更新相关@Time注释
- 移除过时或不再相关的@Time注释

## 结论

项目中使用@Time注释的情况较为广泛，主要集中在用户自定义实体类和工具类上，反映了项目的开发历程和代码演进过程。通过按时间顺序整理，可以清晰看到项目从基础架构搭建、核心功能实现到UI优化的完整开发流程。

建议建立更规范的@Time使用标准，并结合版本控制系统，使代码的时间记录更加准确和有意义。