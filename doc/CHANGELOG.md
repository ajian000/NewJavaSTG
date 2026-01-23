# 更新日志

## v2.1 - Java脚本系统 (2026-01-23)

### 新增功能

#### Java脚本支持
- ✅ 新增`LevelScript`接口，用于编写纯Java关卡脚本
- ✅ 新增`JavaLevelLoader`加载器，支持通过反射加载Java类
- ✅ 新增`SimpleBullet`、`SimpleEnemy`、`SimpleLaser`默认实现类
- ✅ 更新`LevelManager`支持Java脚本语言

#### 辅助方法
LevelScript接口提供便捷的辅助方法：
- `addBasicEnemy()` - 添加基础敌人
- `addLaserShootingEnemy()` - 添加激光射击敌人
- `addCustomEnemy()` - 添加自定义敌人

#### 文档
- ✅ `JAVA_SCRIPT_GUIDE.md` - Java脚本系统完整使用指南
- ✅ `JAVA_QUICK_REFERENCE.md` - Java脚本快速参考
- ✅ `TASK_SYSTEM.md` - 更新说明task()为抽象方法
- ✅ `src/user/level/README.md` - Java关卡脚本目录说明

#### 示例
- ✅ `JavaLevelDemo.java` - 完整的Java脚本示例，包含7波敌人

### 改进

#### 抽象方法设计
- ✅ Bullet、Enemy、Laser改为抽象类
- ✅ task()方法改为抽象方法，强制子类实现
- ✅ 所有子类必须实现task()方法或使用Simple*类

#### 类型安全
- ✅ 编译时类型检查
- ✅ IDE自动补全和重构支持
- ✅ 消除运行时脚本错误

### 代码变更

#### 新增文件
```
src/stg/util/script/
├── LevelScript.java          # Java脚本接口
└── JavaLevelLoader.java      # Java加载器

src/stg/game/
├── bullet/
│   └── SimpleBullet.java     # 简单子弹
├── enemy/
│   └── SimpleEnemy.java      # 简单敌人
└── laser/
    └── SimpleLaser.java      # 简单激光

src/user/level/
├── JavaLevelDemo.java        # Java脚本示例
└── README.md               # 目录说明

doc/
├── JAVA_SCRIPT_GUIDE.md     # 使用指南
└── JAVA_QUICK_REFERENCE.md  # 快速参考
```

#### 修改文件
- `LevelManager.java` - 添加Java脚本支持，默认使用Java加载器
- `Player.java` - 使用SimpleBullet替代Bullet
- `ReimuPlayer.java` - 使用SimpleBullet替代Bullet
- `MarisaPlayer.java` - 使用SimpleBullet替代Bullet
- `EnemyBullet.java` - 添加task()实现
- `BasicEnemy.java` - 添加task()实现
- `LaserShootingEnemy.java` - 添加task()实现
- `EnemyLaser.java` - 添加task()实现
- `LinearLaser.java` - 添加task()实现
- `CurvedLaser.java` - 添加task()实现
- `EnemyLinearLaser.java` - 添加task()实现
- `EnemyCurvedLaser.java` - 添加task()实现

### 性能提升

- ✅ Java脚本直接编译执行，无需解释器
- ✅ 更好的JIT优化机会
- ✅ 减少运行时类型检查开销

### 向后兼容性

- ✅ 保留JSON加载器用于配置文件
- ✅ 可以通过`setScriptLanguage()`切换到JSON
- ✅ 现有Java脚本无需修改

### JAR更新

- ✅ JAR大小：128,878 字节（约126KB）
- ✅ 包含所有Java脚本系统组件

## v2.0 - Task线程系统 (2026-01-22)

### 新增功能
- ✅ 为Bullet、Enemy、Laser添加独立task线程机制
- ✅ 自动启动task线程（构造函数调用）
- ✅ 线程安全设计（volatile标志）
- ✅ 优雅停止机制（join超时）

### 演示类
- ✅ TaskDemoBullet - 子弹task示例
- ✅ TaskDemoEnemy - 敌人task示例

### 文档
- ✅ `TASK_SYSTEM.md` - Task系统完整文档
- ✅ `TASK_QUICK_REFERENCE.md` - Task快速参考

## v1.x - 早期版本

详见其他文档了解历史更新。
